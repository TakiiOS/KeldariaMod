package fr.nathanael2611.keldaria.mod.features.skill;

import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.player.EntityPlayer;

public enum EnumComplement
{

    COOKING("cooking", "Cuisine", 4),
    FISHING("fishing", "Pêche", 1),
    HUNTING("hunting", "Chasse", 3),
    MUSIC("music", "Musique", 2),
    FIRST_AID("first_aid", "Premiers Soins", 5),
    MENDING("mending", "Raccommodage", 3),
    WEAPON_MASTERY("weapon_mastery", "Maîtrise d'une arme", 4),
    SURVIVAL("survival", "Survie", 2),
    NAVIGATION("navigation", "Navigation", 2),
    CLIMBING("climbing", "Escalade", 2),
    TINKERING("tinkering", "Bricolage", 4),
    PICKING("picking", "Crochetage", 4);

    private String key;
    private String name;
    private String formattedName;
    private int cost;

    EnumComplement(String name, String formattedName, int cost)
    {
        this.name = name;
        this.key = "complement:" + this.name;
        this.formattedName = formattedName;
        this.cost = cost;
    }

    public String getName()
    {
        return name;
    }

    public String getFormattedName()
    {
        return formattedName;
    }

    public int getCost()
    {
        return cost;
    }

    public boolean has(EntityPlayer player)
    {
        return player.world.isRemote ? ClientDatabases.getPersonalPlayerData().getInteger(this.key) == 1 : Databases.getPlayerData(player).getInteger(this.key) == 1;
    }

    public void set(EntityPlayer player, boolean bool)
    {
        Databases.getPlayerData(player).setInteger(this.key, bool ? 1 : 0);
    }

    public static boolean canCook(EntityPlayer player)
    {
        return COOKING.has(player) || EnumJob.COOK.has(player);
    }

    public static EnumComplement byName(String name)
    {
        for (EnumComplement value : values())
        {
            if(value.getName().equalsIgnoreCase(name)) return value;
        }
        return null;
    }

    public static int getPickingChancesPercent(EntityPlayer player)
    {
        if(PICKING.has(player))
        {
            switch (EnumAptitudes.INTELLIGENCE.getPoints(player))
            {
                case 1: return 2;
                case 2: return 5;
                case 3: return 10;
                case 4: return 15;
                case 5: return 25;
                default: return 0;
            }
        }
        return 0;
    }

    public static int getTheoricMaxComplements(EntityPlayer player)
    {
        switch (EnumAptitudes.INTELLIGENCE.getPoints(player))
        {
            case 1: return 2;
            case 2: return 3;
            case 3: return 5;
            case 4: return 7;
            case 5: return 10;
            default: return 0;
        }
    }


}
