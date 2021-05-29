package fr.nathanael2611.keldaria.mod.crafting.storage;

import fr.nathanael2611.keldaria.mod.crafting.storage.api.IKnownRecipes;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class KnownRecipesStorage implements Capability.IStorage<IKnownRecipes>
{

    @CapabilityInject(IKnownRecipes.class)
    public static final Capability<IKnownRecipes> CAPABILITY_KNOWN_RECIPES = null;

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IKnownRecipes> capability, IKnownRecipes recipes, EnumFacing side)
    {
        NBTTagList compound = new NBTTagList();
        for (String knownKey : recipes.getKnownKeys())
        {
            compound.appendTag(new NBTTagString(knownKey));
        }
        return compound;
    }

    @Override
    public void readNBT(Capability<IKnownRecipes> capability, IKnownRecipes snowManager, EnumFacing side, NBTBase nbt)
    {
        if(nbt instanceof NBTTagList)
        {
            NBTTagList list = (NBTTagList) nbt;
            for (NBTBase nbtBase : list)
            {
                if(nbtBase instanceof NBTTagString)
                {
                    snowManager.discover(((NBTTagString) nbtBase).getString());
                }
            }
        }
    }
}
