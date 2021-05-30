/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features;

import com.google.common.collect.Maps;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.HashMap;

public class StackSizeModifier
{

    private static HashMap<String, Integer> stackSizes = Maps.newHashMap();

    public static void modify(RegistryNamespaced<ResourceLocation, Item> registry)
    {
        add("toughasnails:purified_water_bottle", 4);
        add(Items.APPLE,4);
        add(Items.ARROW, 32);
        add(Items.COAL, 20);
        add(Items.DIAMOND, 8);
        add(Items.IRON_INGOT, 10);
        add(Items.GOLD_INGOT, 24);
        add(Items.STICK, 24);
        add(Items.BOWL, 10);
        add(Items.GUNPOWDER, 32);
        add(Items.WHEAT, 24);
        add(Items.BREAD, 4);
        add(Items.FLINT, 10);
        add(Items.PORKCHOP, 12);
        add(Items.COOKED_PORKCHOP, 4);
        add(Items.PAINTING, 1);
        add(Items.GOLDEN_APPLE, 1);
        add(Items.SIGN, 4);
        add(Items.OAK_DOOR, 1);
        add(Items.SPRUCE_DOOR, 1);
        add(Items.BIRCH_DOOR, 1);
        add(Items.JUNGLE_DOOR, 1);
        add(Items.ACACIA_DOOR, 1);
        add(Items.DARK_OAK_DOOR, 1);
        add(Items.BUCKET, 4);
        add(Items.MINECART, 1);
        add(Items.REDSTONE, 32);
        add(Items.SNOWBALL, 20);
        add(Items.LEATHER, 30);
        add(Items.BRICK, 16);
        add(Items.CLAY_BALL, 30);
        add(Items.BOOK,4);
        add(Items.SLIME_BALL, 32);
        add(Items.CHEST_MINECART, 1);
        add(Items.FURNACE_MINECART, 1);
        add(Items.GLOWSTONE_DUST, 32);
        add(Items.FISH, 10);
        add(Items.COOKED_FISH, 10);
        add(Items.REEDS, 20);
        add(Items.DYE, 10);
        add(Items.BONE, 16);
        add(Items.SUGAR, 32);
        add(Items.BED, 1);
        add(Items.REPEATER, 2);
        add(Items.FILLED_MAP, 1);
        add(Items.COOKIE, 24);
        add(Items.MELON, 8);
        add(Items.BEEF, 12);
        add(Items.COOKED_BEEF, 4);
        add(Items.CHICKEN, 12);
        add(Items.COOKED_CHICKEN, 4);
        add(Items.MUTTON, 12);
        add(Items.COOKED_MUTTON, 4);
        add(Items.RABBIT, 12);
        add(Items.COOKED_RABBIT, 4);
        add(Items.RABBIT_FOOT, 12);
        add(Items.RABBIT_HIDE, 30);
        add(Items.ROTTEN_FLESH, 12);
        add(Items.ENDER_PEARL, 4);
        add(Items.BLAZE_ROD, 4);
        add(Items.GHAST_TEAR, 8);
        add(Items.NETHER_WART, 26);
        add(Items.POTIONITEM, 3);
        add(Items.SPLASH_POTION, 3);
        add(Items.LINGERING_POTION, 3);
        add(Items.GLASS_BOTTLE, 3);
        add(Items.DRAGON_BREATH, 3);
        add(Items.SPIDER_EYE, 16);
        add(Items.FERMENTED_SPIDER_EYE, 16);
        add(Items.BLAZE_POWDER, 14);
        add(Items.MAGMA_CREAM, 32);
        add(Items.BREWING_STAND, 1);
        add(Items.CAULDRON, 1);
        add(Items.ENDER_EYE, 4);
        add(Items.SPECKLED_MELON, 8);
        add(Items.SPAWN_EGG, 16);
        add(Items.EXPERIENCE_BOTTLE, 3);
        add(Items.FIRE_CHARGE, 3);
        add(Items.WRITABLE_BOOK, 4);
        add(Items.EMERALD, 4);
        add(Items.ITEM_FRAME, 2);
        add(Items.FLOWER_POT, 1);
        add(Items.CARROT, 24);
        add(Items.POTATO, 24);
        add(Items.BAKED_POTATO, 12);
        add(Items.POISONOUS_POTATO, 24);
        add(Items.GOLDEN_CARROT, 18);
        add(Items.SKULL, 1);
        add(Items.IRON_NUGGET, 32);
        add(Items.NETHER_STAR, 4);
        add(Items.FIREWORKS, 60);
        add(Items.FIREWORK_CHARGE, 4);
        add(Items.ENCHANTED_BOOK, 4);
        add(Items.COMPARATOR, 2);
        add(Items.NETHERBRICK, 16);
        add(Items.QUARTZ, 32);
        add(Items.TNT_MINECART, 1);
        add(Items.HOPPER_MINECART, 1);
        add(Items.ARMOR_STAND, 1);
        add(Items.IRON_HORSE_ARMOR, 1);
        add(Items.GOLDEN_HORSE_ARMOR, 1);
        add(Items.DIAMOND_HORSE_ARMOR, 1);
        add(Items.LEAD, 4);
        add(Items.NAME_TAG, 32);
        add(Items.PRISMARINE_SHARD, 1);
        add(Items.PRISMARINE_CRYSTALS, 1);
        add(Items.BANNER, 1);
        add(Items.END_CRYSTAL, 1);
        add(Items.SHIELD, 1);
        add(Items.ELYTRA, 1);
        add(Items.CHORUS_FRUIT, 6);
        add(Items.CHORUS_FRUIT_POPPED, 1);
        add(Items.BEETROOT_SEEDS, 1);
        add(Items.BEETROOT, 24);
        add(Items.SHULKER_SHELL, 1);
        for(Item item : registry)
        {
            if(item instanceof ItemBlock)
            {
                ItemBlock itemBlock = (ItemBlock) item;
                Material material = itemBlock.getBlock().getDefaultState().getMaterial();
                int size = 20;
                if(material == Material.ROCK) size = 8;
                else if(material == Material.ANVIL) size = 1;
                else if(material == Material.WOOD) size = 15;
                else if(material == Material.IRON) size = 4;
                item.setMaxStackSize(size);
            }
            if(stackSizes.containsKey(item.getRegistryName().toString()))
            {
                item.setMaxStackSize(stackSizes.get(item.getRegistryName().toString()));
            }
            else if(!(item instanceof ItemBlock))
            {
                int stackSize = ObfuscationReflectionHelper.getPrivateValue(Item.class, item, 11);
                if(stackSize == 64)
                {
                    item.setMaxStackSize(20);
                }
            }
        }
    }

    private static void add(Item item, int size)
    {
        add(item.getRegistryName().toString(), size);
    }

    private static void add(String item, int size)
    {
        stackSizes.put(item, size);
    }

}
