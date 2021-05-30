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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemKey extends Item
{

    public static final String TAG_KEY_ID = "keyid";

    public ItemKey() {
        this.setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        final NBTTagCompound tag = stack.getTagCompound();
        if(tag != null)
        {
            if(LocksHelper.hasKeyId(stack))
            {
                tooltip.add("KeyTag: " + LocksHelper.getKeyId(stack));
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        return LocksHelper.hasKeyId(stack) ? super.getItemStackDisplayName(stack) : "Clef Vierge";
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        return true;
    }
}
