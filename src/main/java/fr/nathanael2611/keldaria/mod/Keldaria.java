/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.mennomax.astikorcarts.AstikorCarts;
import fr.nathanael2611.keldaria.mod.asm.MixinHooks;
import fr.nathanael2611.keldaria.mod.clothe.ClothesManager;
import fr.nathanael2611.keldaria.mod.command.*;
import fr.nathanael2611.keldaria.mod.command.inventory.CommandInvsee;
import fr.nathanael2611.keldaria.mod.command.inventory.CommandSwitchInv;
import fr.nathanael2611.keldaria.mod.crafting.CraftEntry;
import fr.nathanael2611.keldaria.mod.crafting.CraftIngredient;
import fr.nathanael2611.keldaria.mod.crafting.CraftManager;
import fr.nathanael2611.keldaria.mod.crafting.PossibleWith;
import fr.nathanael2611.keldaria.mod.crafting.storage.KnownRecipesStorage;
import fr.nathanael2611.keldaria.mod.crafting.storage.api.IKnownRecipes;
import fr.nathanael2611.keldaria.mod.crafting.storage.impl.KnownRecipes;
import fr.nathanael2611.keldaria.mod.discord.DiscordImpl;
import fr.nathanael2611.keldaria.mod.entity.EntityHomingPigeon;
import fr.nathanael2611.keldaria.mod.features.*;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.features.accessories.SyncedAccessories;
import fr.nathanael2611.keldaria.mod.features.armoposes.ArmPoses;
import fr.nathanael2611.keldaria.mod.features.backweapons.WeaponsManager;
import fr.nathanael2611.keldaria.mod.features.cleanliness.CleanilessManager;
import fr.nathanael2611.keldaria.mod.features.cleanliness.Soap;
import fr.nathanael2611.keldaria.mod.features.combat.EnumAttackType;
import fr.nathanael2611.keldaria.mod.features.containment.Points;
import fr.nathanael2611.keldaria.mod.features.containment.Regions;
import fr.nathanael2611.keldaria.mod.features.food.ExpiredFoods;
import fr.nathanael2611.keldaria.mod.features.skill.EnumJob;
import fr.nathanael2611.keldaria.mod.item.ItemFlambadou;
import fr.nathanael2611.keldaria.mod.item.ItemWashingSoap;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.proxy.CommonProxy;
import fr.nathanael2611.keldaria.mod.registry.*;
import fr.nathanael2611.keldaria.mod.season.snow.storage.SnowStorage;
import fr.nathanael2611.keldaria.mod.season.snow.storage.api.ISnowManager;
import fr.nathanael2611.keldaria.mod.season.snow.storage.impl.SnowManager;
import fr.nathanael2611.keldaria.mod.server.chat.SoundResistance;
import fr.nathanael2611.keldaria.mod.server.chat.voicechatbridge.KeldariaVoices;
import fr.nathanael2611.keldaria.mod.server.group.GroupManager;
import fr.nathanael2611.keldaria.mod.server.zone.Zones;
import fr.nathanael2611.keldaria.mod.server.zone.storage.ZoneStorage;
import fr.nathanael2611.keldaria.mod.server.zone.storage.api.IZoneManager;
import fr.nathanael2611.keldaria.mod.server.zone.storage.impl.ZoneManager;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import fr.nathanael2611.simpledatabasemanager.core.SyncedDatabases;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.DataSerializerEntry;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Mod(modid = Keldaria.MOD_ID, name = Keldaria.MOD_NAME, version = Keldaria.MOD_VERSION, dependencies = Keldaria.MOD_DEPENDS)
public class Keldaria
{

    public static final String MOD_NAME = "Keldaria";
    public static final String MOD_ID = "keldaria";
    public static final String MOD_VERSION = "1.0";
    public static final String MOD_DEPENDS = /*"required-after:toughasnails;" + */ "required-after:sdm;"/*required-after:armourers_workshop;*/ /*+ "required-after:sereneseasons;"*/;
    public static final String SERVER_IP = "keldaria.fr:8956";

    @Mod.Instance(MOD_ID)
    private static Keldaria instance;

    private static AstikorCarts astikorCarts = new AstikorCarts();

    @SidedProxy(clientSide = "fr.nathanael2611.keldaria.mod.proxy.ClientProxy", serverSide = "fr.nathanael2611.keldaria.mod.proxy.ServerProxy")
    private static CommonProxy proxy;

    public static final String API_URL = "http://keldaria.fr/api.php";

    private KeldariaFiles files;
    private KeldariaCommands commands;
    private KeldariaRegistry registry;
    private ClothesManager clothesManager;
    private WeaponsManager weaponsManager;
    private SyncedAccessories syncedAccessories;
    private ArmPoses armPoses;
    private ClimbSystem climbSystem;
    private EntityHomingPigeon.PigeonTravels pigeonTravels;

    @Mod.EventHandler
    public void preInitialization(FMLPreInitializationEvent e)
    {
        proxy.preInitialization(e);
        this.registry = new KeldariaRegistry();


        this.registry.registerRegistriesClass(KeldariaItems.class);
        this.registry.registerRegistriesClass(KeldariaBlocks.class);
        this.registry.getRegistries();


        this.files = new KeldariaFiles(e.getModConfigurationDirectory(), e.getSide());
        this.commands = new KeldariaCommands();
        this.registry.registerAnimations();
        this.registry.registerTileEntities();
        this.registry.registerRecipes();
        this.registry.registerCommonHandlers(this);
        this.clothesManager = new ClothesManager(e.getSide());
        this.weaponsManager = new WeaponsManager();
        this.syncedAccessories = new SyncedAccessories();
        this.armPoses = new ArmPoses();
        this.climbSystem = new ClimbSystem();
        CapabilityManager.INSTANCE.register(ISnowManager.class, new SnowStorage(), SnowManager::new);
        CapabilityManager.INSTANCE.register(IZoneManager.class, new ZoneStorage(), ZoneManager::new);
        CapabilityManager.INSTANCE.register(IKnownRecipes.class, new KnownRecipesStorage(), KnownRecipes::new);
        //CapabilityManager.INSTANCE.register(FightControl.class, new FightStorage(), FightControl::new);
        //CapabilityManager.INSTANCE.register(IChunkGrowManager.class, new GrowStorage(), ChunkGrowManagerImpl::new);
        KeldariaPacketHandler.getInstance().registerPackets();
        ForgeRegistries.DATA_SERIALIZERS.register(new DataSerializerEntry(MixinHooks.DOUBLE).setRegistryName(MOD_ID, "double"));
        ForgeRegistries.DATA_SERIALIZERS.register(new DataSerializerEntry(MixinHooks.LONG).setRegistryName(MOD_ID, "long"));
        ForgeRegistries.DATA_SERIALIZERS.register(new DataSerializerEntry(MixinHooks.GENDER).setRegistryName(MOD_ID, "gender"));
        ForgeRegistries.DATA_SERIALIZERS.register(new DataSerializerEntry(MixinHooks.ANIMAL_STATS).setRegistryName(MOD_ID, "animal_stats"));
        ForgeRegistries.DATA_SERIALIZERS.register(new DataSerializerEntry(MixinHooks.PREGNANCY).setRegistryName(MOD_ID, "pregnancy"));
        ForgeRegistries.DATA_SERIALIZERS.register(new DataSerializerEntry(MixinHooks.FLEECE).setRegistryName(MOD_ID, "fleece"));
        astikorCarts.preInit(e);


        this.registry.registerEntities(this);

        Zones.registerAll();

    }

    @Mod.EventHandler
    public void initialization(FMLInitializationEvent e)
    {
        proxy.initialization(e);
        astikorCarts.init(e);
        ForgeChunkManager.setForcedChunkLoadingCallback(this, (tickets, world) ->
        {

        });
    }

    @Mod.EventHandler
    public void postInitialization(FMLPostInitializationEvent e)
    {
        astikorCarts.postInit(e);

        StackSizeModifier.modify(Item.REGISTRY);
        ExpiredFoods.init();
        this.registry.modifyArmorMaterials();
        Helpers.log("Getting all foods. Found " + KeldariaItems.getFoodItems().size() + ".");
        Helpers.handleMinimapCheck(e);
    }

    public static Keldaria getInstance()
    {
        return instance;
    }

    public KeldariaFiles getFiles()
    {
        return files;
    }

    public KeldariaCommands getCommands()
    {
        return commands;
    }

    public KeldariaRegistry getRegistry()
    {
        return registry;
    }

    public ClothesManager getClothesManager()
    {
        return clothesManager;
    }

    public WeaponsManager getWeaponsManager()
    {
        return weaponsManager;
    }

    public SyncedAccessories getSyncedAccessories()
    {
        return syncedAccessories;
    }

    public ArmPoses getArmPoses()
    {
        return armPoses;
    }


    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent e)
    {

        this.commands.clear();
        this.commands.addAll(new CommandSignFatBook(), new CommandHorseTexture(), new CommandHeal(), new CommandLore(), new CommandVanish(), new CommandStartBot(), new CommandDiscordOP(), new CommandGroup(), new CommandPlayerSize(), new CommandWarp(), new CommandInvsee(), new CommandSeen(), new CommandSkyColor(), new CommandJob(), new CommandLocks(), new CommandSpeed(), new CommandCopyToClipboard(), new CommandGetBookAsString(), new CommandClothe(), new CommandPeristentEffects(), new CommandGM(), new CommandNathanael2611(), new CommandAmputation(), new CommandFlagUrl(), new CommandCraftManager(), new CommandGetJsonStack(), new CommandHRP());
        this.commands.add(new CommandOpticIssue());
        this.commands.add(new CommandComplement());
        this.commands.add(new CommandAptitude());
        this.commands.add(new CommandMorph());
        this.commands.add(new CommandRoll());
        this.commands.add(new CommandZone());
        this.commands.add(new CommandCleaniless());
        this.commands.add(new CommandDefinePortcullis());
        this.commands.add(new CommandHeal());
        this.commands.add(new CommandCustomPainting());
        this.commands.add(new CommandSharpening());
        this.commands.add(new CommandItemName());
        this.commands.add(new CommandWL());
        this.commands.add(new CommandMillicie());
        this.commands.add(new CommandKeldaSpy());
        this.commands.add(new CommandRot());
        this.commands.add(new CommandDebugMode());
        this.commands.add(new CommandSwitchInv());
        this.commands.add(new CommandBirthday());
        this.commands.add(new CommandItemLayer());
        this.commands.add(new CommandContainment());
        this.commands.add(new CommandCreateFruit());
        this.commands.add(new CommandWineAging());
        //this.commands.add(new CommandMakeGrow());
        this.commands.add(new CommandPopulate());
        this.commands.add(new CommandSeason());
        this.commands.add(new CommandSupervise());
        this.commands.add(new CommandReloadSoundResistance());
        this.commands.add(new CommandReloadClimbSystem());
        this.commands.add(new CommandStaffReply());
        this.commands.add(new CommandArmorSee());
        this.commands.add(new CommandWeaponStats());
        this.commands.add(new CommandItemTexture());
        this.commands.add(new CommandRepair());
        this.commands.add(new CommandFoodQuality());
        this.commands.add(new CommandVoiceDistance());
        this.commands.register((CommandHandler) e.getServer().commandManager);
        Map<String, ICommand> commands = e.getServer().commandManager.getCommands();
        commands.put("pop", commands.get("summon"));
        commands.put("depop", commands.get("kick"));

        if (e.getSide() == Side.SERVER && DiscordImpl.getInstance().getConfigManager().isBotEnabled())
        {
            DiscordImpl.getInstance().getConfigManager().readConfig();
            try
            {
                DiscordImpl.getInstance().initBot();
            } catch (LoginException ex)
            {
                ex.printStackTrace();
            }

        }


        CraftManager.createManager("blacksmith");
        CraftManager.createManager("apothecary");
        {
            CraftManager apothecary = CraftManager.getManager("apothecary");
            for (EnumFlower flower : EnumFlower.values())
            {
                String soapName = "Savon parfum " + flower.getFormattedName();
                String soapDesc = "L'odeur du savon au parfum " + flower.getFormattedName() + " se répand dans la pièce";
                Color color = new Color(flower.getColor());
                String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                int value = 30;
                ItemStack stack = new ItemStack(KeldariaItems.SOAP);
                ItemWashingSoap.setSoap(stack, Soap.create(soapName, soapDesc, value, hex));
                HashMap<EnumAptitudes, Integer> aptitudes = Maps.newHashMap();
                aptitudes.put(EnumAptitudes.CRAFTING, 1);
                HashMap<CraftIngredient, Integer> ingredients = Maps.newHashMap();
                ingredients.put(new CraftIngredient(KeldariaItems.NEUTRAL_SOAP, 0), 1);
                ingredients.put(new CraftIngredient(flower.getItem(), 0), 1);

                apothecary.registerCraftEntry(new CraftEntry("apothecary/default/soap_" + flower.getName(), aptitudes, ingredients, stack, 0, "Un savon parfumé, et oui!", Lists.newArrayList(new PossibleWith(EnumJob.APOTHECARY, EnumJob.Level.MEDIUM))));
            }
        }
        CraftManager.createManager("woodworker");
        CraftManager.createManager("stonecutter");
        CraftManager.createManager("cooking");
        CraftManager.createManager("dressmaker");
        CraftManager.createManager(ItemFlambadou.FlambadouCrafts.DB_NAME);
        GroupManager.getInstance().init();
        SoundResistance.init();
        SyncedDatabases.add(PlayerSizes.DB_NAME);
        SyncedDatabases.add(SkyColor.DB_KEY);
        //SyncedDatabases.add(CustomSkinManager.DB_NAME);
        SyncedDatabases.add(MorphingManager.KEY);
        SyncedDatabases.add(CleanilessManager.DB_NAME);
        SyncedDatabases.add("roleplayinfos");
        SyncedDatabases.add("amputation");
        ItemFlambadou.FlambadouCrafts.addSyncedDB();
        SyncedDatabases.add("Containment:" + Points.KEY);
        SyncedDatabases.add("Containment:" + Regions.KEY);
        SyncedDatabases.add(EnumAttackType.KEY_ATTACK_TYPE);

        Database serverInfos = Databases.getDatabase("keldaria:serverInfos");
        NBTTagList pigeonsList = new NBTTagList();
        if (serverInfos.contains("PigeonTravels"))
        {
            try
            {
                NBTTagCompound nbt = JsonToNBT.getTagFromJson(serverInfos.getString("PigeonTravels"));
                pigeonsList = nbt.getTagList("list", Constants.NBT.TAG_COMPOUND);
            } catch (NBTException ex)
            {
                ex.printStackTrace();
            }
        }
        this.pigeonTravels = new EntityHomingPigeon.PigeonTravels(e.getServer().getWorld(0), pigeonsList);
        this.climbSystem.init(Helpers.readFileToString(this.files.CLIMB_SYSTEM));

        this.registry.getBlastFurnace().reload();
    }

    public EntityHomingPigeon.PigeonTravels getPigeonTravels()
    {
        return pigeonTravels;
    }

    @Mod.EventHandler
    public void onServerStop(FMLServerStoppingEvent event)
    {
        if (DiscordImpl.getInstance().hasBot())
        {
            DiscordImpl.getInstance().getBot().MESSAGE_SERVICE.shutdown();
        }
        KeldariaVoices.SERVICE.shutdown();
    }

    public ClimbSystem getClimbSystem()
    {
        return climbSystem;
    }

}
