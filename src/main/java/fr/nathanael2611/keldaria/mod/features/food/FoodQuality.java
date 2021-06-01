package fr.nathanael2611.keldaria.mod.features.food;

import fr.nathanael2611.keldaria.mod.features.skill.EnumComplement;
import fr.nathanael2611.keldaria.mod.features.skill.EnumJob;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;

public enum FoodQuality
{

    LOW(0, "low", "§cMédiocre", 0.5),
    STARTER(1, "medium", "§fMoyenne", 1),
    MEDIUM(2, "normal", "§aNormale", 1.25),
    MASTER(3, "good", "§2Bonne", 1.5),
    PERFECT(4, "perfect", "§eParfaite", 2);

    private static final String KEY = "Quality";

    private int id;
    private String name;
    private String formattedName;
    private double multiplier;

    FoodQuality(int id, String name, String formattedName, double multiplier)
    {
        this.id = id;
        this.name = name;
        this.formattedName = formattedName;
        this.multiplier = multiplier;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getFormattedName()
    {
        return formattedName;
    }

    public double getMultiplier()
    {
        return multiplier;
    }

    public static FoodQuality byName(String name)
    {
        return Arrays.stream(values()).filter(issueType -> issueType.name.equalsIgnoreCase(name)).findFirst().orElse(LOW);
    }

    public static FoodQuality byId(int id)
    {
        return Arrays.stream(values()).filter(issueType -> issueType.id == id).findFirst().orElse(LOW);
    }



    public static boolean isDefined(ItemStack stack)
    {
        return Helpers.getCompoundTag(stack).hasKey(KEY);
    }

    public static FoodQuality getQuality(ItemStack stack)
    {
        return byId(Helpers.getCompoundTag(stack).getInteger(KEY));
    }

    public void set(ItemStack stack)
    {
        NBTTagCompound compound = Helpers.getCompoundTag(stack);
        compound.setInteger(KEY, this.getId());
        stack.setTagCompound(compound);
    }

    public static void init(EntityPlayer player, ItemStack stack)
    {
        if(!player.world.isRemote)
        {
            Item item = stack.getItem();
            if(item instanceof ItemFood)
            {
                EnumJob job = item instanceof ItemSeedFood ? EnumJob.PEASANT : EnumJob.COOK;
                int level = job.getLevel(player).getId();
                if(level == 0)
                {
                    System.out.println(job);
                    if(job.getEquivalent() != null && job.getEquivalent().has(player))
                    {
                        level = -1;
                    }
                }
                FoodQuality quality = null;
                switch (level)
                {
                    case -1: quality = Helpers.randomInteger(0, 100) < 20 ? STARTER : LOW; break;
                    case 1: quality = Helpers.randomInteger(0, 100) < 20 ? MEDIUM : STARTER; break;
                    case 2: quality = Helpers.randomInteger(0, 100) < 20 ? MASTER : MEDIUM; break;
                    case 3: quality = Helpers.randomInteger(0, 100) < 20 ? PERFECT : MASTER; break;
                    default: quality = LOW;
                }
                quality.set(stack);
            }
        }
    }

}
