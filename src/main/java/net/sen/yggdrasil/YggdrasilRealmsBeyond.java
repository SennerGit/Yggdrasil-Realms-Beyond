package net.sen.yggdrasil;

import com.google.common.reflect.Reflection;
import net.neoforged.api.distmarker.Dist;
import net.sen.yggdrasil.common.config.Config;
import net.sen.yggdrasil.common.events.YggdrasilRealmsBeyondEventHandler;
import net.sen.yggdrasil.common.world.biomes.builders.AlfheimrBiomeBuilder;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.sen.yggdrasil.common.registries.*;
import net.sen.yggdrasil.client.*;
@Mod(YggdrasilRealmsBeyond.MODID)
public class YggdrasilRealmsBeyond {
    public static final String MODID = "yggdrasil";
    public static final Logger LOGGER = LogUtils.getLogger();

    public YggdrasilRealmsBeyond(IEventBus eventBus, ModContainer modContainer, Dist dist) {
        if (dist.isClient()) {
            YggdrasilRealmsBeyondClient.YggdrasilRealmsBeyondClientRegistry(eventBus);
        }

        YggdrasilRealmsBeyondEventHandler.initCommonEvents(eventBus);

        YggdrasilRealmsBeyondBlocks.register(eventBus);
        YggdrasilRealmsBeyondItems.register(eventBus);
        YggdrasilRealmsBeyondBlockEntities.register(eventBus);
        YggdrasilRealmsBeyondEntityTypes.register(eventBus);
        YggdrasilRealmsBeyondArmourMaterials.register(eventBus);
        YggdrasilRealmsBeyondCreativeModeTabs.register(eventBus);
        YggdrasilRealmsBeyondPointOfInterests.register(eventBus);
        YggdrasilRealmsBeyondCarvers.register(eventBus);

        NeoForge.EVENT_BUS.register(this);
        eventBus.addListener(this::commonSetup);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        preInit(event);
        init(event);
        postInit(event);
    }

    private void preInit(FMLCommonSetupEvent event) {
        Reflection.initialize(
                AlfheimrBiomeBuilder.class
        );
    }

    private void init(FMLCommonSetupEvent event) {
    }

    private void postInit(FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
