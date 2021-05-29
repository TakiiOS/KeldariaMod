package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class CommandBirthday extends KeldariaCommand
{

    public CommandBirthday()
    {
        super("birthday", "/anniversaire <jour> <mois> <année>", createAliases("anniversaire"));
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        WrongUsageException wrongUsage = new WrongUsageException(getUsage(user.getSender()));
        if(args.length == 3)
        {
            int day = parseInt(args[0]);
            String month = args[1];
            int year = parseInt(args[2]);
            int monthIndex = KeldariaDate.getIndexByMonth(month);
            if(monthIndex == -1)
            {
                user.sendMessage(RED + "Veuillez indiquer un mois valide: " + Arrays.toString(KeldariaDate.MONTHS));
                return;
            }
            KeldariaDate.KeldariaBirthday birthday = new KeldariaDate.KeldariaBirthday(day, monthIndex, year);
            Databases.getPlayerData(user.asPlayer()).setString("Birthday", birthday.toStorableString());
            user.sendMessage(GREEN + "Anniversaire de personnage définit!");
        }
        else
        {
            throw wrongUsage;
        }
    }


    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, KeldariaDate.MONTHS);
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
