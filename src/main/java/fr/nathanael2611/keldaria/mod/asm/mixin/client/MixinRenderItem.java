package fr.nathanael2611.keldaria.mod.asm.mixin.client;

import fr.nathanael2611.keldaria.mod.features.ItemTextures;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(RenderItem.class)
public abstract class MixinRenderItem implements IResourceManagerReloadListener
{

    @Shadow
    public abstract IBakedModel getItemModelWithOverrides(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entitylivingbaseIn);

    @Shadow
    protected abstract void renderItemModel(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform, boolean leftHanded);

    /**
     * @author Mojang yee
     */
    @Overwrite
    public void renderItem(ItemStack stack, EntityLivingBase entitylivingbaseIn, ItemCameraTransforms.TransformType transform, boolean leftHanded)
    {
        if (!stack.isEmpty() && entitylivingbaseIn != null)
        {
            if (ItemTextures.hasTexture(stack))
            {
                ItemTextures.renderItem(entitylivingbaseIn, stack, transform);
            } else
            {
                IBakedModel ibakedmodel = this.getItemModelWithOverrides(stack, entitylivingbaseIn.world, entitylivingbaseIn);
                this.renderItemModel(stack, ibakedmodel, transform, leftHanded);
            }
        }
    }

}
