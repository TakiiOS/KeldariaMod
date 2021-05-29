package fr.nathanael2611.keldaria.mod.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class KeldariaCommand extends CommandBase
{

    public static final TextFormatting BLACK = TextFormatting.BLACK;
    public static final TextFormatting DARK_BLUE = TextFormatting.DARK_BLUE;
    public static final TextFormatting DARK_GREEN = TextFormatting.DARK_GREEN;
    public static final TextFormatting DARK_AQUA = TextFormatting.DARK_AQUA;
    public static final TextFormatting DARK_RED = TextFormatting.DARK_RED;
    public static final TextFormatting DARK_PURPLE = TextFormatting.DARK_PURPLE;
    public static final TextFormatting GOLD = TextFormatting.GOLD;
    public static final TextFormatting GRAY = TextFormatting.GRAY;
    public static final TextFormatting DARK_GRAY = TextFormatting.DARK_GRAY;
    public static final TextFormatting BLUE = TextFormatting.BLUE;
    public static final TextFormatting GREEN = TextFormatting.GREEN;
    public static final TextFormatting AQUA = TextFormatting.AQUA;
    public static final TextFormatting RED = TextFormatting.RED;
    public static final TextFormatting LIGHT_PURPLE = TextFormatting.LIGHT_PURPLE;
    public static final TextFormatting YELLOW = TextFormatting.YELLOW;
    public static final TextFormatting WHITE = TextFormatting.WHITE;
    public static final TextFormatting OBFUSCATED = TextFormatting.OBFUSCATED;
    public static final TextFormatting BOLD = TextFormatting.BOLD;
    public static final TextFormatting STRIKETHROUGH = TextFormatting.STRIKETHROUGH;
    public static final TextFormatting UNDERLINE = TextFormatting.UNDERLINE;
    public static final TextFormatting ITALIC = TextFormatting.ITALIC;
    public static final TextFormatting RESET = TextFormatting.RESET;

    private String name, usage;
    private List<String> aliases;

    public KeldariaCommand(String name, String usage, List<String> aliases)
    {
        this.name = name;
        this.usage = usage;
        this.aliases = aliases;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return this.usage;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        run(new CommandUser(server, sender), args);
    }

    public abstract void run(CommandUser user, String[] args) throws CommandException;

    public static List<String> createAliases(String... aliases)
    {
        return new ArrayList<>(Arrays.asList(aliases));
    }

    public boolean isExecutableByAll()
    {
        return false;
    }
}
