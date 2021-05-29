package fr.nathanael2611.keldaria.mod.asm.mixin.client;

import fr.nathanael2611.keldaria.mod.asm.MixinClientHooks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockRendererDispatcher.class)
public abstract class MixinBlockRendererDispatcher
{

    @Shadow @Final private BlockFluidRenderer fluidRenderer;

    @Shadow @Final private BlockModelRenderer blockModelRenderer;





    /**
     * @author
     */
    @Overwrite
    public boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, BufferBuilder bufferBuilderIn)
    {
        return MixinClientHooks.renderBlock(
                (BlockRendererDispatcher) (Object) this,
                this.blockModelRenderer, this.fluidRenderer,
                state, pos, blockAccess, bufferBuilderIn, true
        );
    }


}
