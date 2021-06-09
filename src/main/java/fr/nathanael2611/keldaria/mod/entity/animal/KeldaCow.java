package fr.nathanael2611.keldaria.mod.entity.animal;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.entity.ai.EntityAIReproduction;
import fr.nathanael2611.keldaria.mod.entity.ai.KeldAITempt;
import fr.nathanael2611.keldaria.mod.features.AnimalGender;
import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class KeldaCow extends EntityKeldAnimal
{

    private long lastMilkProvide = -1;

    public KeldaCow(World worldIn)
    {
        super(worldIn);
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 2.0D));
        this.tasks.addTask(2, new EntityAIReproduction(this, 1.0D));
        this.tasks.addTask(3, new KeldAITempt(this, 1.2D, false));
        //this.tasks.addTask(4, new EntityAIFollowParent(this, 1.25D));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (this.getGender() == AnimalGender.FEMALE && (lastMilkProvide == -1 || System.currentTimeMillis() - lastMilkProvide > TimeUnit.MINUTES.toMillis(30)) && itemstack.getItem() == Items.BUCKET && !player.capabilities.isCreativeMode && !this.isChild())
        {
            this.lastMilkProvide = System.currentTimeMillis();
            player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
            itemstack.shrink(1);

            if (itemstack.isEmpty())
            {
                player.setHeldItem(hand, new ItemStack(Items.MILK_BUCKET));
            }
            else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET)))
            {
                player.dropItem(new ItemStack(Items.MILK_BUCKET), false);
            }

            return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_COW_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_COW_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume()
    {
        return 0.4F;
    }


    @Override
    public float getBaseWidth()
    {
        return this.isAdult() ? 0.9F : 0.5F;
    }

    @Override
    public float getBaseHeight()
    {
        return this.isAdult() ? 1.4F : 0.9F;
    }

    @Override
    public double getBaseSpeed()
    {
        return 0.20000000298023224D;
    }

    @Override
    public double getBaseHealth()
    {
        return 10.0D;
    }

    @Override
    public long getGrowTime()
    {
        return KeldariaDate.Unit.MONTH.toMillis(1);
    }

    @Override
    public List<FoodEntry> getBelovedFood()
    {
        return Lists.newArrayList(new FoodEntry(Items.WHEAT, 50));
    }

    @Override
    public long getReFertilizationTime()
    {
        return KeldariaDate.Unit.MONTH.toMillis(1);
    }

    @Override
    public long getFullTime()
    {
        return KeldariaDate.Unit.DAYS.toMillis(3);
    }

    @Override
    public long getHydratedTime()
    {
        return KeldariaDate.Unit.DAYS.toMillis(3);
    }

    @Override
    public List<ItemStack> getLoot()
    {
        return Lists.newArrayList(new ItemStack(Items.BEEF));
    }

    @Override
    public EntityKeldAnimal initChild(EntityKeldAnimal mate)
    {
        return new KeldaCow(this.world);
    }

    @Override
    public long getConceptionTime()
    {
        return KeldariaDate.Unit.MONTH.toMillis(1);
    }
}
