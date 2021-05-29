package fr.nathanael2611.keldaria.mod.tileentity;

import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketOpenGui;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class TileEntityHRPSign extends TileEntity
{

    private String text = "";
    private int color = Color.WHITE.getRGB();

    public TileEntityHRPSign()
    {
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity packet)
    {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasKey("Text")) this.text = compound.getString("Text");
        if (compound.hasKey("Color")) this.color = compound.getInteger("Color");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setString("Text", this.text);
        compound.setInteger("Color", this.color);
        return compound;
    }

    public void updateText(String text, int color)
    {
        this.text = text;
        this.color = color;
        this.markDirty();
        IBlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 3);


    }

    public void openEditor(EntityPlayer player, BlockPos signPos)
    {
        if(!player.world.isRemote)
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketOpenGui(PacketOpenGui.HRP_SIGN_EDIT, Helpers.blockPosToString(signPos)), Helpers.getPlayerMP(player));
        }
    }

    public String getText()
    {
        return text;
    }

    public int getColor()
    {
        return color;
    }
}
