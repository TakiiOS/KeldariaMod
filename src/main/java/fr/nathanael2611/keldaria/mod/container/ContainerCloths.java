package fr.nathanael2611.keldaria.mod.container;

import fr.nathanael2611.keldaria.mod.clothe.InventoryClothes;
import fr.nathanael2611.keldaria.mod.item.ItemClothe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCloths extends ContainerKeldaria
{

    private final InventoryClothes cloths;

    public ContainerCloths(InventoryClothes cloths)
    {
        super(cloths.player.inventory);
        this.cloths = cloths;

        int x = 0;
        for (int i = 0; i < this.cloths.getSizeInventory(); i++)
        {
            this.addSlotToContainer(new Slot(this.cloths, i, 8 + x, 84)
            {
                @Override
                public boolean isItemValid(ItemStack stack)
                {
                    if (stack.getItem() instanceof ItemClothe)
                    {
                        return ItemClothe.isClothValid(stack);
                    }
                    return false;
                }
            });
            x += 18;
        }
        this.drawPlayerInventory(44, 108);
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.cloths);
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        return ItemStack.EMPTY;
    }
}