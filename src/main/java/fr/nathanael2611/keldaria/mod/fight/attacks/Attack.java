package fr.nathanael2611.keldaria.mod.fight.attacks;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.features.PlayerSizes;
import fr.nathanael2611.keldaria.mod.fight.attacks.weapons.SwingTypes;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.keldaria.mod.util.math.mine.AABB;
import fr.nathanael2611.keldaria.mod.util.math.mine.Geometry;
import fr.nathanael2611.keldaria.mod.util.math.mine.OBB;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import org.joml.Matrix4d;
import org.joml.Vector3d;

import java.util.List;

public class Attack implements INBTSerializable<NBTTagCompound>
{

    private Entity caster;
    private SwingTypes swingTypes;
    private List<AttackPart> parts = Lists.newArrayList();
    private Vec3d position;
    private Vec3d rotation;

    private Vec3d trajectory = new Vec3d(0, 0, 0);
    private Vec3d roadRot = new Vec3d(0, 0, 0);
    private Vec3d maxRot;

    private Vec3d motion = new Vec3d(0, 0, 0);
    private Vec3d roadMotion = new Vec3d(0, 0, 0);
    private Vec3d maxMotion;
    private boolean ended = false;

    public Attack(Entity caster, SwingTypes type, Vec3d position, Vec3d rotation, List<AttackPart> parts)
    {
        this.caster = caster;
        this.swingTypes = type;
        this.parts = parts;
        this.position = position;
        this.rotation = rotation;
    }

    public Attack(Entity caster, NBTTagCompound compound)
    {
        this.caster = caster;
        this.deserializeNBT(compound);
        //System.out.println(serializeNBT());
    }

    public void setTrajectory(Vec3d trajectory, Vec3d maxRot)
    {
        this.trajectory = trajectory;
        this.maxRot = maxRot;
        this.maxMotion = null;
        //setMotion(this.motion, null);
    }

    public void setMotion(Vec3d motion, Vec3d maxMotion)
    {
        this.motion = motion;
        this.maxMotion = maxMotion;
        this.maxRot = null;
        //setTrajectory(this.trajectory, null);
    }

    public Vec3d getMaxRot()
    {
        return maxRot;
    }

    public Vec3d getMaxMotion()
    {
        return maxMotion;
    }

    public List<AttackPart> getParts()
    {
        List<AttackPart> parts = Lists.newArrayList();
        for (AttackPart part : this.parts)
        {
            Vec3d p = this.position;
            //vec.rotateYaw()
            Vector3d d = new Vector3d(p.x, p.y, p.z);
            d = applySizeModifications(caster, d);
            new Matrix4d()
                    .rotateY((float) Math.toRadians(-(Helpers.getYaw(this.caster))))
                    .transformPosition(d);
            AttackPart nPart = part.offset(this.caster.getPositionVector().add(d.x, d.y, d.z));
            parts.add(nPart);
        }
        return parts;
    }

    public Vec3d getPosition()
    {
        return position;
    }

    public Vec3d getRotation()
    {
        return rotation;
    }

    public SwingTypes getSwingTypes()
    {
        return swingTypes;
    }

    public Entity getCaster()
    {
        return this.caster;
    }

    public void tick()
    {
        if(!ended)
        {
            if(this.maxRot != null)
            {
                this.roadRot = this.roadRot.add(this.trajectory);

                if(Helpers.motionPassed(this.roadRot, this.maxRot))
                {
                    //this.rotation = this.maxRot;
                    ended = true;
                }
                else
                {
                    this.rotation = this.rotation.add(this.trajectory);
                }
            }
            else if(this.maxMotion != null)
            {
                this.roadMotion = this.roadMotion.add(this.motion);
                if(Helpers.motionPassed(this.roadMotion, this.maxMotion))
                {
                    //this.position = this.maxMotion;
                    ended = true;
                }
                else
                {
                    this.position = this.position.add(this.motion);
                }
            }
        }
    }

    public boolean isEnded()
    {
        return ended;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("swingType", this.swingTypes.getId());
        NBTTagList partsList = new NBTTagList();
        for (AttackPart part : this.parts)
        {
            partsList.appendTag(part.serializeNBT());
        }
        compound.setTag("parts", partsList);
        compound.setTag("position", Helpers.serializeVec3d(this.position));
        compound.setTag("rotation", Helpers.serializeVec3d(this.rotation));
        compound.setTag("trajectory", Helpers.serializeVec3d(this.trajectory));
        compound.setTag("maxRot", Helpers.serializeVec3d(this.maxRot));
        compound.setTag("motion", Helpers.serializeVec3d(this.motion));
        compound.setTag("maxMotion", Helpers.serializeVec3d(this.maxMotion));
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.parts = Lists.newArrayList();
        this.swingTypes = SwingTypes.byId(nbt.getInteger("swingType"));
        NBTTagList list = nbt.getTagList("parts", Constants.NBT.TAG_COMPOUND);
        for (NBTBase nbtBase : list)
        {
            if(nbtBase instanceof NBTTagCompound)
            {
                this.parts.add(new AttackPart(this, (NBTTagCompound) nbtBase));
            }
        }
        this.position = Helpers.deserialize(nbt.getCompoundTag("position"));
        this.rotation = Helpers.deserialize(nbt.getCompoundTag("rotation"));
        this.trajectory = Helpers.deserialize(nbt.getCompoundTag("trajectory"));
        this.maxRot = Helpers.deserialize(nbt.getCompoundTag("maxRot"));
        this.motion = Helpers.deserialize(nbt.getCompoundTag("motion"));
        this.maxMotion = Helpers.deserialize(nbt.getCompoundTag("maxMotion"));

    }

    public static class AttackPart implements INBTSerializable<NBTTagCompound>
    {
        private Attack parent;
        private AABB bb;
        private double damages;
        private boolean stopAttack;
        private SwingTypes type;

        public AttackPart(Attack parent, NBTTagCompound compound)
        {
            this.parent = parent;
            this.deserializeNBT(compound);
        }

        public AttackPart(AABB bb, double damages, boolean stopAttack)
        {
            this(null, bb, damages, stopAttack);
        }

        public AttackPart(Attack parent, AABB bb, double damages, boolean stopAttack)
        {
            this.parent = parent;
            this.bb = bb;
            this.damages = damages;
            this.stopAttack = stopAttack;
        }

        public AttackPart withParent(Attack parent)
        {
            return new AttackPart(parent, this.bb, this.damages, this.stopAttack);
        }

        public Attack getParent()
        {
            return parent;
        }

        public boolean isStopAttack()
        {
            return stopAttack;
        }

        public double getDamages()
        {
            return damages;
        }


        public SwingTypes getType()
        {
            return type;
        }

        public void setType(SwingTypes type)
        {
            this.type = type;
        }

        public OBB getOBB(Attack parent)
        {
            if(parent != null)
            {

                //return new OBB(this.bb, parent.rotation.x, parent.rotation.y, parent.rotation.z, parent.caster.getPositionVector().add((parent.position.rotateYaw((float) Math.toRadians(-(Helpers.getYaw(parent.caster)))))));
                Vector3d d = new Vector3d(parent.position.x, parent.position.y, parent.position.z);

                d = applySizeModifications(parent.caster, d);

                new Matrix4d()
                        .rotateY((float) Math.toRadians(-(Helpers.getYaw(parent.caster))))
                        .transformPosition(d);
                return new OBB(this.bb, parent.rotation.x, parent.rotation.y, parent.rotation.z, parent.caster.getPositionVector().add(d.x, d.y, d.z));
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("aabb", this.bb.serializeNBT());
            compound.setDouble("damages", this.damages);
            compound.setBoolean("stopAttack", this.stopAttack);
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            this.bb = new AABB();
            this.bb.deserializeNBT(nbt.getCompoundTag("aabb"));
            this.damages = nbt.getDouble("damages");
            this.stopAttack = nbt.getBoolean("stopAttack");
        }

        public AttackPart offset(Vec3d pos)
        {
            AttackPart part = new AttackPart(this.parent, this.serializeNBT());
            part.bb = part.bb.offset(pos);
            return part;
        }
    }

    public static class Factory
    {

        private List<AttackPart> parts;
        private EntityPlayer player;
        private SwingTypes type;
        private float renderYawOffset = -1;

        public Factory(List<AttackPart> parts)
        {
            this.parts = parts;
        }

        public Factory setRenderYawOffset(float renderYawOffset)
        {
            this.renderYawOffset = renderYawOffset;
            return this;
        }

        public float getRenderYawOffset()
        {
            if(this.renderYawOffset == -1 )
            {
                return this.player.renderYawOffset;
            }
            return this.renderYawOffset;
        }

        public Factory launch(EntityPlayer player, SwingTypes type)
        {
            this.player = player;
            this.type = type;
            return this;
        }

        public Attack build()
        {
            if(this.type == SwingTypes.LATERAL_RIGHT)
            {
                Attack attack = new Attack(this.player, this.type, new Vec3d(-0.30, (1.1), 0), new Vec3d(
                        0, -Helpers.getYaw(player)- 40, 0), this.parts);
                attack.setTrajectory(new Vec3d(0, 7.5, 0), new Vec3d(0, 180, 0));
                return attack;
            }
            else if(this.type == SwingTypes.LATERAL_LEF)
            {
                Attack attack = new Attack(this.player, this.type, new Vec3d(-0.30, (1.1), 0), new Vec3d(
                        0, -Helpers.getYaw(player)- 180 - 40, 0), this.parts);
                attack.setTrajectory(new Vec3d(0, -7.5, 0), new Vec3d(0, -180, 0));
                return attack;
            }
            else if(this.type == SwingTypes.BY_UP_RIGHT)
            {
                Attack attack = new Attack(this.player, this.type, new Vec3d(-0.30, (1.8), 0), new Vec3d(
                        0, -Helpers.getYaw(player), 0), this.parts);
                //attack.setTrajectory(new Vec3d(7.5, 0, 0), new Vec3d(180, 0, 0));
                attack.setMotion(new Vec3d(0, -0.075, 0), new Vec3d(0, -2, 0));
                return attack;
            }
            return new Attack(this.player, SwingTypes.NONE, new Vec3d(0, 0, 0), new Vec3d(0, 0, 0), this.parts);
        }

    }

    public static Vector3d applySizeModifications(Entity p, Vector3d d)
    {
        if(p instanceof EntityPlayer)
        {

            double scale = PlayerSizes.get((EntityPlayer) p);

            EntityPlayer player = (EntityPlayer) p;
            d.mul(scale, scale, scale);
            if(p.isSneaking())
            {
                d.sub(0, 0.35 * PlayerSizes.get(player), 0);
            }
        }
        return d;
    }

}
