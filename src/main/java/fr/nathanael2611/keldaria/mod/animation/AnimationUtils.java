/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.animation;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.Keldaria;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

/**
 * This class is used to handle player animation.
 *
 * @author Nathanael2611
 */
@Mod.EventBusSubscriber(modid = Keldaria.MOD_ID)
public class AnimationUtils {

    public static HashMap<String, Animation> animations = new HashMap<>();
    private static HashMap<String, Animation> playerActualAnimations = new HashMap<>();

    public static final HashMap<KeyBinding, Animation> ANIMATIONS_KEYS = Maps.newHashMap();

    @SubscribeEvent
    public static void playerConnectionEvent(EntityJoinWorldEvent e)
    {
        if(!(e.getEntity() instanceof EntityPlayer)) return;
        playerActualAnimations.put(e.getEntity().getName(), Animations.NONE);
    }

    public static Animation createAnimationFromStaticInstance(Animation instance)
    {
        if(instance == Animations.SIT) return new AnimationSit();
        if(instance == Animations.CRAWL) return new AnimationCrawl();
        if(instance == Animations.SLEEP) return new AnimationSleep();
        if(instance == Animations.WAVE) return new AnimationWave();
        if(instance == Animations.CLAP) return new AnimationClap();
        if(instance == Animations.PRAY) return new AnimationPray();
        if(instance == Animations.POINT) return new AnimationPoint();
        if(instance == Animations.SWIM) return new AnimationSwim();
        return Animations.NONE;
    }

    public static void setPlayerHandledAnimation(EntityPlayer player, Animation animation)
    {
        String playerName = player.getName();
        setPlayerHandledAnimation(playerName, animation);
    }

    public static void setPlayerHandledAnimation(String playerName, Animation animation)
    {
        playerActualAnimations.put(playerName, animation);
    }

    public static Animation getPlayerHandledAnimation(EntityPlayer player)
    {
        return getPlayerHandledAnimation(player.getName());
    }

    public static Animation getPlayerHandledAnimation(String playerName)
    {
        Animation animation = playerActualAnimations.get(playerName);
        if(animation == null)return Animations.NONE;
        return animation;
    }

    @SideOnly(Side.CLIENT)
    public static void registerAnimationKeys()
    {
        AnimationUtils.animations.forEach((s, animation) ->
        {
            KeyBinding keyBinding = new KeyBinding(animation.getName(), 0,"Keldaria - Animations");
            ClientRegistry.registerKeyBinding(keyBinding);
            ANIMATIONS_KEYS.put(keyBinding, animation);
        });



    }

}
