/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.server.chat;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.util.HashMap;
import java.util.Map;

public class SoundResistance
{

    private static final HashMap<Material, Integer> MATERIAL_RESISTANCE = Maps.newHashMap();
    private static final HashMap<SoundResistance, Integer> BLOCK_RESISTANCE = Maps.newHashMap();

    public static int getResistance(IBlockState state)
    {
        if(state.getBlock() == Blocks.AIR) return 1;
        SoundResistance toTest = new SoundResistance(state);
        for (Map.Entry<SoundResistance, Integer> resistance : BLOCK_RESISTANCE.entrySet())
        {
            if(resistance.getKey().equals(toTest))
            {
                return resistance.getValue();
            }
        }
        return getResistance(state.getMaterial());
    }

    public static int getResistance(Material mat)
    {
        return MATERIAL_RESISTANCE.getOrDefault(mat, 12);
    }

    public static void init()
    {
        MATERIAL_RESISTANCE.clear();
        BLOCK_RESISTANCE.clear();

        JsonObject obj = new JsonParser().parse(Helpers.readFileToString(Keldaria.getInstance().getFiles().SOUND_RESISTANCE)).getAsJsonObject();
        if(obj.has("materials") && obj.get("materials").isJsonObject())
        {
            JsonObject materialsObj = obj.get("materials").getAsJsonObject();
            for (Map.Entry<String, JsonElement> element : materialsObj.entrySet())
            {
                Material mat = Helpers.getMat(element.getKey());
                if(mat != null)
                {
                    MATERIAL_RESISTANCE.put(mat, element.getValue().getAsInt());
                }
            }
        }
        if(obj.has("blocks") && obj.get("blocks").isJsonObject())
        {
            JsonObject blocksObj = obj.get("blocks").getAsJsonObject();
            for (Map.Entry<String, JsonElement> element : blocksObj.entrySet())
            {
                SoundResistance resistance = new SoundResistance(element.getKey());
                if(resistance.getBlock() != null)
                {
                    BLOCK_RESISTANCE.put(resistance, element.getValue().getAsInt());
                }
            }
        }
    }

    public Block getBlock()
    {
        return block;
    }

    public int getMeta()
    {
        return meta;
    }

    public HashMap<String, Boolean> getProperties()
    {
        return properties;
    }

    private Block block;
    private int meta;
    private HashMap<String, Boolean> properties;

    public SoundResistance(Block block, int meta, HashMap<String, Boolean> properties)
    {
        this.block = block;
        this.meta = meta;
        this.properties = properties;
    }

    public SoundResistance(String key)
    {
        String[] parts = key.split("/");
        String registryName = parts[0];
        this.block = Block.getBlockFromName(registryName);
        this.meta = parts.length > 1 ? Helpers.parseOrZero(parts[1]) : -1;
        this.properties = Maps.newHashMap();
        if(parts.length > 2)
        {
            String[] propertiesParts = parts[2].split(",");
            for (String propertiesPart : propertiesParts)
            {
                String[] pP = propertiesPart.split("=");
                if(pP.length == 2)
                {
                    this.properties.put(pP[0], Helpers.parseOrFalse(pP[1]));
                }
            }
        }
    }

    public SoundResistance(IBlockState state)
    {
        this.block = state.getBlock();
        this.meta = state.getBlock().getMetaFromState(state);
        this.properties = Maps.newHashMap();
        for (IProperty<?> propertyKey : state.getPropertyKeys())
        {
            Object value =  state.getValue(propertyKey);
            if(propertyKey.getValueClass() == Boolean.class && value instanceof Boolean)
            {
                this.properties.put(propertyKey.getName(), (Boolean) value);
            }
        }
    }

    public boolean ignoreMeta()
    {
        return this.meta == -1;
    }

    public boolean equals(SoundResistance other)
    {
        boolean propertyOK = true;
        if(this.properties.size() > 0)
        {
            for (String s : other.properties.keySet())
            {
                if (this.properties.containsKey(s) && this.properties.get(s) == other.properties.get(s))
                {
                    propertyOK = true;
                } else
                {
                    propertyOK = false;
                    break;
                }
            }
        }
        return this.block == other.block && (this.ignoreMeta() || this.meta == other.meta) && propertyOK;
    }


}
