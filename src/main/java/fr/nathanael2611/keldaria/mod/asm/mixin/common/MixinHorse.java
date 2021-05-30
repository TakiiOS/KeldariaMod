/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import com.google.common.base.Optional;
import fr.nathanael2611.keldaria.mod.asm.ILivingHorse;
import fr.nathanael2611.keldaria.mod.asm.MixinHooks;
import fr.nathanael2611.keldaria.mod.features.AnimalGender;
import fr.nathanael2611.keldaria.mod.features.HorsesTextures;
import fr.nathanael2611.keldaria.mod.inventory.InventoryHorseStorage;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(AbstractHorse.class)
public abstract class MixinHorse extends EntityAnimal implements IInventoryChangedListener, IJumpingMount, ILivingHorse
{

    @Shadow public abstract void setRearing(boolean rearing);
    @Shadow public abstract void setEatingHaystack(boolean p_110227_1_);
    @Shadow private int openMouthCounter;
    @Shadow protected abstract void setHorseWatchableBoolean(int p_110208_1_, boolean p_110208_2_);
    @Shadow private int jumpRearingCounter;
    @Shadow public int tailCounter;
    @Shadow public int sprintCounter;
    @Shadow private float prevHeadLean;
    @Shadow private float headLean;
    @Shadow public abstract boolean isEatingHaystack();
    @Shadow private boolean allowStandSliding;
    @Shadow private float rearingAmount;
    @Shadow private float mouthOpenness;
    @Shadow private float prevMouthOpenness;
    @Shadow protected abstract boolean getHorseWatchableBoolean(int p_110233_1_);
    @Shadow private float prevRearingAmount;
    @Shadow public abstract boolean isRearing();

    @Shadow @Final private static DataParameter<Byte> STATUS;

    @Shadow @Final private static DataParameter<Optional<UUID>> OWNER_UNIQUE_ID;

    @Shadow public abstract void setOwnerUniqueId(@Nullable UUID uniqueId);

    protected void updateHorseSlots()
    {
        if (!this.world.isRemote)
        {
            this.setHorseSaddled(!this.horseChest.getStackInSlot(0).isEmpty() && this.canBeSaddled());

        }

    };

    @Shadow protected ContainerHorseChest horseChest;

    @Shadow public abstract void setTemper(int temperIn);

    @Shadow public abstract void setBreeding(boolean breeding);

    @Shadow public abstract void setHorseTamed(boolean tamed);

    @Shadow @Nullable public abstract UUID getOwnerUniqueId();

    @Shadow public abstract boolean isTame();

    @Shadow public abstract int getTemper();

    @Shadow public abstract boolean isBreeding();

    @Shadow @Final protected static IAttribute JUMP_STRENGTH;

    @Shadow public abstract void setHorseSaddled(boolean saddled);

    @Shadow public abstract boolean canBeSaddled();

    public MixinHorse(World worldIn) {
        super(worldIn);
    }


    /**
     * @author
     */
  //  @Overwrite
    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger))
        {
            float f = -0.3F;
            float f1 = (float)((this.isDead ? 0.009999999776482582D : this.getMountedYOffset()) + passenger.getYOffset()) + (0.15F * this.prevRearingAmount);

            if (this.getPassengers().size() > 1)
            {
                int i = this.getPassengers().indexOf(passenger);
                if (i == 0)
                {
                    f = MixinHooks.getHorsePassengerOffset(i);
                }
                else
                {
                    f = MixinHooks.getHorsePassengerOffset(i);
                }
                if (passenger instanceof EntityAnimal)
                {
                    f = (float)((double)f + 0.2D);
                }
            }
            if(rearingAmount > 0)
            {
                f -= rearingAmount / 2;
            }
            Vec3d vec3d = (new Vec3d((double)f, 0.0D, 0.0D)).rotateYaw(-this.rotationYaw * 0.017453292F - ((float)Math.PI / 2F));
            passenger.setPosition(this.posX + vec3d.x, this.posY + (double)f1, this.posZ + vec3d.z);
            if (passenger instanceof EntityAnimal && this.getPassengers().size() > 1)
            {
                int j = passenger.getEntityId() % 2 == 0 ? 90 : 270;
                passenger.setRenderYawOffset(((EntityAnimal)passenger).renderYawOffset + (float)j);
                passenger.setRotationYawHead(passenger.getRotationYawHead() + (float)j);
            }
        }
    }

   // @Overwrite
    protected void mountTo(EntityPlayer player)
    {
        if(getPassengers().size() > 2) return;
        player.rotationYaw = this.rotationYaw;
        player.rotationPitch = this.rotationPitch;
        this.setEatingHaystack(false);
        this.setRearing(false);

        if (!this.world.isRemote)
        {
            player.startRiding(this);
        }
    }


    public void onUpdate()
    {
        super.onUpdate();

        if (this.openMouthCounter > 0 && ++this.openMouthCounter > 30)
        {
            this.openMouthCounter = 0;
            this.setHorseWatchableBoolean(64, false);
        }

        if (this.canPassengerSteer() && this.jumpRearingCounter > 0 && ++this.jumpRearingCounter > 20)
        {
            this.jumpRearingCounter = 0;
            this.setRearing(false);
        }

        if (this.tailCounter > 0 && ++this.tailCounter > 8)
        {
            this.tailCounter = 0;
        }

        if (this.sprintCounter > 0)
        {
            ++this.sprintCounter;

            if (this.sprintCounter > 300)
            {
                this.sprintCounter = 0;
            }
        }

        this.prevHeadLean = this.headLean;

        if (this.isEatingHaystack())
        {
            this.headLean += (1.0F - this.headLean) * 0.4F + 0.05F;

            if (this.headLean > 1.0F)
            {
                this.headLean = 1.0F;
            }
        }
        else
        {
            this.headLean += (0.0F - this.headLean) * 0.4F - 0.05F;

            if (this.headLean < 0.0F)
            {
                this.headLean = 0.0F;
            }
        }

        this.prevRearingAmount = this.rearingAmount;

        if (this.isRearing())
        {
            this.headLean = 0.0F;
            this.prevHeadLean = this.headLean;
            this.rearingAmount += (1.0F - this.rearingAmount) * 0.4F + 0.05F;

            if (this.rearingAmount > 1.0F)
            {
                this.rearingAmount = 1.0F;
            }
        }
        else
        {
            this.allowStandSliding = false;
            this.rearingAmount += (0.8F * this.rearingAmount * this.rearingAmount * this.rearingAmount - this.rearingAmount) * 0.6F - 0.05F;

            if (this.rearingAmount < 0.0F)
            {
                this.rearingAmount = 0.0F;
            }
        }

        this.prevMouthOpenness = this.mouthOpenness;

        if (this.getHorseWatchableBoolean(64))
        {
            this.mouthOpenness += (1.0F - this.mouthOpenness) * 0.7F + 0.05F;

            if (this.mouthOpenness > 1.0F)
            {
                this.mouthOpenness = 1.0F;
            }
        }
        else
        {
            this.mouthOpenness += (0.0F - this.mouthOpenness) * 0.7F - 0.05F;

            if (this.mouthOpenness < 0.0F)
            {
                this.mouthOpenness = 0.0F;
            }
        }
        if(lifeUpdateTimer < 20 * 5)
        {
            lifeUpdateTimer ++;
        }
        else
        {
            lifeUpdateTimer = 0;
            updateLife();
        }
    }

    private int lifeUpdateTimer = 0;

    private InventoryHorseStorage horseStorage = new InventoryHorseStorage((AbstractHorse) (Object) this);

    @Override
    public InventoryHorseStorage getHorseStorage()
    {
        return this.horseStorage;
    }

    @Override
    public double getFood()
    {
        return dataManager.get(MixinHooks.HORSE_FOOD);
    }

    @Override
    public double getThirst()
    {
        return dataManager.get(MixinHooks.HORSE_THIRST);
    }

    @Override
    public double getCleaniless()
    {
        return dataManager.get(MixinHooks.HORSE_CLEANILESS);
    }

    @Override
    public AnimalGender getGender()
    {
        return AnimalGender.byId(dataManager.get(MixinHooks.HORSE_GENDER));
    }

    @Override
    public void setFood(double food)
    {
        dataManager.set(MixinHooks.HORSE_FOOD, food);
    }

    @Override
    public void setThirst(double thirst)
    {
        dataManager.set(MixinHooks.HORSE_THIRST, thirst);
    }

    @Override
    public void setCleaniless(double cleaniless)
    {
        dataManager.set(MixinHooks.HORSE_CLEANILESS, cleaniless);
    }

    @Override
    public void setGender(AnimalGender gender)
    {
        dataManager.set(MixinHooks.HORSE_GENDER, gender.getId());
    }

    /**
     * @author
     */
    @Overwrite
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(STATUS, Byte.valueOf((byte) 0));
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
        if((((AbstractHorse) (Object) this) instanceof EntityLlama))return;
            this.dataManager.register(MixinHooks.HORSE_FOOD, 100d);
            this.dataManager.register(MixinHooks.HORSE_THIRST, 100d);
            this.dataManager.register(MixinHooks.HORSE_CLEANILESS, 100d);
            this.dataManager.register(MixinHooks.HORSE_GENDER, Helpers.RANDOM.nextInt(100) > 65 ? 0 : 1);
        this.dataManager.register(HorsesTextures.TEXTURE, "");
        this.dataManager.register(MixinHooks.HORSE_STORAGE_1, ItemStack.EMPTY);
        this.dataManager.register(MixinHooks.HORSE_STORAGE_2, ItemStack.EMPTY);
        this.dataManager.register(MixinHooks.HORSE_STORAGE_3, ItemStack.EMPTY);
        this.dataManager.register(MixinHooks.HORSE_STORAGE_4, ItemStack.EMPTY);
    }

    @Override
    public AbstractHorse get()
    {
        return (AbstractHorse ) (Object ) this;
    }


    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);


        compound.setBoolean("EatingHaystack", this.isEatingHaystack());
        compound.setBoolean("Bred", this.isBreeding());
        compound.setInteger("Temper", this.getTemper());
        compound.setBoolean("Tame", this.isTame());

        if (this.getOwnerUniqueId() != null)
        {
            compound.setString("OwnerUUID", this.getOwnerUniqueId().toString());
        }

        if (!this.horseChest.getStackInSlot(0).isEmpty())
        {
            compound.setTag("SaddleItem", this.horseChest.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
        }

        if(!(((AbstractHorse) (Object) this) instanceof EntityLlama))
        {
            compound.setDouble("LifeFood", getFood());
            compound.setDouble("LifeThirst", getThirst());
            compound.setDouble("LifeCleaniless", getCleaniless());
            compound.setInteger("LifeGender", getGender().getId());

            if (!this.horseStorage.getStackInSlot(0).isEmpty())
            {
                compound.setTag("HorseStorage1", this.horseStorage.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
            }
            if (!this.horseStorage.getStackInSlot(1).isEmpty())
            {
                compound.setTag("HorseStorage2", this.horseStorage.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
            }
            if (!this.horseStorage.getStackInSlot(2).isEmpty())
            {
                compound.setTag("HorseStorage3", this.horseStorage.getStackInSlot(2).writeToNBT(new NBTTagCompound()));
            }
            if (!this.horseStorage.getStackInSlot(3).isEmpty())
            {
                compound.setTag("HorseStorage4", this.horseStorage.getStackInSlot(3).writeToNBT(new NBTTagCompound()));
            }

        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);



        this.setEatingHaystack(compound.getBoolean("EatingHaystack"));
        this.setBreeding(compound.getBoolean("Bred"));
        this.setTemper(compound.getInteger("Temper"));
        this.setHorseTamed(compound.getBoolean("Tame"));
        String s;

        if (compound.hasKey("OwnerUUID", 8))
        {
            s = compound.getString("OwnerUUID");
        }
        else
        {
            String s1 = compound.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
        }

        if (!s.isEmpty())
        {
            this.setOwnerUniqueId(UUID.fromString(s));
        }

        IAttributeInstance iattributeinstance = this.getAttributeMap().getAttributeInstanceByName("Speed");

        if (iattributeinstance != null)
        {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(iattributeinstance.getBaseValue() * 0.25D);
        }

        if (compound.hasKey("SaddleItem", 10))
        {
            ItemStack itemstack = new ItemStack(compound.getCompoundTag("SaddleItem"));

            if (itemstack.getItem() == Items.SADDLE)
            {
                this.horseChest.setInventorySlotContents(0, itemstack);
            }
        }


        if(!(((AbstractHorse) (Object) this) instanceof EntityLlama))
        {
            if(compound.hasKey("LifeFood"))
            {
                setFood(compound.getDouble("LifeFood"));
            }
            if(compound.hasKey("LifeThirst"))
            {
                setThirst(compound.getDouble("LifeThirst"));
            }
            if(compound.hasKey("LifeCleaniless"))
            {
                setCleaniless(compound.getDouble("LifeCleaniless"));
            }
            if(compound.hasKey("LifeGender"))
            {
                setGender(AnimalGender.byId(compound.getInteger("LifeGender")));
            }

                //this.horseStorage.readNBT(compound.getCompoundTag("HorseStorage"));
                //dataManager.set(MixinHooks.HORSE_STORAGE_COMPOUND, compound.getCompoundTag("HorseStorage"));
            if (compound.hasKey("HorseStorage1", 10))
            {
                this.horseStorage.setInventorySlotContents(0, new ItemStack(compound.getCompoundTag("HorseStorage1")));
            }
            if (compound.hasKey("HorseStorage2", 10))
            {
                this.horseStorage.setInventorySlotContents(1, new ItemStack(compound.getCompoundTag("HorseStorage2")));
            }
            if (compound.hasKey("HorseStorage3", 10))
            {
                this.horseStorage.setInventorySlotContents(2, new ItemStack(compound.getCompoundTag("HorseStorage3")));
            }
            if (compound.hasKey("HorseStorage4", 10))
            {
                this.horseStorage.setInventorySlotContents(3, new ItemStack(compound.getCompoundTag("HorseStorage4")));
            }



        }



        this.updateHorseSlots();
    }

    @Override
    public void setJumpStrenght(double value)
    {
        this.getEntityAttribute(JUMP_STRENGTH).setBaseValue(value);
    }


    protected boolean handleEating(EntityPlayer player, ItemStack stack)
    {
        return this.eat(player, stack);
    }
}
