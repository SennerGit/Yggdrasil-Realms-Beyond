package net.sen.yggdrasil.data.models;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.sen.yggdrasil.common.block.portal.alfheimr.AlfheimrPortalBlock;

import static net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondBlocks.*;

public class ModBlockStateProvider extends ModBlockStateHelper {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.blockWithItem(ALFHEIMR_PORTAL_FRAME_BLOCK);
        this.makePortalBlock(ALFHEIMR_PORTAL_BLOCK, AlfheimrPortalBlock.AXIS);
    }
}
