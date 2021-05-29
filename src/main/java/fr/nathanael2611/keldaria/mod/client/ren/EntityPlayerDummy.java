package fr.nathanael2611.keldaria.mod.client.ren;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityPlayerDummy extends Entity
{
    public EntityPlayerDummy(final World worldIn)
    {
        super(worldIn);
        this.setSize(1.0f, 2.0f);
        this.ignoreFrustumCheck = true;
    }

    public void onUpdate()
    {
        final EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().player;
        this.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        super.onUpdate();
    }

    public boolean canRenderOnFire()
    {
        return false;
    }

    protected void entityInit()
    {
    }

    protected void readEntityFromNBT(final NBTTagCompound compound)
    {
    }

    protected void writeEntityToNBT(final NBTTagCompound compound)
    {
    }
}
