package com.liuyue.igny.mixins.features.rule.furnaceHasIncombustibleHighlight;

import com.liuyue.igny.IGNYSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.level.chunk.LevelChunk$RebindableTickingBlockEntityWrapper")
public abstract class RebindableTickingBlockEntityWrapperMixin {
    @Shadow
    public abstract BlockPos getPos();

    @Inject(method = "rebind", at = @At("HEAD"), cancellable = true)
    private void onRebind(TickingBlockEntity newTicker, CallbackInfo ci) {
            if (IGNYSettings.furnaceHasIncombustibleHighlight && newTicker.getType().equals("<lithium_sleeping>")) {
                ci.cancel();
            }

    }
}
