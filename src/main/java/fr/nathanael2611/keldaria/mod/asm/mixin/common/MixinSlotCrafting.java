/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import fr.nathanael2611.keldaria.mod.features.lockpick.LocksHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SlotCrafting.class)
public abstract class MixinSlotCrafting extends Slot
{
    @Shadow @Final private InventoryCrafting craftMatrix;

    @Shadow @Final private EntityPlayer player;

    public MixinSlotCrafting(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
    {
        this.onCrafting(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
        NonNullList<ItemStack> nonnulllist = CraftingManager.getRemainingItems(this.craftMatrix, thePlayer.world);
        if(LocksHelper.isMatrixWillCraftKey(this.craftMatrix))
        {
            nonnulllist.set(0, this.craftMatrix.getStackInSlot(0).copy());
        }
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

        for (int i = 0; i < nonnulllist.size(); ++i)
        {
            ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            ItemStack itemstack1 = nonnulllist.get(i);

            if (!itemstack.isEmpty())
            {
                this.craftMatrix.decrStackSize(i, 1);
                itemstack = this.craftMatrix.getStackInSlot(i);
            }

            if (!itemstack1.isEmpty())
            {
                if (itemstack.isEmpty())
                {
                    this.craftMatrix.setInventorySlotContents(i, itemstack1);
                }
                else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1))
                {
                    itemstack1.grow(itemstack.getCount());
                    this.craftMatrix.setInventorySlotContents(i, itemstack1);
                }
                else if (!this.player.inventory.addItemStackToInventory(itemstack1))
                {
                    this.player.dropItem(itemstack1, false);
                }
            }
        }

        return stack;
    }

}
