/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.handler;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.log.PlayerSpy;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.server.RolePlayNames;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

/**
 * This class is used for all things that is in link with the player inventory
 * Like: the locked player slots in the inventory
 *
 * @author Nathanael2611
 */
public class InventoryHandler
{

    public static final String DISABLED_SLOT_NAME = "§§§";
    public static final ItemStack DISABLED_ITEM = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 0);

    private static final HashMap<Integer, Long> LAST_REFILL = Maps.newHashMap();

    static {
        DISABLED_ITEM.setStackDisplayName(DISABLED_SLOT_NAME);

    }

    /**
     * Automatically fill locked slots in the player inventory
     */
    @SubscribeEvent
    public void playerTickEvent(TickEvent.PlayerTickEvent e)
    {
        if(e.player.world.isRemote) return;
        EntityPlayerMP player = (EntityPlayerMP) e.player;
        if(player == null) return;
        if(player.interactionManager.getGameType() == GameType.SPECTATOR) return;

        long l = System.currentTimeMillis();
        LAST_REFILL.putIfAbsent(player.getEntityId(),l);
        if(l - LAST_REFILL.get(player.getEntityId()) > 5000)
        {
            LAST_REFILL.put(player.getEntityId(), l);

            player.experience = 1000000;

            InventoryPlayer inventory = player.inventory;
            for(int i = 9; i<27; i++)
            {
                ItemStack stack = inventory.getStackInSlot(i);
                if(!stack.getDisplayName().equals(DISABLED_SLOT_NAME) || stack.getCount() != 1)
                {
                    inventory.mainInventory.set(i, DISABLED_ITEM.copy());
                }
            }

        }


    }

    /**
     * Prevent the tooltip rendering if the selected ItemStack is is a locked slot
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void drawTooltipEvent(RenderTooltipEvent.Pre e)
    {
        e.setCanceled(e.getStack().getDisplayName().equalsIgnoreCase(DISABLED_SLOT_NAME));

    }

    /**
     * Simply cancel the item-drop if the dropped-item is a disabled_slot :)
     */
    @SubscribeEvent
    public void onItemToss(ItemTossEvent e)
    {
        if(e.isCancelable())
        {
            e.setCanceled(e.getEntityItem().getItem().getDisplayName().equalsIgnoreCase(DISABLED_SLOT_NAME));
        }
        if(e.getEntityItem().getItem().getItem() == KeldariaItems.DICE)
        {
            Helpers.sendInRadius(e.getPlayer().getPosition(), new TextComponentString("§6 �? §e" + RolePlayNames.getName(e.getPlayer()) + " a lancé un dé... Résultat: " + (Helpers.RANDOM.nextInt(6) + 1)), 6);
        }
        PlayerSpy.ITEMS_LOG.log("**Item Toss:** " + e.getEntityItem().getItem().getDisplayName() + "\n**Position**: " + Helpers.blockPosToString(e.getEntityItem().getPosition()));

        e.getEntityItem().setNoDespawn();
    }

    @SubscribeEvent
    public void onItemPickup(PlayerEvent.ItemPickupEvent e)
    {
        PlayerSpy.ITEMS_LOG.log("**Item Toss:** " + e.getStack().getDisplayName() + "\n**Position**: " + Helpers.blockPosToString(e.player.getPosition()));
    }

    /**
     * Prevent all items used for blacklisted inventory slots to spawn in world after the player-death.
     */
    @SubscribeEvent
    public void killAllBlackListedItemsInWorld(LivingDeathEvent e)
    {
        if(e.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)e.getEntity();
            for(int i = 0; i<36; i++)
            {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if(stack.getDisplayName().equalsIgnoreCase(DISABLED_SLOT_NAME))
                {
                    player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }
            }
        }
    }

    public static boolean canTouch(EntityPlayer player, Slot theSlot)
    {
        return !(theSlot != null && (theSlot.getStack().getDisplayName().equalsIgnoreCase(InventoryHandler.DISABLED_SLOT_NAME) || player.getCooldownTracker().hasCooldown(theSlot.getStack().getItem())));
    }
}