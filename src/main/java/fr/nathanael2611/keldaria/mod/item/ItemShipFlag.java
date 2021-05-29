package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemShipFlag extends Item
{

    public ItemShipFlag()
    {
        setMaxStackSize(1);
        setCreativeTab(KeldariaTabs.KELDARIA);
    }

    public static void setFlagUrl(ItemStack stack, String flagUrl)
    {
        getCompoundTag(stack).setString("FlagUrl", flagUrl);
    }

    public static boolean isEmpty(ItemStack stack)
    {
        return !getCompoundTag(stack).hasKey("FlagUrl");
    }

    public static String getFlagUrl(ItemStack stack)
    {
        if(!isEmpty(stack))
        {
            return getCompoundTag(stack).getString("FlagUrl");
        }
        return null;
    }

    public static NBTTagCompound getCompoundTag(ItemStack stack)
    {
        if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
        return stack.getTagCompound();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if(isEmpty(stack))
        {
            return "Drapeau pour bateau vierge";
        }
        else
        {
            return "Drapeau pour bateau";
        }
    }
}
