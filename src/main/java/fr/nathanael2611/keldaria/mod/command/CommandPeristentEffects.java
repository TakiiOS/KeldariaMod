/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Keldaria.MOD_ID)
public class CommandPeristentEffects extends KeldariaCommand {

    private static final String KEY = Keldaria.MOD_ID + ":effects";

    public CommandPeristentEffects()
    {
        super("persistenteffects", "hehe", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException {
        String correctUsage =
                "§cUtilisations correctes:"
                        + "\n - /persistentEffects <player> <add/remove> <effect> <level> <state>"
                        + "\n - /persistentEffects <player> clear";

        if(args.length >= 2){

            EntityPlayer player = getPlayer(user.getServer(), user.getSender(), args[0]);

            Database playerData = Databases.getPlayerData(player);

            if(!playerData.isString(KEY))playerData.setString(KEY, "");

            List<PersistentEffect> effects = getEffectsFromString(playerData.getString(KEY));

            if(args.length == 5){

                PersistentEffect effect = new PersistentEffect(args[4], args[2], Integer.parseInt(args[3]));

                if(args[1].equalsIgnoreCase("add")){
                    if(!effects.contains(effect))effects.add(effect);
                    user.sendMessage("§a �? Ajout de §2" + effect.toString() + "§a à  la liste des effets persistants de §2" + args[0] + "§a effectué avec succès.");
                }else if(args[1].equalsIgnoreCase("remove")){
                    effects.forEach(e -> {
                        if(e.effectName.equalsIgnoreCase(effect.getEffectName()) && e.effectLevel == effect.getEffectLevel() && e.state.equalsIgnoreCase(effect.getState()))
                        {
                            effects.remove(e);
                        }
                    });
                    user.sendMessage("§a �? Retrait de §4" + effect.toString() + "§c de  la liste des effets persistants de §4" + args[0] + "§c effectué avec succès.");
                }
                playerData.setString(KEY, effectsListToString(effects));
            }else if(args.length == 2 && args[1].equalsIgnoreCase("clear")){
                playerData.setString(KEY, "");
                user.sendMessage(
                        "§cRetrait de tous les effets de  la liste des effets persistants de §4" + args[0] + "§c effectué avec succès."
                );
            }else if(args.length == 2 && args[1].equalsIgnoreCase("list")){
                user.sendMessage("§aListe des effets persistants de §2" + args[0] + "§a:");
                for(PersistentEffect effect : effects){
                    user.sendMessage("§a   - §2" + effect.toString());
                }
            }else{
                user.sendMessage(correctUsage);
            }

        }else{
            user.sendMessage(correctUsage);
        }
    }


    public static String effectsListToString(List<PersistentEffect> list) {
        StringBuilder listBuiler = new StringBuilder();
        for(PersistentEffect effect : list) {
            listBuiler.append("=").append(effect.toString());
        }
        return listBuiler.toString().substring(1);
    }
    public static List<PersistentEffect> getEffectsFromString(String effects){
        String[] list = effects.split("=");
        List<PersistentEffect> effects1 = new ArrayList<>();
        for(String str : list){
            effects1.add(PersistentEffect.fromString(str));
        }
        return effects1;
    }


    public static class PersistentEffect{
        private String state;
        private String effectName;
        private int effectLevel;

        public PersistentEffect(String state, String effectName, int effectLevel) {
            this.state = state;
            this.effectName = effectName;
            this.effectLevel = effectLevel;
        }

        public String getState() {
            return state;
        }

        public String getEffectName() {
            return effectName;
        }

        public int getEffectLevel() {
            return effectLevel;
        }

        @Override
        public String toString() {
            return getState() + "/" + getEffectName() + "/" + getEffectLevel();
        }

        public static PersistentEffect fromString(String effect){
            String[] strings = effect.split("/");
            if(strings.length == 3){
                return new PersistentEffect(strings[0], strings[1], Integer.parseInt(strings[2]));
            }
            return new PersistentEffect("all", "elfamoso", 1);
        }
    }

    static int timer = 0;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e){
        timer++;
        if(timer > 180) {
            timer = 0;
            if (e.player.world.isRemote) return;
            EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(e.player.getName());
            if (player == null) return;
            if (player.interactionManager.getGameType() == GameType.SURVIVAL) {
                Database playerData = Databases.getPlayerData(player);
                if (!playerData.isString(KEY)) playerData.setString(KEY, "");
                List<PersistentEffect> effects = getEffectsFromString(playerData.getString(KEY));
                for (PersistentEffect effect : effects) {
                    boolean exec = true;
                    if (effect.getState().equalsIgnoreCase("jour")) {
                        if (!player.world.isDaytime()) exec = false;
                    } else if (effect.getState().equalsIgnoreCase("nuit")) {
                        if (player.world.isDaytime()) exec = false;
                    } else if (effect.getState().equalsIgnoreCase("fullmoon")) {
                        if (player.world.isDaytime() && player.world.getMoonPhase() != 0) exec = false;
                    }
                    if(exec) {
                        if (Potion.REGISTRY.containsKey(new ResourceLocation(effect.getEffectName()))) {
                            player.addPotionEffect(
                                    new PotionEffect(
                                            Potion.REGISTRY.getObject(new ResourceLocation(effect.getEffectName())), 400, effect.getEffectLevel(), false, false
                                    )
                            );
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length == 1){
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }else if(args.length == 2){
            return getListOfStringsMatchingLastWord(args, "add", "remove", "clear", "list");
        }else if(args.length == 3){
            return getListOfStringsMatchingLastWord(args, Potion.REGISTRY.getKeys());
        }else if(args.length == 5){
            return getListOfStringsMatchingLastWord(args, "jour", "nuit", "all", "fullmoon");
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}