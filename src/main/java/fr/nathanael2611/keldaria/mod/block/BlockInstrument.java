/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.features.IInstrument;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockInstrument extends Block implements IInstrument
{

    private String name;
    private SoundEvent[] sounds;
    private SoundEvent[] sneakSounds;

    public BlockInstrument(String name, SoundEvent[] sounds, SoundEvent[] sneakSounds)
    {
        super(Material.WOOD);
        this.name = name;
        this.sounds = sounds;
        this.sneakSounds = sneakSounds;
        this.setCreativeTab(KeldariaTabs.KELDARIA);
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public SoundEvent[] getSounds()
    {
        return this.sounds;
    }

    @Override
    public SoundEvent[] getSneakSounds()
    {
        return this.sneakSounds;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        SoundEvent[] possibleSounds = playerIn.isSneaking() ? this.sneakSounds : this.sounds;
        playerIn.playSound(possibleSounds[Helpers.RANDOM.nextInt(possibleSounds.length)], 0.4f, 1);
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
}
