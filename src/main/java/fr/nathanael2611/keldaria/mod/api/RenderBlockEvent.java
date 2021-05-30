/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderBlockEvent extends Event
{

    private BlockRendererDispatcher blockRendererDispatcher;
    private BlockPos pos;
    private IBlockAccess access;
    private IBlockState state;
    private BufferBuilder bufferBuilder;
    private BlockModelRenderer blockModelRenderer;
    private BlockFluidRenderer blockFluidRenderer;

    public RenderBlockEvent(BlockRendererDispatcher blockRendererDispatcher, BlockPos pos, IBlockAccess access, IBlockState state, BufferBuilder bufferBuilder, BlockModelRenderer blockModelRenderer, BlockFluidRenderer blockFluidRenderer)
    {
        this.blockRendererDispatcher = blockRendererDispatcher;
        this.pos = pos;
        this.access = access;
        this.state = state;
        this.bufferBuilder = bufferBuilder;
        this.blockModelRenderer = blockModelRenderer;
        this.blockFluidRenderer = blockFluidRenderer;
    }

    public BlockRendererDispatcher getBlockRendererDispatcher()
    {
        return blockRendererDispatcher;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public IBlockAccess getAccess()
    {
        return access;
    }

    public IBlockState getState()
    {
        return state;
    }

    public BufferBuilder getBufferBuilder()
    {
        return bufferBuilder;
    }

    public BlockFluidRenderer getBlockFluidRenderer()
    {
        return blockFluidRenderer;
    }

    public BlockModelRenderer getBlockModelRenderer()
    {
        return blockModelRenderer;
    }

    public void setState(IBlockState state)
    {
        this.state = state;
    }

    @Override
    public boolean isCancelable()
    {
        return true;
    }
}
