/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import fr.nathanael2611.keldaria.mod.features.Wine;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.features.skill.EnumJob;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWineBottle extends Item
{

    public ItemWineBottle()
    {
        this.setCreativeTab(KeldariaTabs.KELDARIA);
        this.setMaxStackSize(1);
        this.setMaxDamage(6);

    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(entityIn instanceof EntityPlayer)
        {
            if(!Wine.getWine(stack).isValid())
            {
                int quality = 10 + (Helpers.RANDOM.nextInt(1 + EnumAptitudes.CRAFTING.getPoints((EntityPlayer) entityIn) * 10));
                if(EnumJob.COOK.has((EntityPlayer)entityIn))
                {
                    quality = 0;
                }
                Wine.setWine(stack, new Wine(quality, KeldariaDate.getKyrgonDate().getTotalDaysInRP()));
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if(!worldIn.isRemote)
        {
            for (ItemStack stack : playerIn.inventory.mainInventory)
            {
                if (stack.getItem() == KeldariaItems.EMPTY_WINE_GLASS)
                {
                    stack.shrink(1);
                    ItemStack wineGlass = new ItemStack(KeldariaItems.WINE_GLASS);
                    Wine wine = Wine.getWine(playerIn.getHeldItem(handIn));
                    Wine.setWine(wineGlass, wine);
                    playerIn.addItemStackToInventory(wineGlass);
                    playerIn.getHeldItem(handIn).damageItem(1, playerIn);

                    if(playerIn.getHeldItem(handIn).getCount() == 0)
                    {
                        playerIn.addItemStackToInventory(new ItemStack(KeldariaItems.EMPTY_WINE_BOTTLE));
                    }

                    return super.onItemRightClick(worldIn, playerIn, handIn);
                }
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
