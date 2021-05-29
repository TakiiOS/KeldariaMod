package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.SkyColor;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.math.Vec3d;

public class CommandSkyColor extends KeldariaCommand
{

    public CommandSkyColor()
    {
        super("skycolor", "/skycolor <set/get> <sky/fog> [<r>] [<g>] [<b>]", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        String correctUsage = "/skycolor <set/get/reset> <sky/fog> [<red> <green> <blue>]";
        Database database = Databases.getDatabase(SkyColor.DB_KEY);
        if(args.length > 1)
        {
            if(args[1].equalsIgnoreCase("sky") || args[1].equalsIgnoreCase("fog"))
            {
                if(args.length == 2)
                {
                    if(args[0].equalsIgnoreCase("get"))
                    {
                        if(args[1].equalsIgnoreCase("sky") ? !SkyColor.isSkyColorCustom(false) : !SkyColor.isFogColorCustom(false))
                        {
                            throw new CommandException("Le " + (args[1].equalsIgnoreCase("sky") ? "ciel" : "brouillard") + " n'a pas de couleur personnalisée");
                        }
                        Vec3d color = args[1].equalsIgnoreCase("sky") ? SkyColor.getSkyColor(false) : SkyColor.getFogColor(false);
                        user.sendMessage(GREEN + " ● La couleur du " + (args[1].equalsIgnoreCase("sky") ? "ciel" : "brouillard") + String.format(" est définie a rgb(%s, %s, %s)", color.x, color.y, color.z));
                    }
                    else if (args[0].equalsIgnoreCase("reset"))
                    {
                        if(args[1].equalsIgnoreCase("sky")) SkyColor.resetSkyColor();
                        else SkyColor.resetFogColor();
                        user.sendMessage(GREEN + " ● La couleur du " + (args[1].equalsIgnoreCase("sky") ? "ciel" : "brouillard") + " a été reset !");
                    }
                    else
                    {
                        throw new WrongUsageException(correctUsage);
                    }
                } else if(args[0].equalsIgnoreCase("set") && args.length == 5){
                    double r = parseDouble(args[2]);
                    double g = parseDouble(args[3]);
                    double b = parseDouble(args[4]);
                    switch (args[1])
                    {
                        case "sky":
                            SkyColor.setSkyColor(r, g, b);
                            return;
                        case "fog":
                            SkyColor.setFogColor(r, g, b);
                    }
                } else {
                    throw new WrongUsageException(correctUsage);
                }
            } else {
                throw new WrongUsageException(correctUsage);
            }
        } else {
            throw new WrongUsageException(correctUsage);
        }
    }
}
