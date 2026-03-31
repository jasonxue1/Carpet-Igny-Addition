package com.liuyue.igny.mixins.rule.liquidNeverSpread;

import com.liuyue.igny.IGNYSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlowingFluid.class)
public class FlowingFluidMixin {
    @Inject(method = "spread", at = @At(value = "HEAD"), cancellable = true)
    private void spread(Level level, BlockPos pos, FluidState state, CallbackInfo ci) {
        if (!IGNYSettings.liquidNeverSpread.equals("false")) {
            if (IGNYSettings.liquidNeverSpread.equals("true") || state.isSource()) {
                ci.cancel();
            }
        }
    }
}
