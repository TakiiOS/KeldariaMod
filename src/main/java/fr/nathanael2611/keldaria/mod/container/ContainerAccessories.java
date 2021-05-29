package fr.nathanael2611.keldaria.mod.container;

import fr.nathanael2611.keldaria.mod.clothe.InventoryClothes;
import fr.nathanael2611.keldaria.mod.features.accessories.InventoryAccessories;
import fr.nathanael2611.keldaria.mod.inventory.InventoryArmor;
import fr.nathanael2611.keldaria.mod.item.ItemArmorPart;
import fr.nathanael2611.keldaria.mod.item.ItemClothe;
import fr.nathanael2611.keldaria.mod.item.accessory.ItemAccessory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ContainerAccessories extends ContainerKeldaria
{
    private final InventoryAccessories accessories;
    private final InventoryArmor armor;
    private final InventoryClothes clothes;

    public ContainerAccessories(InventoryPlayer player, InventoryAccessories accessories, InventoryArmor armor, InventoryClothes clothes)
    {
        super(player);
        this.accessories = accessories;
        this.armor = armor;
        this.clothes = clothes;

        {
            for (int i = 0; i < this.clothes.getSizeInventory(); i++)
            {
                int y = i < 9 ? 9 : 109;
                this.addSlotToContainer(new Slot(this.clothes, i, 8 + (i * 18) - (i < 9 ? 0 : (18*9)), y)
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
            }
        }

        {
            int y = 0;
            for(int i = 0; i < this.accessories.getSizeInventory(); i ++)
            {
                int finalI = i;
                this.addSlotToContainer(new Slot(this.accessories, finalI, 8, 32 + y)
                {
                    @Override
                    public boolean isItemValid(ItemStack stack)
                    {
                        if(stack.getItem() instanceof ItemAccessory)
                        {
                            ItemAccessory accessory = (ItemAccessory) stack.getItem();
                            return (finalI == 0 && accessory.getRequiredEquipmentSlot() == EntityEquipmentSlot.HEAD)
                                    || (finalI == 1 && accessory.getRequiredEquipmentSlot() == EntityEquipmentSlot.CHEST)
                                    || (finalI == 2 && accessory.getRequiredEquipmentSlot() == EntityEquipmentSlot.LEGS)
                                    || (finalI == 3 && accessory.getRequiredEquipmentSlot() == EntityEquipmentSlot.FEET);
                        }
                        return false;
                    }
                });
                y += 18;
            }
        }
        {
            int y = 0;
            int x = 0;
            for(int i = 0; i < this.armor.getSizeInventory(); i ++)
            {
                if(i == 2)
                {
                    y ++;
                    x = 0;
                }
                else if(i == 7)
                {
                    y ++;
                    x = 0;
                }
                else if(i == 10)
                {
                    y ++;
                    x = 0;
                }
                int finalY = y;
                this.addSlotToContainer(new Slot(this.armor, i, 98 - 18 + x * 18, 32 + finalY * 18)
                {
                    @Override
                    public boolean isItemValid(ItemStack stack)
                    {
                        if(stack.getItem() instanceof ItemArmorPart)
                        {
                            ItemArmorPart part = (ItemArmorPart) stack.getItem();
                            if(finalY == 0) return part.getRequiredSlot() == EntityEquipmentSlot.HEAD;
                            else if(finalY == 1) return part.getRequiredSlot() == EntityEquipmentSlot.CHEST;
                            else if(finalY == 2) return part.getRequiredSlot() == EntityEquipmentSlot.LEGS;
                            else if(finalY == 3) return part.getRequiredSlot() == EntityEquipmentSlot.FEET;
                        }
                        return false;
                    }
                    @Nullable
                    @SideOnly(Side.CLIENT)
                    public String getSlotTexture()
                    {
                        return ItemArmor.EMPTY_SLOT_NAMES[(finalY == 0 ? EntityEquipmentSlot.HEAD : finalY == 1 ? EntityEquipmentSlot.CHEST : finalY == 2 ? EntityEquipmentSlot.LEGS : EntityEquipmentSlot.FEET).getIndex()];
                    }

                });
                x ++;
            }
        }
        this.drawPlayerInventory(8, 132);
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.accessories);
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack stackToReturn = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();
            stackToReturn = stack.copy();


            int invSize = this.inventorySlots.size() - playerIn.inventory.mainInventory.size();
            if (index < invSize)
            {
                if (!this.mergeItemStack(stack, invSize, 36 + invSize, true))
                {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack, 0, invSize, false))
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