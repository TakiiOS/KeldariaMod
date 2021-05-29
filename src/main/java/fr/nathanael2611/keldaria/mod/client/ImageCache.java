package fr.nathanael2611.keldaria.mod.client;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageCache
{

    private static final HashMap<String, BufferedImage> IMAGES = Maps.newHashMap();
    private static final HashMap<String, ResourceLocation> RS = Maps.newHashMap();
    private static final HashMap<String, Boolean> IS_DOWNLOADING = Maps.newHashMap();
    private static final HashMap<String, URL> URLS = Maps.newHashMap();

    private static final ExecutorService SERVICE = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("image-cache-downloader").build());

    private static ResourceLocation blank = null;

    public static BufferedImage get(String url, BufferedImage defaultImage)
    {
        if(IMAGES.containsKey(url))
        {
            return IMAGES.get(url);
        }
        else
        {
            if(IS_DOWNLOADING.containsKey(url))
            {
                return defaultImage;
            }
            else
            {
                IS_DOWNLOADING.put(url, true);
                SERVICE.execute(() -> {
                    try {
                        BufferedImage image = ImageIO.read(asUrl(url));
                        if(image != null)
                        {
                            IMAGES.put(url, image);
                            IS_DOWNLOADING.put(url, false);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        IS_DOWNLOADING.put(url, false);
                    }
                });
                return get(url, defaultImage);
            }
        }
    }

    public static ResourceLocation getAsResourceLocation(String url, ResourceLocation defaultValue)
    {
        if(RS.containsKey(url)) return RS.get(url);
        BufferedImage image = get(url, null);
        if(image == null) return defaultValue;
        return RS.put(url, Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("okok", new DynamicTexture(image)));
    }

    public static ResourceLocation getAsResourceLocation(BufferedImage image)
    {
        return Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("okok", new DynamicTexture(image));
    }

    public static ResourceLocation getBlank()
    {
        return blank == null
                ? blank = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("blank", new DynamicTexture(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB)))
                : blank;
    }

    private static URL asUrl(String url)
    {
        if(URLS.containsKey(url))
        {
            return URLS.get(url);
        }
        else
        {
            try {
                return new URL(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }
    }
}