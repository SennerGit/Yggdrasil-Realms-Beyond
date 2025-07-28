package net.sen.yggdrasil.data.models;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import static net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondBlocks.*;

public class ModBlockStateProvider extends ModBlockStateHelper {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }
}
