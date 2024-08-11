package com.plusls.carpet.mixin.rule.xaeroWorldName;

import com.plusls.carpet.PluslsCarpetAdditionSettings;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

@Mixin(PlayerList.class)
public class MixinPlayerList {
    @Inject(
            method = "sendLevelInfo",
            at = @At(value = "RETURN"
            )
    )
    public void preOnSendWorldInfo(ServerPlayer player, ServerLevel world, CallbackInfo ci) {
        if (PluslsCarpetAdditionSettings.xaeroWorldName.equals(PluslsCarpetAdditionSettings.xaeroWorldNameNone)) {
            return;
        }
        //#if MC < 12100
        ResourceLocation xaeroworldmap = new ResourceLocation("xaeroworldmap", "main");
        ResourceLocation xaerominimap = new ResourceLocation("xaerominimap", "main");
        //#else
        //$$ ResourceLocation xaeroworldmap = ResourceLocation.fromNamespaceAndPath("xaeroworldmap", "main");
        //$$ ResourceLocation xaerominimap = ResourceLocation.fromNamespaceAndPath("xaerominimap", "main");
        //#endif

        CRC32 crc = new CRC32();
        byte[] bytes = PluslsCarpetAdditionSettings.xaeroWorldName.getBytes(StandardCharsets.UTF_8);
        crc.update(bytes, 0, bytes.length);
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0);
        buf.writeInt((int) crc.getValue());

        //#if MC < 12005
        ServerPlayNetworking.send(player, xaeroworldmap, new FriendlyByteBuf(buf.duplicate()));
        ServerPlayNetworking.send(player, xaerominimap, new FriendlyByteBuf(buf.duplicate()));
        //#endif
    }
}
