package fr.nathanael2611.keldaria.debug;

import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.chunk.storage.RegionFile;
import net.minecraftforge.common.util.Constants;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

public class SearcherTest
{


    public static void main(String[] args)
    {

        if(true)
        {
            for(; ;)
            {
                System.out.println(KeldariaDate.getKyrgonDate().toFormattedString());
            }
        }

        File file = new File("C:\\Users\\Nathanël\\Documents\\r.-9.7.mca");
        try
        {
            //NBTTagCompound compound = CompressedStreamTools.read(file);
            RegionFile regionFile = new RegionFile(file);
            for (int i = 0; i < 32; ++i)
            {
                for (int j = 0; j < 32; ++j)
                {

                    DataInputStream datainputstream = regionFile.getChunkDataInputStream(i, j);

                    NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);
                    //System.out.println(nbttagcompound.toString() + "\n \n");

                    NBTTagCompound compound = nbttagcompound.getCompoundTag("Level");


                    compound.removeTag("HeightMap");
                    compound.removeTag("Sections");
                    compound.removeTag("Biomes");
                    compound.removeTag("InhabitedTime");
                    compound.removeTag("LastUpdate");
                    compound.removeTag("LightPopulated");
                    compound.removeTag("zPos");
                    compound.removeTag("xPos");
                    compound.removeTag("Entities");
                    compound.removeTag("TerrainPopulated");
                    //System.out.println(compound.toString());
                    NBTTagList list = compound.getTagList("TileEntities", Constants.NBT.TAG_COMPOUND);
                    if(list.tagCount() > 0)
                    {
                        int finalJ = j;
                        int finalI = i;
                        list.forEach(nbtBase -> {
                            if(nbtBase instanceof NBTTagCompound)
                            {
                                NBTTagCompound letrucquivanoussauverlavie = (NBTTagCompound) nbtBase;
                                String id = letrucquivanoussauverlavie.getString("id");
                                NBTTagList itemsList = letrucquivanoussauverlavie.getTagList("Items", Constants.NBT.TAG_COMPOUND);
                                if(id.contains("crate") && itemsList.tagCount() > 0)
                                {
                                    if(itemsList.toString().contains("crate"))
                                    {
                                        letrucquivanoussauverlavie.removeTag("Items");

                                        System.out.println(letrucquivanoussauverlavie + "  |  " + finalI + " | " + finalJ);
                                    }
                                }
                            }
                        });
                    }
                }
            }
            //System.out.println(compound.toString());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}
