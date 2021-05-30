/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.canvas;

import fr.nathanael2611.keldaria.mod.util.math.Vector2i;

import java.util.HashMap;

/**
 * CanvasContent class, used for manipulate a canvas
 *
 * @author Nathanael2611
 */
public class CanvasContent {

    /*
     * The pixels array, contains integers (1 or 0)
     */
    private int[] pixels = new int[16384];

    public static final int PAINT = 1;
    public static final int ERASE = 0;

    /*
     * Construct the class, build the pixel array from a String
     */
    public CanvasContent(String content)
    {
        String[] pixelsStr = content.split("");
        int index = 0;
        for (String str : pixelsStr)
        {
            try
            {
                this.pixels[index] = Integer.parseInt(str);
            }
            catch (NumberFormatException e)
            {
                this.pixels[index] = 0;
            }
            index ++;
        }
    }

    public void paintInRadius(int color, HashMap<String, Integer> map, Vector2i pos, int radius) {
        if(radius == 1)
        {
            if(map.containsKey(pos.toString())) paint(map.get(pos.toString()), color);
            return;
        }
        for(int x = pos.x - radius / 2; x < pos.x + radius / 2; x ++)
        {
            for(int y = pos.y - radius / 2; y < pos.y + radius / 2; y ++)
            {
                Vector2i itPos = new Vector2i(x, y);
                if(map.containsKey(itPos.toString())) paint(map.get(itPos.toString()), color);
            }
        }
    }

    /* Paint a pixel */
    public void paint(int index, int type)
    {
        if(index > pixels.length || index < 0) return;
        pixels[index] = type;
    }

    /* Convert the CanvasContent to a String (can be used to build the class */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for(int i : pixels) builder.append(i);
        return builder.toString();
    }

    /* Get the pixel array */
    public int[] getPixels()
    {
        return pixels;
    }

}