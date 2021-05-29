package fr.nathanael2611.keldaria.mod.season;

import net.minecraft.world.biome.Biome;
import org.lwjgl.util.Color;

public class SeasonColorUtils
{

    public static int multiplyColours(int colour1, int colour2)
    {
        return (int)((colour1 / 255.0F) * (colour2 / 255.0F) * 255.0F);
    }

    public static int overlayBlendChannel(int underColour, int overColour)
    {
        int retVal;
        if (underColour < 128)
        {
            retVal = multiplyColours(2 * underColour, overColour);
        }
        else
        {
            retVal = multiplyColours(2 * (255 - underColour), 255 - overColour);
            retVal = 255 - retVal;
        }
        return retVal;
    }

    public static int overlayBlend(int underColour, int overColour)
    {
        int r = overlayBlendChannel((underColour >> 16) & 255, (overColour >> 16) & 255);
        int g = overlayBlendChannel((underColour >> 8) & 255, (overColour >> 8) & 255);
        int b = overlayBlendChannel(underColour & 255, overColour & 255);

        return (r & 255) << 16 | (g & 255) << 8 | (b & 255);
    }

    public static int saturateColour(int colour, float saturationMultiplier)
    {
        Color newColour = getColourFromInt(colour);
        float[] hsb = newColour.toHSB(null);
        hsb[1] *= saturationMultiplier;
        newColour.fromHSB(hsb[0], hsb[1], hsb[2]);
        return getIntFromColour(newColour);
    }

    public static int applySeasonalGrassColouring(Season season, Biome biome, int originalColour)
    {
        int overlay = season.getGrassColor();
        float saturationMultiplier = season.getGrassColorMultiplier();
        int newColour = overlay == 0xFFFFFF ? originalColour : overlayBlend(originalColour, overlay);
        return saturationMultiplier != -1 ? saturateColour(newColour, saturationMultiplier) : newColour;
    }

    public static int applySeasonalFoliageColouring(Season season, Biome biome, int originalColour)
    {
        int overlay = season.getFoliageColor();
        float saturationMultiplier = season.getFoliageColorMultiplier();
        int newColour = overlay == 0xFFFFFF ? originalColour : overlayBlend(originalColour, overlay);
        return saturationMultiplier != -1 ? saturateColour(newColour, saturationMultiplier) : newColour;
    }

    private static Color getColourFromInt(int colour)
    {
        return new Color((colour >> 16) & 255, (colour >> 8) & 255, colour & 255);
    }

    private static int getIntFromColour(Color colour)
    {
        return (colour.getRed() & 255) << 16 | (colour.getGreen() & 255) << 8 | colour.getBlue() & 255;
    }


}
