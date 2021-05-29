package fr.nathanael2611.keldaria.mod.crafting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.crafting.storage.api.IKnownRecipes;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CraftEntry implements INBTSerializable<NBTTagCompound>
{

    private String key;
    private HashMap<CraftIngredient, Integer> ingredients = Maps.newHashMap();
    private ItemStack result;
    private int toolDamage;
    private String description;
    private HashMap<EnumAptitudes, Integer> requiredAptitudes = Maps.newHashMap();
    private List<PossibleWith> skillPossibilities = Lists.newArrayList();

    public CraftEntry(String key, HashMap<EnumAptitudes, Integer> requiredAptitudes, HashMap<CraftIngredient, Integer> ingredients, ItemStack result, int toolDamage, String description, List<PossibleWith> skillPossibilities)
    {
        this.key = key;
        this.ingredients = ingredients;
        this.result = result;
        this.toolDamage = toolDamage;
        this.description = description;
        this.requiredAptitudes = requiredAptitudes;
        this.skillPossibilities = skillPossibilities;
    }

    public CraftEntry()
    {
        this.ingredients = Maps.newHashMap();
    }

    public String getKey()
    {
        return key;
    }

    public HashMap<EnumAptitudes, Integer> getRequiredAptitudes()
    {
        return requiredAptitudes;
    }

    public int getToolDamage()
    {
        return toolDamage;
    }

    public HashMap<CraftIngredient, Integer> getIngredients()
    {
        return ingredients;
    }

    public ItemStack getResult()
    {
        return result.copy();
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        StringBuilder ingredientsList = new StringBuilder();
        this.ingredients.forEach((item, size) ->
        {
            ingredientsList.append(item.getItem().getRegistryName()).append("=").append(item.getMeta()).append("/").append(size).append(",");
        });
        compound.setString("Key", this.key);
        compound.setString("Ingredients", ingredientsList.toString().substring(0, ingredientsList.toString().length() - 1));
        compound.setTag("Result", this.result.serializeNBT());
        compound.setInteger("ToolDamage", this.toolDamage);
        compound.setString("Description", this.description);
        StringBuilder requiredAptitudesList = new StringBuilder();
        this.requiredAptitudes.forEach((aptitude, level) ->
        {
            requiredAptitudesList.append(aptitude.getName()).append("=").append(level).append("/");
        });
        compound.setString("RequiredAptitudes", requiredAptitudesList.toString().substring(0, requiredAptitudesList.toString().length() - 1));
        NBTTagList list = new NBTTagList();
        for (PossibleWith skillPossibility : this.skillPossibilities)
        {
            list.appendTag(skillPossibility.serializeNBT());
        }
        compound.setTag("PossibleSkills", list);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound)
    {
        String[] ingredientsString = compound.getString("Ingredients").split(",");
        HashMap<CraftIngredient, Integer> ingredients = Maps.newHashMap();
        for (String s : ingredientsString)
        {
            Item item = Item.REGISTRY.getObject(new ResourceLocation(s.split("/")[0].split("=")[0]));
            int meta = Integer.parseInt(s.split("/")[0].split("=")[1]);
            ingredients.put(new CraftIngredient(item, meta), Integer.parseInt(s.split("/")[1]));
        }
        this.ingredients = ingredients;
        this.key = compound.getString("Key");
        this.result = new ItemStack(compound.getCompoundTag("Result"));
        this.toolDamage = compound.getInteger("ToolDamage");
        this.description = compound.getString("Description");
        String[] requiredAptitudesString = compound.getString("RequiredAptitudes").split("/");
        HashMap<EnumAptitudes, Integer> requiredAptitudesMap = Maps.newHashMap();
        for (String s : requiredAptitudesString)
        {
            String[] parts = s.split("=");
            EnumAptitudes aptitude = EnumAptitudes.byName(parts[0]);
            int level = Integer.parseInt(parts[1]);
            if (aptitude != null)
                requiredAptitudesMap.put(aptitude, level);
        }
        this.requiredAptitudes = requiredAptitudesMap;
        this.skillPossibilities = Lists.newArrayList();
        NBTTagList list = compound.getTagList("PossibleSkills", Constants.NBT.TAG_COMPOUND);
        for (NBTBase nbtBase : list)
        {
            if (nbtBase instanceof NBTTagCompound)
            {
                PossibleWith with = new PossibleWith((NBTTagCompound) nbtBase);
                this.skillPossibilities.add(with);
            }
        }
    }

    public List<PossibleWith> getSkillPossibilities()
    {
        return skillPossibilities;
    }

    public boolean haveIngredients(EntityPlayer player)
    {
        AtomicBoolean can = new AtomicBoolean(true);
        this.ingredients.forEach(((item, integer) ->
        {
            if (Helpers.countItemInInventory(player, item.getItem(), item.getMeta()) < integer)
            {
                can.set(false);
            }
        }));
        return can.get();
    }

    public boolean haveAptitudes(EntityPlayer player)
    {
        for (EnumAptitudes aptitude : this.requiredAptitudes.keySet())
        {
            int playerLevel = aptitude.getPoints(player);
            int requiredLevel = this.requiredAptitudes.get(aptitude);
            if (playerLevel < requiredLevel)
            {
                return false;
            }
        }
        return true;
    }

    public boolean haveSkills(EntityPlayer player)
    {
        for (PossibleWith skillPossibility : this.getSkillPossibilities())
        {
            if(skillPossibility.isPossible(player))
            {
                return true;
            }
        }
        return this.getSkillPossibilities().isEmpty();
    }

    public boolean canCraft(EntityPlayer player)
    {
        return this.haveAptitudes(player) && this.haveIngredients(player) && this.haveSkills(player);
    }

    public boolean isNative(IKnownRecipes recipes)
    {
        return recipes.canCraft(this.key);
    }

}
