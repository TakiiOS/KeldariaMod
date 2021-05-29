package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.container.ContainerItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class ItemContainer extends Item
{

    private final Class<? extends ContainerItem> CONTAINER_TYPE;

    public ItemContainer(Class<? extends ContainerItem> containerType)
    {
        this.CONTAINER_TYPE = containerType;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (worldIn.isRemote || !isSelected) return;
        if (!(entityIn instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entityIn;
        Container openContainer = player.openContainer;
        if (!(this.CONTAINER_TYPE.isAssignableFrom(openContainer.getClass()))) return;
        ContainerItem container = (ContainerItem) openContainer;
        if (container.updateNotification)
        {
            NBTTagCompound compound = new NBTTagCompound();
            /*if(stack.getTagCompound() != null && stack.getTagCompound().getSize() > 0)
            {
                compound = stack.getTagCompound();
            }*/
            container.inventory.writeNBT(compound);
            if (stack.getSubCompound("display") != null)
            {
                compound.setTag("display", stack.getSubCompound("display"));
            }
            stack.setTagCompound(compound);
            container.updateNotification = false;
        }
    }


}
