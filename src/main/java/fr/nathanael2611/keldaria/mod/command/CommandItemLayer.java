package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.command.CommandException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CommandItemLayer extends KeldariaCommand
{
    public CommandItemLayer()
    {
        super("itemlayer", "/itemtexture <url> <transforms>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length > 0)
        {
            String url = args[0];
            String transforms = "T(0,0,0)";
            if(args.length > 1)
            {
                transforms = args[1];
            }
            ItemStack stack = user.asPlayer().getHeldItemMainhand();
            NBTTagCompound compound = Helpers.getCompoundTag(stack);

            String builded = url + "=" + transforms;
            compound.setString("ItemLayer", builded);
            stack.setTagCompound(compound);
            user.sendMessage(GREEN + "Layer appliquée!!!");
        }
    }
}
