package fr.kerlann.tmt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public abstract class Model<T> extends ModelBase
{
    public static final Model<Object> EMPTY = new Model()
    {
        public void render() {}

        public void render(Object type, Entity element) {}

        public void translateAll(float x, float y, float z) {}
    };

    public abstract void render();

    public void render(ModelRendererTurbo[] model)
    {
        for (ModelRendererTurbo sub : model) {
            sub.render();
        }
    }

    protected void translate(ModelRendererTurbo[] model, float x, float y, float z)
    {
        for (ModelRendererTurbo mod : model)
        {
            mod.rotationPointX += x;
            mod.rotationPointY += y;
            mod.rotationPointZ += z;
        }
    }

    public abstract void translateAll(float paramFloat1, float paramFloat2, float paramFloat3);

    protected void rotate(ModelRendererTurbo[] model, float x, float y, float z)
    {
        for (ModelRendererTurbo mod : model)
        {
            mod.rotateAngleX += x;
            mod.rotateAngleY += y;
            mod.rotateAngleZ += z;
        }
    }


    protected void flip(ModelRendererTurbo[] model)
    {
        fixRotations(model);
    }

    public void flipAll() {}

    public void fixRotations(ModelRendererTurbo[] array)
    {
        for (ModelRendererTurbo model : array) {
            if (model.isShape3D)
            {
                model.rotateAngleY = (-model.rotateAngleY);
                model.rotateAngleX = (-model.rotateAngleX);
                model.rotateAngleZ = (-model.rotateAngleZ + 3.14159F);
            }
            else
            {
                model.rotateAngleZ = (-model.rotateAngleZ);
            }
        }
    }

    public static final void bindTexture(ResourceLocation rs)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(rs);
    }
}