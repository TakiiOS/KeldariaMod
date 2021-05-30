/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import com.mojang.authlib.GameProfile;
import fr.nathanael2611.keldaria.mod.animation.AnimationUtils;
import fr.nathanael2611.keldaria.mod.animation.Animations;
import fr.nathanael2611.keldaria.mod.asm.IKeldariaPlayer;
import fr.nathanael2611.keldaria.mod.asm.ISwimingEntity;
import fr.nathanael2611.keldaria.mod.clothe.InventoryClothes;
import fr.nathanael2611.keldaria.mod.features.accessories.InventoryAccessories;
import fr.nathanael2611.keldaria.mod.inventory.InventoryArmor;
import fr.nathanael2611.keldaria.mod.network.animation.PacketHandleAnimationResponse;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase implements ISwimingEntity, IKeldariaPlayer
{

    @Shadow private int sleepTimer;
    @Shadow public abstract void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn);
    @Shadow public abstract boolean isSpectator();
    @Shadow public int xpCooldown;
    @Shadow public PlayerCapabilities capabilities;
    @Shadow public Container openContainer;
    @Shadow public Container inventoryContainer;
    @Shadow public abstract void closeScreen();
    @Shadow public abstract void addStat(StatBase stat);
    @Shadow protected FoodStats foodStats;
    @Shadow protected abstract void updateCape();
    @Shadow private ItemStack itemStackMainHand;
    @Shadow protected abstract boolean isInBed();
    @Shadow public BlockPos bedLocation;
    @Shadow @Final private CooldownTracker cooldownTracker;
    @Shadow public abstract void resetCooldown();
    @Shadow public abstract String getName();
    @Shadow public float eyeHeight;
    @Shadow protected boolean sleeping;
    @Shadow public InventoryPlayer inventory;
    @Shadow public float experience;
    @Shadow public int experienceLevel;
    @Shadow public int experienceTotal;
    @Shadow protected int xpSeed;
    @Shadow public abstract int getScore();
    @Shadow protected BlockPos spawnPos;
    @Shadow protected boolean spawnForced;
    @Shadow protected HashMap<Integer, BlockPos> spawnChunkMap;
    @Shadow protected HashMap<Integer, Boolean> spawnForcedMap;
    @Shadow public abstract boolean hasSpawnDimension();
    @Shadow public abstract int getSpawnDimension();
    @Shadow protected InventoryEnderChest enderChest;
    @Shadow public abstract NBTTagCompound getLeftShoulderEntity();
    @Shadow public abstract NBTTagCompound getRightShoulderEntity();
    @Shadow @Final private GameProfile gameProfile;
    @Shadow protected abstract void setLeftShoulderEntity(NBTTagCompound tag);
    @Shadow protected abstract void setRightShoulderEntity(NBTTagCompound tag);
    @Shadow @Nullable private Integer spawnDimension;
    @Shadow public abstract void setScore(int scoreIn);

    @Shadow public abstract float getCooledAttackStrength(float adjustTicks);

    @Shadow public abstract void spawnSweepParticles();

    @Shadow public abstract void onCriticalHit(Entity entityHit);

    @Shadow public abstract void onEnchantmentCritical(Entity entityHit);

    @Shadow public abstract void addStat(StatBase stat, int amount);

    @Shadow public abstract void addExhaustion(float exhaustion);

    @Shadow protected abstract void updateSize();

    private boolean isSwiming = false;
    private int swimTime = 0;

    private InventoryClothes cloths = new InventoryClothes((EntityPlayer) (Object) this);
    private InventoryAccessories accessories = new InventoryAccessories();
    private InventoryArmor armor = new InventoryArmor();

    public MixinEntityPlayer(World worldIn)
    {
        super(worldIn);
    }

    public void onUpdate()
    {
        net.minecraftforge.fml.common.FMLCommonHandler.instance().onPlayerPreTick((EntityPlayer) (Object) this);


        if (this.xpCooldown > 0) -- this.xpCooldown;


        if (this.isPlayerSleeping())
        {
            ++ this.sleepTimer;

            if (this.sleepTimer > 100) this.sleepTimer = 100;


            if (!this.world.isRemote)
            {
                if (!this.isInBed()) this.wakeUpPlayer(true, true, false);
                else if (!net.minecraftforge.event.ForgeEventFactory.fireSleepingTimeCheck((EntityPlayer) (Object) this, this.bedLocation)) this.wakeUpPlayer(false, true, true);
            }
        }
        else if (this.sleepTimer > 0)
        {
            ++ this.sleepTimer;
            if (this.sleepTimer >= 110) this.sleepTimer = 0;
        }

        super.onUpdate();

        if (!this.world.isRemote && this.openContainer != null && !this.openContainer.canInteractWith((EntityPlayer) (Object) this))
        {
            this.closeScreen();
            this.openContainer = this.inventoryContainer;
        }

        if (this.isBurning() && this.capabilities.disableDamage) this.extinguish();


        /**
         * The NoClip system
         */
        // -> boolean noClipEnabled = CommandVanish.isNoClipEnabled((EntityPlayer) (Object) this);
        this.noClip = this.isSpectator();

        if (this.isSpectator() /*|| noClipEnabled*/) this.onGround = false;

        this.updateCape();

        if (!this.world.isRemote)
        {
            this.foodStats.onUpdate((EntityPlayer) (Object) this);
            this.addStat(StatList.PLAY_ONE_MINUTE);
            if (this.isEntityAlive()) this.addStat(StatList.TIME_SINCE_DEATH);
            if (this.isSneaking()) this.addStat(StatList.SNEAK_TIME);
        }

        int i = 29999999;
        double d0 = MathHelper.clamp(this.posX, -2.9999999E7D, 2.9999999E7D);
        double d1 = MathHelper.clamp(this.posZ, -2.9999999E7D, 2.9999999E7D);

        if (d0 != this.posX || d1 != this.posZ) this.setPosition(d0, this.posY, d1);

        ++this.ticksSinceLastSwing;
        ItemStack itemstack = this.getHeldItemMainhand();

        if (!ItemStack.areItemStacksEqual(this.itemStackMainHand, itemstack))
        {
            if (!ItemStack.areItemsEqualIgnoreDurability(this.itemStackMainHand, itemstack)) this.resetCooldown();
            this.itemStackMainHand = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
        }

        this.cooldownTracker.tick();
        this.updateSize();

        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(0.20000000298023224D, -0.009999999776482582D, 0.20000000298023224D), EntitySelectors.getTeamCollisionPredicate(this));

        if (!list.isEmpty())
        {
            boolean flag = !this.world.isRemote && !(this.getControllingPassenger() instanceof EntityPlayer);

            for (Entity entity : list)
            {
                if (!entity.isPassenger(this))
                {
                    /*if (flag && this.getPassengers().size() < 2 && !entity.isRiding() && entity.width < this.width && entity instanceof EntityLivingBase && !(entity instanceof EntityWaterMob) && !(entity instanceof EntityPlayer)) entity.startRiding(this);
                    else */this.applyEntityCollision(entity);
                }
            }
        }

        EntityPlayer p = ((EntityPlayer) (Object) this);
        if(isInsideOfMaterial(Material.WATER) && isSprinting())
        {
            swimTime ++;
            if(swimTime > 10 && !world.isRemote && AnimationUtils.getPlayerHandledAnimation(p).getClass() != Animations.SWIM.getClass())
            {
                Helpers.sendToAll(new PacketHandleAnimationResponse(p.getName(), Animations.SWIM.getName()));
                AnimationUtils.setPlayerHandledAnimation(((EntityPlayer) (Object) this), Animations.SWIM);
            }
        }
        else
        {
            if(!this.world.isRemote && AnimationUtils.getPlayerHandledAnimation(p).getClass() == Animations.SWIM.getClass())
            {
                Helpers.sendToAll(new PacketHandleAnimationResponse(p.getName(), Animations.NONE.getName()));
                AnimationUtils.setPlayerHandledAnimation(((EntityPlayer) (Object) this), Animations.NONE);
            }
            int oof = (int) (0.5 * 20);
            if(this.swimTime > oof) this.swimTime = oof;
            this.swimTime--;
            if(this.swimTime < 0) this.swimTime = 0;

        }
        this.isSwiming = swimTime > 0;

        //setVelocity(1, 1, 1);

    }

    /*
    @Override
    public float getEyeHeight()
    {
        float f = this.eyeHeight;
        Animation animation = AnimationUtils.getPlayerHandledAnimation(getName());
        double playerSize = PlayerSizes.get((EntityPlayer) (Object) this);
        if (this.isPlayerSleeping()) f = 0.2F;
        else if (!this.isSneaking())
        {
            if (this.isElytraFlying() || ((ISwimingEntity) this).isSwiming()) f = 0.4F;
            else
            {
                AxisAlignedBB b = getEntityBoundingBox();
                f = animation.getEyeHeight((float) playerSize);
            }
        }
        else f = animation.getSneakingEyeHeight((float) playerSize);
        return f;
    }
*/

    @Override
    public boolean isSwiming()
    {
        return this.isSwiming;
    }

    @Override
    public int getSwimTime()
    {
        return this.swimTime;
    }

    /**
     * @author Mojang
     */
    @Overwrite
    public boolean canEat(boolean ignoreHunger)
    {
        return true;
    }


    @Override
    public InventoryClothes getInventoryCloths()
    {
        return this.cloths;
    }

    @Override
    public InventoryAccessories getInventoryAccessories()
    {
        return this.accessories;
    }

    @Override
    public InventoryArmor getInventoryArmor()
    {
        return armor;
    }

    /**
     * @author Mojang
     */
    //@Overwrite
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("DataVersion", 1343);
        compound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
        compound.setTag("ClothsItems", this.cloths.saveInventoryToNBT());
        compound.setTag("AccessoriesItems", this.accessories.saveInventoryToNBT());
        compound.setTag("ArmorItems", this.armor.saveInventoryToNBT());
        compound.setInteger("SelectedItemSlot", this.inventory.currentItem);
        compound.setBoolean("Sleeping", this.sleeping);
        compound.setShort("SleepTimer", (short)this.sleepTimer);
        compound.setFloat("XpP", this.experience);
        compound.setInteger("XpLevel", this.experienceLevel);
        compound.setInteger("XpTotal", this.experienceTotal);
        compound.setInteger("XpSeed", this.xpSeed);
        compound.setInteger("Score", this.getScore());
        net.minecraftforge.fml.common.FMLCommonHandler.instance().getDataFixer().writeVersionData(compound);

        if (this.spawnPos != null)
        {
            compound.setInteger("SpawnX", this.spawnPos.getX());
            compound.setInteger("SpawnY", this.spawnPos.getY());
            compound.setInteger("SpawnZ", this.spawnPos.getZ());
            compound.setBoolean("SpawnForced", this.spawnForced);
        }

        NBTTagList spawnlist = new NBTTagList();
        for (java.util.Map.Entry<Integer, BlockPos> entry : this.spawnChunkMap.entrySet())
        {
            BlockPos spawn = entry.getValue();
            if (spawn == null) continue;
            Boolean forced = spawnForcedMap.get(entry.getKey());
            if (forced == null) forced = false;
            NBTTagCompound spawndata = new NBTTagCompound();
            spawndata.setInteger("Dim", entry.getKey());
            spawndata.setInteger("SpawnX", spawn.getX());
            spawndata.setInteger("SpawnY", spawn.getY());
            spawndata.setInteger("SpawnZ", spawn.getZ());
            spawndata.setBoolean("SpawnForced", forced);
            spawnlist.appendTag(spawndata);
        }
        compound.setTag("Spawns", spawnlist);

        compound.setBoolean("HasSpawnDimensionSet", this.hasSpawnDimension());
        if (this.hasSpawnDimension())
            compound.setInteger("SpawnDimension", this.getSpawnDimension());

        this.foodStats.writeNBT(compound);
        this.capabilities.writeCapabilitiesToNBT(compound);
        compound.setTag("EnderItems", this.enderChest.saveInventoryToNBT());

        if (!this.getLeftShoulderEntity().isEmpty())
        {
            compound.setTag("ShoulderEntityLeft", this.getLeftShoulderEntity());
        }

        if (!this.getRightShoulderEntity().isEmpty())
        {
            compound.setTag("ShoulderEntityRight", this.getRightShoulderEntity());
        }
    }


    /**
     * @author Mojang
     */
    //@Overwrite
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setUniqueId(EntityPlayer.getUUID(this.gameProfile));
        NBTTagList nbttaglist = compound.getTagList("Inventory", 10);
        this.inventory.readFromNBT(nbttaglist);
        this.inventory.currentItem = compound.getInteger("SelectedItemSlot");
        if(compound.hasKey("ClothsItems"))
        {
            this.cloths.loadInventoryFromNBT(compound.getCompoundTag("ClothsItems"));
        }
        if(compound.hasKey("AccessoriesItems"))
        {
            this.accessories.loadInventoryFromNBT(compound.getCompoundTag("AccessoriesItems"));
        }
        if(compound.hasKey("ArmorItems"))
        {
            this.armor.loadInventoryFromNBT(compound.getCompoundTag("ArmorItems"));
        }
        this.sleeping = compound.getBoolean("Sleeping");
        this.sleepTimer = compound.getShort("SleepTimer");
        this.experience = compound.getFloat("XpP");
        this.experienceLevel = compound.getInteger("XpLevel");
        this.experienceTotal = compound.getInteger("XpTotal");
        this.xpSeed = compound.getInteger("XpSeed");

        if (this.xpSeed == 0)
        {
            this.xpSeed = this.rand.nextInt();
        }

        this.setScore(compound.getInteger("Score"));

        if (this.sleeping)
        {
            this.bedLocation = new BlockPos(this);
            this.wakeUpPlayer(true, true, false);
        }

        if (compound.hasKey("SpawnX", 99) && compound.hasKey("SpawnY", 99) && compound.hasKey("SpawnZ", 99))
        {
            this.spawnPos = new BlockPos(compound.getInteger("SpawnX"), compound.getInteger("SpawnY"), compound.getInteger("SpawnZ"));
            this.spawnForced = compound.getBoolean("SpawnForced");
        }

        NBTTagList spawnlist = null;
        spawnlist = compound.getTagList("Spawns", 10);
        for (int i = 0; i < spawnlist.tagCount(); i++)
        {
            NBTTagCompound spawndata = (NBTTagCompound)spawnlist.getCompoundTagAt(i);
            int spawndim = spawndata.getInteger("Dim");
            this.spawnChunkMap.put(spawndim, new BlockPos(spawndata.getInteger("SpawnX"), spawndata.getInteger("SpawnY"), spawndata.getInteger("SpawnZ")));
            this.spawnForcedMap.put(spawndim, spawndata.getBoolean("SpawnForced"));
        }
        this.spawnDimension = compound.getBoolean("HasSpawnDimensionSet") ? compound.getInteger("SpawnDimension") : null;

        this.foodStats.readNBT(compound);
        this.capabilities.readCapabilitiesFromNBT(compound);

        if (compound.hasKey("EnderItems", 9))
        {
            NBTTagList nbttaglist1 = compound.getTagList("EnderItems", 10);
            this.enderChest.loadInventoryFromNBT(nbttaglist1);
        }

        if (compound.hasKey("ShoulderEntityLeft", 10))
        {
            this.setLeftShoulderEntity(compound.getCompoundTag("ShoulderEntityLeft"));
        }

        if (compound.hasKey("ShoulderEntityRight", 10))
        {
            this.setRightShoulderEntity(compound.getCompoundTag("ShoulderEntityRight"));
        }
    }







}