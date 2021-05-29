package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.registry.KeldariaBlocks;
import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CommandCustomPainting extends KeldariaCommand
{
    public CommandCustomPainting()
    {
        super("custompainting", "/custompainting <width> <height> <url>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length < 3)
            throw new WrongUsageException(getUsage(user.getSender()));
        int width = parseInt(args[0]);
        int height = parseInt(args[1]);
        StringBuilder urlBuilder = new StringBuilder();
        for (int i = 2; i < args.length; i++)
        {
            urlBuilder.append(" ").append(args[i]);
        }
        String url = urlBuilder.toString().substring(1);

        ItemStack stack = new ItemStack(KeldariaBlocks.CUSTOM_PAINTING);
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagCompound entityTag = new NBTTagCompound();
        entityTag.setInteger("Width", width);
        entityTag.setInteger("Height", height);
        entityTag.setString("Link", url);
        compound.setTag("BlockEntityTag", entityTag);
        stack.setTagCompound(compound);
        user.asPlayer().addItemStackToInventory(stack);

    }
}
