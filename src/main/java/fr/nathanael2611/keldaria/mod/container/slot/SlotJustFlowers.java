package fr.nathanael2611.keldaria.mod.container.slot;

import fr.nathanael2611.keldaria.mod.item.ItemFlowerBlock;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotJustFlowers extends Slot
{
    public SlotJustFlowers(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() instanceof ItemFlowerBlock;
    }
}
