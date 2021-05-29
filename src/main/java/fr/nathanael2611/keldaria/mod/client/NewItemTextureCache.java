package fr.nathanael2611.keldaria.mod.client;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import fr.nathanael2611.keldaria.mod.client.model.ModelCustomItemTexture;
import fr.nathanael2611.keldaria.mod.client.model.custom.Transforms;
import fr.nathanael2611.keldaria.mod.features.ItemTextures;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewItemTextureCache
{

    public static final HashMap<String, ModelCustomItemTexture> MODELS = Maps.newHashMap();
    public static final HashMap<String, Boolean> IS_CREATING = Maps.newHashMap();

    private static final ExecutorService SERVICE = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("item-textures-cache").build());

    public static final HashMap<String, ItemLayer> LAYERS = Maps.newHashMap();

    public static ItemLayer getLayer(ItemStack stack, ItemLayer def)
    {


        if(ItemTextures.hasTexture(stack))
        {
            String url = ItemTextures.getTexture(stack);
            if(ItemTextures.hasLayer(stack))
            {

                if (LAYERS.containsKey(url))
                {
                    return LAYERS.get(url);
                } else
                {
                    return LAYERS.put(url, ItemLayer.from(ItemTextures.getLayer(stack)));
                }
            }
            else
            {

                return def;
            }
        } else {

            return def;
        }
    }

    public static final HashMap<String, Transforms> TRANSFORMS = Maps.newHashMap();

    public static Transforms getTransforms(ItemStack stack, Transforms def)
    {
        if(ItemTextures.hasTexture(stack))
        {
            String url = ItemTextures.getTexture(stack);
            if(ItemTextures.hasTransforms(stack))
            {
                if (TRANSFORMS.containsKey(url))
                {
                    return TRANSFORMS.get(url);
                } else
                {
                    Transforms transforms = new Transforms(ItemTextures.getTransforms(stack));
                    return TRANSFORMS.put(url, transforms);
                }
            }
            else
            {
                return def;
            }
        } else {
            return def;
        }
    }

    public static ModelCustomItemTexture getModel(ItemStack stack, ModelCustomItemTexture def)
    {
        boolean flag = ItemTextures.hasTexture(stack);
        String url = ItemTextures.getTexture(stack);
        if (flag)
        {
            return getModel(url, def);
        } else
        {
            return def;
        }
    }

    public static ModelCustomItemTexture getModel(String url, ModelCustomItemTexture def)
    {
        if (MODELS.containsKey(url))
        {
            return MODELS.get(url);
        } else
        {
            if (IS_CREATING.containsKey(url))
            {
                return def;
            } else
            {
                if(ImageCache.getAsResourceLocation(url, null) != null)
                {
                    IS_CREATING.put(url, true);
                    SERVICE.execute(() ->
                    {
                        MODELS.put(url, new ModelCustomItemTexture(url));
                        IS_CREATING.put(url, false);
                    });
                    return getModel(url, def);
                }
                else return def;
            }
        }
    }

}