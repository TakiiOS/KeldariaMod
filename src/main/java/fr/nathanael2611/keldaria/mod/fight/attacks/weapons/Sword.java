package fr.nathanael2611.keldaria.mod.fight.attacks.weapons;

import fr.nathanael2611.keldaria.mod.fight.attacks.Attack;
import fr.nathanael2611.keldaria.mod.util.math.mine.AABB;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class Sword extends Weapon
{

    public Sword(SwingTypes type)
    {
        if(type == SwingTypes.LATERAL_RIGHT || type == SwingTypes.LATERAL_LEF)
        {
            this.parts.add(new Attack.AttackPart(new AABB(new Vec3d(-1.2, 0, 0), new Vec3d(0.5, 0.2, 0.1)), 1, true));
        }
        else if(type == SwingTypes.BY_UP_RIGHT || type == SwingTypes.BY_UP_LEFT)
        {
            this.parts.add(new Attack.AttackPart(new AABB(new Vec3d(0, 1, 0.9), new Vec3d(0.4, 0.1, 0.5)), 1, true));
        }
        //this.parts.add(new Attack.AttackPart(new AABB(new AxisAlignedBB(-0.2, -0.1, -0.1, 0.2, 0.1, 0.1).offset(0, 0.7, 0)), 1, true));
        //this.parts.add(new Attack.AttackPart(new AABB(new AxisAlignedBB(-0.2, 0.1, -0.1, 0.2, 1, 0.1).offset(0, 0.7, 0)), 3, true));
    }

    @Override
    public Attack createAttack(EntityPlayer player, float renderYawOffset, SwingTypes type)
    {
        return new Attack.Factory(this.parts).setRenderYawOffset(renderYawOffset).launch(player, type).build();
    }

}
