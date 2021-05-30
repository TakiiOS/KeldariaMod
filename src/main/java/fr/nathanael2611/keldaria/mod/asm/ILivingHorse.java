/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm;

import fr.nathanael2611.keldaria.mod.features.AnimalGender;
import fr.nathanael2611.keldaria.mod.inventory.InventoryHorseStorage;
import fr.nathanael2611.keldaria.mod.proxy.CommonProxy;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFeeder;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public interface ILivingHorse
{

    InventoryHorseStorage getHorseStorage();

    default InventoryHorseStorage getClientHorseStorage()
    {
        AbstractHorse horse = get();
        InventoryHorseStorage inv = new InventoryHorseStorage(horse);
        inv.setInventorySlotContents(0, horse.getDataManager().get(MixinHooks.HORSE_STORAGE_1));
        inv.setInventorySlotContents(1, horse.getDataManager().get(MixinHooks.HORSE_STORAGE_2));
        inv.setInventorySlotContents(2, horse.getDataManager().get(MixinHooks.HORSE_STORAGE_3));
        inv.setInventorySlotContents(3, horse.getDataManager().get(MixinHooks.HORSE_STORAGE_4));

        return inv;
    }

    double getFood();

    double getThirst();

    double getCleaniless();

    AnimalGender getGender();

    void setFood(double food);

    void setThirst(double food);

    void setCleaniless(double cleaniless);

    void setGender(AnimalGender gender);

    default void incrementFood(double value)
    {
        setFood(Math.max(Math.min(getFood() + value, 100), 0));
    }

    default void incrementThirst(double value)
    {
        setThirst(Math.max(Math.min(getThirst() + value, 100), 0));
    }

    default void incrementCleaniless(double value)
    {
        setCleaniless(Math.max(Math.min(getCleaniless() + value, 100), 0));
    }

    default void updateLife()
    {
        Random rand = Helpers.RANDOM;
        AbstractHorse horse = get();
        if(horse.world.isRemote) return;
        /* Cleaniless */
        {
            if (horse.isInWater())
            {
                incrementCleaniless(0.5);
            } else if (horse.world.isRainingAt(horse.getPosition()))
            {
                incrementCleaniless(0.005);
            } else
            {
                double menoser = -0.006;
                if(horse.getPassengers().size()> 0)
                {
                    menoser = -0.05;
                }
                incrementThirst(menoser * (getGender() == AnimalGender.MALE ? 1 : 0.7));
            }
        }

        {
            Block block = horse.world.getBlockState(horse.getPosition().down()).getBlock();
            if(block instanceof BlockGrass && horse.getPassengers().size() == 0 && getFood() < 60)
            {
                if(rand.nextInt(100) > 70)
                {
                    horse.setEatingHaystack(true);
                    incrementFood(5);
                    horse.world.setBlockState(horse.getPosition().down(), Blocks.DIRT.getDefaultState(), 3);

                }
            }
            else
            {
                if(horse.getPassengers().size() > 0)
                {
                    if(horse.isSprinting()) incrementFood(-0.1);
                    else incrementFood(-0.03);
                }
                else incrementFood(-0.004);
            }
        }

        {
            if(horse.isInWater() && horse.getPassengers().size() == 0 && getThirst() < 60)
            {
                if(Helpers.RANDOM.nextInt(100) > 70)
                {
                    horse.setEatingHaystack(true);
                    incrementThirst(5);
                }
            }
            else
            {
                if(horse.getPassengers().size() > 0)
                {
                    if(horse.isSprinting()) incrementThirst(-0.1);
                    else incrementThirst(-0.04);
                }
                incrementThirst(-0.005);
            }
        }

        // Update Attributes
        {
            //Health
            {
                float base = 2;
                float foodAdder = (float) ((getFood() / 10));
                float thirstAdder = (float) ((getThirst() / 10));
                float cleanilessAdder = (float) ((getCleaniless() / 10));
                float genderAdder = getGender() == AnimalGender.MALE ? 5 : 2;

                horse.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(base +
                        foodAdder + thirstAdder + cleanilessAdder + genderAdder);
            }
            //Jump
            {
                float base = 0.1f;
                base += 0.8 * (getFood() / 100);
                base += 0.8 * (getThirst() / 100);
                base += getGender() == AnimalGender.MALE ? 0.1 : 0.2;
                setJumpStrenght(base);
            }
            //Speed
            {
                float base = 0.05f;
                base += 0.025 * (getFood() / 100);
                base += 0.075 * (getThirst() / 100);
                base += 0.015 * (getCleaniless() / 100);
                base += getGender() == AnimalGender.MALE ? 0.05 : 0.1;
                horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(base);
            }
        }



        //PathFinding
        if(horse.getPassengers().size() == 0)
        {
            if(rand.nextInt(100) > 40)
            {
                int fY = horse.getPosition().getY() - 1;
                int fX = horse.getPosition().getX() - 5;
                int fZ = horse.getPosition().getZ() - 5;

                List<TileEntityFeeder> feeders = new ArrayList<>();

                for (int i = fY; i < fY + 2; i ++)
                {
                    for (int j = fX; j < fX + 10; j++)
                    {
                        for (int k = fZ; k < fZ + 10; k++)
                        {
                            BlockPos pos = new BlockPos(j, i, k);
                            //IBlockState state = horse.world.getBlockState(pos);
                            TileEntity te = horse.world.getTileEntity(pos);
                            if(te instanceof TileEntityFeeder)
                            {
                                feeders.add((TileEntityFeeder) te);
                            }
                        }
                    }
                }

                List<TileEntityFeeder> waters = feeders.stream().filter(feeder -> feeder.getContentType() == TileEntityFeeder.ContentType.WATER).collect(Collectors.toList());
                List<TileEntityFeeder> foods = feeders.stream().filter(feeder -> feeder.getContentType() == TileEntityFeeder.ContentType.WHEAT).collect(Collectors.toList());
                waters.sort(Comparator.comparingDouble(o -> o.getPos().getDistance((int) horse.posX, (int) horse.posY, (int) horse.posZ)));
                foods.sort(Comparator.comparingDouble(o -> o.getPos().getDistance((int) horse.posX, (int) horse.posY, (int) horse.posZ)));

                TileEntityFeeder.ContentType priority = rand.nextInt(100) > 50 ? TileEntityFeeder.ContentType.WATER : TileEntityFeeder.ContentType.WHEAT;

                List<TileEntityFeeder> list = priority == TileEntityFeeder.ContentType.WATER ? waters : foods;

                if((priority == TileEntityFeeder.ContentType.WATER && getThirst() < 80) || (priority == TileEntityFeeder.ContentType.WHEAT && getFood() < 80))
                {
                    for (TileEntityFeeder feeder : list)
                    {
                        horse.getNavigator().setPath(new Path(new PathPoint[]{new PathPoint(feeder.getPos().getX(), feeder.getPos().getY() -3, feeder.getPos().getZ())}), 1);
                        CommonProxy.executeAfter(() ->
                        {
                            double distance = horse.getDistance(feeder.getPos().getX(), feeder.getPos().getY(), feeder.getPos().getZ());
                            if(distance <= 3)
                            {
                                horse.setEatingHaystack(true);
                                if(feeder.getContentType() == TileEntityFeeder.ContentType.WATER)
                                {
                                    incrementThirst(20);
                                }
                                else
                                {
                                    incrementFood(20);
                                }
                                feeder.incrementPercent(-5);
                            }
                        }, 3000);
                        break;
                    }
                }



            }
            //horse.getNavigator().setPath(new Path(new PathPoint[]{new PathPoint(10, 10, 10)}), 2);
        }

    }

    AbstractHorse get();

    void setJumpStrenght(double value);

    default boolean eat(EntityPlayer player, ItemStack stack)
    {
        if(true) return false;
        AbstractHorse horse = get();
        boolean flag = false;
        float f = 0.0F;
        int i = 0;
        int j = 0;
        Item item = stack.getItem();

        if (item == Items.WHEAT)
        {
            f = 2.0F;
            i = 20;
            j = 3;
            incrementFood(10);
        }
        else if (item == Items.SUGAR)
        {
            f = 1.0F;
            i = 30;
            j = 3;
        }
        else if (item == Item.getItemFromBlock(Blocks.HAY_BLOCK))
        {
            f = 20.0F;
            i = 180;
        }
        else if (item == Items.CARROT)
        {
            f = 4.0F;
            i = 60;
            j = 5;

            if (horse.isTame() && horse.getGrowingAge() == 0 && !horse.isInLove())
            {
                flag = true;
                horse.setInLove(player);
            }
        }
        else if (item == Items.APPLE)
        {
            f = 10.0F;
            i = 240;
            j = 10;

            if (horse.isTame() && horse.getGrowingAge() == 0 && !horse.isInLove())
            {
                flag = true;
                horse.setInLove(player);
            }
        }

        if (horse.getHealth() < horse.getMaxHealth() && f > 0.0F)
        {
            horse.heal(f);
            flag = true;
        }

        if (horse.isChild() && i > 0)
        {
            //horse.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, horse.posX + (double)(horse.rand.nextFloat() * horse.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, 0.0D, 0.0D, 0.0D);

            if (!horse.world.isRemote)
            {
                horse.addGrowth(i);
            }

            flag = true;
        }

        if (j > 0 && (flag || !horse.isTame()) && horse.getTemper() < horse.getMaxTemper())
        {
            flag = true;

            if (!horse.world.isRemote)
            {
                horse.increaseTemper(j);
            }
        }

        if (flag)
        {

            if (!horse.isSilent())
            {
                horse.world.playSound((EntityPlayer)null, horse.posX, horse.posY, horse.posZ, SoundEvents.ENTITY_HORSE_EAT, horse.getSoundCategory(), 1.0F, 1.0F + 1);
            }
        }

        return flag;
    }

}
