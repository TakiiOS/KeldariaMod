/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.features.lockpick.LocksHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLock extends Item {

    public ItemLock()
    {
        setMaxStackSize(1);
    }

    @Override
    public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        super.readNBTShareTag(stack, nbt);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        final NBTTagCompound tag = stack.getTagCompound();

            if(tag != null && LocksHelper.hasKeyId(stack))
            {
                tooltip.add("KeyTag: " + LocksHelper.getKeyId(stack));
            }
            else
            {
                tooltip.add("Cadenas vierge");
                tooltip.add("Clique-droit pour définir un keytag aléatoire");
            }

    }



    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if(!LocksHelper.hasKeyId(playerIn.getHeldItem(handIn)))
        {
            LocksHelper.giveRandomKeyCode(playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        return LocksHelper.hasKeyId(stack) ? super.getItemStackDisplayName(stack) : "Cadenas Vierge";
    }
}
