/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.features.Wine;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemWineGlass extends Item
{

    public ItemWineGlass()
    {
        setMaxStackSize(1);
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 32;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
    {
        if (!world.isRemote && entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;
            Wine wine = Wine.getWine(stack);
            stack.shrink(1);
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, ((100 - wine.getQuality()) * 10), 0, false, false));
            player.sendMessage(new TextComponentString("§4 * §7Le vin que vous venez de boire est d'une qualité " + wine.getQualityString()));
        }
        return new ItemStack(KeldariaItems.EMPTY_WINE_GLASS);
    }

}
