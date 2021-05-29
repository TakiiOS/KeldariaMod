package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.writable.FatBook;
import fr.nathanael2611.keldaria.mod.item.ItemFatBook;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class CommandSignFatBook extends KeldariaCommand
{
    public CommandSignFatBook()
    {
        super("signfatbook", "/signfatbook <title>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length > 0)
        {
            StringBuilder builder = new StringBuilder();
            for (String arg : args)
            {
                builder.append(" ").append(arg);
            }
            EntityPlayerMP player = user.asPlayer();
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() == KeldariaItems.FAT_BOOK)
            {
                FatBook book = ItemFatBook.getFatBook(stack);
                book.sign(builder.toString().substring(1));
                ItemFatBook.setFatBook(stack, book);
            }
        }
    }

    @Override
    public boolean isExecutableByAll()
    {
        return true;
    }
}
