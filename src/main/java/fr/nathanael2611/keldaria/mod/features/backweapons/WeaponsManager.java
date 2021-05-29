package fr.nathanael2611.keldaria.mod.features.backweapons;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.item.ItemMace;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketUpdateWeapons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.HashMap;

public class WeaponsManager implements INBTSerializable<NBTTagCompound>
{


    private final HashMap<String, Weapons> PLAYER_WEAPONS = Maps.newHashMap();

    public WeaponsManager collect()
    {
        //PLAYER_WEAPONS.clear();
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(player ->
        {
            NonNullList<ItemStack> stacks = collectWeapons(player);
            ItemStack shield = getShield(player);
            Weapons weapons = new Weapons(player.getName(), stacks.get(0), stacks.get(1), stacks.get(2), stacks.get(3), shield);
            PLAYER_WEAPONS.put(player.getName(), weapons);
        });
        return this;
    }

    public WeaponsManager sync()
    {
        /*if(this.server)*/
        {
            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(player -> KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketUpdateWeapons(), player));
        }
        return this;
    }

    public NonNullList<ItemStack> collectWeapons(EntityPlayerMP player)
    {
        NonNullList<ItemStack> weapons = NonNullList.withSize(4, ItemStack.EMPTY);
        int foundWeapons = 0;
        for (ItemStack stack : player.inventory.mainInventory)
        {
            if(foundWeapons < 4)
            {
                if(stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemBow || stack.getItem() instanceof ItemAxe || stack.getItem() instanceof ItemMace)
                {
                    if(player.getHeldItemMainhand() != stack && player.getHeldItemOffhand() != stack)
                    {
                        weapons.set(foundWeapons, stack.copy());
                    }
                    else
                    {
                        weapons.set(foundWeapons, ItemStack.EMPTY);
                    }
                    foundWeapons ++;
                }
            }
        }
        return weapons;
    }

    public ItemStack getShield(EntityPlayerMP player)
    {
        for (ItemStack stack : player.inventory.mainInventory)
        {
            if(stack.getItem() instanceof ItemShield)
            {
                if(player.getHeldItemMainhand() != stack && player.getHeldItemOffhand() != stack)
                {
                    //weapons.set(foundWeapons, stack.copy());
                    return stack;
                }
            }

        }
        return ItemStack.EMPTY;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        this.PLAYER_WEAPONS.forEach((playerName, weapons) ->
        {
            list.appendTag(weapons.serializeNBT());
        });
        compound.setTag("Weapons", list);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.PLAYER_WEAPONS.clear();
        NBTTagList list = nbt.getTagList("Weapons", Constants.NBT.TAG_COMPOUND);
        list.forEach(nbtBase ->
        {
            NBTTagCompound compound = (NBTTagCompound) nbtBase;
            Weapons weapons = new Weapons(compound);
            this.PLAYER_WEAPONS.put(weapons.getPlayerName(), weapons);
        });
    }

    public Weapons getWeapons(EntityPlayer player)
    {
        if(PLAYER_WEAPONS.containsKey(player.getName()))
        {
            return PLAYER_WEAPONS.get(player.getName());
        }
        else
        {
            return PLAYER_WEAPONS.put(player.getName(), new Weapons(player.getName()));
        }
    }
}
