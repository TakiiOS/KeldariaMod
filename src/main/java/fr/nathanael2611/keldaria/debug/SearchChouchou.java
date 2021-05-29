package fr.nathanael2611.keldaria.debug;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.*;

public class SearchChouchou
{

    public static void main(String[] args)
    {
        StringBuilder b = new StringBuilder();
        File dir = new File("C:\\Users\\Nathanël\\Desktop\\cc\\");


        for (File file : dir.listFiles())
        {
            //RegionFile regionFile = new RegionFile(file);

            try
            {
                NBTTagCompound nbttagcompound = CompressedStreamTools.read(new DataInputStream(new FileInputStream(file)));
                b.append(nbttagcompound.toString());
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            for (int i = 0; i < 32; ++i)
            {
                for (int j = 0; j < 32; ++j)
                {

                    /*try
                    {
                        DataInputStream datainputstream = regionFile.getChunkDataInputStream(i, j);

                        NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);
                        //System.out.println(nbttagcompound.toString() + "\n \n");

                        NBTTagCompound compound = nbttagcompound.getCompoundTag("Level");

                        if(!compound.getTagList("Entities", Constants.NBT.TAG_COMPOUND).isEmpty())
                        {
                            String str = compound.getTagList("Entities", Constants.NBT.TAG_COMPOUND).toString();
                            if(str.contains("iceandfire"))
                            {
                                b.append(str);
                            }

                        }
                    } catch (Exception e)
                    {

                    }*/

                }
            }
        }
        File f = new File("C:\\Users\\Nathanël\\Desktop\\cc.txt");
        FileWriter writer = null;
        try
        {
            writer = new FileWriter(f);
            writer.write(b.toString());
            writer.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
