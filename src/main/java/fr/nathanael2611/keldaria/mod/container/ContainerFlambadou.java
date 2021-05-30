/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.container;

import fr.nathanael2611.keldaria.mod.container.slot.SlotResult;
import fr.nathanael2611.keldaria.mod.inventory.InventoryFlambadou;
import fr.nathanael2611.keldaria.mod.item.ItemFlambadou;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFlambadou extends ContainerKeldaria
{
    private EntityPlayer player;

    private InventoryFlambadou inventoryFlambadou = new InventoryFlambadou(this);

    public ContainerFlambadou(InventoryPlayer inventoryPlayer, EntityPlayer player)
    {
        super(inventoryPlayer);
        this.player = player;

        this.addSlotToContainer(new Slot(inventoryFlambadou, 0, 62, 16)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return ItemFlambadou.FlambadouCrafts.getResult(player, stack.getItem()) != ItemStack.EMPTY;
            }
        });
        this.addSlotToContainer(new Slot(inventoryFlambadou, 1, 62, 52)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() == KeldariaItems.ANIMAL_GREASE;
            }
        });
        this.addSlotToContainer(new SlotResult(inventoryFlambadou, 2, 98, 34)
        {


            @Override
            public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
            {
                inventoryFlambadou.decrStackSize(0, 1);
                inventoryFlambadou.decrStackSize(1, 1);
                return super.onTake(thePlayer, stack);
            }
        });

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, k, 8 + k * 18, 84 + (18 * 3) + 4));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        if (!playerIn.world.isRemote)
        {
            for (int i = 0; i < this.inventoryFlambadou.getSizeInventory() - 1; i++)
            {
                ItemStack itemstack = this.inventoryFlambadou.removeStackFromSlot(i);
                player.dropItem(itemstack, false);
            }
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        super.onCraftMatrixChanged(inventoryIn);
        if (this.inventoryFlambadou.getStackInSlot(1).getItem() == KeldariaItems.ANIMAL_GREASE)
        {
            this.inventoryFlambadou.setInventorySlotContents(2, ItemFlambadou.FlambadouCrafts.getResult(player, this.inventoryFlambadou.getStackInSlot(0).getItem()));
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack stackToReturn = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();
            stackToReturn = stack.copy();

            if (index < 10)
            {
                if (!this.mergeItemStack(stack, 10, 40, true))
                {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack, 0, 4, false))
            {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            } else
            {
                slot.onSlotChanged();
            }
        }
        return stackToReturn;
    }
}