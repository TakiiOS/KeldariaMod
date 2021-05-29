package fr.nathanael2611.keldaria.mod.client.model;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.features.EnumFlower;
import fr.nathanael2611.keldaria.mod.registry.KeldariaBlocks;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.registry.KeldariaRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class register all models of all mod items
 *
 * @author Nathanel2611
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = {Side.CLIENT}, modid = Keldaria.MOD_ID)
public class KeldariaItemModels
{

    /**
     * The models registry event
     */
    @SubscribeEvent
    public static void register(ModelRegistryEvent e)
    {

        for (KeldariaRegistry.RegistryEntry<Item> entry : Keldaria.getInstance().getRegistry().getRegistriesFor(Item.class))
        {
            if(entry.isAutoModel())
            {
                registerItemModel(entry.getObject());
            }
        }
        ModelLoader.setCustomModelResourceLocation(KeldariaItems.KEYRING, 0, new ModelResourceLocation(KeldariaItems.KEYRING.getRegistryName() + "_" + "havekeys", "inventory"));


        for (KeldariaRegistry.RegistryEntry<Block> entry : Keldaria.getInstance().getRegistry().getRegistriesFor(Block.class))
        {
            if(entry.isAutoModel())
            {
                registerItemModel(Item.getItemFromBlock(entry.getObject()));
            }
        }

        for (EnumFlower value : EnumFlower.values())
        {
            registerItemModel(Item.getItemFromBlock(value.getBlock()));
        }

    }


    private static void registerItemModel(Item item)
    {
        if (item.getHasSubtypes())
        {
            NonNullList<ItemStack> items = NonNullList.create();
            item.getSubItems(item.getCreativeTab(), items);
            for (ItemStack stack : items)
                ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(), new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    /**
     * Register an item with an metadata and a variant
     */
    private static void registerItemModel(Item item, int metadata, String variant)
    {
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(item.getRegistryName(), variant));
    }
}
