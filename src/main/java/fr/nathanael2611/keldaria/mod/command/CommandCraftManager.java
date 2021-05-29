package fr.nathanael2611.keldaria.mod.command;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.crafting.CraftManager;
import fr.nathanael2611.keldaria.mod.crafting.storage.KnownRecipesStorage;
import fr.nathanael2611.keldaria.mod.crafting.storage.api.IKnownRecipes;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CommandCraftManager extends KeldariaCommand
{

    public CommandCraftManager()
    {
        super("craftmanager", "craftmanager", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if (args.length > 0)
        {
            if (args[0].equalsIgnoreCase("reload"))
            {
                user.sendMessage("§aReload effectué!");
                CraftManager.MANAGERS.forEach((name, manager) -> manager.reload());
            } else if (args.length > 2)
            {
                if (args[0].equalsIgnoreCase("crafts"))
                {
                    boolean hand = false;
                    IKnownRecipes recipes = null;

                    if (args[1].equalsIgnoreCase("hand"))
                    {
                        hand = true;
                    } else
                    {
                        EntityPlayerMP playerMP = user.getPlayer(args[1]);
                        if (playerMP == null)
                        {
                            user.sendMessage(":D");
                            return;
                        }
                        recipes = CraftManager.getRecipes(playerMP);
                    }

                    if (args[2].equalsIgnoreCase("list"))
                    {
                        user.sendMessage("§cListe des crafts:");
                        for (String knownKey : recipes.getKnownKeys())
                        {
                            user.sendMessage("§6 - §e" + knownKey);
                        }
                    } else if (args.length > 3 && (args[2].equalsIgnoreCase("forget") || args[2].equalsIgnoreCase("discover")))
                    {
                        CraftManager manager = CraftManager.getManager(args[3]);
                        if (manager != null)
                        {
                            if (args.length > 4)
                            {
                                if (manager.getInfoMap().containsKey(args[4]))
                                {
                                    String str = manager.getName() + "/" + args[4] + "/";
                                    if (args.length > 5 && manager.getInfoMap().get(args[4]).contains(args[5]))
                                    {
                                        str += args[5];
                                    } else if (args.length > 5)
                                    {
                                        user.sendMessage("§cLe craft donné n'existe pas.");
                                        return;
                                    }
                                    if (hand)
                                    {
                                        ItemStack mainHand = user.asPlayer().getHeldItemMainhand();
                                        recipes = mainHand.getCapability(KnownRecipesStorage.CAPABILITY_KNOWN_RECIPES, null);
                                        if (recipes == null)
                                        {
                                            user.sendMessage("§cErreur :D :D :D");
                                            return;
                                        }
                                    }
                                    if (args[2].equalsIgnoreCase("forget"))
                                    {
                                        recipes.forget(str);
                                        user.sendMessage("§aCraft oublié.");
                                    } else
                                    {
                                        recipes.discover(str);
                                        user.sendMessage("§aCraft découvert.");
                                    }
                                } else
                                {
                                    user.sendMessage("§cVeuillez fournir une catégorie valide.");
                                }
                            } else
                            {
                                user.sendMessage("§cVeuillez fournir au moins une catégorie.");
                            }
                        } else
                        {
                            user.sendMessage("§cMerci de fournir un craftmanager valide");
                        }

                    }
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "crafts", "reload");
        } else if (args.length == 2)
        {
            List<String> l = Lists.newArrayList();
            Collections.addAll(l, server.getOnlinePlayerNames());
            l.add("hand");
            return getListOfStringsMatchingLastWord(args, l);
        } else if (args.length == 3)
        {
            return getListOfStringsMatchingLastWord(args, "discover", "forget", "list");
        } else if (args.length == 4)
        {
            return getListOfStringsMatchingLastWord(args, CraftManager.MANAGERS.keySet());
        } else if (args.length == 5 || args.length == 6)
        {
            CraftManager manager = CraftManager.getManager(args[3]);
            if (manager != null)
            {
                HashMap<String, List<String>> infos = manager.getInfoMap();
                if (args.length == 5) return getListOfStringsMatchingLastWord(args, infos.keySet());
                else if (infos.containsKey(args[4]))
                {
                    return getListOfStringsMatchingLastWord(args, infos.get(args[4]));
                }
            }
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
