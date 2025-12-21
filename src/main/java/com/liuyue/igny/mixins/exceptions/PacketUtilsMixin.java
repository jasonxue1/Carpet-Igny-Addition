package com.liuyue.igny.mixins.exceptions;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.ReportedException;
import com.liuyue.igny.exception.IAEUpdateSuppressException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 12109
//$$ import net.minecraft.network.PacketProcessor;
//#endif

@Mixin(PacketUtils.class)
public class PacketUtilsMixin {
    @SuppressWarnings("unchecked")
    //#if MC >= 12109
    //$$  @WrapOperation(method = "ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketProcessor;scheduleIfPossible(Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/protocol/Packet;)V"))
    //$$  private static <T extends PacketListener> void exceptionReason(PacketProcessor instance, T listener, Packet<T> packet, Operation<Void> original) {
    //#else
    @WrapOperation(method = "method_11072", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/Packet;handle(Lnet/minecraft/network/PacketListener;)V"))
    private static <T extends PacketListener> void exceptionReason(final Packet<T> packet, final T listener, Operation<Void> original) {
    //#endif
        try {
            //#if MC >= 12109
            //$$ original.call(instance, listener, packet);
            //#else
            original.call(packet, listener);
            //#endif
        } catch (RuntimeException e) {
            IAEUpdateSuppressException iae;
            if (e instanceof IAEUpdateSuppressException) {
                iae = (IAEUpdateSuppressException) e;
            } else if (e instanceof ReportedException crashException && crashException.getCause() instanceof IAEUpdateSuppressException exception) {
                iae = exception;
            } else {
                throw e;
            }
            exceptionReason((Packet<ServerGamePacketListener>) packet, listener, iae);
            throw e;
        }
    }

    @Unique
    private static <T extends PacketListener> void exceptionReason(Packet<ServerGamePacketListener> packet, T listener, IAEUpdateSuppressException iaeUpdateSuppressException) {
        if (listener instanceof ServerGamePacketListenerImpl networkHandler) {
            iaeUpdateSuppressException.onCatch(networkHandler.player, packet);
        }
    }
}