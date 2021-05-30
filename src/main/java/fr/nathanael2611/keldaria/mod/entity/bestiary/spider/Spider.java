/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.entity.bestiary.spider;

import fr.nathanael2611.keldaria.mod.entity.bestiary.EntityBestiary;
import fr.nathanael2611.keldaria.mod.features.combat.EntityStats;
import fr.nathanael2611.keldaria.mod.features.combat.EnumAttackType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class Spider extends EntityBestiary implements IRangedAttackMob
{


    private EnumSpiderType type;

    public Spider(World worldIn)
    {
        this(worldIn, EnumSpiderType.BIG);
    }

    public Spider(World worldIn, EnumSpiderType type)
    {
        super(worldIn);
        this.type = type;
        this.setSize((float) (1.5 * type.getSize()), (float) (1 * type.getSize()));
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25 * this.type.getStatModifier());
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(4.5 / this.type.getStatModifier());
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4D * this.type.getStatModifier());

        stepHeight = 1.0F;


        this.tasks.addTask(0, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));

        if(this.type == EnumSpiderType.QUEEN)
        {
            this.tasks.addTask(1, new EntityAIAttackRanged(this, 0.6, 30, 15.0F));
        }
        else
        {
            this.tasks.addTask(1, new EntityAIAttackMelee(this, 0.075, true){
                @Override
                protected double getAttackReachSqr(EntityLivingBase attackTarget)
                {
                    return Spider.this.type == EnumSpiderType.BIG ? 4*4 : 2*2;
                }
            });
        }

        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));

    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    @Override
    protected float getSoundVolume()
    {
        return (float) (super.getSoundVolume() * (type.getStatModifier()));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        System.out.println("cc");
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    @Override
    protected void initEntityAI()
    {




    }



    @Override
    public EntityStats getCombatStats()
    {
        return EntityStats.from(EnumAttackType.THRUST, (int)(30 * this.type.getStatModifier()), (int) (10 * this.type.getStatModifier()), (int) (20 * this.type.getStatModifier()));
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
    {
        EntityTippedArrow entitysnowball = new EntityTippedArrow(this.world, this);
        double d0 = target.posY + (double)target.getEyeHeight() - 1.100000023841858D;
        double d1 = target.posX - this.posX;
        double d2 = d0 - entitysnowball.posY;
        double d3 = target.posZ - this.posZ;
        float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
        entitysnowball.shoot(d1, d2 + (double)f, d3, 1.6F, 12.0F);
        this.playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entitysnowball);
    }

    @Override
    public void setSwingingArms(boolean swingingArms)
    {

    }


    public enum EnumSpiderType
    {
        BASE(0, 1, 1),
        BIG(1, 2, 1.8),
        QUEEN(2, 2.5, 2.5);

        private int id;
        private double size;
        private double statModifier;

        EnumSpiderType(int id, double size, double statModifier)
        {
            this.id = id;
            this.size = size;
            this.statModifier = statModifier;
        }

        public double getSize()
        {
            return size;
        }

        public int getId()
        {
            return id;
        }

        public double getStatModifier()
        {
            return statModifier;
        }

        public static EnumSpiderType byId(int id)
        {
            for (EnumSpiderType value : values())
            {
                if(value.getId() == id) return value;
            }
            return BASE;
        }

    }

    public EnumSpiderType getType()
    {
        return type;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("SpiderType", this.type.getId());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        if(compound.hasKey("SpiderType", Constants.NBT.TAG_INT))
        {
            this.type = EnumSpiderType.byId(compound.getInteger("SpiderType"));
        }
        else
        {
            this.type = EnumSpiderType.BASE;
        }
    }
}
