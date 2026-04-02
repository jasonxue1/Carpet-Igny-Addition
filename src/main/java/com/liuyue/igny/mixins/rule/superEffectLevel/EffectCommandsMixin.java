package com.liuyue.igny.mixins.rule.superEffectLevel;

import com.liuyue.igny.IGNYSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.server.commands.EffectCommands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EffectCommands.class)
public class EffectCommandsMixin {
    @WrapOperation(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/arguments/IntegerArgumentType;integer(II)Lcom/mojang/brigadier/arguments/IntegerArgumentType;"))
    private static IntegerArgumentType integer(int min, int max, Operation<IntegerArgumentType> original) {
        if (IGNYSettings.superEffectLevel && max == 255) {
            max = Integer.MAX_VALUE;
        }
        return original.call(min, max);
    }
}
