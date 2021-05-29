package fr.nathanael2611.keldaria.mod.util;

import fr.nathanael2611.keldaria.mod.command.inventory.InvseeHandler;
import fr.nathanael2611.keldaria.mod.handler.InventoryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class PlayerInvChest extends InventoryBasic
{
    public EntityPlayerMP vieuwer;
    public InventoryPlayer ownerInv;
    public EntityPlayerMP owner = null;
    public OfflinePlayerAccessor playerAccessor = null;
    public boolean allowUpdate;

    private PlayerInvChest(InventoryPlayer ownerInv, EntityPlayerMP vieuwer)
    {
        super("Invsee", false, ownerInv.mainInventory.size());
        this.ownerInv  = ownerInv;
        final ItemStack DISABLED_SLOT = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 14).setStackDisplayName(InventoryHandler.DISABLED_SLOT_NAME);
        for(int i = 18; i < 27; i ++)
        {
            setInventorySlotContents(
                    i, DISABLED_SLOT
            );
        }

        setInventorySlotContents(27, DISABLED_SLOT);
        setInventorySlotContents(29, DISABLED_SLOT);
        setInventorySlotContents(30, DISABLED_SLOT);
        setInventorySlotContents(getSizeInventory() - 1, DISABLED_SLOT);
        this.vieuwer = vieuwer;
    }

    public PlayerInvChest(EntityPlayerMP owner, EntityPlayerMP vieuwer)
    {
        this(owner.inventory, vieuwer);
        this.owner = owner;
    }

    public PlayerInvChest(OfflinePlayerAccessor owner, EntityPlayerMP vieuwer)
    {
        this(owner.inventory, vieuwer);
        this.playerAccessor = owner;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
        if(owner != null) InvseeHandler.register(this);
        allowUpdate = false;
        for(int i = 0; i < 9; i ++)
        {
            setInventorySlotContents(i, ownerInv.mainInventory.get(i));
        }
        for(int i = 27; i < 27 + 9; i ++)
        {
            setInventorySlotContents(i - 18, ownerInv.mainInventory.get(i));
        }
        setInventorySlotContents(getSizeInventory() - 5, ownerInv.armorInventory.get(0));
        setInventorySlotContents(getSizeInventory() - 4, ownerInv.armorInventory.get(1));
        setInventorySlotContents(getSizeInventory() - 3, ownerInv.armorInventory.get(2));
        setInventorySlotContents(getSizeInventory() - 2, ownerInv.armorInventory.get(3));

        setInventorySlotContents(28, ownerInv.offHandInventory.get(0));

        allowUpdate = true;
        super.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        InvseeHandler.remove(this);
        if (allowUpdate)
        {
            for(int i = 0; i < 9; i ++)
            {
                ownerInv.mainInventory.set(i, getStackInSlot(i));
            }
            for(int i = 27; i < 27 + 9; i ++)
            {
                ownerInv.mainInventory.set(i, getStackInSlot(i - 18));
            }
            ownerInv.armorInventory.set(0, getStackInSlot(getSizeInventory() - 5));
            ownerInv.armorInventory.set(1, getStackInSlot(getSizeInventory() - 4));
            ownerInv.armorInventory.set(2, getStackInSlot(getSizeInventory() - 3));
            ownerInv.armorInventory.set(3, getStackInSlot(getSizeInventory() - 2));

            ownerInv.offHandInventory.set(0, getStackInSlot(28));

        }

        markDirty();
        if(playerAccessor != null) this.playerAccessor.rewrite();
        super.closeInventory(player);
    }

    @Override
    public void markDirty()
    {
        super.markDirty();
        if (allowUpdate)
        {
            for(int i = 0; i < 9; i ++)
            {
                ownerInv.mainInventory.set(i, getStackInSlot(i));
            }
            for(int i = 27; i < 27 + 9; i ++)
            {
                ownerInv.mainInventory.set(i, getStackInSlot(i - 18));
            }
            ownerInv.armorInventory.set(0, getStackInSlot(getSizeInventory() - 5));
            ownerInv.armorInventory.set(1, getStackInSlot(getSizeInventory() - 4));
            ownerInv.armorInventory.set(2, getStackInSlot(getSizeInventory() - 3));
            ownerInv.armorInventory.set(3, getStackInSlot(getSizeInventory() - 2));

            ownerInv.offHandInventory.set(0, getStackInSlot(28));

        }
    }

    public void update()
    {
        allowUpdate = false;
        for(int i = 0; i < 9; i ++)
        {
            setInventorySlotContents(i, ownerInv.mainInventory.get(i));
        }
        for(int i = 27; i < 27 + 9; i ++)
        {
            setInventorySlotContents(i - 18, ownerInv.mainInventory.get(i));
        }
        setInventorySlotContents(getSizeInventory() - 5, ownerInv.armorInventory.get(0));
        setInventorySlotContents(getSizeInventory() - 4, ownerInv.armorInventory.get(1));
        setInventorySlotContents(getSizeInventory() - 3, ownerInv.armorInventory.get(2));
        setInventorySlotContents(getSizeInventory() - 2, ownerInv.armorInventory.get(3));

        setInventorySlotContents(28, ownerInv.offHandInventory.get(0));

        allowUpdate = true;
        markDirty();
    }
}