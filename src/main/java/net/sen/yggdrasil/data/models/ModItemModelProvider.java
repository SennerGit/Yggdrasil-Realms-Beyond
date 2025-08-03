package net.sen.yggdrasil.data.models;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import static net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondItems.*;

public class ModItemModelProvider extends ModItemModelHelper {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, existingFileHelper);
    }

    @Override
    protected void generateItems() {
        simpleItem(BASIC_PORTAL_ACTIVATOR);
    }

    @Override
    protected void generateBlocks() {
    }
}