package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import net.minecraft.item.Item;

/**
 * This item is used to store the page content of a paper.
 *
 * @author Nathanael2611
 */
public class ItemWritedPaper extends Item {

    public static final String NBT_PAPER_CONTENT_ID = "paper_content";
    public static final String NBT_PAPER_AUTHOR_ID  = "author";

    public ItemWritedPaper(){
        setCreativeTab(KeldariaTabs.KELDARIA);
    }

}
