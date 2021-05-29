package fr.nathanael2611.keldaria.mod.command.inventory;

import com.google.common.collect.HashMultimap;
import fr.nathanael2611.keldaria.mod.util.PlayerInvChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class InvseeHandler
{

    private static HashMultimap<EntityPlayer, PlayerInvChest> map = HashMultimap.create();

    public static void register(PlayerInvChest chest)
    {
        map.put(chest.owner, chest);
    }

    public static void remove(PlayerInvChest chest)
    {
        map.remove(chest.owner, chest);
    }

    @SubscribeEvent
    public void tickStart(TickEvent.PlayerTickEvent event)
    {

        if (map.containsKey(event.player))
        {
            for (PlayerInvChest inv : map.get(event.player))
            {
                inv.update();
            }
        }
    }

}
