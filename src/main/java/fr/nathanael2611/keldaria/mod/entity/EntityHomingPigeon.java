/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.entity;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.features.combat.EntityStats;
import fr.nathanael2611.keldaria.mod.features.combat.EnumAttackType;
import fr.nathanael2611.keldaria.mod.features.combat.IHasStat;
import fr.nathanael2611.keldaria.mod.features.skill.EnumJob;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketOpenHomingPigeon;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.List;

public class EntityHomingPigeon extends EntityAnimal implements IHasStat, EntityFlying
{

    private static EntityStats STATS = EntityStats.from(EnumAttackType.HIT, 0, 0, 0);

    private static final DataParameter<Boolean> TAMED = EntityDataManager.createKey(EntityHomingPigeon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> LETTER = EntityDataManager.createKey(EntityHomingPigeon.class, DataSerializers.ITEM_STACK);

    private TravelState travelState = TravelState.IDLING;

    private PigeonLocation destination = new PigeonLocation();

    private PigeonLocation loc1 = new PigeonLocation();
    private PigeonLocation loc2 = new PigeonLocation();

    private ChunkPos landingPosition = null;
    private ForgeChunkManager.Ticket chunkToLoadTicket = null;
    private long landDate = -1;

    public EntityHomingPigeon(World worldIn)
    {
        super(worldIn);
        this.moveHelper = new EntityFlyHelper(this);
        this.setSize(0.5F, 0.5F);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if(landDate != -1 && chunkToLoadTicket != null && this.landingPosition != null)
        {
            if(System.currentTimeMillis() - landDate > 60000 * 4)
            {
                this.forgotLastLanding();
            }
        }

        if (!this.onGround && this.motionY < 0.0D)
        {
            if(this.travelState == TravelState.LANDING)
            {
                double haha= ((posY * 50 / 255)) * 0.01;
                this.motionY *= (0.5 + haha);
            }else
            {
                this.motionY *= 0.6D;
            }
        }

    }

    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        //System.out.println(this.getDestination().getPos());
        if (!world.isRemote)
        {
            if (this.isTamed())
            {
                if(this.travelState != TravelState.IDLING)
                {
                    PigeonLocation destination = this.destination;
                    BlockPos destPos = destination.getPos();
                    if (this.getDestination().isValid())
                    {
                        if(this.travelState == TravelState.TAKING_FLIGHT)
                        {
                            if(posY > 255)
                            {
                                this.takeOff();
                                return;
                            }
                            double posX = this.posX;
                            double posY = this.posY;
                            double posZ = this.posZ;
                            for (int y = (int) posY; y < ((int) posY + 15); y++)
                            {
                                BlockPos p = new BlockPos(posX + 0.5, y, posZ + 0.5);
                                if (!(world.getBlockState(p).getBlock() instanceof BlockAir))
                                {
                                    this.getNavigator().tryMoveToXYZ(posX, y + 5, posZ, 0.5);
                                    return;
                                }
                            }
                            int haha= 55 + (int) ((posY * 45 / 255));
                            motionY = (haha/2) * 0.01;
                            this.getNavigator().setPath(new Path(new PathPoint[]{
                                    new PathPoint(destPos.getX(),
                                            (int) (posY + (Helpers.randomInteger(0, 100) < haha ? 1 : 0)), destPos.getZ())}
                                            ), 1.8);
                        }
                        else
                        {
                            this.getNavigator().tryMoveToXYZ(destPos.getX(), destPos.getY(), destPos.getZ(), 3);
                            if(this.destination.getPos().getDistance((int) this.posX, (int) this.posY, (int) this.posZ) < 2)
                            {
                                this.travelState = TravelState.IDLING;
                            }
                        }
                    }
                }
            }
        }
    }

    private void forgotLastLanding()
    {
        if(landDate != -1 && chunkToLoadTicket != null && this.landingPosition != null)
        {
            Helpers.log(String.format("Unloaded chunk %s due to pigeon landing! c:", this.landingPosition.toString()));
            PigeonLocation.getAround(this.landingPosition).forEach(chunkPos -> ForgeChunkManager.unforceChunk(this.chunkToLoadTicket, chunkPos));
        }
        this.landingPosition = null;
        this.landDate = -1;
        this.chunkToLoadTicket = null;
    }

    private void takeOff()
    {
        this.forgotLastLanding();
        this.travelState = TravelState.LANDING;
        Keldaria.getInstance().getPigeonTravels().takeOff(this);
    }

    public void setDestination(PigeonLocation destination)
    {
        this.travelState = TravelState.TAKING_FLIGHT;
        this.destination = destination;
    }

    public PigeonLocation getDestination()
    {
        return destination;
    }

    public void setLoc1(PigeonLocation loc1)
    {
        this.loc1 = loc1;
    }

    public PigeonLocation getLoc1()
    {
        return loc1;
    }

    public void setLoc2(PigeonLocation loc2)
    {
        this.loc2 = loc2;
    }

    public PigeonLocation getLoc2()
    {
        return loc2;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(TAMED, Boolean.FALSE);
        this.dataManager.register(LETTER, ItemStack.EMPTY);
    }

    public ItemStack getLetter()
    {
        return this.dataManager.get(LETTER);
    }

    public void setLetter(ItemStack stack)
    {
        this.dataManager.set(LETTER, stack);
    }

    public void dropLetter()
    {
        EntityItem item = new EntityItem(this.world, this.posX, this.posY + 0.25, this.posZ);
        item.setItem(this.getLetter().copy());
        this.world.spawnEntity(item);
        this.setLetter(ItemStack.EMPTY);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setTag("Letter", this.getLetter().serializeNBT());
        compound.setInteger("TravelState", this.travelState.getId());
        compound.setTag("Destination", this.destination.serializeNBT());
        compound.setTag("Location1", this.loc1.serializeNBT());
        compound.setTag("Location2", this.loc2.serializeNBT());
        compound.setBoolean("Tamed", this.isTamed());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Letter"))
        {
            this.setLetter(new ItemStack(compound.getCompoundTag("Letter")));
        }
        this.travelState = TravelState.byId(compound.getInteger("TravelState"));
        if (compound.hasKey("Destination"))
        {
            this.destination = new PigeonLocation(compound.getCompoundTag("Destination"));
        }
        if (compound.hasKey("Location1"))
        {
            this.loc1 = new PigeonLocation(compound.getCompoundTag("Location1"));
        }
        if (compound.hasKey("Location2"))
        {
            this.loc2 = new PigeonLocation(compound.getCompoundTag("Location2"));
        }
        this.setTamed(compound.getBoolean("Tamed"));

    }

    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        Predicate<EntityPlayer> avoidPredicate = input ->
        {
            boolean result1 = (input != null);
            boolean result2 = !(((EntityPlayer) input).getHeldItemMainhand().getItem() == Items.BREAD);
            return result1 && result2 && !this.isTamed();
        };
        this.tasks.addTask(2, new EntityAIAvoidEntity<EntityPlayer>(this, EntityPlayer.class, avoidPredicate, 10F, 0.8D, 1D));

        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(4, new EntityAIMate(this, 0.4F));
        this.tasks.addTask(5, new EntityAIWanderAvoidWaterFlying(this, 1.0D));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
    }

    @Override
    protected PathNavigate createNavigator(World worldIn)
    {
        PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
        pathnavigateflying.setCanOpenDoors(false);
        pathnavigateflying.setCanFloat(true);
        pathnavigateflying.setCanEnterDoors(true);
        return pathnavigateflying;
    }

    @Override
    public EntityStats getCombatStats()
    {
        return STATS;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable)
    {
        return new EntityHomingPigeon(ageable.world);
    }

    public boolean isTamed()
    {
        return this.dataManager.get(TAMED);
    }

    public void setTamed(boolean tamed)
    {
        this.dataManager.set(TAMED, tamed);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        if (world.isRemote || hand != EnumHand.MAIN_HAND)
        {
            return super.processInteract(player, hand);
        }
        ItemStack stack = player.getHeldItem(hand);
        if (world instanceof WorldServer)
        {
            if (this.isTamed())
            {
                if (player.isSneaking())
                {
                    if (this.getLetter().isEmpty())
                    {
                        this.setLetter(stack.copy());
                        player.setHeldItem(hand, ItemStack.EMPTY);
                    } else
                    {
                        this.dropLetter();
                    }
                } else
                {
                    KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketOpenHomingPigeon(this.getEntityId(), this.loc1.getName(), this.loc2.getName()), (EntityPlayerMP) player);
                }
            } else
            {
                if (EnumJob.PEASANT.has(player))
                {
                    if (stack.getItem() instanceof ItemSeeds)
                    {
                        if (Helpers.randomInteger(0, 100) < EnumAptitudes.INTELLIGENCE.getPoints(player) * 5)
                        {
                            this.setTamed(true);
                            ((WorldServer) world).spawnParticle(EnumParticleTypes.HEART, true, posX, posY, posZ, 1, 0, 0, 0, 1, new int[]{});
                        }
                        stack.shrink(1);
                    }

                }
            }
        }

        return super.processInteract(player, hand);
    }

    public static class PigeonLocation implements INBTSerializable<NBTTagCompound>
    {

        private String name = "Non-Définie";
        private BlockPos pos = BlockPos.ORIGIN;

        public PigeonLocation()
        {
        }

        public PigeonLocation(NBTTagCompound compound)
        {
            this.deserializeNBT(compound);
        }

        public PigeonLocation(String name, BlockPos pos)
        {
            this.name = name;
            this.pos = pos;
        }

        public String getName()
        {
            return name;
        }

        public BlockPos getPos()
        {
            return pos;
        }

        public ChunkPos getChunkPos()
        {
            return new ChunkPos(this.pos.getX() >> 4, this.pos.getZ() >> 4);
        }

        public static List<ChunkPos> getAround(ChunkPos center)
        {
            return Lists.newArrayList(center,
                    new ChunkPos(center.x + 1, center.z),
                    new ChunkPos( center.x + 1, center.z + 1),
                    new ChunkPos(center.x + 1, center.z - 1),
                    new ChunkPos(center.x, center.z + 1),
                    new ChunkPos(center.x, center.z - 1),
                    new ChunkPos(center.x -1, center.z -1),
                    new ChunkPos(center.x - 1, center.z + 1),
                    new ChunkPos(center.x - 1, center.z));
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setString("Name", this.name);
            compound.setString("Position", Helpers.blockPosToString(this.pos));
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            this.name = nbt.getString("Name");
            this.pos = Helpers.parseBlockPosFromString(nbt.getString("Position"));
        }

        public boolean isValid()
        {
            return this.pos.getX() != 0 && this.pos.getY() != 0 && this.pos.getZ() != 0;
        }

    }

    public void land(ChunkPos landingPosition, long landDate, ForgeChunkManager.Ticket chunkToLoadTicket)
    {
        this.landingPosition = landingPosition;
        this.landDate = landDate;
        this.chunkToLoadTicket = chunkToLoadTicket;
    }

    public enum TravelState
    {
        IDLING(0), TAKING_FLIGHT(1), LANDING(2);

        int id;

        TravelState(int id)
        {
            this.id = id;
        }

        public static TravelState byId(int id)
        {
            for (TravelState value : values())
            {
                if(value.getId() == id)
                {
                    return value;
                }
            }
            return IDLING;
        }

        public int getId()
        {
            return id;
        }
    }

    public static class PigeonTravels implements INBTSerializable<NBTTagList>
    {

        private final World world;
        private List<PigeonTravel> travels = Lists.newArrayList();

        public PigeonTravels(World world, NBTTagList list)
        {
            this.world = world;
            this.deserializeNBT(list);
        }

        public void checkAndSpawn()
        {
            long actual = System.currentTimeMillis();
            for (int i = 0; i < this.travels.size(); i++)
            {
                PigeonTravel travel = this.travels.get(i);
                if(travel.needLanding())
                {
                    if(travel.land())
                    {
                        this.travels.remove(i);
                    }
                }
            }
        }

        public void takeOff(EntityHomingPigeon pigeon)
        {
            PigeonLocation destination = pigeon.getDestination();
            if(destination.isValid())
            {
                PigeonTravel travel = new PigeonTravel(pigeon.world, pigeon, System.currentTimeMillis() + 1000);
                this.travels.add(travel);
                world.removeEntity(pigeon);
            }
        }

        @Override
        public NBTTagList serializeNBT()
        {
            NBTTagList list = new NBTTagList();
            for (PigeonTravel travel : travels)
            {
                list.appendTag(travel.serializeNBT());
            }
            return list;
        }

        @Override
        public void deserializeNBT(NBTTagList nbt)
        {
            this.travels.clear();
            for (int i = 0; i < nbt.tagCount(); i++)
            {
                NBTBase base = nbt.get(i);
                if(base instanceof NBTTagCompound)
                {
                    this.travels.add(new PigeonTravel(world, (NBTTagCompound) base));
                }
            }
        }

        public static class PigeonTravel implements INBTSerializable<NBTTagCompound>
        {

            private final World world;
            private EntityHomingPigeon pigeon;
            private PigeonLocation destination;
            private long landingDate;
            private ForgeChunkManager.Ticket ticket = null;

            public PigeonTravel(World world, NBTTagCompound compound)
            {
                this.world = world;
                this.deserializeNBT(compound);
            }

            public boolean land()
            {
                this.pigeon.isDead = false;
                this.pigeon.land(this.getDestination().getChunkPos(), this.landingDate, this.ticket);
                this.pigeon.setPosition(this.destination.getPos().getX(), 255, this.destination.getPos().getZ());
                this.world.spawnEntity(this.pigeon);
                Helpers.log(String.format("Loaded chunk %s for make the pigeon land!", this.destination.getChunkPos()));

                this. ticket = ForgeChunkManager.requestTicket(Keldaria.getInstance(), world, ForgeChunkManager.Type.NORMAL);
                PigeonLocation.getAround(this.destination.getChunkPos()).forEach(chunkPos -> ForgeChunkManager.forceChunk(this.ticket, chunkPos));
                return true;

            }

            public PigeonTravel(World world, EntityHomingPigeon pigeon, long landingDate)
            {
                this.world = world;
                this.pigeon = pigeon;
                this.destination = pigeon.getDestination();
                this.landingDate = landingDate;
            }

            public EntityHomingPigeon getPigeon()
            {
                return pigeon;
            }

            public PigeonLocation getDestination()
            {
                return destination;
            }

            public long getLandingDate()
            {
                return landingDate;
            }

            public boolean needLanding()
            {
                return System.currentTimeMillis() > getLandingDate();
            }

            @Override
            public NBTTagCompound serializeNBT()
            {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setTag("Entity", this.pigeon.serializeNBT());
                compound.setTag("Destination", this.destination.serializeNBT());
                compound.setLong("LandingDate", this.landingDate);
                return compound;
            }

            @Override
            public void deserializeNBT(NBTTagCompound nbt)
            {
                this.pigeon = new EntityHomingPigeon(this.world);
                this.pigeon.deserializeNBT(nbt.getCompoundTag("Entity"));
                this.destination = new PigeonLocation(nbt.getCompoundTag("Destination"));
                this.landingDate = nbt.getLong("LandingDate");
            }
        }
    }
}
