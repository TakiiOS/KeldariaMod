package fr.nathanael2611.keldaria.mod.command.inventory;

import fr.nathanael2611.keldaria.mod.command.CommandUser;
import fr.nathanael2611.keldaria.mod.command.KeldariaCommand;
import net.minecraft.command.CommandException;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class CommandSwitchInv extends KeldariaCommand
{
    public CommandSwitchInv()
    {
        super("switchinv", "/switchinv", createAliases("sw"));
    }

    public static final String KEY_INV = "storedInventory";

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        EntityPlayerMP player = user.asPlayer();
        NBTTagList actualInventory = player.inventory.writeToNBT(new NBTTagList());
        NBTTagList inventoryTag = new NBTTagList();
        if(player.getEntityData().hasKey(KEY_INV, Constants.NBT.TAG_LIST))
        {
            inventoryTag = player.getEntityData().getTagList(KEY_INV, Constants.NBT.TAG_COMPOUND);
        }
        player.inventory.readFromNBT(inventoryTag);
        player.getEntityData().setTag(KEY_INV, actualInventory);
        user.sendMessage(GREEN + "Inventaire changé!");
    }
}
