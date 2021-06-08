package fr.nathanael2611.keldaria.mod.entity.animal;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.client.render.entity.animal.FoodEntry;
import fr.nathanael2611.keldaria.mod.entity.ai.EntityAIReproduction;
import fr.nathanael2611.keldaria.mod.entity.ai.KeldAITempt;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class EntityPig extends EntityKeldAnimal
{

    private static final List<FoodEntry> TEMPTATION_ITEMS = Lists.newArrayList(new FoodEntry(Items.CARROT, 30), new FoodEntry(Items.POTATO, 20), new FoodEntry(Items.BEETROOT, 40));

    public EntityPig(World worldIn)
    {
        super(worldIn);
    }

    @Override
    public float getBaseWidth()
    {
        return isAdult() ? 0.9F : 0.7F;
    }

    @Override
    public float getBaseHeight()
    {
        return isAdult() ? 0.9F : 0.7F;
    }

    @Override
    public double getBaseSpeed()
    {
        return 0.25;
    }

    @Override
    public double getBaseHealth()
    {
        return isAdult() ? 20 : 10;
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(3, new EntityAIReproduction(this, 1.0D));
        this.tasks.addTask(4, new KeldAITempt(this, 1.2D, false));
        //this.tasks.addTask(5, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    public long getGrowTime()
    {
        return TimeUnit.DAYS.toMillis(7);
    }

    @Override
    public List<FoodEntry> getBelovedFood()
    {
        return TEMPTATION_ITEMS;
    }

    @Override
    public long getReFertilizationTime()
    {
        return 10000;
    }

    @Override
    public long getFullTime()
    {
        return TimeUnit.DAYS.toMillis(2);
    }

    @Override
    public long getHydratedTime()
    {
        return TimeUnit.DAYS.toMillis(2);
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_PIG_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_PIG_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_PIG_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
    }

    @Override
    public List<ItemStack> getLoot()
    {
        return Lists.newArrayList(new ItemStack(Items.PORKCHOP));
    }

    @Override
    public EntityKeldAnimal initChild(EntityKeldAnimal mate)
    {
        return new EntityPig(this.world);
    }

    @Override
    public long getConceptionTime()
    {
        return 10000;
    }

}