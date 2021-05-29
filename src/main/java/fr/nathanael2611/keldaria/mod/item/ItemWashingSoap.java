package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.features.cleanliness.Soap;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWashingSoap extends Item
{


    public static final Soap NEUTRAL = Soap.create("Savon Neutre", "Aucune odeur particulière ne semble s'échaper de ce savon.", 15, "#ffffff");

    public ItemWashingSoap()
    {
        setMaxDamage(10);
        setMaxStackSize(1);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 40;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        return getSoap(stack).getSoapName();
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if(!worldIn.isRemote)

        {
            if(entityLiving instanceof EntityPlayer)
            {
                if(entityLiving.isInWater())
                {
                    Soap soap = getSoap(stack);
                    soap.applyEffects((EntityPlayer) entityLiving);
                    stack.damageItem(1, entityLiving);
                }
                else
                {
                    Helpers.sendPopMessage((EntityPlayerMP) entityLiving, "§cVous devez être dans l'eau pour vous laver.", 1500);
                }
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    public static Soap getSoap(ItemStack stack)
    {
        if(stack.getItem() == KeldariaItems.NEUTRAL_SOAP)
        {
            return NEUTRAL;
        }
        Soap soap = Soap.create(getCompoundTag(stack).getCompoundTag("Soap"));
        return soap;

    }

    public static void setSoap(ItemStack stack, Soap soap)
    {
        getCompoundTag(stack).setTag("Soap", soap.serializeNBT());
    }

    public static NBTTagCompound getCompoundTag(ItemStack stack)
    {
        if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
        return stack.getTagCompound();
    }

}
