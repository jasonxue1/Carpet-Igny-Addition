package com.liuyue.igny.commands;

import carpet.patches.EntityPlayerMPFake;
import com.liuyue.igny.IGNYSettings;
import com.liuyue.igny.data.CustomPickupManager;
import com.liuyue.igny.utils.CommandPermissions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CustomPlayerPickupItemCommand {
    private static final Set<String> VALID_ITEM_IDS = BuiltInRegistries.ITEM.keySet()
            .stream()
            .map(ResourceLocation::toString)
            .map(String::toLowerCase)
            .collect(Collectors.toUnmodifiableSet());

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("customPlayerPickupItem")
                        .requires(source -> CommandPermissions.canUseCommand(source, IGNYSettings.commandCustomPlayerPickupItem))
                        .then(Commands.argument("target", StringArgumentType.string())
                                .suggests(CustomPlayerPickupItemCommand::suggestPlayers)
                                .then(Commands.literal("get")
                                        .executes(CustomPlayerPickupItemCommand::executeGet)
                                )
                                .then(Commands.literal("mode")
                                        .then(Commands.argument("mode", StringArgumentType.string())
                                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(Arrays.asList("disable", "whitelist", "blacklist"), builder))
                                                .executes(CustomPlayerPickupItemCommand::executeMode)
                                        )
                                )
                                .then(Commands.literal("items")
                                        .then(Commands.argument("itemlist", StringArgumentType.greedyString())
                                                .executes(CustomPlayerPickupItemCommand::executeItems)
                                        )
                                )
                        )
        );
    }

    private static int executeGet(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        String targetName = StringArgumentType.getString(ctx, "target");
        if (!checkPermission(source, targetName)) return 0;

        CustomPickupManager.PlayerSetting setting = CustomPickupManager.getOrCreate(targetName);

        MutableComponent message = Component.translatable("igny.command.customPlayerPickupItem.header", targetName)
                .withStyle(ChatFormatting.GOLD).append("\n");

        message.append(Component.translatable("igny.command.customPlayerPickupItem.mode").withStyle(ChatFormatting.YELLOW))
                .append(": ").append(Component.literal(setting.getMode().toString().toLowerCase()).withStyle(ChatFormatting.AQUA)).append("\n");

        Set<String> items = setting.getItems();
        message.append(Component.translatable("igny.command.customPlayerPickupItem.items").withStyle(ChatFormatting.YELLOW)).append(": ");

        if (items.isEmpty()) {
            message.append(Component.translatable("igny.command.customPlayerPickupItem.none").withStyle(ChatFormatting.GRAY));
        } else {
            message.append(Component.literal("[").withStyle(ChatFormatting.AQUA));
            int i = 0;
            for (String id : items) {
                ResourceLocation res = ResourceLocation.tryParse(id);
                if (res != null) {
                    BuiltInRegistries.ITEM.getOptional(res).ifPresent(holder -> message.append(Component.translatable(holder.getDescriptionId()).withStyle(ChatFormatting.AQUA)));
                }
                if (++i < items.size()) {
                    message.append(Component.literal(", ").withStyle(ChatFormatting.GRAY));
                }
            }
            message.append(Component.literal("]").withStyle(ChatFormatting.AQUA));

        }

        source.sendSuccess(
                //#if MC > 11904
                () ->
                //#endif
                message, false);
        return 1;
    }

    private static int executeMode(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        String targetName = StringArgumentType.getString(ctx, "target");
        if (!checkPermission(source, targetName)) return 0;

        String modeStr = StringArgumentType.getString(ctx, "mode").toLowerCase();
        CustomPickupManager.Mode mode;
        switch (modeStr) {
            case "whitelist" -> mode = CustomPickupManager.Mode.WHITELIST;
            case "blacklist" -> mode = CustomPickupManager.Mode.BLACKLIST;
            case "disable" -> mode = CustomPickupManager.Mode.DISABLED;
            default -> {
                source.sendFailure(Component.translatable("igny.command.customPlayerPickupItem.invalid_mode", modeStr)
                        .withStyle(ChatFormatting.RED));
                return 0;
            }
        }

        CustomPickupManager.PlayerSetting setting = CustomPickupManager.getOrCreate(targetName);
        setting.setMode(mode);
        CustomPickupManager.updateAndSave(targetName, setting);

        source.sendSuccess(
                //#if MC > 11904
                () ->
                //#endif
                Component.translatable("igny.command.customPlayerPickupItem.mode_success", targetName, modeStr)
                .withStyle(ChatFormatting.GREEN), true);
        return 1;
    }

    private static int executeItems(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        String targetName = StringArgumentType.getString(ctx, "target");
        if (!checkPermission(source, targetName)) return 0;

        String itemsStr = StringArgumentType.getString(ctx, "itemlist");
        Set<String> items = Arrays.stream(itemsStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .map(s -> s.contains(":") ? s : "minecraft:" + s)
                .collect(Collectors.toSet());

        for (String id : items) {
            if ("minecraft:air".equals(id) || !isValidItemName(id)) {
                source.sendFailure(Component.translatable("igny.command.customPlayerPickupItem.invalid_item", id)
                        .withStyle(ChatFormatting.RED));
                return 0;
            }
        }

        CustomPickupManager.PlayerSetting setting = CustomPickupManager.getOrCreate(targetName);
        setting.setItems(items);
        CustomPickupManager.updateAndSave(targetName, setting);

        source.sendSuccess(
                //#if MC > 11904
                () ->
                //#endif
                Component.translatable("igny.command.customPlayerPickupItem.items_success", targetName, itemsStr)
                .withStyle(ChatFormatting.GREEN), true);
        return 1;
    }

    private static boolean checkPermission(CommandSourceStack source, String targetName) {
        CustomPickupManager.setServer(source.getServer());
        if (!source.hasPermission(2)) {
            if (source.getPlayer() == null || (!source.getPlayer().getGameProfile()
                    //#if MC >= 12110
                    //$$ .name()
                    //#else
                    .getName()
                    //#endif
                    .equalsIgnoreCase(targetName) && !(source.getPlayer() instanceof EntityPlayerMPFake))) {
                source.sendFailure(Component.translatable("igny.command.customPlayerPickupItem.no_permission")
                        .withStyle(ChatFormatting.RED));
                return false;
            }
        }
        return true;
    }

    private static CompletableFuture<Suggestions> suggestPlayers(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        CommandSourceStack source = context.getSource();
        Collection<String> players;
        if (source.hasPermission(2)) {
            players = Arrays.asList(source.getServer().getPlayerNames());
        } else if (source.getPlayer() != null) {
            players = Set.of(source.getPlayer().getGameProfile()
                    //#if MC >= 12110
                    //$$ .name()
                    //#else
                    .getName()
                    //#endif
            );
        } else {
            players = Set.of();
        }
        return SharedSuggestionProvider.suggest(players, builder);
    }

    private static boolean isValidItemName(String name) {
        ResourceLocation id = ResourceLocation.tryParse(name);
        return id != null && VALID_ITEM_IDS.contains(name);
    }
}