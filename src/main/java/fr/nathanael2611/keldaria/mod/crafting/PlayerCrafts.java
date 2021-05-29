package fr.nathanael2611.keldaria.mod.crafting;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.crafting.storage.KnownRecipesStorage;
import fr.nathanael2611.keldaria.mod.crafting.storage.api.IKnownRecipes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public class PlayerCrafts implements INBTSerializable<NBTTagCompound>
{

    private String managerName;
    private List<CraftEntry> entries = Lists.newArrayList();

    public PlayerCrafts(String managerName)
    {
        this.managerName = managerName;
    }

    public PlayerCrafts()
    {
    }

    public static PlayerCrafts retrieve(CraftManager manager, EntityPlayerMP player)
    {
        return retrieve(manager, player, true);
    }

    public static PlayerCrafts retrieve(CraftManager manager, EntityPlayerMP player, boolean retrieveLookedRecipes)
    {
        PlayerCrafts crafts = new PlayerCrafts(manager.getName());
        IKnownRecipes recipes = CraftManager.getRecipes(player);

        List<String> lookedRecipes = retrieveLookedRecipes ? getLookedRecipes(player) : Lists.newArrayList();

        manager.getCraftEntries().forEach((key, entry) -> {
            if(lookedRecipes.contains(key) || key.startsWith(manager.getName() + "/default/") || recipes.canCraft(key))
            {
                crafts.addCraft(entry);
            }
        });

        return crafts;
    }

    public static List<String> getLookedRecipes(EntityPlayerMP player)
    {
        ItemStack stack = player.getHeldItemOffhand();
        if(stack.hasCapability(KnownRecipesStorage.CAPABILITY_KNOWN_RECIPES, null))
        {
            IKnownRecipes recipes = stack.getCapability(KnownRecipesStorage.CAPABILITY_KNOWN_RECIPES, null);
            if(recipes != null)
                return recipes.getKnownKeys();
        }
        return Lists.newArrayList();
    }

    public void addCraft(CraftEntry entry)
    {
        this.entries.add(entry);
    }

    public List<CraftEntry> getEntries()
    {
        return entries;
    }

    public String getManagerName()
    {
        return managerName;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("Manager", this.managerName);
        NBTTagList list = new NBTTagList();
        entries.forEach((entry) -> list.appendTag(entry.serializeNBT()));
        compound.setTag("Entries", list);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.managerName = nbt.getString("Manager");
        NBTTagList list = nbt.getTagList("Entries", Constants.NBT.TAG_COMPOUND);
        list.forEach(nbtBase ->
        {
            if (nbtBase instanceof NBTTagCompound)
            {
                CraftEntry entry = new CraftEntry();
                entry.deserializeNBT((NBTTagCompound) nbtBase);
                this.entries.add(entry);
            }
        });
    }

    public boolean contains(String key)
    {
        for (CraftEntry entry : this.entries)
        {
            if(entry.getKey().equalsIgnoreCase(key)) return true;
        }
        return false;
    }



}
