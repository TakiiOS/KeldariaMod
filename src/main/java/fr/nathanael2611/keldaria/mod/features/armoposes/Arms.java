/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.armoposes;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class Arms implements INBTSerializable<NBTTagCompound>
{

    private String name;
    private ArmPose rightArm;
    private ArmPose leftArm;

    public Arms(String name, ArmPose rightArm, ArmPose leftArm)
    {
        this.name = name;
        this.rightArm = rightArm;
        this.leftArm = leftArm;
    }

    public Arms()
    {
        this.name = "";
        this.rightArm = new ArmPose();
        this.leftArm = new ArmPose();
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Arms(NBTTagCompound compound)
    {
        this.deserializeNBT(compound);
    }

    public String getName()
    {
        return name;
    }

    public ArmPose getLeftArm()
    {
        return leftArm;
    }

    public ArmPose getRightArm()
    {
        return rightArm;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("Name", this.name);
        nbt.setTag("RightArm", this.rightArm.serializeNBT());
        nbt.setTag("LeftArm", this.leftArm.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.name = nbt.getString("Name");
        this.rightArm = new ArmPose(nbt.getCompoundTag("RightArm"));
        this.leftArm = new ArmPose(nbt.getCompoundTag("LeftArm"));
    }

    public static class ArmPose implements INBTSerializable<NBTTagCompound>
    {
        private float rotX;
        private float rotY;
        private float rotZ;

        public ArmPose(float rotX, float rotY, float rotZ)
        {
            this.rotX = rotX;
            this.rotY = rotY;
            this.rotZ = rotZ;
        }

        public ArmPose()
        {
        }

        public ArmPose(NBTTagCompound compound)
        {
            this.deserializeNBT(compound);
        }

        public void apply(EntityPlayer player, ModelRenderer renderer, boolean right)
        {
            if(this.rotX != 0) renderer.rotateAngleX = this.rotX + (player.isSneaking() ? 0.5f : 0);
            if(this.rotY != 0) renderer.rotateAngleY = this.rotY;
            renderer.rotationPointY *= 1.2;
            if(this.rotZ != 0) renderer.rotateAngleZ = this.rotZ + (player.isSneaking() ? right ? -0.2f : 0.2f : 0);
        }

        public float getRotX()
        {
            return rotX;
        }

        public float getRotY()
        {
            return rotY;
        }

        public float getRotZ()
        {
            return rotZ;
        }

        public void setRotZ(float rotZ)
        {
            this.rotZ = rotZ;
        }

        public void setRotY(float rotY)
        {
            this.rotY = rotY;
        }

        public void setRotX(float rotX)
        {
            this.rotX = rotX;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setFloat("RotX", this.rotX);
            nbt.setFloat("RotY", this.rotY);
            nbt.setFloat("RotZ", this.rotZ);
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            this.rotX = nbt.getFloat("RotX");
            this.rotY = nbt.getFloat("RotY");
            this.rotZ = nbt.getFloat("RotZ");
        }
    }

}
