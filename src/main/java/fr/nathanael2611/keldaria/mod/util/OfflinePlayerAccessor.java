package fr.nathanael2611.keldaria.mod.util;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class OfflinePlayerAccessor
{

    private final File dataFile;

    private MinecraftServer server;

    private String name;
    private UUID uuid;

    private NBTTagCompound baseCompound;

    public OfflinePlayerInventory inventory = new OfflinePlayerInventory(this);

    private float health = 20;
    private FoodStats foodStats = new FoodStats();

    public double posX = 0;
    public double posY = 0;
    public double posZ = 0;

    private GameType gameType = GameType.SURVIVAL;
    public PlayerCapabilities capabilities = new PlayerCapabilities();
    public int experienceLevel = 0;

    public static boolean hasOfflineGameProfile(MinecraftServer server, String lastPlayerName)
    {
        File dataFile = new File(server.getWorld(0).getSaveHandler().getWorldDirectory(), "playerdata/" + server.getPlayerProfileCache().getGameProfileForUsername(lastPlayerName).getId().toString() + ".dat");
        return dataFile.exists();
    }

    public static GameProfile getOfflineGameProfile(MinecraftServer server, String lastPlayerName)
    {
        PlayerProfileCache playerprofilecache = server.getPlayerProfileCache();
        return playerprofilecache.getGameProfileForUsername(lastPlayerName);
    }

    public OfflinePlayerAccessor(MinecraftServer server, String lastKnownName) throws NullPointerException
    {
        this(server, (server.getPlayerList().getPlayerByUsername(lastKnownName) != null ? server.getPlayerList().getPlayerByUsername(lastKnownName).getGameProfile() : getOfflineGameProfile(server, lastKnownName)));
    }

    public OfflinePlayerAccessor(MinecraftServer server, GameProfile gameProfile)
    {
        if(gameProfile == null) throw new NullPointerException("Given game profile is null");
        this.server = server;
        SaveHandler saveHandler = (SaveHandler) server.getWorld(0).getSaveHandler();
        this.name = gameProfile.getName();
        this.uuid = gameProfile.getId();

        WorldServer worldServer = server.getWorld(0);

        this.dataFile = new File(saveHandler.getWorldDirectory(), "playerdata/" + this.uuid.toString() + ".dat");

        NBTTagCompound compound;
        try {
            compound  = CompressedStreamTools.readCompressed(new FileInputStream(this.dataFile));
        } catch (IOException e) {
            e.printStackTrace();
            compound = new NBTTagCompound();
        }
        this.baseCompound = compound;
        readFromNBT(compound);
    }

    private void readFromNBT(NBTTagCompound compound)
    {
        if(compound.hasKey("Inventory"))
        {
            NBTTagList nbttaglist = compound.getTagList("Inventory", 10);
            this.inventory.readFromNBT(nbttaglist);
            // -> this.inventory.currentItem = compound.getInteger("SelectedItemSlot"); Not really important
        }
        if(compound.hasKey("playerGameType"))
        {
            this.gameType = GameType.getByID(compound.getInteger("playerGameType"));
        }
        if (compound.hasKey("Health", 99))
        {
            this.health = compound.getFloat("Health");
        }
        this.foodStats.readNBT(compound);
        this.capabilities.readCapabilitiesFromNBT(compound);
        if(compound.hasKey("experienceLevel"))
        {
            this.experienceLevel = compound.getInteger("XpLevel");
        }
        if(compound.hasKey("Pos"))
        {
            NBTTagList nbttaglist = compound.getTagList("Pos", 6);
            posX = nbttaglist.getDoubleAt(0);
            posY = nbttaglist.getDoubleAt(1);
            posZ = nbttaglist.getDoubleAt(2);
        }
    }

    public void rewrite()
    {
        NBTTagCompound compound = this.baseCompound;

        NBTTagList nbttaglist = new NBTTagList();
        this.inventory.writeToNBT(nbttaglist);
        compound.setTag("Inventory", nbttaglist);

        compound.setInteger("playerGameType", this.gameType.getID());

        compound.setFloat("Health", this.health);

        this.foodStats.writeNBT(compound);
        this.capabilities.writeCapabilitiesToNBT(compound);

        compound.setInteger("XpLevel", this.experienceLevel);

        NBTTagList position = new NBTTagList();
        position.appendTag(new NBTTagDouble(this.posX));
        position.appendTag(new NBTTagDouble(this.posY));
        position.appendTag(new NBTTagDouble(this.posZ));

        try {
            CompressedStreamTools.writeCompressed(compound, new FileOutputStream(this.dataFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName()
    {
        return name;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public GameType getGameType()
    {
        return gameType;
    }

    public void setGameType(GameType gameType)
    {
        this.gameType = gameType;
    }

    public BlockPos getPosition()
    {
        return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
    }

    public void setPosition(double posX, double posY, double posZ)
    {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public float getHealth()
    {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public FoodStats getFoodStats()
    {
        return foodStats;
    }
}
