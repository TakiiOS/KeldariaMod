package fr.nathanael2611.keldaria.mod.features;

import fr.nathanael2611.keldaria.mod.client.ImageCache;
import fr.nathanael2611.keldaria.mod.client.ItemLayer;
import fr.nathanael2611.keldaria.mod.client.NewItemTextureCache;
import fr.nathanael2611.keldaria.mod.client.model.ModelCustomItemTexture;
import fr.nathanael2611.keldaria.mod.client.model.custom.Transforms;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.awt.image.BufferedImage;

public class ItemTextures
{

    public static void setTexture(ItemStack stack, String url)
    {
        NBTTagCompound c = Helpers.getCompoundTag(stack);
        c.setString("ItemTexture", url);
        stack.setTagCompound(c);
    }

    public static String getTexture(ItemStack stack)
    {
        return hasTexture(stack) ? Helpers.getCompoundTag(stack).getString("ItemTexture") : "";
    }

    public static double getSize(ItemStack stack)
    {
        NBTTagCompound c = Helpers.getCompoundTag(stack);
        return c.hasKey("ItemSize") ? c.getDouble("ItemSize") : 1;
    }

    public static boolean hasTexture(ItemStack stack)
    {
        return Helpers.getCompoundTag(stack).hasKey("ItemTexture");
    }

    public static boolean hasTransforms(ItemStack stack)
    {
        return Helpers.getCompoundTag(stack).hasKey("GLTransforms");
    }

    public static boolean hasBuiltInTransforms(ItemStack stack)
    {
        return Helpers.getCompoundTag(stack).hasKey("BuiltInTransform");
    }

    public static boolean hasLayer(ItemStack stack)
    {
        return Helpers.getCompoundTag(stack).hasKey("ItemLayer");
    }

    public static String getTransforms(ItemStack stack)
    {
        return hasTransforms(stack) ? Helpers.getCompoundTag(stack).getString("GLTransforms") : "";
    }

    public static String getBuiltInTransforms(ItemStack stack)
    {
        return hasBuiltInTransforms(stack) ? Helpers.getCompoundTag(stack).getString("BuiltInTransform") : "sword";
    }

    public static String getLayer(ItemStack stack)
    {
        return hasLayer(stack) ? Helpers.getCompoundTag(stack).getString("ItemLayer") : "";
    }

    private static ModelCustomItemTexture emptyModel = new ModelCustomItemTexture();
    private static Transforms emptyTransforms = new Transforms();
    private static ItemLayer emptyLayer = new ItemLayer(null, emptyTransforms);

    public static void renderItem(EntityLivingBase holder, ItemStack stack, ItemCameraTransforms.TransformType transform)
    {
        GlStateManager.pushMatrix();
        BufferedImage img = ImageCache.get(getTexture(stack), null);
        if (img != null)
        {
            double scale = ItemTextures.getSize(stack);
            GlStateManager.scale(scale, scale, scale);
            if (transform == ItemCameraTransforms.TransformType.FIXED)
            {
                GlStateManager.rotate(-12, 0, 0, 1);
                GlStateManager.translate(0, 0.6, 0);
                GlStateManager.translate(0.55, 0, 0);
            } else if (transform == ItemCameraTransforms.TransformType.GROUND)
            {
                GlStateManager.translate(0, -1, 0);
                GlStateManager.translate(0.8, 0, 0);
                GlStateManager.translate(0, 0, -.1);
                GlStateManager.rotate(-90, 0, 0, 1);
            } else
            {
                GlStateManager.rotate(45, 1, 0, 0);
                GlStateManager.translate(0, 0.95, 0);
                GlStateManager.translate(0, 0, 0.32);
                GlStateManager.rotate(-90, 0, 1, 0);
            }
            try
            {

                Transforms.builtInTransform(getBuiltInTransforms(stack)).apply(stack, holder);
                GlStateManager.pushMatrix();
                final double factorX = (double) img.getWidth() / 64;
                final double factorY = (double) img.getHeight() / 64;
                GlStateManager.scale(1 / factorX, 1 / factorY, 1 / factorX);

                NewItemTextureCache.getTransforms(stack, emptyTransforms).apply();
                NewItemTextureCache.getModel(stack, emptyModel).render();
                if(hasLayer(stack))
                {
                    if(!holder.getHeldItemOffhand().equals(stack))
                    {
                        GlStateManager.translate(0, 0, 0.001);
                    }
                    else
                    {
                        GlStateManager.translate(0, 0, -0.001);
                    }
                    NewItemTextureCache.getLayer(stack, emptyLayer).apply();
                }
                GlStateManager.popMatrix();
            } catch (Exception ex)
            {
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }


}
