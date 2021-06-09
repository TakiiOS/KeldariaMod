/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.debug;


import fr.nathanael2611.keldaria.mod.client.model.custom.Transforms;
import fr.nathanael2611.keldaria.mod.discord.bot.KeldaBot;
import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.nbt.*;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.zip.GZIPOutputStream;

public class Generator
{
    public static void main(String[] args)
    {

        System.out.println(KeldariaDate.getKyrgonDate().toFormattedString());

        //RotatedBB bb = new RotatedBB(new AxisAlignedBB(10, 10, 10, -10, -10, -10), 30, 30);
        //System.out.println(bb.containsPoint(0, 0, 0));

        //  File file = new File("C:\\Users\\Nathanël\\Keldaria\\Schems\\ooo\\");
        //analyse(file);


        if(true) return ;
        try
        {
            KeldaBot bot = new KeldaBot("NjM5MjM1NTk0OTYyMjA2NzMz.XdnxXg.UlmFLA2B5CXghO01jB5hMEsYrcs");
            bot.getJda().getPresence().setStatus(OnlineStatus.OFFLINE);
            bot.getJda().addEventListener(new ListenerAdapter()
            {
                @Override
                public void onReady(@Nonnull ReadyEvent event)
                {
                    System.out.println("cc");
                    super.onReady(event);
                    for (TextChannel textChannel : bot.getKeldariaGuild().getTextChannels())
                    {
                        if(textChannel.getName().contains("général"))
                        {
                            System.out.println(textChannel.getName() + ">>>>");
                            int i = 0;
                            for (Message message : textChannel.getIterableHistory())
                            {
                                if(i++ > 40) break;
                                System.out.println("   - " + message.getAuthor().getName() + "->" + message.getContentRaw());
                            }
                        }
                    }
                }
            });
        } catch (LoginException e)
        {
            e.printStackTrace();
        }


        if(true)return;
        Transforms.Transform.parse("T(10,30,20)");
        Date date = new Date();
        date.setYear(2020);
        date.setDate(24);
        date.setMonth(Calendar.DECEMBER);
        Calendar calendar = new GregorianCalendar();
        calendar.set(2021, Calendar.JANUARY, 3, 18, 30);
        System.out.println(KeldariaDate.getKyrgonDate(calendar.getTime().getTime()).toFormattedString());


    }




    public static void analyse(File dir)
    {
        for (File f : dir.listFiles())
        {
            if(f.isDirectory())
            {
                analyse(f);
            }
            else
            {
                process(f);
            }
        }
    }

    public static void process(File f)
    {



        if(f.getName().endsWith(".schematic"))
        {
            try
            {

                NBTTagCompound compound = CompressedStreamTools.readCompressed(new FileInputStream(f));
                byte[] bBlockArray = compound.getByteArray("Blocks");
                byte[] nBlockArray = new byte[bBlockArray.length];
                byte[] bDataArray = compound.getByteArray("Data");
                byte[] nDataArray = new byte[bDataArray.length];

                for (int i = 0; i < bBlockArray.length; i++)
                {
                    byte id = bBlockArray[i];
                    byte data = bDataArray[i];
                    if(id == 2)
                    {
                        id = 0;
                    }
                   // if(id == -66)
                    //{
                      //  id = 85;
                    //}
                    /*if(id == 18 || id == -68 || id == -95)
                    {
                        data = 0;
                        id = 79;
                    }
                    if(id == -94)
                    {
                        id = -94;
                        data = 13;
                    }
                    if(id == 106)
                    {
                        id = 0;
                        data = 0;
                    }*/
                    //list.set(i, new NBTTagByte(id));
                    //nList.appendTag(new NBTTagByte(id));
                    nBlockArray[i] = id;
                    nDataArray[i] = data;
                }
                compound.setByteArray("Blocks", nBlockArray);
                compound.setByteArray("Data", nDataArray);
                //CompressedStreamTools.writeCompressed(comp, new FileOutputStream(f));
                DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(f)));
                try {
                    NBTTagCompound.writeEntry("Schematic", compound, dataOutputStream);
                } finally {
                    dataOutputStream.close();
                }
                System.out.println("Succes!");
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }



}
