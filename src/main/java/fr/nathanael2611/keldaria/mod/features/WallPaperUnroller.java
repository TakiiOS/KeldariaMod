package fr.nathanael2611.keldaria.mod.features;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class WallPaperUnroller
{

    private static final HashMap<String, BlockPos> SELECTED_WALLPAPER = Maps.newHashMap();
    private static final HashMap<String, Long> TIME_SELECTED = Maps.newHashMap();

    public static boolean hasSelectedWallpaper(EntityPlayer player)
    {
        if(SELECTED_WALLPAPER.containsKey(player.getName()))
        {
            if(System.currentTimeMillis() - TIME_SELECTED.get(player.getName()) > 10000)
            {
                SELECTED_WALLPAPER.remove(player.getName());
                TIME_SELECTED.remove(player.getName());
                return false;
            }
            return true;
        }
        return false;
    }

    public static BlockPos getSelectedWallpaper(EntityPlayer player)
    {
        return SELECTED_WALLPAPER.get(player.getName());
    }

    public static void select(EntityPlayer player, BlockPos selected)
    {
        SELECTED_WALLPAPER.put(player.getName(), selected);
        TIME_SELECTED.put(player.getName(), System.currentTimeMillis());
    }

    public static void unSelect(EntityPlayer player)
    {
        SELECTED_WALLPAPER.remove(player.getName());
        TIME_SELECTED.remove(player.getName());
    }

}
