package com.plusls.carpet.impl.network.packet.pca;

import com.plusls.carpet.impl.network.PcaSyncProtocol;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ServerboundSyncBlockEntityPacket(BlockPos pos) implements CustomPacketPayload {
    public static final Type<ServerboundSyncBlockEntityPacket> TYPE = new Type<>(PcaSyncProtocol.SYNC_BLOCK_ENTITY);
    public static final StreamCodec<FriendlyByteBuf, ServerboundSyncBlockEntityPacket> CODEC = CustomPacketPayload.codec(ServerboundSyncBlockEntityPacket::write, ServerboundSyncBlockEntityPacket::new);

    public ServerboundSyncBlockEntityPacket(@NotNull FriendlyByteBuf byteBuf) {
        this(byteBuf.readBlockPos());
    }

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ServerboundSyncBlockEntityPacket.TYPE;
    }

    public void write(@NotNull FriendlyByteBuf byteBuf) {
        byteBuf.writeBlockPos(this.pos);
    }
}
