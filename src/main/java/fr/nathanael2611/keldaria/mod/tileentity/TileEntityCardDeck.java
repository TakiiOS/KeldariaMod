/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.tileentity;

import fr.nathanael2611.keldaria.mod.item.ItemCard;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;

public class TileEntityCardDeck extends TileEntityFurnitureContainer
{

    public TileEntityCardDeck()
    {
        super(9*4, 9, true);
        this.itemConsumer = stack -> stack.getItem() instanceof ItemCard;
    }

    public void pick(EntityPlayer picker)
    {
        if(isEmpty())
        {
            picker.sendMessage(new TextComponentString("§cLa pioche est vide! :C"));
            Helpers.sendInRadius(picker.getPosition(), new TextComponentString("§c" + picker.getName() + " a tenté de pioché, mais la pioche semblait être vide!"), 6);
            return;
        }
        for(; ;)
        {
            int rand = Helpers.randomInteger(0, getSizeInventory() - 1);
            ItemStack stack = getStackInSlot(rand);
            if(!stack.isEmpty())
            {
                picker.addItemStackToInventory(stack.copy());
                picker.sendMessage(new TextComponentString("§aVous avez pioché: " + stack.getDisplayName()));
                setInventorySlotContents(rand, ItemStack.EMPTY);
                markDirty();
                IBlockState state = world.getBlockState(this.pos);
                this.world.notifyBlockUpdate(this.pos, state, state, 3);
                Helpers.sendInRadius(picker.getPosition(), new TextComponentString("§2" + picker.getName() + " a pioché!"), 6);
                return;
            }
        }
    }


}
