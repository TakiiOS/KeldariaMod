package fr.nathanael2611.keldaria.mod.features.backweapons;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class Weapons implements INBTSerializable<NBTTagCompound>
{

    private String playerName;
    private ItemStack item1;
    private ItemStack item2;
    private ItemStack item3;
    private ItemStack item4;
    private ItemStack shield;

    public Weapons(String playerName, ItemStack item1, ItemStack item2, ItemStack item3, ItemStack item4, ItemStack shield)
    {
        this.playerName = playerName;
        this.item1 = item1.copy();
        this.item2 = item2.copy();
        this.item3 = item3.copy();
        this.item4 = item4.copy();
        this.shield = shield.copy();
    }

    public Weapons(String playerName)
    {
        this(playerName, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
    }

    public Weapons(NBTTagCompound compound)
    {
        this("deserialize");
        deserializeNBT(compound);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("PlayerName", this.playerName);
        compound.setTag("Item1", item1.serializeNBT());
        compound.setTag("Item2", item2.serializeNBT());
        compound.setTag("Item3", item3.serializeNBT());
        compound.setTag("Item4", item4.serializeNBT());
        compound.setTag("Shield", shield.serializeNBT());
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        playerName = nbt.getString("PlayerName");
        item1 = new ItemStack(nbt.getCompoundTag("Item1"));
        item2 = new ItemStack(nbt.getCompoundTag("Item2"));
        item3 = new ItemStack(nbt.getCompoundTag("Item3"));
        item4 = new ItemStack(nbt.getCompoundTag("Item4"));
        shield = new ItemStack(nbt.getCompoundTag("Shield"));
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public ItemStack getItem1()
    {
        return item1.copy();
    }

    public ItemStack getItem2()
    {
        return item2.copy();
    }

    public ItemStack getItem3()
    {
        return item3.copy();
    }

    public ItemStack getItem4()
    {
        return item4.copy();
    }

    public ItemStack getShield()
    {
        return shield;
    }
}
