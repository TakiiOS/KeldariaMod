/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.util.math.mine;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

public class OBB implements INBTSerializable<NBTTagCompound>
{

    public Vec3d center;
    public Vec3d size;
    public Vec3d rotCenter = new Vec3d(0, 0, 0);
    public Matrix4d orientation;

    public OBB(AABB box, double roll, double yaw, double pitch, Vec3d rotCenter)
    {
        this.center = box.origin;
        this.size = box.size;
        this.rotCenter = rotCenter;
        this.setOrientation(roll, yaw, pitch);

    }

    public void orient(Vec3d orientation)
    {
        this.orientation = new Matrix4d().translate(rotCenter.x, rotCenter.y, rotCenter.z)
            .rotateTowards(new Vector3d(orientation.x, orientation.y, orientation.z), new Vector3d())
            .translate(-rotCenter.x, -rotCenter.y, -rotCenter.z);
    }

    public void setOrientation(double roll, double yaw, double pitch)
    {
        this.orientation = new Matrix4d().translate(rotCenter.x, rotCenter.y, rotCenter.z)
                .rotateXYZ(Math.toRadians(roll), Math.toRadians(yaw), Math.toRadians(pitch))
                .translate(-rotCenter.x, -rotCenter.y, -rotCenter.z);
    }


    public OBB()
    {
        this(new Vec3d(1, 1, 1), new Vec3d(0, 0, 0));
    }

    public OBB(Vec3d center, Vec3d size)
    {
        this(center, size, new Matrix4d());
    }


    public OBB(Vec3d center, Vec3d size, Matrix4d orientation)
    {
        this.size = size;
        this.orientation = orientation;
        this.center = center;
    }

    public boolean containsPoint(Vec3d point)
    {
        return Geometry.isPointInOBB(point, this);
    }


    @SideOnly(Side.CLIENT)
    public void draw(float partialTicks, boolean red) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(16);
        Geometry.store(buffer, this.orientation);
        GL11.glMultMatrix(buffer);

        Vec3d vec = this.size;
        float maxX = (float) (this.center.x + vec.x);
        float maxY = (float) (this.center.y + vec.y);
        float maxZ = (float) (this.center.z + vec.z);
        float minX = (float) (this.center.x - vec.x);
        float minY = (float) (this.center.y - vec.y);
        float minZ = (float) (this.center.z - vec.z);
        float color = red ? 0.0F : 1.0F;

        bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(maxX, maxY, maxZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(1.0F, color, color, 1.0F).endVertex();

        bufferBuilder.pos(maxX, minY, maxZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(1.0F, color, color, 1.0F).endVertex();

        bufferBuilder.pos(minX, minY, maxZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(1.0F, color, color, 1.0F).endVertex();

        bufferBuilder.pos(minX, maxY, maxZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(1.0F, color, color, 1.0F).endVertex();

        bufferBuilder.pos(minX, maxY, minZ).color(1.0F, color, color, 1.0F).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(1.0F, color, color, 1.0F).endVertex();

        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("center", Helpers.serializeVec3d(this.center));
        compound.setTag("size", Helpers.serializeVec3d(this.size));
        compound.setTag("rotCenter", Helpers.serializeVec3d(this.rotCenter));
        compound.setTag("orientation", this.orientation.serializeNBT());
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.center = Helpers.deserialize(nbt.getCompoundTag("center"));
        this.size = Helpers.deserialize(nbt.getCompoundTag("size"));
        this.rotCenter = Helpers.deserialize(nbt.getCompoundTag("rotCenter"));
        this.orientation = new Matrix4d();
        this.deserializeNBT(nbt.getCompoundTag("orientation"));
    }
}
