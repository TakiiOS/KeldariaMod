package fr.nathanael2611.keldaria.mod.fight;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.fight.attacks.weapons.SwingTypes;
import fr.nathanael2611.keldaria.mod.fight.attacks.weapons.Sword;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FightHandler
{

    public static final ResourceLocation LOC = new ResourceLocation(Keldaria.MOD_ID, "fightcontrol");

    @SubscribeEvent
    public void onCapAttach(AttachCapabilitiesEvent<Entity> event)
    {
        event.addCapability(LOC, new FightProvider(event.getObject()));
    }

    public static boolean isInFightMode(EntityPlayer player)
    {
        FightControl<EntityPlayer> control = getFightControl(player);
        if(control != null)
        {
            return control.isFightMode();
        }
        return false;
    }

    public static <E extends Entity> FightControl<E> getFightControl(Entity entity)
    {
        FightControl<E> fightControl = entity.getCapability(FightStorage.CAPABILITY, null);
        return fightControl;
    }

    @SubscribeEvent
    public void onTickEntity(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase base = event.getEntityLiving();
        FightControl<?> fightControl = getFightControl(base);
        if(fightControl != null)
        {
            fightControl.tick();
        }
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event)
    {

    }

}
