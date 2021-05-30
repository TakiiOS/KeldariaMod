/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.features.armoposes.Arms;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class ArmPosesStorage implements INBTSerializable<NBTTagCompound>
{

    public static ArmPosesStorage get()
    {
        try
        {
            return new ArmPosesStorage(CompressedStreamTools.readCompressed(new FileInputStream(Keldaria.getInstance().getFiles().ARMPOSES_FILE)));
        } catch (IOException e)
        {
            e.printStackTrace();
            return new ArmPosesStorage(new NBTTagCompound());
        }
    }

    public static void write(ArmPosesStorage storage)
    {
        try
        {
            CompressedStreamTools.writeCompressed(storage.serializeNBT(), new FileOutputStream(Keldaria.getInstance().getFiles().ARMPOSES_FILE));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private final HashMap<String, Arms> ARMS = Maps.newHashMap();

    public Collection<Arms> getArms()
    {
        return ARMS.values();
    }

    public ArmPosesStorage(NBTTagCompound compound)
    {
        deserializeNBT(compound);
    }

    public void remove(String name)
    {
        this.ARMS.remove(name);
        write(this);
    }

    public void remove(Arms arms)
    {
        this.remove(arms.getName());
    }

    public void add(Arms arms)
    {
        this.ARMS.put(arms.getName(), arms);
        write(this);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        ARMS.forEach((name, arms) -> {
            compound.setTag(name, arms.serializeNBT());
        });
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.ARMS.clear();
        for (String s : nbt.getKeySet())
        {
            NBTBase baseTag = nbt.getTag(s);
            if(baseTag instanceof NBTTagCompound)
            {
                Arms arms = new Arms((NBTTagCompound) baseTag);
                this.ARMS.put(s, arms);
            }
        }
    }
}
