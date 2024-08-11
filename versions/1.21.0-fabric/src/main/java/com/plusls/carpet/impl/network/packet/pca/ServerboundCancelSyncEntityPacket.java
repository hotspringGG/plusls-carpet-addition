package com.plusls.carpet.impl.network.packet.pca;

import com.plusls.carpet.impl.network.PcaSyncProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ServerboundCancelSyncEntityPacket() implements CustomPacketPayload {
    public static final Type<ServerboundCancelSyncEntityPacket> TYPE = new Type<>(PcaSyncProtocol.CANCEL_SYNC_ENTITY);
    public static final StreamCodec<FriendlyByteBuf, ServerboundCancelSyncEntityPacket> CODEC = CustomPacketPayload.codec(ServerboundCancelSyncEntityPacket::write, ServerboundCancelSyncEntityPacket::new);

    public ServerboundCancelSyncEntityPacket(FriendlyByteBuf byteBuf) {
        this();
    }

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ServerboundCancelSyncEntityPacket.TYPE;
    }

    private void write(FriendlyByteBuf byteBuf) {
    }
}
