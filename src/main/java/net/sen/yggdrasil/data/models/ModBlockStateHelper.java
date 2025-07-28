package net.sen.yggdrasil.data.models;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.sen.yggdrasil.common.utils.ModUtils;
import java.util.function.Supplier;

public abstract class ModBlockStateHelper extends BlockStateProvider {
    public ModBlockStateHelper(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ModUtils.getModId(), exFileHelper);
    }

    protected String name(Block block) {
        return key(block).getPath();
    }

    protected ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
        //        return Registries.BLOCK.registryKey().getKey(block);
    }

    protected void blockItem(DeferredBlock<?> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(ModUtils.getModPath("block/" + BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath())));
    }

    protected void blockItem(DeferredBlock<?> blockRegistryObject, String appendix) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(ModUtils.getModPath("block/" + BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath() + appendix)));
    }

    protected void blockWithItem(Supplier<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    protected void colouredBlockWithItem(DeferredBlock<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().singleTexture(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(),
                        ModUtils.getMinecraftPath("block/cube_all"),
                        "all", blockTexture(blockRegistryObject.get())).renderType("solid"));
    }

    protected void makeCarpet(DeferredBlock<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().carpet(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(),
                        blockTexture(blockRegistryObject.get()))
        );
    }

    protected void leavesBlock(DeferredBlock<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().cubeAll(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    protected void saplingBlock(DeferredBlock<SaplingBlock> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    protected void hangingSignBlock(Block signBlock, Block wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(name(signBlock), texture);
        hangingSignBlock(signBlock, wallSignBlock, sign);
    }

    protected void hangingSignBlock(Block signBlock, Block wallSignBlock, ModelFile sign) {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    protected void makeFlower(DeferredBlock<Block> flowerBlock, DeferredBlock<FlowerPotBlock> potBlock) {
        simpleBlock(flowerBlock.get(),
                models().cross(blockTexture(flowerBlock.get()).getPath(),
                        blockTexture(flowerBlock.get())).renderType("cutout"));
        simpleBlock(potBlock.get(),
                models().withExistingParent(potBlock.getId().getPath(),
                                mcLoc("block/flower_pot_cross")).renderType("cutout")
                        .texture("plant", blockTexture(flowerBlock.get())));
    }

    protected void makeVegetation(DeferredBlock<Block> vegBlock) {
        simpleBlock(vegBlock.get(),
                models().cross(blockTexture(vegBlock.get()).getPath(),
                        blockTexture(vegBlock.get())).renderType("cutout"));
    }

    protected void makeMushroom(DeferredBlock<Block> mushroomBlock, DeferredBlock<FlowerPotBlock> potBlock) {
        simpleBlock(mushroomBlock.get(),
                models().cross(BuiltInRegistries.BLOCK.getKey(mushroomBlock.get()).getPath(),
                        blockTexture(mushroomBlock.get())).renderType("cutout"));
        simpleBlock(potBlock.get(),
                models().withExistingParent(potBlock.getId().getPath(),
                                mcLoc("block/flower_pot_cross")).renderType("cutout")
                        .texture("plant", blockTexture(mushroomBlock.get())));
    }

    protected void makeMushroom(DeferredBlock<Block> mushroomBlock) {
        simpleBlock(mushroomBlock.get(),
                models().cross(blockTexture(mushroomBlock.get()).getPath(),
                        blockTexture(mushroomBlock.get())).renderType("cutout"));
    }

    protected void makeVine(DeferredBlock<Block> vineBlock) {
        simpleBlock(vineBlock.get(),
                models().withExistingParent(vineBlock.getId().getPath(),
                                mcLoc("block/vine")).renderType("vine")
                        .texture("vine", blockTexture(vineBlock.get()))
        );
    }

    protected ResourceLocation modBlockResourceLocation(DeferredBlock<Block> block) {
        return modBlockResourceLocation(block.getId().getPath());
    }

    protected ResourceLocation modBlockResourceLocation(DeferredBlock<Block> block, String addedData) {
        return modBlockResourceLocation(block.getId().getPath() + addedData);
    }

    protected ResourceLocation modBlockResourceLocation(String block) {
        ResourceLocation resourceLocation;
        resourceLocation = ModUtils.getModPath("block/" + block);
        return resourceLocation;
    }

    /*
     * ChaosAwakens
     * https://github.com/ChaosAwakens/ChaosAwakens/tree/6bb21a2e15361e3aa3a15ebc1d427e5f746019cd
     */

    protected void grassBlock(DeferredBlock<Block> block, ResourceLocation particle, ResourceLocation bottom, ResourceLocation top, ResourceLocation side, ResourceLocation overlay) {
        grassBlock(block.get(), models().getExistingFile(modBlockResourceLocation(block.getId().getPath())));
    }

    protected void grassBlock(Block block, ModelFile model) {
        grassBlock(block, new ConfiguredModel(model));
    }

    protected void grassBlock(Block block, ConfiguredModel... model) {
        getVariantBuilder(block).partialState().addModels(model);
    }

    /**
     * This function simplifies the generation of block states for wood blocks.
     * @param log The log block.
     * @param wood The wood block.
     * @param strippedLog The stripped log block.
     * @param strippedWood The stripped wood block.
     * @param planks The planks block.
     * @param planksStairs The stair block.
     * @param planksSlab The slab block.
     * @param planksPressurePlate The pressureplate block.
     * @param planksFence The fence block.
     * @param planksFenceGate The fence gate block.
     * @param planksDoor The door block.
     * @param planksTrapDoor The trap door block.
     * @param planksButton The button block.
     * @param planksSapling The sapling block.
     * @param planksLeaves The leaves block.
     * @param planksSign The sign block.
     * @param planksWallSign The wall sign block.
     * @param planksHangingSign The hanging sign block.
     * @param planksHangingWallSign The hanging wall sign block.
     */
    protected void woodBlockGroup(DeferredBlock<RotatedPillarBlock> log, DeferredBlock<RotatedPillarBlock> wood, DeferredBlock<RotatedPillarBlock> strippedLog,
                               DeferredBlock<RotatedPillarBlock> strippedWood, DeferredBlock<Block> planks, DeferredBlock<StairBlock> planksStairs,
                               DeferredBlock<SlabBlock> planksSlab, DeferredBlock<PressurePlateBlock> planksPressurePlate, DeferredBlock<FenceBlock> planksFence,
                               DeferredBlock<FenceGateBlock> planksFenceGate, DeferredBlock<DoorBlock> planksDoor, DeferredBlock<TrapDoorBlock> planksTrapDoor,
                               DeferredBlock<ButtonBlock> planksButton, DeferredBlock<SaplingBlock> planksSapling, DeferredBlock<Block> planksLeaves,
                               DeferredBlock<StandingSignBlock> planksSign, DeferredBlock<WallSignBlock> planksWallSign,
                               DeferredBlock<CeilingHangingSignBlock> planksHangingSign, DeferredBlock<WallHangingSignBlock> planksHangingWallSign) {

        logBlock(((RotatedPillarBlock) log.get()));
        axisBlock((RotatedPillarBlock) wood.get(), blockTexture(log.get()), blockTexture(log.get()));
        axisBlock((RotatedPillarBlock) strippedLog.get(), modBlockResourceLocation(strippedLog.getId().getPath()), modBlockResourceLocation(strippedLog.getId().getPath() + "_top"));
        axisBlock((RotatedPillarBlock) strippedWood.get(), modBlockResourceLocation(strippedLog.getId().getPath()), modBlockResourceLocation(strippedLog.getId().getPath()));

        blockItem(log);
        blockItem(wood);
        blockItem(strippedLog);
        blockItem(strippedWood);
        blockWithItem(planks);

        stairsBlock((StairBlock) planksStairs.get(), blockTexture(planks.get()));
        slabBlock(((SlabBlock) planksSlab.get()), blockTexture(planks.get()), blockTexture(planks.get()));
        pressurePlateBlock((PressurePlateBlock) planksPressurePlate.get(), blockTexture(planks.get()));
        fenceBlock((FenceBlock) planksFence.get(), blockTexture(planks.get()));
        fenceGateBlock((FenceGateBlock) planksFenceGate.get(), blockTexture(planks.get()));
        doorBlockWithRenderType((DoorBlock) planksDoor.get(), modLoc("block/" + planksDoor.getId().getPath() + "_bottom"), modLoc("block/" + planksDoor.getId().getPath() + "_top"), "cutout");
        trapdoorBlockWithRenderType((TrapDoorBlock) planksTrapDoor.get(), modLoc("block/" + planksTrapDoor.getId().getPath()), true, "cutout");
        buttonBlock((ButtonBlock) planksButton.get(), blockTexture(planks.get()));

        blockItem(planksStairs);
        blockItem(planksSlab);
        blockItem(planksFenceGate);
        blockItem(planksPressurePlate);
        blockItem(planksTrapDoor, "_bottom");
        leavesBlock(planksLeaves);
        saplingBlock(planksSapling);
        signBlock(((StandingSignBlock) planksSign.get()),((WallSignBlock) planksWallSign.get()),
                blockTexture(planks.get()));
        hangingSignBlock(planksHangingSign.get(), planksHangingWallSign.get(),
                blockTexture(planks.get()));
    }

    /**
     * This function simplifies the generation of block states for stone blocks.
     * @param stone The stone block.
     * @param cobblestone The cobblestone block.
     * @param stoneBricks The stone bricks block.
     * @param stoneSmooth The smooth stone block.
     * @param cobblestoneMossy The mossy cobblestone block.
     * @param stoneBricksMossy The mossy stone bricks block.
     * @param stoneBricksCracked The cracked stone bricks block.
     * @param stoneBricksChiseled The chiseled stone bricks block.
     * @param stoneStairs The stone stairs block.
     * @param cobblestoneStairs The cobblestone stairs block.
     * @param stoneBricksStairs The stone bricks stairs block.
     * @param stoneSlabs The stone slab block.
     * @param cobblestoneSlabs The cobblestone slab block.
     * @param stoneBricksSlabs The stone bricks block.
     * @param stoneWall The stone wall block.
     * @param cobblestoneWall The cobblestone wall block.
     * @param stoneBricksWall The stone bricks wall block.
     * @param stonePressurePlate The stone pressureplate block.
     * @param stoneButton The stone button block.
     */
    protected void StoneBlockGroup(DeferredBlock<Block> stone, DeferredBlock<Block> cobblestone, DeferredBlock<Block> stoneBricks,
                                DeferredBlock<Block> stoneSmooth, DeferredBlock<Block> cobblestoneMossy, DeferredBlock<Block> stoneBricksMossy,
                                DeferredBlock<Block> stoneBricksCracked, DeferredBlock<Block> stoneBricksChiseled,
                                DeferredBlock<StairBlock> stoneStairs, DeferredBlock<StairBlock> cobblestoneStairs, DeferredBlock<StairBlock> stoneBricksStairs,
                                DeferredBlock<SlabBlock> stoneSlabs, DeferredBlock<SlabBlock> cobblestoneSlabs, DeferredBlock<SlabBlock> stoneBricksSlabs,
                                DeferredBlock<WallBlock> stoneWall, DeferredBlock<WallBlock> cobblestoneWall, DeferredBlock<WallBlock> stoneBricksWall,
                                DeferredBlock<PressurePlateBlock> stonePressurePlate, DeferredBlock<ButtonBlock> stoneButton) {

        blockWithItem(stone);
        blockWithItem(cobblestone);
        blockWithItem(stoneBricks);
        blockWithItem(stoneSmooth);
        blockWithItem(cobblestoneMossy);
        blockWithItem(stoneBricksMossy);
        blockWithItem(stoneBricksCracked);
        blockWithItem(stoneBricksChiseled);

        stairsBlock((StairBlock) stoneStairs.get(), blockTexture(stone.get()));
        stairsBlock((StairBlock) cobblestoneStairs.get(), blockTexture(cobblestone.get()));
        stairsBlock((StairBlock) stoneBricksStairs.get(), blockTexture(stoneBricks.get()));
        slabBlock(((SlabBlock) stoneSlabs.get()), blockTexture(stone.get()), blockTexture(stone.get()));
        slabBlock(((SlabBlock) cobblestoneSlabs.get()), blockTexture(cobblestone.get()), blockTexture(cobblestone.get()));
        slabBlock(((SlabBlock) stoneBricksSlabs.get()), blockTexture(stoneBricks.get()), blockTexture(stoneBricks.get()));
        wallBlock((WallBlock) stoneWall.get(), blockTexture(stone.get()));
        wallBlock((WallBlock) cobblestoneWall.get(), blockTexture(stone.get()));
        wallBlock((WallBlock) stoneBricksWall.get(), blockTexture(stone.get()));
        pressurePlateBlock((PressurePlateBlock) stonePressurePlate.get(), blockTexture(stone.get()));
        buttonBlock((ButtonBlock) stoneButton.get(), blockTexture(stone.get()));

        blockItem(stoneStairs);
        blockItem(stoneSlabs);
        blockItem(stonePressurePlate);
//        blockItem(stoneButton);
//        blockItem(stoneWall);
        blockItem(cobblestoneStairs);
        blockItem(cobblestoneSlabs);
//        blockItem(cobblestoneWall);
        blockItem(stoneBricksStairs);
        blockItem(stoneBricksSlabs);
//        blockItem(stoneBricksWall);
    }
}