package net.sen.yggdrasil.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sen.yggdrasil.client.YggdrasilRealmsBeyondClient;
import net.sen.yggdrasil.common.utils.ModUtils;

public class YggdrasilRealmsBeyondPortalSoundPacket {
    public static class Alfheimr implements CustomPacketPayload {
        public static final YggdrasilRealmsBeyondPortalSoundPacket.Alfheimr INSTANCE = new YggdrasilRealmsBeyondPortalSoundPacket.Alfheimr();
        public static final Type<YggdrasilRealmsBeyondPortalSoundPacket.Alfheimr> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ModUtils.getModId(), "alfheimr_portal_sound"));
        public static final StreamCodec<RegistryFriendlyByteBuf, YggdrasilRealmsBeyondPortalSoundPacket.Alfheimr> STREAM_CODEC = StreamCodec.unit(INSTANCE);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static void handle(IPayloadContext context) {
            context.enqueueWork(YggdrasilRealmsBeyondClient::playAlfheimrPortalSound);
        }
    }
}
