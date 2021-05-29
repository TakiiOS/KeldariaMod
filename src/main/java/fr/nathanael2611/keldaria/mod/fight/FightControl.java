package fr.nathanael2611.keldaria.mod.fight;

import fr.nathanael2611.keldaria.mod.fight.attacks.Attack;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketFightControl;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class FightControl<T extends Entity> implements INBTSerializable<NBTTagCompound>
{

    private Entity entity;
    private boolean fightMode = false;
    private Attack attack = null;

    public FightControl()
    {

    }

    public void setFightMode(boolean fightMode)
    {
        this.fightMode = fightMode;
        sync();
    }

    public void sync()
    {
        if(this.entity != null && !this.entity.world.isRemote)
        {
            Helpers.sendToAll(new PacketFightControl(this));
        }
    }

    public boolean isFightMode()
    {
        return this.fightMode;
    }


    public boolean isAttacking()
    {
        return attack != null;
    }

    public Attack getAttack()
    {
        return attack;
    }

    public void attack(Attack attack)
    {
        this.attack = attack;
        sync();
    }

    public void tick()
    {
        if(attack != null)
        {

            if(attack.isEnded()) attack = null;
            else attack.tick();
            //System.out.println(entity.world.);
        }
    }

    public FightControl(Entity entity)
    {
        this.entity = entity;
    }

    public Entity getEntity()
    {
        return entity;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        write(compound);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        read(nbt);
    }

    public void write(NBTTagCompound compound)
    {
        compound.setBoolean("FightMode", this.fightMode);
        if(this.attack != null)
        {
            compound.setTag("Attack", this.attack.serializeNBT());
        }
    }

    public void read(NBTTagCompound compound)
    {
        this.fightMode = compound.getBoolean("FightMode");
        if(compound.hasKey("Attack"))
        {
          this.attack = new Attack(this.entity, compound.getCompoundTag("Attack"));
        }
    }
}
