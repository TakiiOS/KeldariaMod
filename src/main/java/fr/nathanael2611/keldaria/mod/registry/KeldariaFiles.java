package fr.nathanael2611.keldaria.mod.registry;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class will be a mod file-manager
 */
public class KeldariaFiles
{

    public final File CONFIG_DIR;

    public final File DISCORD_CONFIG;
    public final File GROUPS;
    public final File PORTCULLIS_CONFIG;
    public final File SOUND_RESISTANCE;
    public final File CLIMB_SYSTEM;

    public final File ARMPOSES_FILE;



    public KeldariaFiles(File defaultConfigDirectory, Side side)
    {
        this.CONFIG_DIR = new File(defaultConfigDirectory, String.format("/%s/", Keldaria.MOD_ID));
        this.notExistsAndCreate(this.CONFIG_DIR, true);

        this.SOUND_RESISTANCE = new File(this.CONFIG_DIR, "sound_resistance.json");
        if(this.notExistsAndCreate(this.SOUND_RESISTANCE, false))
        {
            writeDefaultInFile(this.SOUND_RESISTANCE, "{\"materials\": {}, \"blocks\": {}}");
        }

        this.CLIMB_SYSTEM = new File(this.CONFIG_DIR, "climb_system.json");
        if (this.notExistsAndCreate(this.CLIMB_SYSTEM, false))
        {
            writeDefaultInFile(this.CLIMB_SYSTEM, "{}");
        }


        if(side == Side.CLIENT)
        {
            this.ARMPOSES_FILE = new File(this.CONFIG_DIR, "armposes.dat");
            if(this.notExistsAndCreate(this.ARMPOSES_FILE, false))
            {
                //writeDefaultInFile(this.ARMPOSES_FILE, );
                try
                {
                    CompressedStreamTools.write(new NBTTagCompound(), this.ARMPOSES_FILE);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            this.ARMPOSES_FILE = null;
        }

        this.PORTCULLIS_CONFIG = new File(this.CONFIG_DIR, "portcullis_config.json");
        if(this.notExistsAndCreate(this.PORTCULLIS_CONFIG, false))
        {
            writeDefaultInFile(this.PORTCULLIS_CONFIG, "[]");
        }

        if(side == Side.SERVER)
        {
            this.DISCORD_CONFIG = new File(this.CONFIG_DIR, "discord.json");
            if(this.notExistsAndCreate(this.DISCORD_CONFIG, false) || !Helpers.isValidJson(Helpers.readFileToString(this.DISCORD_CONFIG)))
            {
                writeDefaultInFile(this.DISCORD_CONFIG, "{}");
            }

            this.GROUPS = new File(this.CONFIG_DIR, "groups.json");
            if(this.notExistsAndCreate(this.GROUPS, false))
            {
                writeDefaultInFile(this.GROUPS, "{}");
            }

        } else {
            this.DISCORD_CONFIG = null;
            this.GROUPS = null;

        }

    }

    private boolean notExistsAndCreate(File file, boolean dir)
    {
        if(!file.exists())
        {
            if(dir)
            {
                file.mkdirs();
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    private void writeDefaultInFile(File file, String defaultValue)
    {
        if(!notExistsAndCreate(file, false))
        {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(defaultValue);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
