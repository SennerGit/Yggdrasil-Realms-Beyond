package net.sen.yggdrasil.common.registries;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.sen.yggdrasil.common.world.biomes.alfheimr.AlfheimrBiomes;

public class YggdrasilRealmsBeyondBiomes {
    public static void bootstrap(BootstrapContext<Biome> context) {
        AlfheimrBiomes.bootstrap(context);
    }
}
