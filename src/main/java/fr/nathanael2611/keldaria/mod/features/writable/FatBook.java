package fr.nathanael2611.keldaria.mod.features.writable;

import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketUpdateFatBookPages;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public class FatBook implements INBTSerializable<NBTTagCompound>
{

    private String title = "";
    private boolean isSigned;
    private NonNullList<String> pages = NonNullList.withSize(100, "");

    public int getMaxBookPages()
    {
        return pages.size();
    }

    public void setPage(int page, String text)
    {
        pages.set(page, text);
    }

    public String getPage(int page)
    {
        return pages.get(page).replace("&", "§");
    }

    public boolean isSigned()
    {
        return isSigned;
    }

    public String getTitle()
    {
        return title;
    }

    public void sign(String title)
    {
        this.isSigned = true;
        this.title = title;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("Title", this.title);
        compound.setBoolean("IsSigned", this.isSigned);
        NBTTagList list = new NBTTagList();
        for (String page : this.pages)
        {
            list.appendTag(new NBTTagString(page));
        }
        compound.setTag("Pages", list);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.title = nbt.hasKey("Title", Constants.NBT.TAG_STRING) ? nbt.getString("Title") : "";
        this.isSigned = nbt.getBoolean("IsSigned");
        NBTTagList list = nbt.getTagList("Pages", Constants.NBT.TAG_STRING);
        for (int i = 0; i < list.tagCount(); i++)
        {
            if(list.get(i) instanceof NBTTagString)
            {
                this.pages.set(i, ((NBTTagString) list.get(i)).getString());
            }
        }
    }

    /* Only use on client */
    public void updateToServer()
    {
        for (int i = 0; i < 10; i++)
        {
            NBTTagCompound pages = new NBTTagCompound();
            for (int k = 0; k < 10; k++)
            {
                pages.setString(i * 10 + k + "", this.getPage(i * 10 + k));
            }
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketUpdateFatBookPages(pages));
        }
    }

    public void incorporePages(NBTTagCompound compound)
    {
        for (String s : compound.getKeySet())
        {
            try
            {
                int i = Integer.parseInt(s);
                if(i >= 0 && i < this.getMaxBookPages())
                {
                    this.pages.set(i, compound.getString(s));
                }
            } catch(Exception ignored) {}
        }
    }
}
