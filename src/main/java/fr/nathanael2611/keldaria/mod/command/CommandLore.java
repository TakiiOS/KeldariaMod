package fr.nathanael2611.keldaria.mod.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

public class CommandLore extends KeldariaCommand
{
    public CommandLore()
    {
        super("lore", "/lore <line> <text>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        EntityPlayerMP player = user.asPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        WrongUsageException usageException = new WrongUsageException("/lore setLine <line> <text> OR /lore set <text> OR /lore resetLore");
        if (args.length > 2 && args[0].equalsIgnoreCase("setLine"))
        {
            if (isSealed(stack))
            {
                throw new WrongUsageException("Le lore est scellé!");

            }
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i < args.length; i++)
            {
                sb.append(" ").append(args[i]);
            }
            String line = sb.toString().substring(1);
            setLore(stack, Integer.parseInt(args[1]), line);
        } else if (args.length > 1 && args[0].equalsIgnoreCase("set"))
        {
            if (isSealed(stack))
            {
                throw new WrongUsageException("Le lore est scellé!");

            }
            resetLore(stack);
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
            {
                sb.append(" ").append(args[i]);
            }
            String text = sb.toString().substring(1);
            String[] words = text.split(" ");
            String line = "";
            int lines = 0;
            for (int i = 0; i < words.length; i++)
            {
                if (i > 8 + (lines * 8))
                {
                    lines++;
                    setLore(stack, lines, line);
                    line = "";
                }
                line += " " + words[i];
            }
            if (line.length() > 0)
            {
                lines++;
                setLore(stack, lines, line);
            }

        } else if (args.length == 1)
        {
            if((args[0].equalsIgnoreCase("resetLore") || args[0].equalsIgnoreCase("seal")))
            {
                if (isSealed(stack))
                {
                    throw new WrongUsageException("Le lore est scellé!");
                }
                if(args[0].equalsIgnoreCase("resetLore"))
                {
                    resetLore(stack);
                }
                else if(args[0].equalsIgnoreCase("seal"))
                {
                    seal(stack, player);
                    user.sendMessage(RED + "[Keldaria] " + YELLOW + " Lore scellé");
                }
            }
            else if(args[0].equalsIgnoreCase("unseal"))
            {
                if (unSeal(stack, player))
                {
                    user.sendMessage(RED + "[Keldaria] " + YELLOW + " Lore dé-scellé.");
                }
                else
                {
                    user.sendMessage(RED + "[Keldaria] " + YELLOW + " Seul celui qui a scellé le lore peut faire ça.");
                }
            }
        } else throw usageException;
    }

    public static void seal(ItemStack stack, EntityPlayer player)
    {
        NBTTagCompound compound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        compound.setString("LoreSealed", player.getName());
        stack.setTagCompound(compound);
    }

    public static boolean isSealed(ItemStack stack)
    {
        if(stack.hasTagCompound())
        {
            return stack.getTagCompound().hasKey("LoreSealed");
        }
        return false;
    }

    public static boolean unSeal(ItemStack stack, EntityPlayer player)
    {
        if(isSealed(stack))
        {
            if(getSealOwner(stack).equalsIgnoreCase(player.getName()))
            {
                if(stack.hasTagCompound())
                {
                    stack.getTagCompound().removeTag("LoreSealed");
                }
                return true;
            }
        }
        return false;
    }

    public static String getSealOwner(ItemStack stack)
    {
        if (isSealed(stack))
        {
            return stack.getTagCompound().getString("LoreSealed");
        }
        return "";
    }

    public static void setLore(ItemStack stack, int line, String str)
    {
        NBTTagCompound compound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        NBTTagCompound displayCompound = compound.hasKey("display", Constants.NBT.TAG_COMPOUND) ? compound.getCompoundTag("display") : new NBTTagCompound();
        NBTTagList baseList = displayCompound.hasKey("Lore", Constants.NBT.TAG_LIST) ? displayCompound.getTagList("Lore", 8) : new NBTTagList();
        NBTTagList newList = new NBTTagList();
        NonNullList<String> l = NonNullList.withSize(Math.max(line, baseList.tagCount()), "");
        for (int i = 0; i < baseList.tagCount(); i++)
        {
            l.set(i, baseList.getStringTagAt(i));
        }
        l.set(line - 1, str);
        for (String s : l)
        {
            newList.appendTag(new NBTTagString(s));
        }
        displayCompound.setTag("Lore", newList);
        compound.setTag("display", displayCompound);
        stack.setTagCompound(compound);
    }

    public static void resetLore(ItemStack stack)
    {
        NBTTagCompound compound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        NBTTagCompound displayCompound = compound.hasKey("display", Constants.NBT.TAG_COMPOUND) ? compound.getCompoundTag("display") : new NBTTagCompound();
        displayCompound.setTag("Lore", new NBTTagList());
        compound.setTag("display", displayCompound);
        stack.setTagCompound(compound);
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
}
