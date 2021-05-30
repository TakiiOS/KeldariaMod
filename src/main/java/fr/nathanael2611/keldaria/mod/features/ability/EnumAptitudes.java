/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.ability;

import fr.nathanael2611.keldaria.mod.features.cleanliness.CleanilessManager;
import fr.nathanael2611.keldaria.mod.features.combat.EnumAttackType;
import fr.nathanael2611.keldaria.mod.features.combatstats.WeaponStat;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public enum EnumAptitudes
{

    AGILITY("agility", 5, "Agilité"),
    STRENGTH("strength", 5, "Force"),
    RESISTANCE("resistance", 5, "Résistance"),
    INTELLIGENCE("intelligence", 5, "Intelligence"),
    CRAFTING("crafting", 3, "Artisanat"),
    ART("art", 2, "Art")
    ;

    private final String name;
    private final String key;
    private final String displayName;
    private final int maxPoints;

    EnumAptitudes(String name, int maxPoints, String displayName)
    {
        this.name = name;
        this.key = "ability:" + name;
        this.maxPoints = maxPoints;
        this.displayName = displayName;
    }

    public static EnumAptitudes byName(String name)
    {
        for (EnumAptitudes value : values())
        {
            if(value.name.equals(name))
            {
                return value;
            }
        }
        return null;
    }

    public String getName()
    {
        return name;
    }

    public int getMaxPoints()
    {
        return maxPoints;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void set(EntityPlayer player, int points)
    {
        int i = Math.min(points, this.maxPoints);
        Database db = Databases.getPlayerData(player);
        db.setInteger(this.key, i);
    }

    public int getPoints(EntityPlayer player)
    {
        if(player.world.isRemote)
        {
            return ClientDatabases.getPersonalPlayerData().getInteger(this.key);
        }
        else
        {
            return Databases.getPlayerData(player).getInteger(this.key);
        }
    }

    public static void updateModifiers(EntityPlayer player)
    {

        {
            int agilityPoints = AGILITY.getPoints(player);
            if(agilityPoints == 3)
            {
                player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 200, 0, false, false));
            }
            else if(agilityPoints == 4)
            {
                player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 200, 0, false, false));
            }
            else if(agilityPoints == 5)
            {
                player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 200, 1, false, false));
            }
        }
        {
            int strengthPoints = STRENGTH.getPoints(player);
            double force = 0.5 + (strengthPoints * 0.30);
            player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(force);
            double attackSpeed = 3 + (strengthPoints * 0.25);
            EnumAttackType attackType = EnumAttackType.getAttackType(player);
            if(EnumAttackType.getPreferedAttackType(player.getHeldItemMainhand()) != attackType)
            {
                attackSpeed = attackSpeed / EnumAttackType.getMultiplier(player.getHeldItemMainhand(), attackType) ;
            }
            {
                WeaponStat stat = WeaponStat.getAttackStat(player);
                if(stat != null)
                {
                    double a = (Math.max(0, stat.getLevel(player) - 10) * (attackSpeed / 2) / 10);
                    attackSpeed = (attackSpeed) + a;
                }
                player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(attackSpeed);
            }
        }
        {
            int resistancePoints = RESISTANCE.getPoints(player);
            int health = 15 + (resistancePoints * 5);

            double clean = CleanilessManager.getCleanilessValue(player);
            health -= clean < 10 ? 8 : clean < 20 ? 7 : clean < 30 ? 6 : clean < 40 ? 5 : clean < 50 ? 4 : clean < 60 ? 3 : clean < 70 ? 2 : clean < 80 ? 1 : 0;

            player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
            double kbResist = 0 + (resistancePoints * 0.15);
            player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
        }

    }

    public String getDescriptionFor(int points)
    {
        String desc = "<invalid>";
        if(this == AGILITY)
        {
            if(points == 0)
                desc = "Personnage dénué de souplesse, se déplace lentement, et court difficilement.";
            else if(points == 1)
                desc = "Pas très souple, mais suffisante pour se déplacer à vitesse normale. Peut courir mais se fatigue assez facilement.";
            else if(points == 2)
                desc = "Pas très souple, mais assez pour marcher plus rapidement que la moyenne, et par conséquent courir plus rapidement aussi. Il se fatigue toutefois moins vite que la normale.";
            else if(points == 3)
                desc = "Assez souple, marche et court plus rapidement que la moyenne. Cependant, il a aussi des capacités de saut plus fortes que la majorité des gens. Il possède une bonne endurance, et se fatigue assez difficilement.";
            else if(points == 4)
                desc = "Très souple, a de très grandes compétences en course, et une très bonne endurance de marche. Il court extrêmment vite et saute haut.";
            else if(points == 5)
                desc = "D'une souplesse extraordinaire, et d'une très grande endurance de marche. Il court extrêmmement vite, et saute bien plus haut que la moyenne. Une endurance incroyable, il est difficile de le fatiguer.";
        }
        else if(this == STRENGTH)
        {
            if(points == 0)
                desc = "Personnage maigrichon dénué de muscle, on peut dire qu'il n'a aucune force dans les bras. Il aura du mal à tenir tout type d'outil lourd, comme des haches ou même des épées. Il serait incapable de porter une armure, sauf peut être quelques pièces de maille.";
            else if(points == 1)
                desc = "Personnage faible, vraiment pas de force dans les bras, bien qu'il puisse tenir certains outils comme des pelles ou des houes, il lui sera difficile de manier l'épée ou la hache. Il pourrait difficilement porter une cotte de maille. Pourrait bander un arc.";
            else if(points == 2)
                desc = "Pas particulièrement fort, mais pas forcément faible! Il est dans la moyenne! Il saura manier tout type d'outil, et même une épée basique. Il pourrait porter une cotte de maille sans réel problème, il aura un peu plus de mal avec des pièces en fer.";
            else if(points == 3)
                desc = "Assez fort, mais pas franchement plus que la moyenne. Il pourra facilement manier tout type d'outil, et même l'épée, que ce soit légère ou lourde. Bander un arc ne lui est d'aucune difficulté, et porter une armure ne sera pas très dur pour lui.";
            else if(points == 4)
                desc = "Plus fort que la moyenne des gens, on peut dire qu'il y a peut de choses qu'il ne peut porter, et que les armures et les armes ne lui poseront pas de problème.";
            else if(points == 5)
                desc = "Personnage vraiment plus fort que la majorité des gens, il ferait un très grand combattant. Quel que soit l'arme ou l'armure, il saura facilement la maîtriser.";
        }
        else if(this == RESISTANCE)
        {
            if(points == 0)
                desc = "Faible résistance aux coups. Le corps du personnage est fragile, et pas très résistant.";
            else if(points == 1)
                desc = "Résistance aux coups normale, dans la norme.";
            else if(points == 2 || points == 3)
                desc = "Personnage plutôt résistant, il encaissera un peu mieux les coups que la moyenne.";
            else if(points == 4 || points == 5)
                desc = "Remarquable résistance aux coups, le personnage a d'ailleurs une capacité à rester en place plutôt atypique.";
        }
        else if(this == INTELLIGENCE)
        {
            if(points == 0)
                desc = "Personnage débile mental. Incapable de lire et galère en mathématiques. Emploi généralement un vocabulaire basique et minimement développé.";
            else if(points == 1)
                desc = "Personnage capable de réfléchir un minimum, bien que ne sachant pas lire, il sait compter, sans pour autant être très fort en maths. Vocabulaire banal et moyennement développé.";
            else if(points == 2)
                desc = "Personnage assez instruit, sait lire, et peut maîtriser les maths convenablement. Cultivé dans les domaines qui l'intéressent. Emploi généralement un vocabulaire assez développé.";
            else if(points == 3)
                desc = "Personnage pouvant être cutlivé dans beaucoup de domaines, sait lire, peut maîtriser les maths. Peut réfléchir relativement vite, et anticiper certaines situations. Vocabulaire large.";
            else if(points == 4)
                desc = "Personnage capable d'élaborer des stratégies, de construire des phrases développées rapidement avec un vocabulaire riche. Possibilité de compétence dans la lecture, l'écriture et les mathématiques. Si c'est un combattant, il pourra être doué pour prévoir les mouvements de ses ennemis.";
            else if(points == 5)
                desc = "Personnage très intelligent, pourrait être un grand statège, voir un manipulateur.";
        }
        else if(this == CRAFTING)
        {
            if(points == 0)
                desc = "";
            else if(points == 1)
                desc = "";
            else if(points == 2)
                desc = "";
            else if(points == 3)
                desc = "";
            else if(points == 4)
                desc = "";
            else if(points == 5)
                desc = "";
        }
        return desc;
    }

}
