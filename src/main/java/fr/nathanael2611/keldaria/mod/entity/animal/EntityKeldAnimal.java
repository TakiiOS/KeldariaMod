package fr.nathanael2611.keldaria.mod.entity.animal;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fr.nathanael2611.keldaria.mod.asm.MixinHooks;
import fr.nathanael2611.keldaria.mod.client.render.entity.animal.FoodEntry;
import fr.nathanael2611.keldaria.mod.entity.AnimalStat;
import fr.nathanael2611.keldaria.mod.features.AnimalGender;
import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class EntityKeldAnimal extends EntityCreature
{

    private static final DataParameter<AnimalStat> STATS = EntityDataManager.createKey(EntityKeldAnimal.class, MixinHooks.ANIMAL_STATS);
    private static final DataParameter<Long> LAST_FEED = EntityDataManager.createKey(EntityKeldAnimal.class, MixinHooks.LONG);
    private static final DataParameter<Long> LAST_DRINK = EntityDataManager.createKey(EntityKeldAnimal.class, MixinHooks.LONG);
    private static final DataParameter<AnimalGender> GENDER = EntityDataManager.createKey(EntityKeldAnimal.class, MixinHooks.GENDER);
    private static final DataParameter<Long> BORN_DATE = EntityDataManager.createKey(EntityKeldAnimal.class, MixinHooks.LONG);

    private UUID owner = null;

    public EntityKeldAnimal(World worldIn)
    {
        super(worldIn);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getBaseHealth() * this.getStats().getResistance());
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.getBaseSpeed() * this.getStats().getSpeed());
        this.setSize((float)(this.getBaseWidth() * this.getStats().getSize()), (float) (this.getBaseHeight() * this.getStats().getSize()));
    }

    public abstract float getBaseWidth();

    public abstract float getBaseHeight();

    public abstract double getBaseSpeed();

    public abstract double getBaseHealth();


    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(STATS, new AnimalStat(1, 1, 1));
        this.dataManager.register(LAST_FEED, System.currentTimeMillis());
        this.dataManager.register(LAST_DRINK, System.currentTimeMillis());
        this.dataManager.register(GENDER, AnimalGender.MALE);
        this.dataManager.register(BORN_DATE, System.currentTimeMillis());

    }

    public abstract long getGrowTime();

    public boolean isAdult()
    {
        return System.currentTimeMillis() - this.getBornDate() > getGrowTime();
    }

    public abstract List<FoodEntry> getBelovedFood();

    public Set<Item> getTemptationItems()
    {
        Set<Item> set = Sets.newHashSet();
        for (FoodEntry foodEntry : this.getBelovedFood())
        {
            set.add(foodEntry.getItem());
        }
        return set;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        if(!world.isRemote)
        {
            return this.tryProvide(player, player.getHeldItem(hand));
        }
        return true;
    }

    public AnimalGender getGender()
    {
        return this.dataManager.get(GENDER);
    }

    public void setGender(AnimalGender gender)
    {
        this.dataManager.set(GENDER, gender);
    }

    public AnimalStat getStats()
    {
        return this.dataManager.get(STATS);
    }

    public void setStats(AnimalStat gender)
    {
        this.dataManager.set(STATS, gender);
    }

    public long getBornDate()
    {
        return this.dataManager.get(BORN_DATE);
    }

    public void setBornDate(long bornDate)
    {
        this.dataManager.set(BORN_DATE, bornDate);
    }

    public void feed()
    {
        this.feed(System.currentTimeMillis());
    }

    public void feed(long time)
    {
        this.dataManager.set(LAST_FEED, time);
    }

    public boolean isHungry()
    {
        return /*!isTamed() || */System.currentTimeMillis() - getLastFeed() > getFullTime();
    }

    public void drink()
    {
        this.drink(System.currentTimeMillis());
    }

    public void drink(long time)
    {
        this.dataManager.set(LAST_DRINK, time);
    }

    public boolean isThirsty()
    {
        return /*!isTamed() ||*/ System.currentTimeMillis() - getLastDrink() > getHydratedTime();
    }

    public abstract long getFullTime();

    public abstract long getHydratedTime();

    private long getLastFeed()
    {
        return this.dataManager.get(LAST_FEED);
    }

    private long getLastDrink()
    {
        return this.dataManager.get(LAST_DRINK);
    }

    public boolean isTamed()
    {
        return this.owner != null;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        if(compound.hasKey("Stats"))
            this.setStats(new AnimalStat(compound.getCompoundTag("Stats")));

        if(compound.hasKey("BornDate"))
            this.setBornDate(compound.getLong("BornDate"));
        if(compound.hasUniqueId("Owner"))
            owner = compound.getUniqueId("Owner");

        if(compound.hasKey("LastFeed"))
            feed(compound.getLong("LastFeed"));
        if(compound.hasKey("LastDrink"))
            drink(compound.getLong("LastDrink"));

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setTag("Stats", this.getStats().serializeNBT());

        compound.setLong("BornDate", this.getBornDate());
        if(this.owner != null)
            compound.setUniqueId("Owner", this.owner);

        compound.setLong("LastFeed", this.getLastFeed());
        compound.setLong("LastDrink", this.getLastDrink());
    }

    @Override
    public boolean isChild()
    {
        return !isAdult();
    }

    public boolean canMateWith(EntityKeldAnimal otherAnimal)
    {
        if (otherAnimal == this)
        {
            return false;
        }
        else if (otherAnimal.getClass() != this.getClass())
        {
            return false;
        }
        else
        {
            return this.getGender() != otherAnimal.getGender() && (this.isHealthy() && otherAnimal.isHealthy());
        }
    }

    public boolean isHealthy()
    {
        return !this.isHungry() && !this.isThirsty();
    }

    public boolean loveFood(ItemStack stack)
    {
        return loveFood(stack.getItem());
    }

    public boolean loveFood(Item item)
    {
        return getLovePercent(item) > 0;
    }

    public int getLovePercent(ItemStack stack)
    {
        return this.getLovePercent(stack.getItem());
    }

    public int getLovePercent(Item item)
    {
        for (FoodEntry foodEntry : this.getBelovedFood())
        {
            if(foodEntry.getItem() == item) return foodEntry.getAffectionPercent();
        }
        return 0;
    }

    public boolean tryProvide(EntityPlayer player, ItemStack stack)
    {
        if(isHungry() && loveFood(stack))
        {
            int lovePercent = getLovePercent(stack);
            int rand = Helpers.randomInteger(0, 100);
            if(rand <= lovePercent)
            {
                this.feed();
            }
            stack.shrink(1);
            world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 1f, 1f);
            return true;
        }
        else if(isThirsty() && stack.getItem() == Items.WATER_BUCKET)
        {
            this.drink();
            stack.shrink(1);
            world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 1f, 1f);
            return true;
        }
        return false;
    }

    public abstract List<ItemStack> getLoot();

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        for (ItemStack itemStack : this.getLoot())
        {
            this.entityDropItem(itemStack, 0.0F);
        }
        super.dropLoot(wasRecentlyHit, lootingModifier, source);
    }

    public List<KeldariaDate.Month> getFertileMonths()
    {
        return Lists.newArrayList(KeldariaDate.Month.PAIS, KeldariaDate.Month.MAGUIA);
    }

    public boolean isFertile()
    {
        return this.isAdult() && getFertileMonths().contains(KeldariaDate.lastDate.getMonth());
    }

    public boolean canMate()
    {
        return this.isFertile();
    }




}
