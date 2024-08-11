package com.plusls.carpet.impl.network.packet.pca;

import com.plusls.carpet.impl.network.PcaSyncProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ServerboundCancelSyncBlockEntityPacket() implements CustomPacketPayload {
    public static final Type<ServerboundCancelSyncBlockEntityPacket> TYPE = new Type<>(PcaSyncProtocol.CANCEL_SYNC_BLOCK_ENTITY);
    public static final StreamCodec<FriendlyByteBuf, ServerboundCancelSyncBlockEntityPacket> CODEC = CustomPacketPayload.codec(ServerboundCancelSyncBlockEntityPacket::write, ServerboundCancelSyncBlockEntityPacket::new);

    public ServerboundCancelSyncBlockEntityPacket(FriendlyByteBuf byteBuf) {
        this();
    }

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ServerboundCancelSyncBlockEntityPacket.TYPE;
    }

    public void write(FriendlyByteBuf byteBuf) {
    }
}
