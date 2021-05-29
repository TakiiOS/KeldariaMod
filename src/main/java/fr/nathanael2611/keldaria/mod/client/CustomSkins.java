package fr.nathanael2611.keldaria.mod.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.clothe.Clothes;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

public class CustomSkins
{

    private static final HashMap<String, List<String>> LAST_LINKS = Maps.newHashMap();
    public static final HashMap<String, ResourceLocation> LAST_RESOURCESLOCATIONS = Maps.newHashMap();
    public static final HashMap<String, Boolean> FORCE_REFRESH = Maps.newHashMap();
    private static final HashMap<String, BufferedImage> LAST = Maps.newHashMap();

    public static ResourceLocation getSkin(AbstractClientPlayer player)
    {
        //Skin skin = CustomSkinManager.getInstance().getSkin(player, true);
        String skinLink = ClientSkinManager.getSkinURL(player);
        Clothes clothes = Keldaria.getInstance().getClothesManager().getClothes(player.getName());
        List<String> links = Lists.newArrayList(skinLink);
        links.addAll(clothes.getClotheUrls());
        if(LAST_LINKS.containsKey(player.getName()))
        {
            if(!LAST_RESOURCESLOCATIONS.containsKey(player.getName()) || !Helpers.spaceListWithComa(links).equalsIgnoreCase(Helpers.spaceListWithComa(LAST_LINKS.get(player.getName()))))
            {
                BufferedImage skinImage = getSkinImage(player);
                if(skinImage == null) return player.getLocationSkin();
                LAST_RESOURCESLOCATIONS.put(player.getName(), Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("ok", new DynamicTexture(skinImage)));
                skinImage = getSkinImage(player);
                LAST_RESOURCESLOCATIONS.put(player.getName(), Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("ok", new DynamicTexture(skinImage)));
            }
            LAST_LINKS.put(player.getName(), links);
            return LAST_RESOURCESLOCATIONS.get(player.getName());
        }
        else
        {
            LAST_LINKS.put(player.getName(), links);
            return getSkin(player);
        }
    }

    public static BufferedImage getRawSkinImage(AbstractClientPlayer player)
    {
        String skin = ClientSkinManager.getSkinURL(player);
        if(skin.equalsIgnoreCase("default")) return ImageCache.get("https://minotar.net/skin/" + player.getName(), null);
        return ImageCache.get(skin, null);
    }

    public static BufferedImage getSkinImage(AbstractClientPlayer player)
    {
        BufferedImage rawSkin = getRawSkinImage(player);

        BufferedImage clothes = Keldaria.getInstance().getClothesManager().getClothes(player.getName()).assemble();
        List<BufferedImage> skin = Lists.newArrayList();
        skin.add(rawSkin);

        skin.add(clothes);
        return LAST.put(player.getName(), RenderHelpers.superpose(skin));
    }

}