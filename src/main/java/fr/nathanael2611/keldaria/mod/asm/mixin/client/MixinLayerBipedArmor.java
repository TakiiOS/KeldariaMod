package fr.nathanael2611.keldaria.mod.asm.mixin.client;

import fr.nathanael2611.keldaria.mod.asm.MixinClientHooks;
import fr.nathanael2611.keldaria.mod.item.ItemClothe;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.client.renderer.entity.layers.LayerArmorBase.renderEnchantedGlint;

@Mixin(LayerArmorBase.class)
public abstract class MixinLayerBipedArmor<T extends ModelBase> implements LayerRenderer
{

    @Shadow @Final private RenderLivingBase<?> renderer;

    @Shadow protected abstract void setModelSlotVisible(T p_188359_1_, EntityEquipmentSlot slotIn);

    @Shadow public abstract T getModelFromSlot(EntityEquipmentSlot slotIn);

    @Shadow protected abstract T getArmorModelHook(EntityLivingBase entity, ItemStack itemStack, EntityEquipmentSlot slot, T model);

    @Shadow protected abstract boolean isLegSlot(EntityEquipmentSlot slotIn);

    @Shadow public abstract ResourceLocation getArmorResource(Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type);

    @Shadow private float colorR;

    @Shadow private float colorG;

    @Shadow private float colorB;

    @Shadow private float alpha;

    @Shadow private boolean skipRenderGlint;

    @Overwrite
    private void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn)
    {
        ItemStack itemstack = entityLivingBaseIn.getItemStackFromSlot(slotIn);


        // START

        if(entityLivingBaseIn instanceof EntityPlayer && ItemClothe.isClothValid(itemstack))
        {

            if(MixinClientHooks.doesCancelArmorRender(itemstack, (EntityPlayer) entityLivingBaseIn, slotIn)) return;

        }

        // END

        if (itemstack.getItem() instanceof ItemArmor)
        {
            ItemArmor itemarmor = (ItemArmor)itemstack.getItem();

            if (itemarmor.getEquipmentSlot() == slotIn)
            {
                T t = this.getModelFromSlot(slotIn);
                t = getArmorModelHook(entityLivingBaseIn, itemstack, slotIn, t);
                t.setModelAttributes(this.renderer.getMainModel());
                t.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
                this.setModelSlotVisible(t, slotIn);
                boolean flag = this.isLegSlot(slotIn);
                this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, null));

                {
                    if (itemarmor.hasOverlay(itemstack)) // Allow this for anything, not only cloth
                    {
                        int i = itemarmor.getColor(itemstack);
                        float f = (float)(i >> 16 & 255) / 255.0F;
                        float f1 = (float)(i >> 8 & 255) / 255.0F;
                        float f2 = (float)(i & 255) / 255.0F;
                        GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
                        t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                        this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, "overlay"));
                    }
                    { // Non-colored
                        GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                        t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    } // Default
                    if (!this.skipRenderGlint && itemstack.hasEffect())
                    {
                        renderEnchantedGlint(this.renderer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                    }
                }
            }
        }
    }

}
