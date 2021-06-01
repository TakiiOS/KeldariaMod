/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.registry;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.api.registry.Register;
import fr.nathanael2611.keldaria.mod.block.*;
import fr.nathanael2611.keldaria.mod.block.furniture.*;
import fr.nathanael2611.keldaria.mod.features.EnumFlower;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Keldaria.MOD_ID)
public class KeldariaBlocks
{

    public static final HashMap<String, BlockFlower> FLOWERS = Maps.newHashMap();

    @Register(name = "pigeon_cage")
    public static final Block PIGEON_CAGE = new BlockPigeonCage();

    @Register(name = "wallpaper")
    public static final Block WALLPAPER = new BlockWallpaper();

    @Register(name = "card_carpet")
    public static final Block CARD_CARPET = new BlockCardCarpet();

    @Register(name = "crafting_anvil")
    public static final Block CRAFTING_ANVIL = new BlockCraftingAnvil();

    @Register(name = "cooking_furnace")
    public static final Block COOKING_FURNACE = new BlockCookingFurnace();
    @Register(name = "blast_furnace")
    public static final Block BLAST_FURNACE = new BlockBlastFurnace(false);
    @Register(name = "lit_blast_furnace")
    public static final Block LIT_BLAST_FURNACE = new BlockBlastFurnace(true);

    @Register(name = "white_onion", hasItem = false)
    public static final Block WHITE_ONION = new BlockWhiteOnion();

    @Register(name = "onion", hasItem = false)
    public static final Block ONION = new BlockOnion();

    @Register(name = "orge", hasItem = false)
    public static final Block ORGE = new BlockOrge();

    @Register(name = "mill")
    public static final Block MILL = new BlockMill();

    @Register(name = "beer_barell")
    public static final Block BEER_BARELL = new BlockBeerBarell();

    @Register(name = "brazier")
    public static final Block BRAZIER = new BlockBrazier();

    @Register(name = "sharpening_stone")
    public static final Block SHARPENING_STONE = new BlockSharpeningStone();

    @Register(name = "millstone")
    public static final Block MILLSTONE = new BlockMillstone();

    @Register(name = "drum")
    public static final Block DRUM = new BlockInstrument("drum", new SoundEvent[]{SoundEvents.ENTITY_ITEM_BREAK}, new SoundEvent[]{SoundEvents.ENTITY_HORSE_JUMP});

    @Register(name = "woodworker_table")
    public static final Block WOODWORKER_TABLE = new BlockWoodworkerTable();

    @Register(name = "cooking_table")
    public static final Block COOKING_TABLE = new BlockCookingTable();

    @Register(name = "stonecutter")
    public static final Block STONECUTTER = new BlockStonecutter();

    @Register(name = "portcullis")
    public static final Block PORTCULLIS = new BlockPortcullis();

    @Register(name = "weaving_loom")
    public static final Block WEAVING_LOOM = new BlockWeavingLoom();

    @Register(name = "flowering_pot")
    public static final Block FLOWERING_POT = new BlockFloweringPot(Material.LEAVES);

    @Register(name = "vine_support")
    public static final Block VINE_SUPPORT = new BlockVineSupport();

    @Register(name = "salt_bag")
    public static final Block SALT_BAG = new BlockSaltBag();

    @Register(name = "hrp_sign")
    public static final Block HRP_SIGN = new BlockHRPSign();

    @Register(name = "hrp_carpet")
    public static final Block HRP_CARPET = new BlockHRPCarpet();


    @Register(name = "feeder")
    public static final Block FEEDER = new BlockFeeder();

    @Register(name = "custom_painting")
    public static final Block CUSTOM_PAINTING = new BlockCustomPainting();


    @Register(name = "clothing_mannequin")
    public static final Block CLOTHING_MANNEQUIN = new BlockClothingMannequin();


    public static final AxisAlignedBB chair_bb = new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.6, 0.9);
    @Register(name = "oak_chair")

    public static final Block OAK_CHAIR = new BlockRotatableFurniture(Material.WOOD, chair_bb);
    @Register(name = "spruce_chair")
    public static final Block SPRUCE_CHAIR = new BlockRotatableFurniture(Material.WOOD, chair_bb);
    @Register(name = "dark_oak_chair")
    public static final Block DARK_OAK_CHAIR = new BlockRotatableFurniture(Material.WOOD, chair_bb);
    @Register(name = "birch_chair")
    public static final Block BIRCH_CHAIR = new BlockRotatableFurniture(Material.WOOD, chair_bb);

    @Register(name = "oak_table")
    public static final Block OAK_TABLE = new BlockTable(Material.WOOD);
    @Register(name = "spruce_table")
    public static final Block SPRUCE_TABLE = new BlockTable(Material.WOOD);
    @Register(name = "dark_oak_table")
    public static final Block DARK_OAK_TABLE = new BlockTable(Material.WOOD);
    @Register(name = "birch_table")
    public static final Block BIRCH_TABLE = new BlockTable(Material.WOOD);

    @Register(name = "iron_bar")
    public static final Block IRON_BAR = new BlockIronBar(Material.IRON);
    @Register(name = "chandeler")
    public static final Block CHANDELER = new BlockChandeler().setLightLevel(0.9375F);
    @Register(name = "chain")
    public static final Block CHAIN = new BlockChain();
    @Register(name = "cobblestone_bench")
    public static final Block COBBLESTONE_BENCH = new BlockBench(Material.ROCK);
    @Register(name = "stone_bench")
    public static final Block STONE_BENCH = new BlockBench(Material.ROCK);
    @Register(name = "diorite_bench")
    public static final Block DIORITE_BENCH = new BlockBench(Material.ROCK);
    @Register(name = "andesite_bench")
    public static final Block ANDESITE_BENCH = new BlockBench(Material.ROCK);
    @Register(name = "granite_bench")
    public static final Block GRANITE_BENCH = new BlockBench(Material.ROCK);

    @Register(name = "oak_cabinet")
    public static final Block OAK_CABINET = new BlockFurnitureContainer(Material.WOOD, null);
    @Register(name = "spruce_cabinet")
    public static final Block SPRUCE_CABINET = new BlockFurnitureContainer(Material.WOOD, null);
    @Register(name = "dark_oak_cabinet")
    public static final Block DARK_OAK_CABINET = new BlockFurnitureContainer(Material.WOOD, null);
    @Register(name = "birch_cabinet")
    public static final Block BIRCH_CABINET = new BlockFurnitureContainer(Material.WOOD, null);

    @Register(name = "oak_crate")
    public static final Block OAK_CRATE = new BlockFurnitureContainer(Material.WOOD, null);
    @Register(name = "spruce_crate")
    public static final Block SPRUCE_CRATE = new BlockFurnitureContainer(Material.WOOD, null);
    @Register(name = "dark_oak_crate")
    public static final Block DARK_OAK_CRATE = new BlockFurnitureContainer(Material.WOOD, null);
    @Register(name = "birch_crate")
    public static final Block BIRCH_CRATE = new BlockFurnitureContainer(Material.WOOD, null);

    @Register(name = "oak_wall_cabinet")
    public static final Block OAK_WALL_CABINET = new BlockWallCabinet(Material.WOOD);
    @Register(name = "spruce_wall_cabinet")
    public static final Block SPRUCE_WALL_CABINET = new BlockWallCabinet(Material.WOOD);
    @Register(name = "dark_oak_wall_cabinet")
    public static final Block DARK_OAK_WALL_CABINET = new BlockWallCabinet(Material.WOOD);
    @Register(name = "birch_wall_cabinet")
    public static final Block BIRCH_WALL_CABINET = new BlockWallCabinet(Material.WOOD);

    @Register(name = "fruit_block")
    public static final Block FRUIT_BLOCK = new BlockFruit();

    @Register(name = "sieve")
    public static final Block SIEVE = new BlockSieve();

    @Register(name = "oak_refined_log")
    public static final Block OAK_REFINED_LOG = new BlockRefinedLog();
    @Register(name = "spruce_refined_log")
    public static final Block SPRUCE_REFINED_LOG = new BlockRefinedLog();
    @Register(name = "dark_oak_refined_log")
    public static final Block DARK_OAK_REFINED_LOG = new BlockRefinedLog();
    @Register(name = "birch_refined_log")
    public static final Block BIRCH_REFINED_LOG = new BlockRefinedLog();
    @Register(name = "acacia_refined_log")
    public static final Block ACACIA_REFINED_LOG = new BlockRefinedLog();
    @Register(name = "jungle_refined_log")
    public static final Block JUNGLE_REFINED_LOG = new BlockRefinedLog();



    @Register(name = "candlestick")
    public static final Block CANDLESTICK = new BlockRotatableFurniture(Material.IRON, new AxisAlignedBB(0.4, 0, 0.4, 0.6, 0.8, 0.6))
    {
        @Override
        public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
        {
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 0, 0, 0);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.2, 0, 0, 0);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.8, 0, 0, 0);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.8, pos.getY() + 1, pos.getZ() + 0.5, 0, 0, 0);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.2, pos.getY() + 1, pos.getZ() + 0.5, 0, 0, 0);
            super.randomDisplayTick(stateIn, worldIn, pos, rand);
        }
    }.setLightLevel(0.96f);
    @Register(name = "big_lantern")
    public static final Block BIG_LANTERN = new BlockRotatableFurniture(Material.IRON, new AxisAlignedBB(0.9, 1, 0.9, 0.1, 0, 0.1))
    {
        @Override
        public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
        {
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
            super.randomDisplayTick(stateIn, worldIn, pos, rand);
        }
    }.setLightLevel(0.96f);
    @Register(name = "wall_lantern")
    public static final Block WALL_LANTERN = new BlockWallLantern(Material.IRON).setLightLevel(0.96f);
    @Register(name = "wall_candlestick")
    public static final Block WALL_CANDLESTICK = new BlockWallCandlestick(Material.IRON).setLightLevel(0.96f);

    @Register(name = "siege_ladder")
    public static final Block SIEGE_LADDER = new BlockSiegeLadder();
    @Register(name = "chess_plate")
    public static final Block CHESS_PLATE = new BlockChessPlate();
    @Register(name = "card_deck")
    public static final Block CARD_DECK = new BlockCardDeck();

    @Register(name = "fields_analyzer")
    public static final Block FIELDS_ANALYZER = new BlockFieldsAnalyzer();


    public static void registerBlock(IForgeRegistry<Block> registry, Block block, String name)
    {
        block.setRegistryName(Keldaria.MOD_ID, name);
        block.setTranslationKey(block.getRegistryName().toString());
        registry.register(block);
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();

        for (KeldariaRegistry.RegistryEntry<Block> entry : Keldaria.getInstance().getRegistry().getRegistriesFor(Block.class))
        {
            registerBlock(registry, entry.getObject(), entry.getName());
        }

        for (EnumFlower value : EnumFlower.values())
        {
            value.registerToList();
            registerBlock(registry, value.getBlock(), value.getName());
        }
    }

}