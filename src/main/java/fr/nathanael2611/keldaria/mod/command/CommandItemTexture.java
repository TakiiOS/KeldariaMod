/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.command.CommandException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CommandItemTexture extends KeldariaCommand
{
    public CommandItemTexture()
    {
        super("itemtexture", "/itemtexture <url> <size>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length > 0)
        {
            double size = 1;
            String builtInTransform = "sword";
            String transforms = null;
            if(args.length > 1)
            {
                size = parseDouble(args[1]);
                if(args.length > 2)
                {
                    builtInTransform = args[2];
                    if(args.length > 3)
                    {
                        transforms = args[2];
                    }
                }
            }
            String url = args[0];
            ItemStack stack = user.asPlayer().getHeldItemMainhand();
            NBTTagCompound compound = Helpers.getCompoundTag(stack);
            compound.setDouble("ItemSize", size);
            compound.setString("ItemTexture", url);
            compound.setString("BuiltInTransform", builtInTransform);
            if(transforms != null)
            {
                compound.setString("GLTransforms", transforms);
            }
            else
            {
                compound.removeTag("GLTransforms");
            }
            stack.setTagCompound(compound);
            user.sendMessage(GREEN + "Texture appliquée!!!");
        }
    }
}
