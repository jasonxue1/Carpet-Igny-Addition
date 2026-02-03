package com.liuyue.igny.network.packet.render;

//#if MC >= 12005
import com.liuyue.igny.network.packet.PacketUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
//#endif
import net.minecraft.core.BlockPos;

public record BoxPayload(
        BlockPos pos,           // 动画基准点 & 唯一 ID
        int color,
        int durationTicks,
        boolean permanent,
        boolean deathTest,
        double minX, double minY, double minZ,
        double maxX, double maxY, double maxZ,
        boolean withLine,
        boolean lineDeathTest,
        boolean smooth
)
        //#if MC >= 12005
        implements CustomPacketPayload
        //#endif
{
    //#if MC >= 12005
    public static final Type<BoxPayload> TYPE = PacketUtil.createId("render_box");

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static final StreamCodec<RegistryFriendlyByteBuf, BoxPayload> CODEC =
            new StreamCodec<>() {
                @Override
                public @NotNull BoxPayload decode(RegistryFriendlyByteBuf buf) {
                    return new BoxPayload(
                            buf.readBlockPos(), buf.readInt(), buf.readInt(),
                            buf.readBoolean(), buf.readBoolean(),
                            buf.readDouble(), buf.readDouble(), buf.readDouble(),
                            buf.readDouble(), buf.readDouble(), buf.readDouble(),
                            buf.readBoolean(), buf.readBoolean(), buf.readBoolean()
                    );
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, BoxPayload v) {
                    buf.writeBlockPos(v.pos);
                    buf.writeInt(v.color);
                    buf.writeInt(v.durationTicks);
                    buf.writeBoolean(v.permanent);
                    buf.writeBoolean(v.deathTest);
                    buf.writeDouble(v.minX);
                    buf.writeDouble(v.minY);
                    buf.writeDouble(v.minZ);
                    buf.writeDouble(v.maxX);
                    buf.writeDouble(v.maxY);
                    buf.writeDouble(v.maxZ);
                    buf.writeBoolean(v.withLine);
                    buf.writeBoolean(v.lineDeathTest);
                    buf.writeBoolean(v.smooth);
                }
            };
    //#endif
}