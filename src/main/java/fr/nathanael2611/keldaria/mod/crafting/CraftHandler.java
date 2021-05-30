/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.crafting;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.crafting.storage.KnownRecipesProvider;
import fr.nathanael2611.keldaria.mod.features.skill.EnumJob;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CraftHandler
{


    @SubscribeEvent
    public void onClickBlock(PlayerInteractEvent.RightClickBlock e)
    {
        IBlockState state = e.getWorld().getBlockState(e.getPos());

        if (e.getHand() == EnumHand.MAIN_HAND)
        {
            if (!e.getWorld().isRemote)
            {
                Block clickedBlock = state.getBlock();
                if (clickedBlock == Blocks.BREWING_STAND)
                {
                    e.setCanceled(true);
                    if (EnumJob.APOTHECARY.has(e.getEntityPlayer()))
                    {
                        if (!e.getWorld().isRemote)
                        {
                            CraftManager.openManager("apothecary", (EntityPlayerMP) e.getEntityPlayer());
                        }
                    } else
                    {
                        Helpers.sendPopMessage((EntityPlayerMP) e.getEntityPlayer(), "§c �? Vous devez avoir la compétence §4" + EnumJob.APOTHECARY.getFormattedName() + "§c pour faire cela!", 1000);

                    }
                } else if (clickedBlock == Blocks.ANVIL)
                {
                    boolean canceled = true;
                    if (EnumJob.BLACKSMITH.has(e.getEntityPlayer()))
                    {
                        if (e.getEntityPlayer().getHeldItemMainhand().getItem() == KeldariaItems.IRON_FORGE_HAMMER)
                        {
                            canceled = false;
                        } else
                        {
                            Helpers.sendPopMessage((EntityPlayerMP) e.getEntityPlayer(), "§cVeuillez utiliser l'enclume avec un marteau de forgeron!", 1000);
                        }
                    } else
                    {
                        Helpers.sendPopMessage((EntityPlayerMP) e.getEntityPlayer(), "§cVous devez avoir la compétence §4" + EnumJob.BLACKSMITH.getName() + "§c pour faire cela.", 1200);
                    }
                    e.setCanceled(canceled);


                } else if (clickedBlock == Blocks.CRAFTING_TABLE)
                {
                    if (!e.getEntityPlayer().isCreative())
                    {
                        Helpers.sendPopMessage((EntityPlayerMP) e.getEntityPlayer(), "§cTable de craft désactivée! Utilisez les tables spéciales de votre métier!", 3000);
                        e.setCanceled(true);
                    }
                }
            }
        }
    }
    public static final ResourceLocation KNOWN_RECIPES_LOCATION = new ResourceLocation(Keldaria.MOD_ID, "known_recipes");

    @SubscribeEvent
    public void onCapAttack(AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof EntityPlayer)
        {
            event.addCapability(KNOWN_RECIPES_LOCATION, new KnownRecipesProvider());
        }
    }

    @SubscribeEvent
    public void onItemAttach(AttachCapabilitiesEvent<ItemStack> event)
    {
        //if(event.getObject() instanceof EntityPlayer)
        {
            event.addCapability(KNOWN_RECIPES_LOCATION, new KnownRecipesProvider());
        }
    }

}
