package net.sen.yggdrasil.data.loottable;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import static net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondBlocks.*;
import static net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondItems.*;

public class ModBlockLootTables extends BlockLootTableHelper {
    private static final LootItemCondition.Builder SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));

    public ModBlockLootTables(HolderLookup.Provider registries) {
        super(registries);
    }

    @Override
    protected void generate() {
        dropSelf(ALFHEIMR_PORTAL_FRAME_BLOCK);
    }
}
