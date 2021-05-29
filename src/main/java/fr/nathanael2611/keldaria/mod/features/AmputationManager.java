package fr.nathanael2611.keldaria.mod.features;

import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.DatabaseReadOnly;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.player.EntityPlayer;

public class AmputationManager
{

    public enum Member
    {
        HEAD("head"), RIGHT_ARM("right_arm"), LEFT_ARM("left_arm"), RIGHT_LEG("right_leg"), LEFT_LEG("left_leg");

        String name;

        Member(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        public String toKey(EntityPlayer player)
        {
            return player.getName() + ".member." + this.name + ".isAmputated";
        }
    }

    public static void amputate(EntityPlayer player, Member member)
    {
        Databases.getDatabase("amputation").setInteger(member.toKey(player), 1);
    }

    public static void graft(EntityPlayer player, Member member)
    {
        Databases.getDatabase("amputation").setInteger(member.toKey(player), 0);
    }

    public static boolean isAmputated(EntityPlayer player, Member member)
    {
        DatabaseReadOnly readOnly = player.world.isRemote ? ClientDatabases.getDatabase("amputation") : Databases.getDatabase("amputation");
        return readOnly.getInteger(member.toKey(player)) == 1;
    }

}
