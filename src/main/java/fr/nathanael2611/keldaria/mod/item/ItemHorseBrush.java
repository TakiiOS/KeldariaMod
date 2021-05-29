package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemHorseBrush extends Item
{

    public ItemHorseBrush()
    {
        this.setMaxDamage(200);
        this.setMaxStackSize(1);
        this.setCreativeTab(KeldariaTabs.KELDARIA);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add("§6Utilisez cette brosse sur un cheval pour le laver!");
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
