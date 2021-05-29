package fr.nathanael2611.keldaria.mod.entity;

import fr.nathanael2611.keldaria.mod.animation.Animation;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityLucrain extends EntityAnimal implements EntityFlying
{

    private AnimalStat stat;
    private long bornTime = System.currentTimeMillis();
    private long growTime = 20000;

    public EntityLucrain(World worldIn)
    {
        super(worldIn);

        stepHeight = 1.0F;

        //setSize(3, 3);
    }

    public AnimalStat getStat()
    {
        return stat;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        double s = this.stat.getSize(this.bornTime, this.growTime);
        width = (float) s;
        height = (float) s;
        setEntityBoundingBox(Animation.createHitboxByDimensions(this, width, height));


    }



    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.stat = new AnimalStat(2, 3, 3);
        bornTime = System.currentTimeMillis();
    }


    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        if(compound.hasKey("AnimalStat"))
        {
            this.stat = new AnimalStat(compound.getCompoundTag("AnimalStat"));
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setTag("AnimalStat", this.stat.serializeNBT());
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        boolean bool = player.startRiding(this, true);
        return true;
    }

    public boolean canBeSteered()
    {
        return this.getControllingPassenger() instanceof EntityLivingBase;
    }

    public void travel(float strafe, float vertical, float forward)
    {
        if (this.isBeingRidden() && this.canBeSteered() /*&& this.isHorseSaddled()*/)
        {

            EntityLivingBase entitylivingbase = (EntityLivingBase)this.getControllingPassenger();
            this.rotationYaw = entitylivingbase.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            //strafe = entitylivingbase.moveStrafing * 0.5F;
            //forward = entitylivingbase.moveForward;

            this.motionX = this.getLookVec().x * entitylivingbase.moveForward;
            this.motionY = (this.getLookVec().y * 2) * entitylivingbase.moveForward;
            this.motionZ = this.getLookVec().z * entitylivingbase.moveForward;

            if(entitylivingbase.isSprinting())
            {
                this.motionX *= 1.3;
                this.motionZ *= 1.3;
            }

            if(this.onGround)
            {
                this.motionX /= 2;
                this.motionZ /= 2;
            }

            if(motionX + motionZ <= 0)
            {
                this.motionY -= 0.05;
            }

            //this.move(MoverType.PLAYER, this.getLookVec().x, this.getLookVec().y, this.getLookVec().z);

            this.motionX /= 2;
            this.motionZ /= 2;
            this.motionY /= 2;


            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

            if (this.canPassengerSteer())
            {
                forward = 0;
                this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                super.travel(strafe, vertical, forward);
            }
            else if (entitylivingbase instanceof EntityPlayer)
            {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }


            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.posX - this.prevPosX;
            double d0 = this.posZ - this.prevPosZ;
            float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

            if (f2 > 1.0F)
            {
                f2 = 1.0F;
            }

            this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        }
    }




    @Override
    public void updatePassenger(Entity passenger)
    {
        super.updatePassenger(passenger);

        if (passenger instanceof EntityLiving)
        {
            EntityLiving entityliving = (EntityLiving)passenger;
            this.renderYawOffset = entityliving.renderYawOffset;
        }
        passenger.setPosition(this.posX, this.posY + (getStat().getSize(bornTime, growTime)*0.45), this.posZ);

        if(false)
        {
            float f3 = MathHelper.sin(this.renderYawOffset * 0.017453292F);
            float f = MathHelper.cos(this.renderYawOffset * 0.017453292F);
            float f1 = 0.7F;
            float f2 = 0.15F;
            passenger.setPosition(
                    this.posX + (double)(f1 * f3),
                    this.posY + this.getMountedYOffset() + passenger.getYOffset() + (double)f2,
                    this.posZ - (double)(f1 * f));

            if (passenger instanceof EntityLivingBase)
            {
                ((EntityLivingBase)passenger).renderYawOffset = this.renderYawOffset;
            }
        }
    }

    @Nullable
    @Override
    public Entity getControllingPassenger()
    {
        return this.getPassengers().isEmpty() ? null : (Entity)this.getPassengers().get(0);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable)
    {
        return null;
    }


}
