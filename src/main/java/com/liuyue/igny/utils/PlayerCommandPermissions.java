package com.liuyue.igny.utils;

import com.liuyue.igny.IGNYSettings;
import net.minecraft.commands.CommandSourceStack;
import carpet.patches.EntityPlayerMPFake;
import net.minecraft.server.level.ServerPlayer;

public class PlayerCommandPermissions {
    public static boolean canDropEnderChest(CommandSourceStack source, ServerPlayer targetPlayer) {
        if (source == null) {
            return true;
        }

        if (!(targetPlayer instanceof EntityPlayerMPFake)) {
            return source.hasPermission(2);
        }

        String ruleValue = IGNYSettings.CommandPlayerEnderChestDrop;

        return switch (ruleValue.toLowerCase()) {
            case "true" -> true;
            case "false" -> false;
            case "0" -> source.hasPermission(0);
            case "1" -> source.hasPermission(1);
            case "3" -> source.hasPermission(3);
            case "4" -> source.hasPermission(4);
            default -> source.hasPermission(2);
        };
    }

    public static boolean canDropEnderChest(CommandSourceStack source) {
        return source == null || source.hasPermission(2);
    }
}