package fr.nathanael2611.keldaria.mod.client.render.tesr;

import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFloweringPot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityFloweringPotRenderer extends TileEntitySpecialRenderer<TileEntityFloweringPot>
{

    @Override
    public void render(TileEntityFloweringPot te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        if(te.isActive())
        {

            IBlockState iblockstate = te.isGrowed() ? te.getFlowerSeed().getBlock().getDefaultState() : Blocks.TALLGRASS.getStateFromMeta(2);
            World world = getWorld();

            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            GlStateManager.pushMatrix();

            GlStateManager.disableLighting();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
            BlockPos blockpos = te.getPos();
            double growTime = te.getFlowerSeed().getGrowTime();
            long diff = (System.currentTimeMillis() - te.getFlowerPlantedTime());
            double percent = Math.min((diff / growTime) * 100, 100);
            GlStateManager.translate(
                    x - blockpos.getX(), y -blockpos.getY() + 0.3 - ((100 - percent) / 170), z - blockpos.getZ()
            );
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            blockrendererdispatcher.getBlockModelRenderer().renderModel(
                    world, blockrendererdispatcher.getModelForState(iblockstate),
                    iblockstate, blockpos, bufferbuilder, true
            );
            tessellator.draw();

            GlStateManager.enableLighting();

            GlStateManager.popMatrix();
        }

    }
}
