package net.sen.yggdrasil.common.events;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.sen.yggdrasil.YggdrasilRealmsBeyond;
import net.sen.yggdrasil.common.network.YggdrasilRealmsBeyondPortalSoundPacket;

@EventBusSubscriber(modid = YggdrasilRealmsBeyond.MODID)
public class YggdrasilRealmsBeyondEventHandler {

    public static void initCommonEvents(IEventBus bus) {
        bus.addListener(YggdrasilRealmsBeyondEventHandler::registerPackets);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {

    }

    private static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(YggdrasilRealmsBeyond.MODID).versioned("0.1.0").optional();
        registrar.playToClient(YggdrasilRealmsBeyondPortalSoundPacket.Alfheimr.TYPE, YggdrasilRealmsBeyondPortalSoundPacket.Alfheimr.STREAM_CODEC, (payload, context) -> YggdrasilRealmsBeyondPortalSoundPacket.Alfheimr.handle(context));
    }
}
