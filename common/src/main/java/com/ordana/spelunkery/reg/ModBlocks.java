package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.SpelunkeryPlatform;
import com.ordana.spelunkery.blocks.*;
import com.ordana.spelunkery.blocks.fungi.*;
import com.ordana.spelunkery.blocks.nephrite.*;
import com.ordana.spelunkery.blocks.rock_salt.*;
import com.ordana.spelunkery.configs.CommonConfigs;
import net.mehvahdjukaar.moonlight.api.block.ModStairBlock;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {

    public static void init() {
    }

    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }

    private static boolean ifIlluminated(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return state.getValue(ModBlockProperties.ILLUMINATED);
    }

    private static boolean ifLit(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return state.getValue(BlockStateProperties.LIT);
    }

    private static boolean ifNotEmpty(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return state.getValue(CarvedNephriteBlock.CHARGE) == CarvedNephriteBlock.ChargeState.FULL;
    }

    private static ToIntFunction<BlockState> createLightLevelFromIlluminatedBlockState(int litLevel) {
        return (state) -> (Boolean)state.getValue(ModBlockProperties.ILLUMINATED) ? litLevel : 0;
    }

    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int lightValue) {
        return (blockState) -> (Boolean)blockState.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    public static <T extends Block> Supplier<T> regBlock(String name, Supplier<T> block) {
        return RegHelper.registerBlock(Spelunkery.res(name), block);
    }

    public static <T extends Block> Supplier<T> regWithItem(String name, Supplier<T> blockFactory) {
        Supplier<T> block = regBlock(name, blockFactory);
        regBlockItem(name, block, new Item.Properties());
        return block;
    }

    public static <T extends Block> Supplier<T> regWithItem(String name, Supplier<T> block, String requiredMod) {
        if (isCompatBlockEanbled(requiredMod)) return regWithItem(name, block);
        else return null;
    }

    public static <T extends Block> Supplier<T> regWithItemConfigurable(String name, Supplier<T> block) {
        if (CommonConfigs.ENABLE_MORES.get()) return regWithItem(name, block);
        else return null;
    }

    public static <T extends Block> Supplier<T> regWithItemConfigurable(String name, Supplier<T> block, String requiredMod) {
        if (isCompatBlockEanbled(requiredMod) && CommonConfigs.ENABLE_MORES.get()) return regWithItem(name, block);
        else return null;
    }

    private static boolean isCompatBlockEanbled(String requiredMod) {
        if(Objects.equals(requiredMod, "quark")) {
            if (PlatHelper.getPlatform().isFabric()) {
                return requiredMod.equals("amogus");
            } else {
                return false;
                //return IntegrationHandler.quark && QuarkPlugin.isVerticalSlabsOn();
            }
        }
        return PlatHelper.isModLoaded(requiredMod);
    }


    public static Supplier<BlockItem> regBlockItem(String name, Supplier<? extends Block> blockSup, Item.Properties properties) {
        return RegHelper.registerItem(Spelunkery.res(name), () -> new BlockItem(blockSup.get(), properties));
    }

    //rough gem blocks
    public static final Supplier<Block> CALCITE_REDSTONE_ORE = regWithItem("calcite_redstone_ore", () ->
        new RedStoneOreBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f).sound(SoundType.CALCITE).lightLevel(createLightLevelFromLitBlockState(9)).randomTicks()));
    public static final Supplier<Block> SANDSTONE_LAPIS_ORE = regWithItem("sandstone_lapis_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE)
            .requiresCorrectToolForDrops().strength(2.5f, 3f)));
    public static final Supplier<Block> ANDESITE_EMERALD_ORE = regWithItem("andesite_emerald_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.EMERALD_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f)));
    public static final Supplier<Block> SMOOTH_BASALT_DIAMOND_ORE = regWithItem("smooth_basalt_diamond_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f).sound(SoundType.BASALT)));

    //mores
    public static final Supplier<Block> GRANITE_COAL_ORE = regWithItem("granite_coal_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.COAL_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f), UniformInt.of(0, 2)));
    public static final Supplier<Block> ANDESITE_COAL_ORE = regWithItem("andesite_coal_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_COAL_ORE.get()), UniformInt.of(0, 2)));
    public static final Supplier<Block> DIORITE_COAL_ORE = regWithItem("diorite_coal_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_COAL_ORE.get()), UniformInt.of(0, 2)));
    public static final Supplier<Block> TUFF_COAL_ORE = regWithItem("tuff_coal_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_COAL_ORE.get()).sound(SoundType.TUFF), UniformInt.of(0, 2)));

    public static final Supplier<Block> GRANITE_IRON_ORE = regWithItem("granite_iron_ore", () ->
        new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f)));
    public static final Supplier<Block> ANDESITE_IRON_ORE = regWithItem("andesite_iron_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_IRON_ORE.get())));
    public static final Supplier<Block> DIORITE_IRON_ORE = regWithItem("diorite_iron_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_IRON_ORE.get())));
    public static final Supplier<Block> TUFF_IRON_ORE = regWithItem("tuff_iron_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_IRON_ORE.get()).sound(SoundType.TUFF)));

    public static final Supplier<Block> GRANITE_COPPER_ORE = regWithItem("granite_copper_ore", () ->
        new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f)));
    public static final Supplier<Block> ANDESITE_COPPER_ORE = regWithItem("andesite_copper_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_COPPER_ORE.get())));
    public static final Supplier<Block> DIORITE_COPPER_ORE = regWithItem("diorite_copper_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_COPPER_ORE.get())));
    public static final Supplier<Block> TUFF_COPPER_ORE = regWithItem("tuff_copper_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_COPPER_ORE.get()).sound(SoundType.TUFF)));

    public static final Supplier<Block> GRANITE_GOLD_ORE = regWithItem("granite_gold_ore", () ->
        new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f)));
    public static final Supplier<Block> ANDESITE_GOLD_ORE = regWithItem("andesite_gold_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_GOLD_ORE.get())));
    public static final Supplier<Block> DIORITE_GOLD_ORE = regWithItem("diorite_gold_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_GOLD_ORE.get())));
    public static final Supplier<Block> TUFF_GOLD_ORE = regWithItem("tuff_gold_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_GOLD_ORE.get()).sound(SoundType.TUFF)));

    public static final Supplier<Block> GRANITE_REDSTONE_ORE = regWithItem("granite_redstone_ore", () ->
        new RedStoneOreBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f).randomTicks().lightLevel(ModBlocks.createLightLevelFromLitBlockState(9))));
    public static final Supplier<Block> ANDESITE_REDSTONE_ORE = regWithItem("andesite_redstone_ore", () ->
        new RedStoneOreBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_REDSTONE_ORE.get())));
    public static final Supplier<Block> DIORITE_REDSTONE_ORE = regWithItem("diorite_redstone_ore", () ->
        new RedStoneOreBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_REDSTONE_ORE.get())));
    public static final Supplier<Block> TUFF_REDSTONE_ORE = regWithItem("tuff_redstone_ore", () ->
        new RedStoneOreBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_REDSTONE_ORE.get()).sound(SoundType.TUFF)));

    public static final Supplier<Block> GRANITE_LAPIS_ORE = regWithItem("granite_lapis_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f), UniformInt.of(2, 5)));
    public static final Supplier<Block> ANDESITE_LAPIS_ORE = regWithItem("andesite_lapis_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_LAPIS_ORE.get()), UniformInt.of(2, 5)));
    public static final Supplier<Block> DIORITE_LAPIS_ORE = regWithItem("diorite_lapis_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_LAPIS_ORE.get()), UniformInt.of(2, 5)));
    public static final Supplier<Block> TUFF_LAPIS_ORE = regWithItem("tuff_lapis_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_LAPIS_ORE.get()).sound(SoundType.TUFF), UniformInt.of(2, 5)));

    public static final Supplier<Block> GRANITE_EMERALD_ORE = regWithItem("granite_emerald_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.EMERALD_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f), UniformInt.of(3, 7)));
    /*public static final Supplier<Block> ANDESITE_EMERALD_ORE = regWithItem("andesite_emerald_ore", () ->
            new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_EMERALD_ORE.get()), UniformInt.of(3, 7))); */
    public static final Supplier<Block> DIORITE_EMERALD_ORE = regWithItem("diorite_emerald_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_EMERALD_ORE.get()), UniformInt.of(3, 7)));
    public static final Supplier<Block> TUFF_EMERALD_ORE = regWithItem("tuff_emerald_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_EMERALD_ORE.get()).sound(SoundType.TUFF), UniformInt.of(3, 7)));

    public static final Supplier<Block> GRANITE_DIAMOND_ORE = regWithItem("granite_diamond_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f), UniformInt.of(3, 7)));
    public static final Supplier<Block> ANDESITE_DIAMOND_ORE = regWithItem("andesite_diamond_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_DIAMOND_ORE.get()), UniformInt.of(3, 7)));
    public static final Supplier<Block> DIORITE_DIAMOND_ORE = regWithItem("diorite_diamond_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_DIAMOND_ORE.get()), UniformInt.of(3, 7)));
    public static final Supplier<Block> TUFF_DIAMOND_ORE = regWithItem("tuff_diamond_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_DIAMOND_ORE.get()).sound(SoundType.TUFF), UniformInt.of(3, 7)));

    //rough gems
    public static final Supplier<Block> ROUGH_CINNABAR_BLOCK = regWithItem("rough_cinnabar_block", () ->
        new RoughCinnabarBlock(BlockBehaviour.Properties.copy(Blocks.RAW_COPPER_BLOCK).mapColor(MapColor.COLOR_RED)
            .requiresCorrectToolForDrops().strength(5f, 6f).sound(SoundType.CALCITE).lightLevel(createLightLevelFromLitBlockState(9)).randomTicks()));
    public static final Supplier<Block> ROUGH_LAZURITE_BLOCK = regWithItem("rough_lazurite_block", () ->
        new Block(BlockBehaviour.Properties.copy(Blocks.RAW_COPPER_BLOCK).mapColor(MapColor.LAPIS)
            .requiresCorrectToolForDrops().strength(5f, 6f).sound(SoundType.CALCITE)));
    public static final Supplier<Block> ROUGH_EMERALD_BLOCK = regWithItem("rough_emerald_block", () ->
        new Block(BlockBehaviour.Properties.copy(Blocks.RAW_COPPER_BLOCK).mapColor(MapColor.EMERALD)
            .requiresCorrectToolForDrops().strength(5f, 6f).sound(SoundType.CALCITE)));
    public static final Supplier<Block> ROUGH_DIAMOND_BLOCK = regWithItem("rough_diamond_block", () ->
        new Block(BlockBehaviour.Properties.copy(Blocks.RAW_COPPER_BLOCK).mapColor(MapColor.DIAMOND)
            .requiresCorrectToolForDrops().strength(5f, 6f).sound(SoundType.CALCITE)));
    public static final Supplier<Block> ROUGH_QUARTZ_BLOCK = regWithItem("rough_quartz_block", () ->
        new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.RAW_COPPER_BLOCK).mapColor(MapColor.QUARTZ)
            .requiresCorrectToolForDrops().strength(5f, 6f).sound(SoundType.CALCITE)));

    public static final Supplier<Block> CINNABAR_BLOCK = regWithItem("cinnabar_block", () ->
        new PoweredBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).mapColor(MapColor.COLOR_RED)
            .requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).isRedstoneConductor(ModBlocks::never)));

    public static final Supplier<Block> DIAMOND_GRINDSTONE = regWithItem("diamond_grindstone", () ->
        new DiamondGrindstoneBlock(BlockBehaviour.Properties.copy(Blocks.GRINDSTONE)));


    //rock salt
    public static final Supplier<Block> ROCK_SALT = regBlock("rock_salt", () ->
        new RockSaltCrystalBlock(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.TERRACOTTA_PINK)
            .requiresCorrectToolForDrops().strength(3f, 2f).sound(SoundType.CALCITE).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated).noOcclusion()));
    public static final Supplier<Block> SALT_LAMP = regWithItem("salt_lamp", () ->
        new SaltLampBlock(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.TERRACOTTA_PINK)
            .strength(0.5f, 2f).sound(SoundType.CALCITE).lightLevel(createLightLevelFromLitBlockState(7)).emissiveRendering(ModBlocks::ifLit).noOcclusion()));
    public static final Supplier<Block> SALT = regBlock("salt", () ->
        new SaltBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE).mapColor(MapColor.TERRACOTTA_PINK).instabreak().randomTicks().noCollission().sound(SoundType.SAND)));
    public static final Supplier<Block> SALT_BLOCK = regWithItem("salt_block", () ->
        new SaltBlockBlock(14606046, BlockBehaviour.Properties.copy(Blocks.SAND).mapColor(MapColor.TERRACOTTA_PINK).strength(0.5F).sound(SoundType.SAND)));

    public static final Supplier<Block> ROCK_SALT_BLOCK = regWithItem("rock_salt_block", () ->
        new RockSaltBlock(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.TERRACOTTA_PINK)
            .requiresCorrectToolForDrops().strength(3f, 2f).sound(SoundType.CALCITE).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> ROCK_SALT_SLAB = regWithItem("rock_salt_slab", () ->
        new RockSaltSlab(BlockBehaviour.Properties.copy(ROCK_SALT_BLOCK.get()).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> ROCK_SALT_STAIRS = regWithItem("rock_salt_stairs", () ->
        new RockSaltStairs(ROCK_SALT_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.copy(ROCK_SALT_BLOCK.get()).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> ROCK_SALT_WALL = regWithItem("rock_salt_wall", () ->
        new RockSaltWall(BlockBehaviour.Properties.copy(ROCK_SALT_BLOCK.get()).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));

    public static final Supplier<Block> POLISHED_ROCK_SALT = regWithItem("polished_rock_salt", () ->
        new RockSaltBlock(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.TERRACOTTA_PINK)
            .requiresCorrectToolForDrops().strength(3f, 2f).sound(SoundType.CALCITE).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> POLISHED_ROCK_SALT_SLAB = regWithItem("polished_rock_salt_slab", () ->
        new RockSaltSlab(BlockBehaviour.Properties.copy(POLISHED_ROCK_SALT.get()).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> POLISHED_ROCK_SALT_STAIRS = regWithItem("polished_rock_salt_stairs", () ->
        new RockSaltStairs(POLISHED_ROCK_SALT.get().defaultBlockState(), BlockBehaviour.Properties.copy(POLISHED_ROCK_SALT.get()).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> POLISHED_ROCK_SALT_WALL = regWithItem("polished_rock_salt_wall", () ->
        new RockSaltWall(BlockBehaviour.Properties.copy(POLISHED_ROCK_SALT.get()).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));

    public static final Supplier<Block> ROCK_SALT_BRICKS = regWithItem("rock_salt_bricks", () ->
        new RockSaltBlock(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.TERRACOTTA_PINK)
            .requiresCorrectToolForDrops().strength(3f, 2f).sound(SoundType.CALCITE).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> ROCK_SALT_BRICK_SLAB = regWithItem("rock_salt_brick_slab", () ->
        new RockSaltSlab(BlockBehaviour.Properties.copy(ROCK_SALT_BRICKS.get()).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> ROCK_SALT_BRICK_STAIRS = regWithItem("rock_salt_brick_stairs", () ->
        new RockSaltStairs(ROCK_SALT_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(ROCK_SALT_BRICKS.get()).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> ROCK_SALT_BRICK_WALL = regWithItem("rock_salt_brick_wall", () ->
        new RockSaltWall(BlockBehaviour.Properties.copy(ROCK_SALT_BRICKS.get()).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));

    public static final Supplier<Block> POLISHED_QUARTZ_BLOCK = regWithItem("polished_quartz_block", () ->
        new Block(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.QUARTZ)
            .requiresCorrectToolForDrops().strength(3f, 2f).sound(SoundType.CALCITE)));

    public static final Supplier<Block> SALTPETER_BLOCK = regWithItem("saltpeter_block", () ->
        new SandBlock(14407892, BlockBehaviour.Properties.copy(Blocks.SAND).mapColor(MapColor.TERRACOTTA_WHITE).strength(0.5F).sound(SoundType.SAND)));
    public static final Supplier<Block> SULFUR_BLOCK = regWithItem("sulfur_block", () ->
        new SandBlock(14794633, BlockBehaviour.Properties.copy(Blocks.SAND).mapColor(MapColor.TERRACOTTA_YELLOW).strength(0.5F).sound(SoundType.SAND)));
    public static final Supplier<Block> SULFUR_GEYSER = regWithItem("sulfur_geyser", () ->
        new SulfuricVentBlock(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.SAND)));

    //nephrite
    public static final Supplier<Block> RAW_NEPHRITE = regWithItem("raw_nephrite", () ->
        new RawNephriteBlock(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.EMERALD).requiresCorrectToolForDrops().strength(3f, 2f)));
    public static final Supplier<Block> NEPHRITE = regWithItem("nephrite", () ->
        new Block(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.EMERALD).requiresCorrectToolForDrops().strength(3f, 2f)));
    public static final Supplier<Block> NEPHRITE_SLAB = regWithItem("nephrite_slab", () ->
        new SlabBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> NEPHRITE_STAIRS = regWithItem("nephrite_stairs", () ->
        new ModStairBlock(NEPHRITE, BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> NEPHRITE_WALL = regWithItem("nephrite_wall", () ->
        new WallBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));

    public static final Supplier<Block> POLISHED_NEPHRITE = regWithItem("polished_nephrite", () ->
        new Block(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> POLISHED_NEPHRITE_SLAB = regWithItem("polished_nephrite_slab", () ->
        new SlabBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> POLISHED_NEPHRITE_STAIRS = regWithItem("polished_nephrite_stairs", () ->
        new ModStairBlock(NEPHRITE, BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> POLISHED_NEPHRITE_WALL = regWithItem("polished_nephrite_wall", () ->
        new WallBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));

    public static final Supplier<Block> POLISHED_NEPHRITE_BRICKS = regWithItem("polished_nephrite_bricks", () ->
        new Block(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> POLISHED_NEPHRITE_BRICK_SLAB = regWithItem("polished_nephrite_brick_slab", () ->
        new SlabBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> POLISHED_NEPHRITE_BRICK_STAIRS = regWithItem("polished_nephrite_brick_stairs", () ->
        new ModStairBlock(NEPHRITE, BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> POLISHED_NEPHRITE_BRICK_WALL = regWithItem("polished_nephrite_brick_wall", () ->
        new WallBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));

    public static final Supplier<Block> CARVED_NEPHRITE = regBlock("carved_nephrite", () ->
        new CarvedNephriteBlock(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.EMERALD).requiresCorrectToolForDrops().strength(3f, 2f).emissiveRendering(ModBlocks::ifNotEmpty)));
    public static final Supplier<Block> NEPHRITE_SIPHON = regBlock("nephrite_siphon", () ->
        new NephriteSiphonBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> NEPHRITE_FOUNTAIN = regBlock("nephrite_fountain", () ->
        new NephriteFountainBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> NEPHRITE_DIODE = regBlock("nephrite_diode", () ->
        new NephriteDiodeBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));

    public static final Supplier<Block> COMPRESSION_BLAST_MINER = regBlock("compression_blast_miner", () ->
        new CompressionBlastMiner(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> MINEOMITE = regBlock("mineomite", () ->
        new MineomiteBlock(BlockBehaviour.Properties.copy(Blocks.TNT).sound(SoundType.CANDLE)));

    public static final Supplier<Block> RAW_MAGNETITE_BLOCK = regWithItem("raw_magnetite_block", () ->
        new MagnetiteBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.LODESTONE)));


    public static final Supplier<Block> DUST_BLOCK = regWithItem("dust_block", () ->
        new DustBlockBlock(BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.WOOL).mapColor(MapColor.COLOR_GRAY)));
    public static final Supplier<Block> DUST = regWithItem("dust", () ->
        new DustBlock(BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.WOOL).mapColor(MapColor.COLOR_GRAY)));
    public static final Supplier<Block> BUNNY_EARS = regBlock("bunny_ears", () ->
        new BunnyEarsUtilBlock(BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.WOOL).mapColor(MapColor.COLOR_GRAY)));
    public static final Supplier<Block> SULFUR = regWithItem("sulfur", () ->
        new FallingLayerBlock(BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.SAND).mapColor(MapColor.TERRACOTTA_YELLOW)));
    public static final Supplier<Block> SALTPETER = regWithItem("saltpeter", () ->
        new FallingLayerBlock(BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.SAND).mapColor(MapColor.TERRACOTTA_WHITE)));

    //plants
    public static final Supplier<Block> TANGLE_ROOTS = regWithItem("tangle_roots", () ->
        new TangleRootsHeadBlock(BlockBehaviour.Properties.copy(Blocks.WEEPING_VINES).mapColor(MapColor.COLOR_BROWN).randomTicks().noCollission().instabreak().sound(SoundType.WEEPING_VINES)));
    public static final Supplier<Block> TANGLE_ROOTS_PLANT = regBlock("tangle_roots_plant", () ->
        new TangleRootsBodyBlock(BlockBehaviour.Properties.copy(Blocks.WEEPING_VINES_PLANT).mapColor(MapColor.COLOR_BROWN).randomTicks().noCollission().instabreak().sound(SoundType.WEEPING_VINES)));
    public static final Supplier<Block> TANGLE_ROOTS_BLOCK = regWithItem("tangle_roots_block", () ->
        new TangleRootsBlockBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).strength(3f, 0.5f).randomTicks().sound(SoundType.MANGROVE_ROOTS).ignitedByLava()));

    public static final Supplier<Block> SPOROPHYTE = regWithItem("sporophyte", () ->
        new SporophyteBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).noCollission().instabreak().sound(SoundType.MOSS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final Supplier<Block> TALL_SPOROPHYTE = regWithItem("tall_sporophyte", () ->
        new DoublePlantBlock(BlockBehaviour.Properties.copy(Blocks.TALL_GRASS).noCollission().instabreak().sound(SoundType.MOSS).offsetType(BlockBehaviour.OffsetType.XZ)));



    //fungi
    public static final Supplier<Block> CONK_FUNGUS = regWithItem("conk_fungus", () ->
        new ConkFungusBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).noCollission().instabreak().sound(SoundType.FUNGUS)));
    public static final Supplier<Block> PORTABELLA = regBlock("portabella", () ->
        new GrowableMushroomBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission().randomTicks().instabreak().sound(SoundType.FUNGUS).offsetType(BlockBehaviour.OffsetType.XZ).hasPostProcess(ModBlocks::always)));
    public static final Supplier<Block> CRIMINI = regBlock("crimini", () ->
        new ModMushroomBlock(BlockBehaviour.Properties.copy(PORTABELLA.get())));
    public static final Supplier<Block> BUTTON_MUSHROOM = regBlock("button_mushroom", () ->
        new ModMushroomBlock(BlockBehaviour.Properties.copy(PORTABELLA.get())));
    public static final Supplier<Block> INKCAP_MUSHROOM = regWithItem("inkcap_mushroom", () ->
        new GrowableMushroomBlock(BlockBehaviour.Properties.copy(PORTABELLA.get()).hasPostProcess(ModBlocks::always)));
    public static final Supplier<Block> WHITE_INKCAP_MUSHROOM = regWithItem("white_inkcap_mushroom", () ->
        new GrowableMushroomBlock(BlockBehaviour.Properties.copy(PORTABELLA.get()).hasPostProcess(ModBlocks::always)));
    public static final Supplier<Block> PHOSPHOR_FUNGUS = regWithItem("phosphor_fungus", () ->
        new FloorAndSidesMushroomBlock(BlockBehaviour.Properties.copy(ModBlocks.CONK_FUNGUS.get()).emissiveRendering(ModBlocks::always).lightLevel((blockStatex) -> 3)));
    public static final Supplier<Block> MUSHGLOOM = regWithItem("mushgloom", () ->
        new FloorAndSidesMushroomBlock(BlockBehaviour.Properties.copy(ModBlocks.CONK_FUNGUS.get()).emissiveRendering(ModBlocks::always).lightLevel((blockStatex) -> 1)));
    public static final Supplier<Block> MILLY_BUBCAP = regWithItem("milly_bubcap", () ->
        new MillyBubcapMushroomBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission().instabreak().sound(SoundType.FUNGUS).offsetType(BlockBehaviour.OffsetType.XZ)));

    public static final Supplier<Block> POTTED_PORTABELLA = regBlock("potted_portabella", () ->
        new FlowerPotBlock(PORTABELLA.get(), BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY).instabreak().noOcclusion()));
    public static final Supplier<Block> POTTED_CRIMINI = regBlock("potted_crimini", () ->
        new FlowerPotBlock(CRIMINI.get(), BlockBehaviour.Properties.copy(POTTED_PORTABELLA.get())));
    public static final Supplier<Block> POTTED_BUTTON_MUSHROOM = regBlock("potted_button_mushroom", () ->
        new FlowerPotBlock(BUTTON_MUSHROOM.get(), BlockBehaviour.Properties.copy(POTTED_PORTABELLA.get())));
    public static final Supplier<Block> POTTED_INKCAP_MUSHROOM = regBlock("potted_inkcap_mushroom", () ->
        new FlowerPotBlock(INKCAP_MUSHROOM.get(), BlockBehaviour.Properties.copy(POTTED_PORTABELLA.get())));
    public static final Supplier<Block> POTTED_WHITE_INKCAP_MUSHROOM = regBlock("potted_white_inkcap_mushroom", () ->
        new FlowerPotBlock(WHITE_INKCAP_MUSHROOM.get(), BlockBehaviour.Properties.copy(POTTED_PORTABELLA.get())));
    public static final Supplier<Block> POTTED_PHOSPHOR_FUNGUS = regBlock("potted_phosphor_fungus", () ->
        new FlowerPotBlock(PHOSPHOR_FUNGUS.get(), BlockBehaviour.Properties.copy(POTTED_PORTABELLA.get()).emissiveRendering(ModBlocks::always).lightLevel((blockStatex) -> 3)));
    public static final Supplier<Block> POTTED_MUSHGLOOM = regBlock("potted_mushgloom", () ->
        new FlowerPotBlock(MUSHGLOOM.get(), BlockBehaviour.Properties.copy(POTTED_PORTABELLA.get()).emissiveRendering(ModBlocks::always).lightLevel((blockStatex) -> 1)));
    public static final Supplier<Block> POTTED_MILLY_BUBCAP = regBlock("potted_milly_bubcap", () ->
        new FlowerPotBlock(MILLY_BUBCAP.get(), BlockBehaviour.Properties.copy(POTTED_PORTABELLA.get())));
    public static final Supplier<Block> POTTED_SPOROPHYTE = regBlock("potted_sporophyte", () ->
        new FlowerPotBlock(SPOROPHYTE.get(), BlockBehaviour.Properties.copy(POTTED_PORTABELLA.get())));

    public static final Supplier<Block> CONK_FUNGUS_BLOCK = regWithItem("conk_fungus_block", () ->
        new HugeMushroomBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM_BLOCK).mapColor(MapColor.DIRT).strength(0.2F).sound(SoundType.STEM)));
    public static final Supplier<Block> PORTABELLA_BLOCK = regWithItem("portabella_block", () ->
        new HugeMushroomBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM_BLOCK).mapColor(MapColor.DIRT).strength(0.2F).sound(SoundType.WOOD)));
    public static final Supplier<Block> INKCAP_MUSHROOM_BLOCK = regWithItem("inkcap_mushroom_block", () ->
        new HugeMushroomBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM_BLOCK).mapColor(MapColor.TERRACOTTA_BLACK).strength(0.2F).sound(SoundType.WOOD)));
    public static final Supplier<Block> WHITE_INKCAP_MUSHROOM_BLOCK = regWithItem("white_inkcap_mushroom_block", () ->
        new HugeMushroomBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM_BLOCK).mapColor(MapColor.SAND).strength(0.2F).sound(SoundType.WOOD)));
    public static final Supplier<Block> MILLY_BUBCAP_BLOCK = regWithItem("milly_bubcap_block", () ->
        new HugeMushroomBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN).strength(0.2F).sound(SoundType.WOOD)));
    public static final Supplier<Block> PHOSPHOR_FUNGUS_BLOCK = regWithItem("phosphor_fungus_block", () ->
        new PhosphorFungusBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_BLUE).strength(0.2F).sound(SoundType.WOOD).emissiveRendering(ModBlocks::always).noOcclusion().isValidSpawn(ModBlocks::never).isRedstoneConductor(ModBlocks::never).isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never).emissiveRendering(ModBlocks::always).lightLevel((blockStatex) -> 1)));
    public static final Supplier<Block> PHOSPHOR_SHROOMLIGHT = regWithItem("phosphor_shroomlight", () ->
        new Block(BlockBehaviour.Properties.copy(Blocks.SHROOMLIGHT).mapColor(MapColor.COLOR_CYAN).sound(SoundType.SHROOMLIGHT).emissiveRendering(ModBlocks::always).lightLevel((blockStatex) -> 8)));
    public static final Supplier<Block> MUSHGLOOM_BLOCK = regWithItem("mushgloom_block", () ->
        new HugeMushroomBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM_BLOCK).mapColor(MapColor.TERRACOTTA_BLUE).strength(0.2F).sound(SoundType.WOOD).emissiveRendering(ModBlocks::always).lightLevel((blockStatex) -> 1)));
    public static final Supplier<Block> CAVE_MUSHROOM_STEM = regWithItem("cave_mushroom_stem", () ->
        new HugeMushroomBlock(BlockBehaviour.Properties.copy(Blocks.MUSHROOM_STEM).mapColor(MapColor.TERRACOTTA_GRAY).strength(0.2F).sound(SoundType.WOOD)));



    //mining gear
    public static final Supplier<Block> GLOWSTICK = regBlock("glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(Blocks.END_ROD).instabreak().noCollission().noOcclusion().emissiveRendering(ModBlocks::always).lightLevel((blockStatex) -> 14).sound(SoundType.CANDLE)));
    public static final Supplier<Block> ROPE_LADDER = regBlock("rope_ladder", () ->
        new RopeLadderBlock(BlockBehaviour.Properties.copy(Blocks.LADDER).strength(1f).sound(SoundType.WOOD)));
    public static final Supplier<Block> WOODEN_RAIL = regBlock("wooden_rail", () ->
        new WoodenRailBlock(true, BlockBehaviour.Properties.copy(Blocks.RAIL).noOcclusion().strength(0.7F).sound(SoundType.WOOD).instabreak()));

    //glowsticks
    public static final Supplier<Block> RED_GLOWSTICK = regBlock("red_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> ORANGE_GLOWSTICK = regBlock("orange_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> YELLOW_GLOWSTICK = regBlock("yellow_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> LIME_GLOWSTICK = regBlock("lime_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> GREEN_GLOWSTICK = regBlock("green_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> CYAN_GLOWSTICK = regBlock("cyan_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> LIGHT_BLUE_GLOWSTICK = regBlock("light_blue_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> BLUE_GLOWSTICK = regBlock("blue_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> PURPLE_GLOWSTICK = regBlock("purple_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> MAGENTA_GLOWSTICK = regBlock("magenta_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> PINK_GLOWSTICK = regBlock("pink_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> BROWN_GLOWSTICK = regBlock("brown_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> BLACK_GLOWSTICK = regBlock("black_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> WHITE_GLOWSTICK = regBlock("white_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> GRAY_GLOWSTICK = regBlock("gray_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> LIGHT_GRAY_GLOWSTICK = regBlock("light_gray_glowstick", () ->
        new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));


    public static final Supplier<Block> WOODEN_CHANNEL = regBlock("wooden_channel", () ->
        new ChannelBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).ignitedByLava()));
    public static final Supplier<Block> WOODEN_SLUICE = regBlock("wooden_sluice", () ->
        new ChannelSluiceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).ignitedByLava()));
    public static final Supplier<Block> STONE_CHANNEL = regBlock("stone_channel", () ->
        new ChannelBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final Supplier<Block> STONE_SLUICE = regBlock("stone_sluice", () ->
        new ChannelSluiceBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));



    public static final Supplier<Block> PORTAL_CAULDRON = regBlock("portal_cauldron", () ->
        new PortalFluidCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON).lightLevel((blockStatex) -> 5), CauldronInteraction.WATER));

    //fluids
    public static final Supplier<LiquidBlock> PORTAL_FLUID = regBlock("portal_fluid", () ->
        SpelunkeryPlatform.doPortalFluid(ModFluids.PORTAL_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER).noCollission().strength(100f).noLootTable().lightLevel((blockStatex) -> 5)));
    public static final Supplier<LiquidBlock> SPRING_WATER = regBlock("spring_water", () ->
        SpelunkeryPlatform.doSpringWater(ModFluids.SPRING_WATER, BlockBehaviour.Properties.copy(Blocks.WATER).noCollission().strength(100f).noLootTable().lightLevel((blockStatex) -> 2)));


    //mod support

    public static final Supplier<Block> GRANITE_ZINC_ORE = regWithItemConfigurable("granite_zinc_ore", () ->
        new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f)));
    public static final Supplier<Block> ANDESITE_ZINC_ORE = regWithItemConfigurable("andesite_zinc_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_ZINC_ORE.get())));
    public static final Supplier<Block> DIORITE_ZINC_ORE = regWithItemConfigurable("diorite_zinc_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_ZINC_ORE.get())));
    public static final Supplier<Block> TUFF_ZINC_ORE = regWithItemConfigurable("tuff_zinc_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_ZINC_ORE.get()).sound(SoundType.TUFF)));

    public static final Supplier<Block> GRANITE_LEAD_ORE = regWithItemConfigurable("granite_lead_ore", () ->
        new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f)));
    public static final Supplier<Block> ANDESITE_LEAD_ORE = regWithItemConfigurable("andesite_lead_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_LEAD_ORE.get())));
    public static final Supplier<Block> DIORITE_LEAD_ORE = regWithItemConfigurable("diorite_lead_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_LEAD_ORE.get())));
    public static final Supplier<Block> TUFF_LEAD_ORE = regWithItemConfigurable("tuff_lead_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_LEAD_ORE.get()).sound(SoundType.TUFF)));

    public static final Supplier<Block> GRANITE_SILVER_ORE = regWithItemConfigurable("granite_silver_ore", () ->
        new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f)));
    public static final Supplier<Block> ANDESITE_SILVER_ORE = regWithItemConfigurable("andesite_silver_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_SILVER_ORE.get())));
    public static final Supplier<Block> DIORITE_SILVER_ORE = regWithItemConfigurable("diorite_silver_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_SILVER_ORE.get())));
    public static final Supplier<Block> TUFF_SILVER_ORE = regWithItemConfigurable("tuff_silver_ore", () ->
        new Block(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_SILVER_ORE.get()).sound(SoundType.TUFF)));

    public static final Supplier<Block> GRANITE_JADE_ORE = regWithItemConfigurable("granite_jade_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.EMERALD_ORE)
            .requiresCorrectToolForDrops().strength(3f, 3f)));
    public static final Supplier<Block> ANDESITE_JADE_ORE = regWithItemConfigurable("andesite_jade_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_JADE_ORE.get())));
    public static final Supplier<Block> DIORITE_JADE_ORE = regWithItemConfigurable("diorite_jade_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_JADE_ORE.get())));
    public static final Supplier<Block> TUFF_JADE_ORE = regWithItemConfigurable("tuff_jade_ore", () ->
        new DropExperienceBlock(BlockBehaviour.Properties.copy(ModBlocks.GRANITE_JADE_ORE.get()).sound(SoundType.TUFF)));



}
