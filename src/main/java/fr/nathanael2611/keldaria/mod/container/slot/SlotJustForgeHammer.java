package fr.nathanael2611.keldaria.mod.container.slot;

import fr.nathanael2611.keldaria.mod.item.ItemForgeHammer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/*
    This class is used for create a Slot that accept only instances of SlotJustForgeHammer
 */
public class SlotJustForgeHammer extends Slot
{
        /*
            Constructor
         */
        public SlotJustForgeHammer(IInventory inventory, int id, int x, int y)
        {
            super(inventory, id, x, y);
        }

        /*
            Check if an ItemStack is valid for the Slot.
         */
        @Override
        public boolean isItemValid(ItemStack stack)
        {
            return stack.getItem() instanceof ItemForgeHammer;
        }

        public ItemStack decrStackSize(int amount)
        {
            return super.decrStackSize(amount);
        }
}
