package fr.nathanael2611.keldaria.mod.client.model.custom;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.features.ItemTextures;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.LinkedList;

public class Transforms
{

    private LinkedList<Transform> transforms = Lists.newLinkedList();

    public Transforms(Transform... transforms)
    {
        this.transforms.addAll(Arrays.asList(transforms));
    }

    public Transforms(String str)
    {
        for (String s : str.split(">"))
        {
            Transform t = Transform.parse(s);
            if(t != null)
            {
                this.transforms.add(t);
            }
        }
    }

    public static BuiltInTransforms builtInTransform(String transformName)
    {
        if(transformName.equalsIgnoreCase("sword"))
        {
            return SWORD;
        }
        else if(transformName.equalsIgnoreCase("shield"))
        {
            return SHIELD;
        }
        else if(transformName.equalsIgnoreCase("base"))
        {
            return BASE;
        }
        return BASE;
    }

    public static abstract class BuiltInTransforms
    {
        public abstract void apply(ItemStack stack, EntityLivingBase base);
    }

    public static final BuiltInTransforms SWORD = new BuiltInTransforms()
    {
        @Override
        public void apply(ItemStack stack, EntityLivingBase base)
        {
        }
    };

    public static final BuiltInTransforms SHIELD = new BuiltInTransforms()
    {

        @Override
        public void apply(ItemStack stack, EntityLivingBase base)
        {
            GlStateManager.translate(0.22, 0,0);
            GlStateManager.translate(0, -0.30, 0);

            if(!base.getHeldItemOffhand().equals(stack))
            {
                GlStateManager.translate(0, 0, -(0.155 + ((1 - ItemTextures.getSize(stack)) * 0.08)));

            }
            else
            {
                GlStateManager.translate(0,0, (0.12 + ((1 - ItemTextures.getSize(stack)) * 0.08)));
            }

            GlStateManager.rotate(20, 0, 0, 1);
            GlStateManager.translate(-0.05, 0, 0);
            GlStateManager.translate(0, 0.05, 0);

        }
    };

    public static final BuiltInTransforms BASE = new BuiltInTransforms()
    {
        @Override
        public void apply(ItemStack stack, EntityLivingBase base)
        {
            GlStateManager.rotate(90, 0, 1, 0);
            GlStateManager.rotate(-18, 1, 0, 0);
            GlStateManager.translate(0, 0, -0.6);
            GlStateManager.translate(0.25, 0, 0);
            GlStateManager.translate(0, -0.3, 0);
        }
    };


    public void apply()
    {
        for (Transform transform : this.transforms)
        {
            transform.transform();
        }
    }

    public static abstract class Transform
    {
        protected float x, y, z;

        public Transform(float x, float y, float z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public abstract void transform();

        public static Transform parse(String str)
        {
            String[] valuesStr = str.substring(2, str.length() - 1).split(",");
            float[] values = new float[valuesStr.length];
            for (int i = 0; i < valuesStr.length; i++)
            {
                values[i] = Helpers.parseFloatOrZero(valuesStr[i]);
            }
            if(str.startsWith("T") && values.length == 3)
            {
                return new Translate(values[0], values[1], values[2]);
            }
            else if(str.startsWith("S") && values.length == 3)
            {
                return new Scale(values[0], values[1], values[2]);
            }
            else if(str.startsWith("R") && values.length == 4)
            {
                return new Rotate(values[0], values[1], values[2], values[3]);
            }
            return null;
        }
    }

    public static class Rotate extends Transform
    {

        float angle;

        public Rotate(float degrees, float x, float y, float z)
        {
            super(x,y,z);
            this.angle = degrees;
        }

        @Override
        public void transform()
        {
            GlStateManager.rotate(this.angle, x, y, z);
        }
    }

    public static class Translate extends Transform
    {

        public Translate(float x, float y, float z)
        {
            super(x,y,z);
        }

        @Override
        public void transform()
        {
            GlStateManager.translate(x, y, z);
        }
    }

    public static class Scale extends Transform
    {

        public Scale(float x, float y, float z)
        {
            super(x,y,z);
        }

        @Override
        public void transform()
        {
            GlStateManager.scale(x, y, z);
        }
    }

}
