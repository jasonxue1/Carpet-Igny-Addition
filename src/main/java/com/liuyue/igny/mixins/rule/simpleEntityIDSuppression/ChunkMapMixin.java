package com.liuyue.igny.mixins.rule.simpleEntityIDSuppression;

import com.liuyue.igny.IGNYSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkMap.class)
public class ChunkMapMixin {
    @WrapOperation(method = "addEntity", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;containsKey(I)Z"))
    private boolean containsKey(Int2ObjectMap<?> instance, int i, Operation<Boolean> original) {
        return IGNYSettings.hasEID || original.call(instance, i);
    }
}
