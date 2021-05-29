package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.container.ContainerKeyring;
import fr.nathanael2611.keldaria.mod.inventory.InventoryKeyRing;
import fr.nathanael2611.keldaria.mod.network.KeldariaGuiHandler;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemKeyring extends ItemContainer
{

    public ItemKeyring()
    {
        super(ContainerKeyring.class);
        this.setCreativeTab(KeldariaTabs.KELDARIA);
        setMaxStackSize(1);
        this.addPropertyOverride(new ResourceLocation("havekeys"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                InventoryKeyRing inventoryKeyRing = new InventoryKeyRing(stack);
                int keys = 0;
                for (int i = 0; i < inventoryKeyRing.getSizeInventory(); i++)
                {
                    if (inventoryKeyRing.getStackInSlot(i).getItem() != Items.AIR) keys++;
                }
                return keys > 0 ? 1 : 0.0F;
            }
        });
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (playerIn.isSneaking())
        {
            playerIn.openGui(Keldaria.getInstance(), KeldariaGuiHandler.ID_KEYRING_INV, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
