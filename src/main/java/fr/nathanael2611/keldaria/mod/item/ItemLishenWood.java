package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLishenWood extends Item
{

    public ItemLishenWood()
    {
        this.setCreativeTab(KeldariaTabs.KELDARIA);

    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add("§6 * Du bois provenant d'un Lishen, il est d'une qualité visiblement remarquable.");
        tooltip.add("§6 * Il semble également très résistant.");
    }
}
