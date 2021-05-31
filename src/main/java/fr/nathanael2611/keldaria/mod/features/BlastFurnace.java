package fr.nathanael2611.keldaria.mod.features;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketBlastFurnaceCrafts;
import fr.nathanael2611.keldaria.mod.registry.KeldariaFiles;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;

public class BlastFurnace implements INBTSerializable<NBTTagCompound>
{

    private final HashMap<Item, ItemStack> VALUES = Maps.newHashMap();

    public void reload()
    {
        VALUES.clear();
        String str = Helpers.readFileToString(Keldaria.getInstance().getFiles().BLAST_FURNACE);
        JsonObject json = new JsonParser().parse(str).getAsJsonObject();
        json.entrySet().forEach(entry -> {
            Item item = Item.REGISTRY.getObject(new ResourceLocation(entry.getKey()));
            if(item != null)
            {
                String val = entry.getValue().getAsString();
                ItemStack stack = Helpers.getItemStackFromString(val);
                if(!stack.isEmpty())
                {
                    VALUES.put(item, stack);
                }
            }
        });
    }

    public int getSmeltTime(Item item)
    {
        return getResult(item).getCount() * 100;
    }

    public ItemStack getResult(Item item)
    {
        return VALUES.getOrDefault(item, ItemStack.EMPTY);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        this.VALUES.forEach((key, value) -> {
            compound.setString(key.getRegistryName().toString(), value.serializeNBT().toString());
        });
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.VALUES.clear();
        for (String s : nbt.getKeySet())
        {
            String value = nbt.getString(s);
            if(value != null)
            {
                Item item = Item.REGISTRY.getObject(new ResourceLocation(s));
                if(item != null)
                {
                    ItemStack stack = Helpers.getItemStackFromString(value);
                    if(!stack.isEmpty())
                    {
                        VALUES.put(item, stack);
                    }
                }
            }
        }
    }

    public void updateOn(EntityPlayerMP player)
    {
        KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketBlastFurnaceCrafts(), player);
    }

    public void updateOnAll()
    {
        KeldariaPacketHandler.getInstance().getNetwork().sendToAll(new PacketBlastFurnaceCrafts());
    }

}
