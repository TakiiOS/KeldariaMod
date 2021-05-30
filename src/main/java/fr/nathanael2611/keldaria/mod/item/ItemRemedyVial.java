/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.features.Remedy;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemRemedyVial extends Item
{
    public ItemRemedyVial()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(KeldariaTabs.APOTHECARY);
    }


    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        EntityPlayer entityplayer = entityLiving instanceof EntityPlayer ? (EntityPlayer)entityLiving : null;

        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode)
        {
            stack.shrink(1);
        }

        if (entityplayer instanceof EntityPlayerMP)
        {
            CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
        }

        if (!worldIn.isRemote)
        {
            if(haveRemedy(stack))
            {
                Remedy remedy = getRemedy(stack);
                for (PotionEffect potioneffect : remedy.getPotionEffects())
                {
                    if (potioneffect.getPotion().isInstant())
                    {
                        potioneffect.getPotion().affectEntity(entityplayer, entityplayer, entityLiving, potioneffect.getAmplifier(), 1.0D);
                    }
                    else
                    {
                        entityLiving.addPotionEffect(new PotionEffect(potioneffect));
                    }
                }
                entityLiving.sendMessage(new TextComponentString(remedy.getMessage()));
            }
        }

        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode)
        {
            if (stack.isEmpty())
            {
                return new ItemStack(KeldariaItems.EMPTY_REMEDY_VIAL);
            }

            if (entityplayer != null)
            {
                entityplayer.inventory.addItemStackToInventory(new ItemStack(KeldariaItems.EMPTY_REMEDY_VIAL));
            }
        }

        return stack;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 50;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        return haveRemedy(stack) ? getRemedy(stack).getName() : "Remède Invalide";
    }


    public static Remedy getRemedy(ItemStack stack)
    {
        if(haveRemedy(stack))
        {
            Remedy remedy = new Remedy(getCompoundTag(stack).getCompoundTag("Remedy"));
            return remedy;
        }
        return new Remedy("Invalid", Lists.newArrayList(), "Invalid", "#000000");
    }
    public static Remedy getRemedyWithoutCheck(ItemStack stack)
    {

            Remedy remedy = new Remedy(getCompoundTag(stack).getCompoundTag("Remedy"));
            return remedy;

    }

    public static boolean haveRemedy(ItemStack stack)
    {
        return getCompoundTag(stack).copy().hasKey("Remedy") && getRemedyWithoutCheck(stack).isAllValid();
    }

    public static void setRemedy(ItemStack stack, Remedy remedy)
    {
        getCompoundTag(stack).setTag("Remedy", remedy.serializeNBT());
    }

    public static NBTTagCompound getCompoundTag(ItemStack stack)
    {
        if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
        return stack.getTagCompound();
    }


}