package com.cerbon.bosses_of_mass_destruction.block;

import com.cerbon.bosses_of_mass_destruction.block.custom.*;
import com.cerbon.bosses_of_mass_destruction.util.BMDConstants;
import com.cerbon.cerbons_api.api.registry.RegistryEntry;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistries;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;

public class BMDBlocks {
    public static final ResourcefulRegistry<Block> BLOCKS = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, BMDConstants.MOD_ID);

    public static final RegistryEntry<Block> OBSIDILITH_RUNE = BLOCKS.register("obsidilith_rune", () ->
            new ObsidilithRuneBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));

    public static final RegistryEntry<Block> VOID_BLOSSOM = BLOCKS.register("void_blossom", () ->
            new VoidBlossomBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instabreak().noCollission().lightLevel(value -> 11).sound(SoundType.SPORE_BLOSSOM)));

    public static final RegistryEntry<Block> VINE_WALL = BLOCKS.register("vine_wall", () ->
            new VineWallBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).sound(SoundType.WOOD).strength(2.0f, 6.0f)));

    public static final RegistryEntry<Block> OBSIDILITH_SUMMON_BLOCK = BLOCKS.register("obsidilith_end_frame", () ->
            new ObsidilithSummonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_PORTAL_FRAME)));

    public static final RegistryEntry<Block> GAUNTLET_BLACKSTONE = BLOCKS.register("gauntlet_blackstone", () ->
            new GauntletBlackstoneBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));

    public static final RegistryEntry<Block> SEALED_BLACKSTONE = BLOCKS.register("sealed_blackstone", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK)));

    public static final RegistryEntry<Block> CHISELED_STONE_ALTAR = BLOCKS.register("chiseled_stone_altar", () ->
            new ChiseledStoneAltarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK).lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 11 : 0)));

    public static final RegistryEntry<Block> MOB_WARD = BLOCKS.register("mob_ward", () ->
            new MobWardBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().noOcclusion().lightLevel(value -> 15).strength(10.0F, 1200.0F)));

    public static final RegistryEntry<Block> MONOLITH_BLOCK = BLOCKS.register("monolith_block", () ->
            new MonolithBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().noOcclusion().lightLevel(value -> 4).strength(10.0F, 1200.0F)));

    public static final RegistryEntry<Block> LEVITATION_BLOCK = BLOCKS.register("levitation_block", () ->
            new LevitationBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).requiresCorrectToolForDrops().noOcclusion().lightLevel(value -> 4).strength(10.0F, 1200.0F)));

    public static final RegistryEntry<Block> VOID_BLOSSOM_SUMMON_BLOCK = BLOCKS.register("void_blossom_block", () ->
            new VoidBlossomSummonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK)));

    public static final RegistryEntry<Block> VOID_LILY_BLOCK = BLOCKS.register("void_lily", () ->
            new VoidLilyBlock(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).lightLevel(value -> 8)));

    public static void register() {
        BLOCKS.register();
    }
}