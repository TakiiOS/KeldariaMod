/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.api.IHasClothe;
import fr.nathanael2611.keldaria.mod.item.ItemArmorPart;
import fr.nathanael2611.keldaria.mod.item.ItemClothe;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class CommandClothe extends KeldariaCommand
{

    public CommandClothe()
    {
        super("clothe", "/clothe <url> <name>", createAliases("vetement"));
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(!(args.length >= 1)) throw new WrongUsageException(this.getUsage(user.getSender()));
        boolean isArmorInHand = user.asPlayer().getHeldItemMainhand().getItem() instanceof ItemArmor || user.asPlayer().getHeldItemMainhand().getItem() instanceof IHasClothe || user.asPlayer().getHeldItemMainhand().getItem() instanceof ItemArmorPart;
        ItemStack clothe = isArmorInHand ? user.asPlayer().getHeldItemMainhand() : new ItemStack(KeldariaItems.CLOTHE);
        ItemClothe.setClothURL(clothe, args[0]);
        if(args.length >= 2) clothe.setStackDisplayName(Helpers.dropFirstString(args));
        if(!isArmorInHand) user.asPlayer().addItemStackToInventory(clothe);
    }

}