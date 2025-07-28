package net.sen.yggdrasil.data.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.sen.yggdrasil.common.registries.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondItems.*;
import static net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondBlocks.*;

public class ModRecipeProvider extends ModRecipeHelper {

    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(pOutput, registries);
    }

    @Override
    void miscRecipes(RecipeOutput pRecipeOutput) {

    }

    @Override
    void stoneRecipes(RecipeOutput pRecipeOutput) {

    }

    @Override
    void foodRecipes(RecipeOutput pRecipeOutput) {

    }

    @Override
    void metalRecipes(RecipeOutput pRecipeOutput) {

    }

    @Override
    void woodRecipes(RecipeOutput pRecipeOutput) {

    }

    @Override
    void flowerRecipes(RecipeOutput pRecipeOutput) {

    }

    @Override
    void vanilla(RecipeOutput output) {

    }
}
