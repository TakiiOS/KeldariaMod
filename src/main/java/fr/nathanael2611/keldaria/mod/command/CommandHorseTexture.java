package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.HorsesTextures;
import net.minecraft.command.CommandException;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class CommandHorseTexture extends KeldariaCommand
{

    public CommandHorseTexture()
    {
        super("horsetexture", "/horsetexture <url>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        EntityPlayerMP mp = user.asPlayer();
        if(mp.isRiding() && args.length > 0)
        {
            if(mp.getRidingEntity() instanceof EntityHorse)
            {
                EntityHorse horse = (EntityHorse) mp.getRidingEntity();
                HorsesTextures.setTexture(horse, args[0]);
            }
        }
    }
}
