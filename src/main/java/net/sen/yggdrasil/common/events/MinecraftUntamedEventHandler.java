package net.sen.yggdrasil.common.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.sen.yggdrasil.YggdrasilRealmsBeyond;

@EventBusSubscriber(modid = YggdrasilRealmsBeyond.MODID)
public class MinecraftUntamedEventHandler {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {

    }
}
