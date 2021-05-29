package fr.nathanael2611.keldaria.mod.command;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFruitBlock;
import fr.nathanael2611.keldaria.mod.season.Season;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CommandCreateFruit extends KeldariaCommand
{
    public CommandCreateFruit()
    {
        super("createfruit", "/createfruit <pos> setTimes <growTime> <maturingTime> OR /createfruit <pos> <setMaturingItem/setFinalItem> <item>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        EntityPlayerMP playerMP = user.asPlayer();
        World world = playerMP.world;
        if(args.length > 2)
        {
            BlockPos pos = Helpers.parseBlockPosFromString(args[0]);
            if(pos != BlockPos.ORIGIN)
            {
                TileEntity te = world.getTileEntity(pos);
                if(te instanceof TileEntityFruitBlock)
                {
                    TileEntityFruitBlock fruit = (TileEntityFruitBlock) te;
                    if(args[1].equalsIgnoreCase("infos"))
                    {
                        user.sendMessage("Ne pousse pas en: " + fruit.getInfertileSeasons() + "\nYay: " + fruit.getGrowTime() + " >> " + fruit.getMaturingStack().getDisplayName() + " >> " + fruit.getMaturingTime() + " >> " + fruit.getMatureStack().getDisplayName());
                    }
                    else if(args.length == 4 && args[1].equalsIgnoreCase("setTimes"))
                    {
                        long growTime = parseLong(args[2]);
                        long maturingTime = parseLong(args[3]);
                        fruit.setTimes(growTime, maturingTime);
                    }
                    if(args[1].equalsIgnoreCase("setFinalStack") || args[1].equalsIgnoreCase("setMaturingStack"))
                    {
                        StringBuilder builder = new StringBuilder();
                        for (int i = 2; i < args.length; i++)
                        {
                            builder.append(" ").append(args[i]);
                        }
                        String itemStr = builder.toString().substring(1);
                        ItemStack stack = Helpers.getItemStackFromString(itemStr);
                        if(args[1].equalsIgnoreCase("setFinalStack"))
                        {
                            fruit.setMatureStack(stack);
                        }
                        else
                        {
                            fruit.setMaturingStack(stack);
                        }
                    }
                    else if(args[1].equalsIgnoreCase("setInfertileSeasons"))
                    {
                        String[] names = args[2].split(",");
                        List<Season> infertileSeasons = Lists.newArrayList();
                        for (String name : names)
                        {
                            Season s = Season.byName(name);
                            if(s != null)
                            {
                                infertileSeasons.add(s);
                            }
                        }
                        fruit.setInfertileSeasons(infertileSeasons);
                    }
                }
                else
                {
                    throw new WrongUsageException("No Fruit Block at this pos!");
                }
            }
            else
            {
                throw new WrongUsageException("Please provide a valid block pos!");
            }
        }
        else throw new WrongUsageException(getUsage(user.getSender()));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 1 && targetPos != null)
        {
            return getListOfStringsMatchingLastWord(args, Helpers.blockPosToString(targetPos));
        }
        else if(args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, "setTimes", "setMaturingStack", "setFinalStack", "setInfertileSeasons");
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
