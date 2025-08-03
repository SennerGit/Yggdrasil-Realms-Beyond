package net.sen.yggdrasil.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.sen.yggdrasil.YggdrasilRealmsBeyond;

@Mod(value = YggdrasilRealmsBeyond.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = YggdrasilRealmsBeyond.MODID, value = Dist.CLIENT)
public class YggdrasilRealmsBeyondClient {
    public YggdrasilRealmsBeyondClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static void YggdrasilRealmsBeyondClientRegistry(IEventBus eventBus) {
        eventBus.addListener(YggdrasilRealmsBeyondClient::renderEntities);
        eventBus.addListener(YggdrasilRealmsBeyondClient::registerLayer);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
        });
    }

    private static void renderEntities(EntityRenderersEvent.RegisterRenderers  event) {
    }

    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    public static void playAlfheimrPortalSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forLocalAmbience(SoundEvents.PORTAL_TRAVEL, 1.0F, 1.0F));
    }
}
