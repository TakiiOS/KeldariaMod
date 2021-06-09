package fr.nathanael2611.keldaria.mod.entity.animal;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.entity.ai.EntityAIReproduction;
import fr.nathanael2611.keldaria.mod.entity.ai.KeldAITempt;
import fr.nathanael2611.keldaria.mod.features.AnimalGender;
import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class KeldaChicken extends EntityKeldAnimal
{

    private static final List<FoodEntry> TEMPTATION_ITEMS = Lists.newArrayList(new FoodEntry(Items.WHEAT_SEEDS, 20), new FoodEntry(Items.MELON_SEEDS, 30), new FoodEntry(Items.PUMPKIN_SEEDS, 30), new FoodEntry(Items.BEETROOT_SEEDS, 20));
    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;
    /** The time until the next egg is spawned. */
    private long lastEgg = -1;
    public static final long TIME_BETWEEN_EGS = KeldariaDate.Unit.DAYS.toMillis(1);

    public KeldaChicken(World worldIn)
    {
        super(worldIn);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.4D));
        this.tasks.addTask(2, new EntityAIReproduction(this, 1.0D));
        this.tasks.addTask(3, new KeldAITempt(this, 1.0D, true));
        //this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));

    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        if(this.lastEgg == -1)
        {
            this.lastEgg = System.currentTimeMillis();
        }
        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);

        if (!this.onGround && this.wingRotDelta < 1.0F)
        {
            this.wingRotDelta = 1.0F;
        }

        this.wingRotDelta = (float)((double)this.wingRotDelta * 0.9D);

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }

        this.wingRotation += this.wingRotDelta * 2.0F;

        if (this.getGender() == AnimalGender.FEMALE && !this.world.isRemote && !this.isChild() && System.currentTimeMillis() - this.lastEgg > TIME_BETWEEN_EGS)
        {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(Items.EGG, 1);
            this.lastEgg = System.currentTimeMillis();
            //this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
    }

    public void fall(float distance, float damageMultiplier)
    {
    }


    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("EggLayTime"))
        {
            this.lastEgg = compound.getLong("EggLayTime");
        }
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setLong("EggLayTime", this.lastEgg);
    }

    @Override
    public float getBaseWidth()
    {
        return isAdult() ? 0.4F : 0.3F;
    }

    @Override
    public float getBaseHeight()
    {
        return isAdult() ? 0.7F : 0.6F;
    }

    @Override
    public double getBaseSpeed()
    {
        return 0.25D;
    }

    @Override
    public double getBaseHealth()
    {
        return 4.0D;
    }

    @Override
    public long getGrowTime()
    {
        return KeldariaDate.Unit.DAYS.toMillis(6);
    }

    @Override
    public List<FoodEntry> getBelovedFood()
    {
        return TEMPTATION_ITEMS;
    }

    @Override
    public long getReFertilizationTime()
    {
        return KeldariaDate.Unit.DAYS.toMillis(7);
    }

    @Override
    public long getFullTime()
    {
        return KeldariaDate.Unit.DAYS.toMillis(4);
    }

    @Override
    public long getHydratedTime()
    {
        return KeldariaDate.Unit.DAYS.toMillis(4);
    }

    @Override
    public List<ItemStack> getLoot()
    {
        return Lists.newArrayList(new ItemStack(Items.FEATHER), new ItemStack(Items.CHICKEN));
    }

    @Override
    public EntityKeldAnimal initChild(EntityKeldAnimal mate)
    {
        KeldaEgg egg = new KeldaEgg(this.world);
        egg.containedAnimal = new KeldaChicken(this.world);
        return egg;
    }

    @Override
    public long getConceptionTime()
    {
        return 10000;
        //return KeldariaDate.Unit.DAYS.toMillis(3);
    }

    public static class KeldaEgg extends EntityKeldAnimal
    {

        private EntityKeldAnimal containedAnimal;

        public KeldaEgg(World worldIn)
        {
            super(worldIn);
        }

        @Override
        public void onUpdate()
        {
            if(!world.isRemote)
            {
                if(getTimeLived() > KeldariaDate.Unit.DAYS.toMillis(3))
                {
                    if(this.containedAnimal != null)
                    {
                        EntityKeldAnimal baby = this.containedAnimal;
                        baby.setStats(this.getStats());
                        baby.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
                        this.world.spawnEntity(baby);
                    }
                    setDead();
                }
            }
            super.onUpdate();
        }

        @Override
        protected boolean processInteract(EntityPlayer player, EnumHand hand)
        {
            if(player.world.isRemote)return true;
            EntityItem item = new EntityItem(this.world, posX, posY, posZ, KeldariaItems.FERTILIZED_EGG.from(this));
            world.spawnEntity(item);
            setDead();
            return super.processInteract(player, hand);
        }


        @Override
        public void writeEntityToNBT(NBTTagCompound compound)
        {
            super.writeEntityToNBT(compound);
            NBTTagCompound tag = new NBTTagCompound();
            if(this.containedAnimal != null)
            {
                this.containedAnimal.writeToNBTAtomically(tag);
                compound.setTag("ContainedEntity", tag);
            }
        }


        @Override
        public void readEntityFromNBT(NBTTagCompound compound)
        {
            super.readEntityFromNBT(compound);
            if(compound.hasKey("ContainedEntity"))
            {
                Entity entity = EntityList.createEntityFromNBT(compound.getCompoundTag("ContainedEntity"), world);
                if (entity instanceof EntityKeldAnimal)
                {
                    this.containedAnimal = (EntityKeldAnimal) entity;
                }

            }
        }

        @Override
        public float getBaseWidth()
        {
            return 0.3F;
        }

        @Override
        public float getBaseHeight()
        {
            return 0.3F;
        }

        @Override
        public double getBaseSpeed()
        {
            return 0;
        }

        @Override
        public double getBaseHealth()
        {
            return 10;
        }

        @Override
        public long getGrowTime()
        {
            return 0;
        }

        @Override
        public List<FoodEntry> getBelovedFood()
        {
            return Lists.newArrayList();
        }

        @Override
        public long getReFertilizationTime()
        {
            return Long.MAX_VALUE;
        }

        @Override
        public long getFullTime()
        {
            return Long.MAX_VALUE;
        }

        @Override
        public long getHydratedTime()
        {
            return Long.MAX_VALUE;
        }

        @Override
        public List<ItemStack> getLoot()
        {
            return Lists.newArrayList();
        }

        @Override
        public EntityKeldAnimal initChild(EntityKeldAnimal mate)
        {
            return null;
        }

        @Override
        public long getConceptionTime()
        {
            return KeldariaDate.Unit.DAYS.toMillis(1);
        }

        @Override
        public List<String> getHoverInfos(EntityPlayer player)
        {
            return Lists.newArrayList("Oeuf fécondé.");
        }
    }
}
