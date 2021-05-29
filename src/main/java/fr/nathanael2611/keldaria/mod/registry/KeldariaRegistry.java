package fr.nathanael2611.keldaria.mod.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.animation.AnimationUtils;
import fr.nathanael2611.keldaria.mod.animation.Animations;
import fr.nathanael2611.keldaria.mod.api.registry.Register;
import fr.nathanael2611.keldaria.mod.crafting.CraftHandler;
import fr.nathanael2611.keldaria.mod.features.rot.ExpiredFoods;
import fr.nathanael2611.keldaria.mod.fight.FightHandler;
import fr.nathanael2611.keldaria.mod.server.zone.ZoneHandler;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFruitBlock;
import fr.nathanael2611.keldaria.mod.block.furniture.TileEntityChessPlate;
import fr.nathanael2611.keldaria.mod.features.beerbarrel.BeerBarrelRegistry;
import fr.nathanael2611.keldaria.mod.features.cookingfurnace.CookingRegistry;
import fr.nathanael2611.keldaria.mod.features.thirst.DrinkHandler;
import fr.nathanael2611.keldaria.mod.features.voice.KeldariaVoiceHandler;
import fr.nathanael2611.keldaria.mod.handler.*;
import fr.nathanael2611.keldaria.mod.network.KeldariaGuiHandler;
import fr.nathanael2611.keldaria.mod.season.Season;
import fr.nathanael2611.keldaria.mod.server.handler.KeldariaSyncHandler;
import fr.nathanael2611.keldaria.mod.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class KeldariaRegistry
{

    private BeerBarrelRegistry beerBarrelRegistry = new BeerBarrelRegistry();
    private CookingRegistry cookingRegistry = new CookingRegistry();
    private List<Class> registriesClasses = Lists.newArrayList();

    public void registerRecipes()
    {
        this.cookingRegistry.init();
        FurnaceRecipes.instance().addSmelting(KeldariaItems.CLAY_CHOP, new ItemStack(KeldariaItems.EMPTY_CHOP), 0);
        this.beerBarrelRegistry.init();

    }

    public void registerAnimations()
    {
        AnimationUtils.animations.put(Animations.NONE.getName(), Animations.NONE);
        AnimationUtils.animations.put(Animations.SIT.getName(), Animations.SIT);
        AnimationUtils.animations.put(Animations.CRAWL.getName(), Animations.CRAWL);
        AnimationUtils.animations.put(Animations.SLEEP.getName(), Animations.SLEEP);
        AnimationUtils.animations.put(Animations.WAVE.getName(), Animations.WAVE);
        AnimationUtils.animations.put(Animations.CLAP.getName(), Animations.CLAP);
        AnimationUtils.animations.put(Animations.PRAY.getName(), Animations.PRAY);
        AnimationUtils.animations.put(Animations.POINT.getName(), Animations.POINT);
        AnimationUtils.animations.put(Animations.SWIM.getName(), Animations.SWIM);
    }

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityCookingFurnace.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityCookingFurnace"));
        GameRegistry.registerTileEntity(TileEntityMill.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityMill"));
        GameRegistry.registerTileEntity(TileEntityBeerBarrel.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityBeerBarrel"));
        GameRegistry.registerTileEntity(TileEntityBrazier.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityBrazier"));
        GameRegistry.registerTileEntity(TileEntityPortcullis.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityPortcullis"));
        GameRegistry.registerTileEntity(TileEntityFloweringPot.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityFloweringPot"));
        GameRegistry.registerTileEntity(TileEntityVineSupport.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityVineSupport"));
        GameRegistry.registerTileEntity(TileEntitySaltBag.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntitySaltBag"));
        GameRegistry.registerTileEntity(TileEntityHRPSign.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityHRPSign"));
        GameRegistry.registerTileEntity(TileEntityHRPCarpet.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityHRPCarpet"));
        GameRegistry.registerTileEntity(TileEntityFeeder.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityFeeder"));
        GameRegistry.registerTileEntity(TileEntityCustomPainting.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityCustomPainting"));
        GameRegistry.registerTileEntity(TileEntityClothingMannequin.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityClothingMannequin"));
        GameRegistry.registerTileEntity(TileEntityFurnitureContainer.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityFurnitureContainer"));
        GameRegistry.registerTileEntity(TileEntityCardCarpet.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityCardCarpet"));
        GameRegistry.registerTileEntity(TileEntityChessPlate.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityChessPlate"));
        GameRegistry.registerTileEntity(TileEntityCardDeck.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityCardDeck"));
        GameRegistry.registerTileEntity(TileEntityWallpaper.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityWallpaper"));
        GameRegistry.registerTileEntity(TileEntityFruitBlock.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntityFruitBlock"));
        GameRegistry.registerTileEntity(TileEntitySieve.class, new ResourceLocation(Keldaria.MOD_ID, ".TileEntitySieve"));

    }

    public void registerCommonHandlers(Keldaria keldaria)
    {
        MinecraftForge.EVENT_BUS.register(new ChatHandler());
        MinecraftForge.EVENT_BUS.register(new InventoryHandler());
        MinecraftForge.EVENT_BUS.register(new KeldariaEventHandler(keldaria));
        MinecraftForge.EVENT_BUS.register(new DateHandler());
        MinecraftForge.EVENT_BUS.register(new LeftClickEventHandler());
        MinecraftForge.EVENT_BUS.register(new KeldariaVoiceHandler());
        MinecraftForge.EVENT_BUS.register(new KeldariaSyncHandler(keldaria));
        MinecraftForge.EVENT_BUS.register(new DrinkHandler());
        MinecraftForge.EVENT_BUS.register(new ZoneHandler());
        MinecraftForge.EVENT_BUS.register(new CraftHandler());
        MinecraftForge.EVENT_BUS.register(new ExpiredFoods());
        //MinecraftForge.EVENT_BUS.register(new FightHandler());

        Season.registerEvents(keldaria);
        NetworkRegistry.INSTANCE.registerGuiHandler(keldaria, new KeldariaGuiHandler());
    }

    public void modifyArmorMaterials()
    {

        for (Block block : Block.REGISTRY)
        {
            float hardness = ObfuscationReflectionHelper.getPrivateValue(Block.class, block, 11);
            if(hardness == 0)
            {
                block.setHardness(0.5f);
            }
        }

        //Items.SHIELD.setMaxDamage(Items.SHIELD.getMaxDamage() * 2);
        Items.SHIELD.setMaxDamage((int) (Items.SHIELD.getMaxDamage() * 2.5));
        Items.IRON_AXE.setMaxDamage((int) (Items.IRON_AXE.getMaxDamage() * 2.5));
        Items.IRON_PICKAXE.setMaxDamage((int) (Items.IRON_PICKAXE.getMaxDamage() * 2.5));
        Items.IRON_SWORD.setMaxDamage((int) (Items.IRON_SWORD.getMaxDamage() * 2.5));
        Items.IRON_SHOVEL.setMaxDamage((int) (Items.IRON_SHOVEL.getMaxDamage() * 2.5));
        Items.IRON_HOE.setMaxDamage((int) (Items.IRON_HOE.getMaxDamage() * 2.5));
        KeldariaItems.IRON_SPEAR.setMaxDamage((int) (KeldariaItems.IRON_SPEAR.getMaxDamage() * 2.5));

        Item.REGISTRY.forEach(item -> {
            if(item instanceof ItemTool)
            {
                if(item instanceof ItemAxe)
                {
                    ItemTool tool = (ItemTool) item;
                    ItemAxe axe = (ItemAxe) tool;
                    Class c = ItemTool.class;
                    Field damageField = c.getDeclaredFields()[2];
                    damageField.setAccessible(true);
                    Field speedField = c.getDeclaredFields()[3];
                    speedField.setAccessible(true);
                    try
                    {
                        float damage = (float) damageField.get(tool) - 2;
                        damageField.set(tool, damage);
                        float speed = (float) ((float) /*speedField.get(tool) * 0.3*/ -3.2 );
                        speedField.set(tool, speed);
                    } catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public CookingRegistry getCookingRegistry()
    {
        return cookingRegistry;
    }

    public BeerBarrelRegistry getBeerBarrelRegistry()
    {
        return beerBarrelRegistry;
    }

    public void registerRegistriesClass(Class c)
    {
        this.registriesClasses.add(c);
    }

    private HashMap<Class, List<RegistryEntry>> registryMap = Maps.newHashMap();

    public <T> List<RegistryEntry<T>> getRegistriesFor(Class<T> c)
    {
        if(this.registryMap.containsKey(c))
        {
            return this.registryMap.get(c).stream().map(entry -> (RegistryEntry<T>) entry).collect(Collectors.toList());
        }
        else
        {
            return Lists.newArrayList();
        }
    }

    public void getRegistries()
    {

        for (Class c : this.registriesClasses)
        {
            System.out.println(c.getName());
            for (Field field : c.getDeclaredFields())
            {
                if(Modifier.isStatic(field.getModifiers()))
                {
                    for (Annotation annotation : field.getAnnotations())
                    {
                        if(annotation instanceof Register)
                        {
                            Register info = (Register) annotation;
                            try
                            {

                                Object obj = field.get(null);
                                if(obj instanceof Item)
                                {

                                    this.registryMap.putIfAbsent(Item.class, Lists.newArrayList());
                                    RegistryEntry<Item> entry = new RegistryEntry<Item>((Item) obj, info.name(), info.defaultModel(), info.hasItem());
                                    this.registryMap.get(Item.class).add(entry);
                                }
                                else if(obj instanceof Block)
                                {
                                    this.registryMap.putIfAbsent(Block.class, Lists.newArrayList());
                                    RegistryEntry<Block> entry = new RegistryEntry<Block>((Block) obj, info.name(), info.defaultModel(), info.hasItem());
                                    this.registryMap.get(Block.class).add(entry);
                                }
                            } catch (IllegalAccessException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }


    }

    public static class RegistryEntry<T>
    {
        private T object;
        private String name;
        private boolean autoModel = true;
        private boolean hasItem;

        public RegistryEntry(T type, String name, boolean autoModel, boolean hasItem)
        {
            this.object = type;
            this.name = name;
            this.autoModel = autoModel;
            this.hasItem = hasItem;
        }

        public String getName()
        {
            return name;
        }

        public T getObject()
        {
            return object;
        }

        public boolean isAutoModel()
        {
            return autoModel;
        }

        public String getRegistryName()
        {
            return Keldaria.MOD_ID + ":" + name;
        }

        public boolean hasItem()
        {
            return hasItem;
        }
    }

}
