package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.container.ContainerPurse;
import fr.nathanael2611.keldaria.mod.network.KeldariaGuiHandler;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemPurse extends ItemContainer
{

    public ItemPurse()
    {
        super(ContainerPurse.class);
        this.setCreativeTab(KeldariaTabs.KELDARIA);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.openGui(Keldaria.getInstance(), KeldariaGuiHandler.ID_PURSE_INV, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
