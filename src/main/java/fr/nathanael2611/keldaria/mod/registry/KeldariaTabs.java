/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.registry;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.item.ItemCard;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

/**
 * This class define all of the mod creative tabs
 *
 * @author Nathanael2611
 */
public class KeldariaTabs
{

    public static final CreativeTabs KELDA_BAZAR = new CreativeTabs("keldaria.bazar.tab")
    {

        private ItemStack icon = ItemStack.EMPTY;
        private int iconTimer = 20;
        private long timeChanged = -1;

        @Override
        public ItemStack createIcon()
        {
            List<KeldariaRegistry.RegistryEntry<Item>> list = Keldaria.getInstance().getRegistry().getRegistriesFor(Item.class);
            return new ItemStack(list.get(
                    Helpers.randomInteger(1, list.size() -1)
            ).getObject());
        }

        @Override
        public ItemStack getIcon()
        {
            if(iconTimer == 0)
            {
                this.icon = this.createIcon();
            }
            if(timeChanged == -1)
            {
                timeChanged = System.currentTimeMillis();
            }
            if(System.currentTimeMillis() - timeChanged > 1000)
            {
                timeChanged = System.currentTimeMillis();
            }
            return this.icon;
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_)
        {
            super.displayAllRelevantItems(p_78018_1_);
            for (KeldariaRegistry.RegistryEntry<Item> entry : Keldaria.getInstance().getRegistry().getRegistriesFor(Item.class))
            {
                p_78018_1_.add(new ItemStack(entry.getObject()));
            }
            for (KeldariaRegistry.RegistryEntry<Block> entry : Keldaria.getInstance().getRegistry().getRegistriesFor(Block.class))
            {
                p_78018_1_.add(new ItemStack(entry.getObject()));
            }


        }

        @Override
        public boolean hasSearchBar()
        {
            return true;
        }
    };

    /**
     * The main creative tab
     */
    public static final CreativeTabs KELDARIA = new CreativeTabs("keldaria.tab")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(KeldariaItems.GOLD_COIN);
        }

        @Override
        public boolean hasSearchBar()
        {
            return true;
        }
    };
    public static final CreativeTabs FURNITURES = new CreativeTabs("keldaria.tab")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(KeldariaBlocks.OAK_CHAIR);
        }

        @Override
        public boolean hasSearchBar()
        {
            return true;
        }
    };
    public static final CreativeTabs APOTHECARY = new CreativeTabs("keldaria.apothecary.tab")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(KeldariaItems.GOLD_COIN);
        }

        @Override
        public boolean hasSearchBar()
        {
            return true;
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_)
        {
            super.displayAllRelevantItems(p_78018_1_);
            p_78018_1_.add(new ItemStack(KeldariaItems.NEEDLE));
        }
    };


    public static final CreativeTabs GAMES = new CreativeTabs("keldaria.games.tab")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(KeldariaItems.CARD);
        }

        @Override
        public boolean hasSearchBar()
        {
            return true;
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_)
        {

            super.displayAllRelevantItems(p_78018_1_);

            p_78018_1_.add(new ItemStack(KeldariaBlocks.CARD_DECK));
            p_78018_1_.add(new ItemStack(KeldariaItems.SLEEPING_MASK));


            p_78018_1_.add(new ItemStack(
                    Item.getItemFromBlock(KeldariaBlocks.CHESS_PLATE)
            ));


            p_78018_1_.add(new ItemStack(KeldariaItems.WHITE_PAWN));
            p_78018_1_.add(new ItemStack(KeldariaItems.WHITE_TOWER));
            p_78018_1_.add(new ItemStack(KeldariaItems.WHITE_HORSE));
            p_78018_1_.add(new ItemStack(KeldariaItems.WHITE_JESTER));
            p_78018_1_.add(new ItemStack(KeldariaItems.WHITE_KING));
            p_78018_1_.add(new ItemStack(KeldariaItems.WHITE_QUEEN));

            p_78018_1_.add(new ItemStack(KeldariaItems.BLACK_PAWN));
            p_78018_1_.add(new ItemStack(KeldariaItems.BLACK_TOWER));
            p_78018_1_.add(new ItemStack(KeldariaItems.BLACK_HORSE));
            p_78018_1_.add(new ItemStack(KeldariaItems.BLACK_JESTER));
            p_78018_1_.add(new ItemStack(KeldariaItems.BLACK_KING));
            p_78018_1_.add(new ItemStack(KeldariaItems.BLACK_QUEEN));

            p_78018_1_.add(new ItemStack(
                    Item.getItemFromBlock(KeldariaBlocks.CARD_CARPET)
            ));


            p_78018_1_.add(ItemCard.create("Chasseur", "http://keldaria.fr/skinhosting/cartes/lg/chasseur.png"));
            p_78018_1_.add(ItemCard.create("Cupidon", "http://keldaria.fr/skinhosting/cartes/lg/cupidon.png"));
            p_78018_1_.add(ItemCard.create("Loup-Garou", "http://keldaria.fr/skinhosting/cartes/lg/lg.png"));
            p_78018_1_.add(ItemCard.create("Salvateur", "http://keldaria.fr/skinhosting/cartes/lg/salvateur.png"));
            p_78018_1_.add(ItemCard.create("Voleur", "http://keldaria.fr/skinhosting/cartes/lg/voleur.png"));
            p_78018_1_.add(ItemCard.create("Villageois", "http://keldaria.fr/skinhosting/cartes/lg/villageois.png"));
            p_78018_1_.add(ItemCard.create("Sorcière", "http://keldaria.fr/skinhosting/cartes/lg/sorciere.png"));
            p_78018_1_.add(ItemCard.create("Voyante", "http://keldaria.fr/skinhosting/cartes/lg/voyante.png"));



            p_78018_1_.add(ItemCard.create("Monde [1]", "http://keldaria.fr/skinhosting/cartes/tarot/magicien.png"));
            p_78018_1_.add(ItemCard.create("Prêtresse [2]", "http://keldaria.fr/skinhosting/cartes/tarot/pretresse.png"));
            p_78018_1_.add(ItemCard.create("Imperatrice [3]", "http://keldaria.fr/skinhosting/cartes/tarot/imperatrice.png"));
            p_78018_1_.add(ItemCard.create("Empereur [4]", "http://keldaria.fr/skinhosting/cartes/tarot/empereur.png"));
            p_78018_1_.add(ItemCard.create("Le Pape [5]", "http://keldaria.fr/skinhosting/cartes/tarot/pape.png"));
            p_78018_1_.add(ItemCard.create("Amants [6]", "http://keldaria.fr/skinhosting/cartes/tarot/amants.png"));
            p_78018_1_.add(ItemCard.create("Chariot [7]", "http://keldaria.fr/skinhosting/cartes/tarot/chariot.png"));
            p_78018_1_.add(ItemCard.create("Force [8]", "http://keldaria.fr/skinhosting/cartes/tarot/force.png"));
            p_78018_1_.add(ItemCard.create("Ermite [9]", "http://keldaria.fr/skinhosting/cartes/tarot/ermite.png"));
            p_78018_1_.add(ItemCard.create("Roue [10]", "http://keldaria.fr/skinhosting/cartes/tarot/roue.png"));
            p_78018_1_.add(ItemCard.create("Justice [11]", "http://keldaria.fr/skinhosting/cartes/tarot/justice.png"));
            p_78018_1_.add(ItemCard.create("Pendu [12]", "http://keldaria.fr/skinhosting/cartes/tarot/pendu.png"));
            p_78018_1_.add(ItemCard.create("Mort [13]", "http://keldaria.fr/skinhosting/cartes/tarot/mort.png"));
            p_78018_1_.add(ItemCard.create("Tempérance [14]", "http://keldaria.fr/skinhosting/cartes/tarot/temperance.png"));
            p_78018_1_.add(ItemCard.create("Diable [15]", "http://keldaria.fr/skinhosting/cartes/tarot/diable.png"));
            p_78018_1_.add(ItemCard.create("Tour [16]", "http://keldaria.fr/skinhosting/cartes/tarot/tour.png"));
            p_78018_1_.add(ItemCard.create("Lune [17]", "http://keldaria.fr/skinhosting/cartes/tarot/lune.png"));
            p_78018_1_.add(ItemCard.create("Soleil [19]", "http://keldaria.fr/skinhosting/cartes/tarot/soleil.png"));
            p_78018_1_.add(ItemCard.create("Etoile [17]", "http://keldaria.fr/skinhosting/cartes/tarot/etoile.png"));
            p_78018_1_.add(ItemCard.create("Jugement [20]", "http://keldaria.fr/skinhosting/cartes/tarot/jugement.png"));
            p_78018_1_.add(ItemCard.create("Joker", "http://keldaria.fr/skinhosting/cartes/tarot/joker.png"));

        }
    };

    public static final CreativeTabs COMBAT = new CreativeTabs("keldaria.combat.tab")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(KeldariaItems.IRON_HELM);
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> items)
        {
            Item[] base = new Item[]{
                    KeldariaItems.GAMBESON,

                    KeldariaItems.CHAINMAIL_HOOD,
                    KeldariaItems.CHAINMAIL_CHESTPLATE,
                    KeldariaItems.CHAINMAIL_LEGGINGS,
                    KeldariaItems.CHAINMAIL_BOOTS,

                    KeldariaItems.IRON_HELM,
                    KeldariaItems.IRON_HELMET,
                    KeldariaItems.IRON_CHESTPLATE,
                    KeldariaItems.IRON_GORGERET,
                    KeldariaItems.IRON_ARMBAND,
                    KeldariaItems.IRON_PAULDRON,
                    KeldariaItems.IRON_LEGGINGS,
                    KeldariaItems.IRON_ELBOW_PADS,
                    KeldariaItems.IRON_BOOTS,

                    KeldariaItems.STEEL_HELM,
                    KeldariaItems.STEEL_HELMET,
                    KeldariaItems.STEEL_CHESTPLATE,
                    KeldariaItems.STEEL_GORGERET,
                    KeldariaItems.STEEL_ARMBAND,
                    KeldariaItems.STEEL_PAULDRON,
                    KeldariaItems.STEEL_LEGGINGS,
                    KeldariaItems.STEEL_ELBOW_PADS,
                    KeldariaItems.STEEL_BOOTS,

                    KeldariaItems.LONGBOW,
                    KeldariaItems.CROSSBOW,
                    KeldariaItems.QUIVER,

                    KeldariaItems.STEEL_SWORD,
                    KeldariaItems.STEEL_SPEAR,
                    KeldariaItems.STEEL_DAGGER,
                    KeldariaItems.STEEL_MACE,
                    KeldariaItems.IRON_MACE,


            };
            for (Item item : base)
            {
                items.add(new ItemStack(item));
            }
        }
    };


    public static final CreativeTabs COOKING = new CreativeTabs("keldaria.cooking.tab")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(KeldariaItems.FLAMBADOU);
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> items)
        {
            Item[] base = new Item[]{

                    KeldariaItems.FLAMBADOU,
                    Item.getItemFromBlock(KeldariaBlocks.COOKING_FURNACE),
                    Item.getItemFromBlock(KeldariaBlocks.COOKING_TABLE),
                    KeldariaItems.ANIMAL_GREASE,
                    KeldariaItems.FRIED_MUSHROOM_BOWL,
                    KeldariaItems.MUSHROOM_BOWL,
                    KeldariaItems.STUFFED_MUSHROOMS,
                    KeldariaItems.WHITE_ONION,
                    KeldariaItems.ONION,
                    KeldariaItems.ONION_SOUP,
                    KeldariaItems.APPLESAUCE,
                    KeldariaItems.POTAUFEU,
                    KeldariaItems.BEETROOT_BROTH,
                    KeldariaItems.FRIED_EGG,
                    KeldariaItems.FRIED_POTATOES,
                    KeldariaItems.MASHED_POTATOES,
                    KeldariaItems.OMELET,
                    KeldariaItems.FISH_PIE,
                    KeldariaItems.BACON,
                    KeldariaItems.COOKED_BACON,
                    KeldariaItems.GRAPES,
                    KeldariaItems.RAISIN_BREAD,
                    KeldariaItems.RAISIN_COOKIE,
                    KeldariaItems.APPLE_PIE,
                    KeldariaItems.BUTTER,
                    KeldariaItems.SHORTBREAD,
                    KeldariaItems.BREAD_DOUGH,
                    KeldariaItems.WINE_BOTTLE,
                    KeldariaItems.WINE_GLASS,
                    KeldariaItems.EMPTY_WINE_BOTTLE,
                    KeldariaItems.EMPTY_WINE_GLASS,
                    KeldariaItems.EMPTY_CHOP,
                    KeldariaItems.CLAY_CHOP,
                    KeldariaItems.BEER_CHOP,
                    KeldariaItems.WATER_CHOP,

                    KeldariaItems.FRESH_WATER
            };
            for (Item item : base)
            {
                items.add(new ItemStack(item));
            }
        }
    };
}
