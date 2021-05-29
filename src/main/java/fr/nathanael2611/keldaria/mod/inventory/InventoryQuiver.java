package fr.nathanael2611.keldaria.mod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;

public class InventoryQuiver extends InventoryBasic implements INBTSerializable<NBTTagCompound>
{

    private EntityPlayer owner;
    private ItemStack quiver;

    public InventoryQuiver(EntityPlayer owner, ItemStack quiver)
    {
        super("Quiver", false, 4);
        this.owner = owner;
        this.quiver = quiver;
        if(quiver.hasTagCompound())
        {
            this.deserializeNBT(quiver.getTagCompound());
        }
    }

    @Override
    public void markDirty()
    {
        super.markDirty();
        if(this.owner.world.isRemote) return;
        quiver.setTagCompound(this.serializeNBT());
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        NonNullList<ItemStack> list = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < list.size(); i++)
        {
            list.set(i, getStackInSlot(i));
        }
        ItemStackHelper.saveAllItems(compound, list);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        NonNullList<ItemStack> list = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, list);
        for (int i = 0; i < list.size(); i++)
        {
            setInventorySlotContents(i, list.get(i));
        }
    }
}
