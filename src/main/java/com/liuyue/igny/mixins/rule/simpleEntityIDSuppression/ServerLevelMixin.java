package com.liuyue.igny.mixins.rule.simpleEntityIDSuppression;

import com.liuyue.igny.IGNYSettings;
import com.liuyue.igny.utils.RuleUtil;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/entity/EntityTickList;forEach(Ljava/util/function/Consumer;)V"))
    private void tick(EntityTickList instance, Consumer<Entity> entity, Operation<Void> original) {
        IGNYSettings.hasEID = false;
        original.call(instance, entity);
    }

    @Inject(method = "method_31420", at = @At(value = "RETURN"))
    private void tickEntity(TickRateManager tickRateManager, ProfilerFiller profilerFiller, Entity entity, CallbackInfo ci) {
        if (RuleUtil.canEntityIDSuppression(entity)) {
            IGNYSettings.hasEID = true;
        }
    }
}