package net.sen.yggdrasil.data.language;

import net.minecraft.data.PackOutput;
import net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondCreativeModeTabs;

import static net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondBlocks.*;
import static net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondItems.*;

public class ModLanguageEnUsProvider extends LanguageProviderHelper {
    public ModLanguageEnUsProvider(PackOutput output, String locale) {
        super(output, locale);
    }

    @Override
    void spawnEggs() {

    }

    @Override
    void blocks() {
    }

    @Override
    void items() {
    }

    @Override
    void paintings() {

    }

    @Override
    void effects() {

    }

    @Override
    void potions() {

    }

    @Override
    void sounds() {

    }

    @Override
    void custom() {

    }

    @Override
    void config() {

    }

    @Override
    void creativeTab() {
        addCreativeTab(YggdrasilRealmsBeyondCreativeModeTabs.MINECRAFT_UNTAMED_TAB, "Yggdrasil Realms Beyond Tab");
    }

    @Override
    void baseAdvancements() {

    }
}
