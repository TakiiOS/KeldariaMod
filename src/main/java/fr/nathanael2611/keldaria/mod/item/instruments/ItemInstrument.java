/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item.instruments;

import fr.nathanael2611.keldaria.mod.features.IInstrument;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemInstrument extends Item implements IInstrument
{

    private String name;
    private SoundEvent[] sounds;
    private SoundEvent[] sneakSounds;
    private boolean agitateOnUse;

    public ItemInstrument(String name, SoundEvent[] sounds, SoundEvent[] sneakSounds, boolean agitateOnUse)
    {
        this.name = name;
        this.sounds = sounds;
        this.sneakSounds = sneakSounds;
        this.agitateOnUse = agitateOnUse;
        this.setMaxStackSize(1);
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
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(this.agitateOnUse)
        {
            this.onItemRightClick(worldIn, player, hand);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        SoundEvent[] possibleSounds = playerIn.isSneaking() ? this.sneakSounds : this.sounds;
        playerIn.playSound(possibleSounds[Helpers.RANDOM.nextInt(possibleSounds.length)], 0.4f, 1);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}