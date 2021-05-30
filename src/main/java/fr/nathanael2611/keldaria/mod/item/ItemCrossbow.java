/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemCrossbow extends ItemBow
{

    public ItemCrossbow()
    {
        setCreativeTab(KeldariaTabs.KELDARIA);
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                if (entityIn == null)
                {
                    return 0.0F;
                }
                else
                {

                    return isCharged(stack) ? 100 : !(entityIn.getActiveItemStack().getItem() instanceof ItemBow) ? 0.0F : (float)(stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F;
                }
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return isCharged(stack) ? 1 : entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return isCharged(stack ) ? EnumAction.BOW : EnumAction.NONE;
    }

    public static boolean isCharged(ItemStack stack)
    {
        NBTTagCompound compound = Helpers.getCompoundTag(stack);
        return compound.hasKey("ChargedArrow", Constants.NBT.TAG_COMPOUND);
    }

    public static void charge(ItemStack stack, ItemStack arrow)
    {
        NBTTagCompound compound = Helpers.getCompoundTag(stack);
        compound.setTag("ChargedArrow", arrow.serializeNBT());
        compound.setLong("ChargedTime", System.currentTimeMillis());
        stack.setTagCompound(compound);
    }

    public static ItemStack getChargedArrow(ItemStack stack)
    {
        NBTTagCompound compound = Helpers.getCompoundTag(stack);
        if (isCharged(stack))
        {
            return new ItemStack(compound.getCompoundTag("ChargedArrow"));
        } else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (isCharged(stack))
        {

            EntityPlayer entityplayer = (EntityPlayer) playerIn;
            boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = getChargedArrow(stack);

            int i = this.getMaxItemUseDuration(stack);
            net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, !itemstack.isEmpty() || flag);

            if (!itemstack.isEmpty() || flag)
            {
                if (itemstack.isEmpty())
                {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getArrowVelocity(i) ;

                if ((double) f >= 0.1D)
                {
                    boolean flag1 = entityplayer.capabilities.isCreativeMode || (itemstack.getItem() instanceof ItemArrow && ((ItemArrow) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer));

                    if (!worldIn.isRemote)
                    {
                        ItemArrow itemarrow = (ItemArrow) (itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Items.ARROW);
                        EntityArrow entityarrow = itemarrow.createArrow(worldIn, itemstack, entityplayer);
                        entityarrow = this.customizeArrow(entityarrow);
                        entityarrow.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.5F, 1.0F);

                        if (f == 1.0F)
                        {
                            entityarrow.setIsCritical(true);
                        }

                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                        if (j > 0)
                        {
                            entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

                        if (k > 0)
                        {
                            entityarrow.setKnockbackStrength(k);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
                        {
                            entityarrow.setFire(100);
                        }

                        stack.damageItem(1, entityplayer);

                        if (flag1 || entityplayer.capabilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW))
                        {
                            entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }

                        entityarrow.setDamage(entityarrow.getDamage() * 1.6);

                        worldIn.spawnEntity(entityarrow);
                    }

                    worldIn.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);


                    entityplayer.addStat(StatList.getObjectUseStats(this));
                }

            }
            NBTTagCompound tag = (Helpers.getCompoundTag(stack));
            tag.removeTag("ChargedArrow");
            stack.setTagCompound(tag);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);

    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return super.getMaxItemUseDuration(stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            if(isCharged(stack))
                return;
            int i = getMaxItemUseDuration(stack) - timeLeft;
            if(i > 20)
            {
                ItemStack arrow = this.findAmmo((EntityPlayer) entityLiving);
                charge(stack, arrow);
                arrow.shrink(1);
            }
        }
        //super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
    }
}
