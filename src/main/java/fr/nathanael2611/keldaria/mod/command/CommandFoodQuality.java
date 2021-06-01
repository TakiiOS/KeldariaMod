package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.food.FoodQuality;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandFoodQuality extends KeldariaCommand
{

    public CommandFoodQuality()
    {
        super("foodquality", "/foodquality <quality>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if (args.length > 1)
        {
            ItemStack mainHand = user.asPlayer().getHeldItemMainhand();
            if (mainHand.getItem() instanceof ItemFood)
            {
                FoodQuality quality = FoodQuality.byName(args[0]);
                quality.set(mainHand);
            } else
            {
                throw new WrongUsageException(getUsage(user.getSender()));
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            List<String> list = Arrays.stream(FoodQuality.values()).map(FoodQuality::getName).collect(Collectors.toList());
            return getListOfStringsMatchingLastWord(args, list);
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
