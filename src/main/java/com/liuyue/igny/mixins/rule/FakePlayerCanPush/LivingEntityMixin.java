package com.liuyue.igny.mixins.rule.FakePlayerCanPush;

import carpet.patches.EntityPlayerMPFake;
import com.liuyue.igny.IGNYSettings;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method ="push",at= @At(value = "HEAD"), cancellable = true)
    private void push(Entity entity, CallbackInfo ci){
        if (entity instanceof EntityPlayerMPFake && !IGNYSettings.FakePlayerCanPush) {
            ci.cancel();
        }
    }
}
