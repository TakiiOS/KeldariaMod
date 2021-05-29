package fr.nathanael2611.keldaria.mod.features;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class ClimbSystem implements INBTSerializable<NBTTagCompound>
{

    private final HashMap<Block, ClimbEntry> REGISTRY = Maps.newHashMap();

    public ClimbSystem()
    {
    }

    public double canClimb(EntityPlayer player, BlockPos pos)
    {
        return canClimb(player, pos, false);
    }

    private double canClimb(EntityPlayer player, BlockPos pos, boolean recusive)
    {
        World world = player.world;
        Block block = world.getBlockState(pos).getBlock();
        if(REGISTRY.containsKey(block))
        {
            ClimbEntry entry = REGISTRY.get(block);
            if(entry.getClimbType() == ClimbType.GRAB)
            {
                if(world.getBlockState(pos.up()).getBlock() == Blocks.AIR)
                {
                    return entry.getClimbSpeed();
                }
                return 0;
            }
            else
            {
                return entry.getClimbSpeed();
            }
        } else if(!recusive)
        {
            return canClimb(player, pos.up(), true);
        }
        return 0;
    }

    public void init(String json)
    {
        this.REGISTRY.clear();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        if(element.isJsonObject())
        {
            JsonObject object = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> set : object.entrySet())
            {
                if(set.getValue() instanceof JsonObject)
                {
                    JsonObject value = set.getValue().getAsJsonObject();
                    Block block = Block.getBlockFromName(set.getKey());
                    if(block != null && value.has("climbType") && value.has("climbSpeed"))
                    {
                        ClimbEntry entry = new ClimbEntry(block, ClimbType.byId(value.get("climbType").getAsInt()), value.get("climbSpeed").getAsDouble());
                        this.REGISTRY.put(block, entry);
                    }

                }
            }
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        this.REGISTRY.forEach(((block, climbEntry) -> compound.setTag(block.getRegistryName().toString(), climbEntry.serializeNBT())));
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound)
    {
        this.REGISTRY.clear();
        for (String s : compound.getKeySet())
        {
            if(compound.getTag(s) instanceof NBTTagCompound)
            {
                ClimbEntry entry = new ClimbEntry(compound.getCompoundTag(s));

                this.REGISTRY.put(entry.getBlock(), entry);
            }
        }
    }

    public static class ClimbEntry implements INBTSerializable<NBTTagCompound>
    {

        private Block block;
        private ClimbType climbType;
        private double climbSpeed;

        public ClimbEntry(Block block, ClimbType climbType, double climbSpeed)
        {
            this.block = block;
            this.climbType = climbType;
            this.climbSpeed = climbSpeed;
        }

        public ClimbEntry(NBTTagCompound compound)
        {
            this.deserializeNBT(compound);
        }

        public Block getBlock()
        {
            return block;
        }

        public ClimbType getClimbType()
        {
            return climbType;
        }

        public double getClimbSpeed()
        {
            return climbSpeed;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setString("Block", this.block.getRegistryName().toString());
            compound.setInteger("ClimbType", this.climbType.getId());
            compound.setDouble("ClimbSpeed", this.climbSpeed);
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound compound)
        {
            this.block = Block.getBlockFromName(compound.getString("Block"));
            this.climbType = ClimbType.byId(compound.getInteger("ClimbType"));
            this.climbSpeed = compound.getDouble("ClimbSpeed");
        }
    }

    public static enum ClimbType
    {
        CLIMB(0), GRAB(1);

        private int id;

        ClimbType(int id)
        {
            this.id = id;
        }

        public int getId()
        {
            return id;
        }

        public static ClimbType byId(int id)
        {
            for (ClimbType value : ClimbType.values())
            {
                if(value.getId() == id) return value;
            }
            return GRAB;
        }
    }

}
