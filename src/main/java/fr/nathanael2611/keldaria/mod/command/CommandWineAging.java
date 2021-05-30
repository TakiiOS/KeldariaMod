/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import fr.nathanael2611.keldaria.mod.features.Wine;
import fr.nathanael2611.keldaria.mod.item.ItemWineBottle;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CommandWineAging extends KeldariaCommand
{
    public CommandWineAging()
    {
        super("vieillissement-vin", "/vieillissement <nombre-de-jours>", createAliases("wine-aging"));
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        EntityPlayerMP player = user.asPlayer();
        ItemStack item = player.getHeldItemMainhand();
        if(item.getItem() instanceof ItemWineBottle)
        {
            if(args.length == 1)
            {
                int days = parseInt(args[0]);
                Wine wine = Wine.getWine(item);
                KeldariaDate.KeldariaDateFormat date = KeldariaDate.getKyrgonDate();
                Wine newWine = new Wine(wine.getBaseQuality(), date.getTotalDaysInRP() - days);
                Wine.setWine(item, newWine);
            }
        }
        else
        {
            user.sendMessage(RED + "L'item tenu n'est pas une bouteille de vin.");
        }
    }
}
