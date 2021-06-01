/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.registry;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.api.registry.Register;
import fr.nathanael2611.keldaria.mod.features.EnumFlower;
import fr.nathanael2611.keldaria.mod.item.*;
import fr.nathanael2611.keldaria.mod.item.accessory.ItemAccessory;
import fr.nathanael2611.keldaria.mod.item.accessory.ItemQuiver;
import fr.nathanael2611.keldaria.mod.item.accessory.ItemSpur;
import fr.nathanael2611.keldaria.mod.item.instruments.ItemInstrument;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

/**
 * This class define all of the mod items...
 * This class is also used for register all of the mod items
 *
 * @author Nathanael2611
 */
@Mod.EventBusSubscriber(modid = Keldaria.MOD_ID)
public class KeldariaItems
{

    public static final List<Item> FOODS = Lists.newArrayList();

    /**
     * Define all the mod items
     */
    @Register(name = "silver_coin")
    public static final Item SILVER_COIN = new ItemCoin();

    @Register(name = "gold_coin")
    public static final Item GOLD_COIN = new ItemCoin();

    @Register(name = "oak_lishen_wood")
    public static final Item OAK_LISHEN_WOOD = new ItemLishenWood();
    @Register(name = "dark_oak_lishen_wood")
    public static final Item DARK_OAK_LISHEN_WOOD = new ItemLishenWood();
    @Register(name = "birch_lishen_wood")
    public static final Item BIRCH_LISHEN_WOOD = new ItemLishenWood();
    @Register(name = "spruce_lishen_wood")
    public static final Item SPRUCE_LISHEN_WOOD = new ItemLishenWood();

    @Register(name = "wooden_pot")
    public static final Item WOODEN_POT = new Item();
    @Register(name = "salt_wooden_pot")
    public static final Item SALT_WOODEN_POT = new Item();

    @Register(name = "grapes")
    public static final Item GRAPES = new ItemFoodBase(1, 3, false).setMaxStackSize(12);

    @Register(name = "unripe_apple")
    public static final Item UNRIPE_APPLE = new UnripeFood(1).setMaxStackSize(4);

    @Register(name = "raisin_bread")
    public static final Item RAISIN_BREAD = new ItemFoodBase(5, 5, false).setMaxStackSize(4);
    @Register(name = "fried_egg")
    public static final Item FRIED_EGG = new ItemFoodBase(4, 6, false).setMaxStackSize(5);
    @Register(name = "butter")
    public static final Item BUTTER = new ItemFoodBase(0, 1, false).setMaxStackSize(8);
    @Register(name = "shortbread")
    public static final Item SHORTBREAD = new ItemFoodBase(2, 2, false).setMaxStackSize(32);
    @Register(name = "raisin_cookie")
    public static final Item RAISIN_COOKIE = new ItemFoodBase(3, 5, false).setMaxStackSize(32);

    @Register(name = "mushroom_bowl")
    public static final Item MUSHROOM_BOWL = new ItemSoupBase(6, 4);
    @Register(name = "fried_mushroom_bowl")
    public static final Item FRIED_MUSHROOM_BOWL = new ItemSoupBase(10, 7);
    @Register(name = "stuffed_mushrooms")
    public static final Item STUFFED_MUSHROOMS = new ItemSoupBase(12, 9);
    @Register(name = "beetroot_broth")
    public static final Item BEETROOT_BROTH = new ItemSoupBase(8, 13);
    @Register(name = "applesauce")
    public static final Item APPLESAUCE = new ItemSoupBase(5, 7);

    @Register(name = "needle")
    public static final Item NEEDLE = new Item();

    @Register(name = "bacon")
    public static final Item BACON = new ItemFoodBase(2, 3, true).setMaxStackSize(24);
    @Register(name = "cooked_bacon")
    public static final Item COOKED_BACON = new ItemFoodBase(4, 5, true).setMaxStackSize(24);

    @Register(name = "white_onion")
    public static final Item WHITE_ONION = new ItemSeedFood(2, 4, KeldariaBlocks.WHITE_ONION, Blocks.FARMLAND);
    @Register(name = "onion")
    public static final Item ONION = new ItemSeedFood(3, 6, KeldariaBlocks.ONION, Blocks.FARMLAND);

    @Register(name = "pemmican")
    public static final Item PEMMICAN = new ItemFoodBase(3, 4, true);

    @Register(name = "animal_grease")
    public static final Item ANIMAL_GREASE = new Item();

    @Register(name = "gambeson")
    public static final Item GAMBESON = new ItemArmorPart(null, EntityEquipmentSlot.CHEST, 0.008, 35, 30, 20, 250, "http://keldaria.fr/skinhosting/up/dh9ag.png");

    @Register(name = "chainmail_hood")
    public static final Item CHAINMAIL_HOOD = new ItemArmorPart(KeldariaItems.CHAINMAIL, EntityEquipmentSlot.HEAD, 0.002, 40, 30, 20, 400, "http://keldaria.fr/skinhosting/up/3u5ve.png");
    @Register(name = "chainmail_chestplate")
    public static final Item CHAINMAIL_CHESTPLATE = new ItemArmorPart(KeldariaItems.CHAINMAIL, EntityEquipmentSlot.CHEST, 0.005, 35, 15, 15, 800, "http://keldaria.fr/skinhosting/up/pijno.png");
    @Register(name = "chainmail_leggings")
    public static final Item CHAINMAIL_LEGGINGS = new ItemArmorPart(KeldariaItems.CHAINMAIL, EntityEquipmentSlot.LEGS, 0.003, 20, 10, 5, 600, "http://keldaria.fr/skinhosting/up/bx1cm.png");
    @Register(name = "chainmail_boots")
    public static final Item CHAINMAIL_BOOTS = new ItemArmorPart(KeldariaItems.CHAINMAIL, EntityEquipmentSlot.FEET, 0.002, 20, 30, 10, 300, "http://keldaria.fr/skinhosting/up/td79j.png");

    @Register(name = "iron_helm")
    public static final Item IRON_HELM = new ItemArmorPart(Items.IRON_INGOT, EntityEquipmentSlot.HEAD, 0.003, 50, 20, 30, 800, "http://keldaria.fr/skinhosting/up/gnxh7.png");
    @Register(name = "iron_helmet")
    public static final Item IRON_HELMET = new ItemArmorPart(Items.IRON_INGOT, EntityEquipmentSlot.HEAD, 0.001, 25, 10, 15, 600, "http://keldaria.fr/skinhosting/up/jrodw.png");
    @Register(name = "iron_chestplate")
    public static final Item IRON_CHESTPLATE = new ItemArmorPart(Items.IRON_INGOT, EntityEquipmentSlot.CHEST, 0.005, 50, 30, 40, 1400, "http://keldaria.fr/skinhosting/up/zpy62.png");
    @Register(name = "iron_gorgeret")
    public static final Item IRON_GORGERET = new ItemArmorPart(Items.IRON_INGOT, EntityEquipmentSlot.CHEST, 0.002, 25, 10, 20, 1000, "http://keldaria.fr/skinhosting/up/2jsy9.png");
    @Register(name = "iron_armband")
    public static final Item IRON_ARMBAND = new ItemArmorPart(Items.IRON_INGOT, EntityEquipmentSlot.CHEST, 0.002, 15, 5, 10, 600, "http://keldaria.fr/skinhosting/up/6gwjn.png");
    @Register(name = "iron_pauldron")
    public static final Item IRON_PAULDRON = new ItemArmorPart(Items.IRON_INGOT, EntityEquipmentSlot.CHEST, 0.001, 10, 2, 4, 500, "http://keldaria.fr/skinhosting/up/hev1k.png");
    @Register(name = "iron_leggings")
    public static final Item IRON_LEGGINGS = new ItemArmorPart(Items.IRON_INGOT, EntityEquipmentSlot.LEGS, 0.003, 60, 30, 45, 1200, "http://keldaria.fr/skinhosting/up/unmts.png");
    @Register(name = "iron_elbow_pads")
    public static final Item IRON_ELBOW_PADS = new ItemArmorPart(Items.IRON_INGOT, EntityEquipmentSlot.LEGS, 0.001, 20, 10, 15, 600, "http://keldaria.fr/skinhosting/up/7ovqa.png");
    @Register(name = "iron_boots")
    public static final Item IRON_BOOTS = new ItemArmorPart(Items.IRON_INGOT, EntityEquipmentSlot.FEET, 0.001, 80, 20, 60, 900, "http://keldaria.fr/skinhosting/up/simln.png");

    @Register(name = "steel_helm")
    public static final Item STEEL_HELM = new ItemArmorPart(KeldariaItems.STEEL_INGOT, EntityEquipmentSlot.HEAD, 0.004, 60, 25, 50, 1400, "http://keldaria.fr/skinhosting/up/pws54.png");
    @Register(name = "steel_helmet")
    public static final Item STEEL_HELMET = new ItemArmorPart(KeldariaItems.STEEL_INGOT, EntityEquipmentSlot.HEAD, 0.002, 30, 20, 20, 1200, "http://keldaria.fr/skinhosting/up/u76dx.png");
    @Register(name = "steel_chestplate")
    public static final Item STEEL_CHESTPLATE = new ItemArmorPart(KeldariaItems.STEEL_INGOT, EntityEquipmentSlot.CHEST, 0.006, 60, 35, 55, 2200, "http://keldaria.fr/skinhosting/up/5zcnf.png");
    @Register(name = "steel_gorgeret")
    public static final Item STEEL_GORGERET = new ItemArmorPart(KeldariaItems.STEEL_INGOT, EntityEquipmentSlot.CHEST, 0.003, 30, 15, 30, 1800, "http://keldaria.fr/skinhosting/up/lw2nr.png");
    @Register(name = "steel_armband")
    public static final Item STEEL_ARMBAND = new ItemArmorPart(KeldariaItems.STEEL_INGOT, EntityEquipmentSlot.CHEST, 0.002, 20, 10, 20, 1200, "http://keldaria.fr/skinhosting/up/9dcvr.png");
    @Register(name = "steel_pauldron")
    public static final Item STEEL_PAULDRON = new ItemArmorPart(KeldariaItems.STEEL_INGOT, EntityEquipmentSlot.CHEST, 0.002, 15, 8, 10, 1000, "http://keldaria.fr/skinhosting/up/v5zli.png");
    @Register(name = "steel_leggings")
    public static final Item STEEL_LEGGINGS = new ItemArmorPart(KeldariaItems.STEEL_INGOT, EntityEquipmentSlot.LEGS, 0.004, 70, 40, 55, 2400, "http://keldaria.fr/skinhosting/up/9nij1.png");
    @Register(name = "steel_elbow_pads")
    public static final Item STEEL_ELBOW_PADS = new ItemArmorPart(KeldariaItems.STEEL_INGOT, EntityEquipmentSlot.LEGS, 0.001, 30, 20, 20, 1200, "http://keldaria.fr/skinhosting/up/4e96b.png");
    @Register(name = "steel_boots")
    public static final Item STEEL_BOOTS = new ItemArmorPart(KeldariaItems.STEEL_INGOT, EntityEquipmentSlot.FEET, 0.001, 80, 25, 80, 1700, "http://keldaria.fr/skinhosting/up/de3xi.png");

    @Register(name = "onion_soup")
    public static final Item ONION_SOUP = new ItemSoupBase(6, 8);
    @Register(name = "flambadou")
    public static final Item FLAMBADOU = new ItemFlambadou();

    @Register(name = "writed_paper")
    public static final Item WRITED_PAPER = new ItemWritedPaper();

    @Register(name = "iron_forge_hammer")
    public static final Item IRON_FORGE_HAMMER = new ItemForgeHammer();

    @Register(name = "empty_chop")
    public static final Item EMPTY_CHOP = new ItemEmptyChop();
    @Register(name = "clay_chop")
    public static final Item CLAY_CHOP = new Item();
    @Register(name = "water_chop")
    public static final Item WATER_CHOP = new ItemChop(3, 2, null);
    @Register(name = "beer_chop")
    public static final Item BEER_CHOP = new ItemChop(5, 3, new PotionEffect(MobEffects.NAUSEA, 200));

    @Register(name = "omelet")
    public static final Item OMELET = new ItemMeal(3, 1.6f, 3);
    @Register(name = "fish_pie")
    public static final Item FISH_PIE = new ItemMeal(5, 3, 2);
    @Register(name = "apple_pie")
    public static final Item APPLE_PIE = new ItemMeal(6, 4, 2);


    @Register(name = "canvas")
    public static final Item CANVAS = new ItemCanvas();

    @Register(name = "lock")
    public static final Item LOCK = new ItemLock();
    @Register(name = "key")
    public static final Item KEY = new ItemKey();
    @Register(name = "dice")
    public static final Item DICE = new Item();

    @Register(name = "lockpick")
    public static final Item LOCKPICK = new ItemLockpick();

    @Register(name = "orge")
    public static final Item ORGE = new Item();
    @Register(name = "orge_seeds")
    public static final Item ORGE_SEEDS = new ItemSeeds(KeldariaBlocks.ORGE, Blocks.FARMLAND);

    @Register(name = "flour")
    public static final Item FLOUR = new Item();

    @Register(name = "potaufeu")
    public static final Item POTAUFEU = new ItemSoupBase(18, 12);
    @Register(name = "mashed_potatoes")
    public static final Item MASHED_POTATOES = new ItemSoupBase(12, 7);
    @Register(name = "fried_potatoes")
    public static final Item FRIED_POTATOES = new ItemSoupBase(10, 5);

    @Register(name = "sunflower_oil")
    public static final Item SUNFLOWER_OIL = new Item();

    @Register(name = "hook_blade")
    public static final Item HOOK_BLADE = new ItemHookBlade(Item.ToolMaterial.DIAMOND);

    @Register(name = "bread_dough")
    public static final Item BREAD_DOUGH = new Item();

    @Register(name = "clothe")
    public static final Item CLOTHE = new ItemClothe();

    @Register(name = "iron_plate")
    public static final Item IRON_PLATE = new Item();
    @Register(name = "chainmail")
    public static final Item CHAINMAIL = new Item();

    @Register(name = "ignited_arrow")
    public static final Item IGNITED_ARROW = new ItemIgnitedArrow();

    @Register(name = "keyring", defaultModel = false)
    public static final Item KEYRING = new ItemKeyring();
    @Register(name = "purse")
    public static final Item PURSE = new ItemPurse();

    // Instruments
    @Register(name = "gittern")
    public static final Item GITTERN = new ItemInstrument("gittern", new SoundEvent[]{KeldariaSounds.GITTERN}, new SoundEvent[]{KeldariaSounds.GITTERN_SNEAK}, false);
    @Register(name = "lyre")
    public static final Item LYRE = new ItemInstrument("lyre", new SoundEvent[]{KeldariaSounds.LYRE}, new SoundEvent[]{KeldariaSounds.LYRE_SNEAK}, true);


    @Register(name = "empty_remedy_vial")
    public static final Item EMPTY_REMEDY_VIAL = new Item();
    @Register(name = "remedy_vial")
    public static final Item REMEDY_VIAL = new ItemRemedyVial();

    @Register(name = "ship_flag")
    public static final Item SHIP_FLAG = new ItemShipFlag();
    @Register(name = "empty_wine_bottle")
    public static final Item EMPTY_WINE_BOTTLE = new Item();
    @Register(name = "empty_wine_glass")
    public static final Item EMPTY_WINE_GLASS = new Item();
    @Register(name = "wine_bottle")
    public static final Item WINE_BOTTLE = new ItemWineBottle();
    @Register(name = "wine_glass")
    public static final Item WINE_GLASS = new ItemWineGlass();


    @Register(name = "neutral_soap")
    public static final Item NEUTRAL_SOAP = new ItemWashingSoap();
    @Register(name = "soap")
    public static final Item SOAP = new ItemWashingSoap();


    public static final Item.ToolMaterial IRON_DAGGER_MATERIAL = EnumHelper.addToolMaterial("ironDagger", 0, 100, 2, 2.8F, 10);
    @Register(name = "iron_dagger")
    public static final Item IRON_DAGGER = new ItemDagger(IRON_DAGGER_MATERIAL);

    public static final Item.ToolMaterial STEEL_DAGGER_MATERIAL = EnumHelper.addToolMaterial("steelDagger", 0, 600, 2, 3.4F, 10);
    @Register(name = "steel_dagger")
    public static final Item STEEL_DAGGER = new ItemDagger(STEEL_DAGGER_MATERIAL);

    public static final Item.ToolMaterial IRON_SPEAR_MATERIAL = EnumHelper.addToolMaterial("ironSpear", 0, 400, 2, 0.5F, 10);
    @Register(name = "iron_spear")
    public static final Item IRON_SPEAR = new ItemSpear(IRON_SPEAR_MATERIAL);

    public static final Item.ToolMaterial STEEL_SPEAR_MATERIAL = EnumHelper.addToolMaterial("steelSpear", 0, 300 * 6, 2, 1.5F, 10);
    @Register(name = "steel_spear")
    public static final Item STEEL_SPEAR = new ItemSpear(STEEL_SPEAR_MATERIAL);

    @Register(name = "fresh_water")
    public static final Item FRESH_WATER = new ItemFreshWater();

    @Register(name = "fat_book")
    public static final Item FAT_BOOK = new ItemFatBook();

    @Register(name = "spur_5cm")
    public static final Item SPUR_5CM = new ItemSpur(5, 2, Lists.newArrayList(new PotionEffect(MobEffects.SPEED, 5 * 20, 1), new PotionEffect(MobEffects.WEAKNESS, 5, 0)));
    @Register(name = "spur_10cm")
    public static final Item SPUR_10CM = new ItemSpur(10, 5, Lists.newArrayList(new PotionEffect(MobEffects.SPEED, 10 * 20, 1), new PotionEffect(MobEffects.WEAKNESS, 10, 0)));
    @Register(name = "spur_30cm")
    public static final Item SPUR_30CM = new ItemSpur(30, 7, Lists.newArrayList(new PotionEffect(MobEffects.SPEED, 10 * 20, 2), new PotionEffect(MobEffects.WEAKNESS, 10, 1)));
    @Register(name = "spur_50cm")
    public static final Item SPUR_50CM = new ItemSpur(50, 10, Lists.newArrayList(new PotionEffect(MobEffects.SPEED, 15 * 20, 3), new PotionEffect(MobEffects.WEAKNESS, 15, 2)));

    @Register(name = "sleeping_mask")
    public static final Item SLEEPING_MASK = new ItemSleepingMask();


    @Register(name = "quiver")
    public static final Item QUIVER = new ItemQuiver();

    @Register(name = "horse_brush")
    public static final Item HORSE_BRUSH = new ItemHorseBrush();


    public static final Item.ToolMaterial STEEL_TOOL_MATERIAL = EnumHelper.addToolMaterial("steelTool", 3, 2000, 9.0F, 3F, 10);
    public static final ItemArmor.ArmorMaterial STEEL_ARMOR_MATERIAL = EnumHelper.addArmorMaterial("steelArmor", Keldaria.MOD_ID + ":steel", 33, new int[]{5, 8, 9, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
    public static final ItemArmor.ArmorMaterial REINFORCED_STEEL_ARMOR_MATERIAL = EnumHelper.addArmorMaterial("reinforcedSteelArmor", Keldaria.MOD_ID + ":reinforced_steel", 33 * 2, new int[]{5, 8, 9, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.5F);

    @Register(name = "steel_ingot")
    public static final Item STEEL_INGOT = new Item();

    @Register(name = "steel_shovel")
    public static final Item STEEL_SHOVEL = new ItemSpade(STEEL_TOOL_MATERIAL);
    @Register(name = "steel_pickaxe")
    public static final Item STEEL_PICKAXE = new ItemCustomPickaxe(STEEL_TOOL_MATERIAL);
    @Register(name = "steel_hoe")
    public static final Item STEEL_HOE = new ItemHoe(STEEL_TOOL_MATERIAL);
    @Register(name = "steel_axe")
    public static final Item STEEL_AXE = new ItemCustomAxe(STEEL_TOOL_MATERIAL, 8.5f, -3.5f);
    @Register(name = "steel_sword")
    public static final Item STEEL_SWORD = new ItemSword(STEEL_TOOL_MATERIAL);

    @Register(name = "iron_mace")
    public static final Item IRON_MACE = new ItemMace(Item.ToolMaterial.IRON, 200, 5f, -3f);
    @Register(name = "steel_mace")
    public static final Item STEEL_MACE = new ItemMace(STEEL_TOOL_MATERIAL, 600, 5.5f, -3.25f);

    @Register(name = "compass")
    public static final Item COMPASS = new Item();

    @Register(name = "longbow")
    public static final Item LONGBOW = new ItemLongbow();
    @Register(name = "crossbow")
    public static final Item CROSSBOW = new ItemCrossbow();

    @Register(name = "wooden_round_shield")
    public static final Item WOODEN_ROUND_SHIELD = new ItemCustomShield();
    @Register(name = "card")
    public static final Item CARD = new ItemCard();


    @Register(name = "white_pawn")
    public static final Item WHITE_PAWN = new ItemPawn();
    @Register(name = "white_tower")
    public static final Item WHITE_TOWER = new ItemPawn();
    @Register(name = "white_horse")
    public static final Item WHITE_HORSE = new ItemPawn();
    @Register(name = "white_jester")
    public static final Item WHITE_JESTER = new ItemPawn();
    @Register(name = "white_king")
    public static final Item WHITE_KING = new ItemPawn();
    @Register(name = "white_queen")
    public static final Item WHITE_QUEEN = new ItemPawn();

    @Register(name = "black_pawn")
    public static final Item BLACK_PAWN = new ItemPawn();
    @Register(name = "black_tower")
    public static final Item BLACK_TOWER = new ItemPawn();
    @Register(name = "black_horse")
    public static final Item BLACK_HORSE = new ItemPawn();
    @Register(name = "black_jester")
    public static final Item BLACK_JESTER = new ItemPawn();
    @Register(name = "black_king")
    public static final Item BLACK_KING = new ItemPawn();
    @Register(name = "black_queen")
    public static final Item BLACK_QUEEN = new ItemPawn();

    @Register(name = "oak_bit")
    public static final Item OAK_BIT = new ItemWoodBit();
    @Register(name = "birch_bit")
    public static final Item BIRCH_BIT = new ItemWoodBit();
    @Register(name = "spruce_bit")
    public static final Item SPRUCE_BIT = new ItemWoodBit();
    @Register(name = "dark_oak_bit")
    public static final Item DARK_OAK_BIT = new ItemWoodBit();
    @Register(name = "acacia_bit")
    public static final Item ACACIA_BIT = new ItemWoodBit();
    @Register(name = "jungle_bit")
    public static final Item JUNGLE_BIT = new ItemWoodBit();

    @Register(name = "smelted_iron")
    public static final Item SMELTED_IRON = new ItemSmeltedIngot(1, Items.IRON_INGOT);
    @Register(name = "smelted_steel")
    public static final Item SMELTED_STEEL = new ItemSmeltedIngot(1, STEEL_INGOT);
    @Register(name = "smelted_gold")
    public static final Item SMELTED_GOLD = new ItemSmeltedIngot(1, Items.GOLD_INGOT);

    @Register(name = "raw_iron")
    public static final Item RAW_IRON = new ItemRawOre();
    @Register(name = "raw_gold")
    public static final Item RAW_GOLD = new ItemRawOre();

    @Register(name = "glasses")
    public static final Item GLASSES = new ItemGlasses();
    @Register(name = "glasses_frame")
    public static final Item GLASSES_FRAME = new ItemFakeGlasses();


    static
    {


        STEEL_ARMOR_MATERIAL.setRepairItem(new ItemStack(STEEL_INGOT));
        REINFORCED_STEEL_ARMOR_MATERIAL.setRepairItem(new ItemStack(STEEL_INGOT));
        STEEL_TOOL_MATERIAL.setRepairItem(new ItemStack(STEEL_INGOT));
        IRON_SPEAR_MATERIAL.setRepairItem(new ItemStack(Items.IRON_INGOT));
        IRON_DAGGER_MATERIAL.setRepairItem(new ItemStack(Items.IRON_INGOT));

        STEEL_INGOT.setCreativeTab(KeldariaTabs.KELDARIA);




        STEEL_SWORD.setCreativeTab(KeldariaTabs.KELDARIA);

        ORGE.setCreativeTab(KeldariaTabs.KELDARIA);
        ORGE_SEEDS.setCreativeTab(KeldariaTabs.KELDARIA);

        ONION.setCreativeTab(KeldariaTabs.KELDARIA);
        ONION.setMaxStackSize(8);
        WHITE_ONION.setCreativeTab(KeldariaTabs.KELDARIA);
        WHITE_ONION.setMaxStackSize(8);

        FLOUR.setMaxStackSize(24);

        LOCK.setCreativeTab(KeldariaTabs.KELDARIA);
        LOCKPICK.setCreativeTab(KeldariaTabs.KELDARIA);
        KEY.setCreativeTab(KeldariaTabs.KELDARIA);
        FLOUR.setCreativeTab(KeldariaTabs.KELDARIA);

        SUNFLOWER_OIL.setCreativeTab(KeldariaTabs.KELDARIA);
        SUNFLOWER_OIL.setMaxStackSize(1);

        BREAD_DOUGH.setMaxStackSize(8);
        BREAD_DOUGH.setContainerItem(Items.BUCKET);

        IRON_PLATE.setMaxStackSize(4);
        IRON_PLATE.setCreativeTab(KeldariaTabs.KELDARIA);

        DICE.setMaxStackSize(1);
        DICE.setCreativeTab(KeldariaTabs.KELDARIA);

        EMPTY_WINE_BOTTLE.setCreativeTab(KeldariaTabs.KELDARIA);
        EMPTY_WINE_GLASS.setCreativeTab(KeldariaTabs.KELDARIA);

        EMPTY_REMEDY_VIAL.setMaxStackSize(1);
        EMPTY_REMEDY_VIAL.setCreativeTab(KeldariaTabs.APOTHECARY);

        IRON_SPEAR.setCreativeTab(KeldariaTabs.KELDARIA);
        STEEL_SPEAR.setCreativeTab(KeldariaTabs.KELDARIA);
        FAT_BOOK.setCreativeTab(KeldariaTabs.KELDARIA);
        COMPASS.setCreativeTab(KeldariaTabs.KELDARIA);
        LONGBOW.setCreativeTab(KeldariaTabs.KELDARIA);
        ANIMAL_GREASE.setCreativeTab(KeldariaTabs.KELDARIA);
        NEUTRAL_SOAP.setCreativeTab(KeldariaTabs.KELDARIA);
        OMELET.setCreativeTab(KeldariaTabs.KELDARIA);
        FISH_PIE.setCreativeTab(KeldariaTabs.KELDARIA);
        BEETROOT_BROTH.setCreativeTab(KeldariaTabs.KELDARIA);



    }

    /**
     * Method used for register an Item with a custom registry name
     */
    public static void registerItem(IForgeRegistry<Item> registry, Item item, String name)
    {
        item.setRegistryName(Keldaria.MOD_ID, name);
        item.setTranslationKey(item.getRegistryName().toString());
        registry.register(item);
    }

    /**
     * Method used for register an ItemBlock with the used block registry name
     */
    private static void registerItemBlock(IForgeRegistry<Item> registry, Block block)
    {
        registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    /**
     * Register all mod items
     */
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e)
    {
        /* Define the registry */
        IForgeRegistry<Item> registry = e.getRegistry();


        for (KeldariaRegistry.RegistryEntry<Item> entry : Keldaria.getInstance().getRegistry().getRegistriesFor(Item.class))
        {
            registerItem(registry, entry.getObject(), entry.getName());
        }

        for (KeldariaRegistry.RegistryEntry<Block> entry : Keldaria.getInstance().getRegistry().getRegistriesFor(Block.class))
        {
            if(entry.hasItem())
            {
                registerItemBlock(registry, entry.getObject());
            }
        }

        for (EnumFlower value : EnumFlower.values())
        {
            registry.register(new ItemFlowerBlock(value.getBlock()).setRegistryName(value.getBlock().getRegistryName()));
        }
    }

    public static List<Item> getFoodItems()
    {
        if (FOODS.isEmpty())
        {
            Item.REGISTRY.forEach(item ->
            {
                if (item instanceof ItemFood)
                {
                    FOODS.add(item);
                }
            });
        }
        return FOODS;
    }

}
