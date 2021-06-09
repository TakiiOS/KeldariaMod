package fr.nathanael2611.keldaria.mod.entity.animal;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.asm.MixinHooks;
import fr.nathanael2611.keldaria.mod.entity.ai.EntityAIReproduction;
import fr.nathanael2611.keldaria.mod.entity.ai.KeldAITempt;
import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class KeldaSheep extends EntityKeldAnimal implements IShearable
{

    private static final List<FoodEntry> BELOVED_FOOD = Lists.newArrayList(new FoodEntry(Items.WHEAT, 30));
    private static final DataParameter<Fleece> FLEECE = EntityDataManager.createKey(KeldaSheep.class, MixinHooks.FLEECE);

    private long lastShear = -1;

    public KeldaSheep(World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(FLEECE, new Fleece(true, EnumDyeColor.WHITE));
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(2, new EntityAIReproduction(this, 1.0D));
        this.tasks.addTask(3, new KeldAITempt(this, 1.1D, false));
        //this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));

    }

    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        if(this.needNewWool())
        {
            this.lastShear = -1;
            this.setFleece(this.getFleece().withFleece(true));
        }
    }

    public void setFleece(Fleece fleece)
    {
        this.dataManager.set(FLEECE, fleece);
    }

    public Fleece getFleece()
    {
        return this.dataManager.get(FLEECE);
    }

    public static EnumDyeColor getRandomSheepColor(Random random)
    {
        int i = random.nextInt(100);

        if (i < 5)
        {
            return EnumDyeColor.BLACK;
        } else if (i < 10)
        {
            return EnumDyeColor.GRAY;
        } else if (i < 15)
        {
            return EnumDyeColor.SILVER;
        } else if (i < 18)
        {
            return EnumDyeColor.BROWN;
        } else
        {
            return random.nextInt(500) == 0 ? EnumDyeColor.PINK : EnumDyeColor.WHITE;
        }
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Fleece"))
        {
            this.setFleece(new Fleece(compound.getCompoundTag("Fleece")));
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setTag("Fleece", this.getFleece().serializeNBT());
    }


    @Override
    public float getBaseWidth()
    {
        return this.isAdult() ? 0.9F : 0.6F;
    }

    @Override
    public float getBaseHeight()
    {
        return this.isAdult() ? 1.3F : 0.9F;
    }

    @Override
    public double getBaseSpeed()
    {
        return 0.23000000417232513D;
    }

    @Override
    public double getBaseHealth()
    {
        return 8.0D;
    }

    @Override
    public long getGrowTime()
    {
        return KeldariaDate.Unit.MONTH.toMillis(1);
    }

    @Override
    public List<FoodEntry> getBelovedFood()
    {
        return BELOVED_FOOD;
    }

    @Override
    public long getReFertilizationTime()
    {
        return KeldariaDate.Unit.MONTH.toMillis(2);
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
        return Lists.newArrayList(new ItemStack(Items.MUTTON));
    }

    private boolean needNewWool()
    {
        return (this.lastShear != -1 && System.currentTimeMillis() - this.lastShear > KeldariaDate.Unit.DAYS.toMillis(3));
    }

    @Override
    public EntityKeldAnimal initChild(EntityKeldAnimal mate)
    {
        KeldaSheep sheep = new KeldaSheep(this.world);
        if(mate instanceof KeldaSheep)
        {
            sheep.setFleece(this.getFleece().withColor(getDyeColorMixFromParents(this, (KeldaSheep) mate)));
        }
        else
        {
            sheep.setFleece(this.getFleece().withColor(getRandomSheepColor(Helpers.RANDOM)));
        }
        return sheep;
    }


    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setFleece(this.getFleece().withColor(getRandomSheepColor(this.world.rand)));
        return livingdata;
    }

    @Override
    public long getConceptionTime()
    {
        return KeldariaDate.Unit.MONTH.toMillis(1);
    }

    private static final InventoryCrafting INVENTORY_CRAFTING = new InventoryCrafting(new Container()
    {
        public boolean canInteractWith(EntityPlayer playerIn)
        {
            return false;
        }
    }, 2, 1);


    private static EnumDyeColor getDyeColorMixFromParents(KeldaSheep father, KeldaSheep mother)
    {
        int i = father.getFleece().getColor().getDyeDamage();
        int j = mother.getFleece().getColor().getDyeDamage();
        INVENTORY_CRAFTING.getStackInSlot(0).setItemDamage(i);
        INVENTORY_CRAFTING.getStackInSlot(1).setItemDamage(j);
        ItemStack itemstack = CraftingManager.findMatchingResult(INVENTORY_CRAFTING, father.world);
        int k;

        if (itemstack.getItem() == Items.DYE)
        {
            k = itemstack.getMetadata();
        }
        else
        {
            k = Helpers.RANDOM.nextBoolean() ? i : j;
        }

        return EnumDyeColor.byDyeDamage(k);
    }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos)
    {
        return !this.getFleece().isSheared() && this.isAdult();
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    {
        this.setFleece(this.getFleece().withFleece(false));
        int i = 1 + this.rand.nextInt(3);

        java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        for (int j = 0; j < i; ++j)
            ret.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, this.getFleece().getColor().getMetadata()));

        this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        this.lastShear = System.currentTimeMillis();
        return ret;
    }
}
