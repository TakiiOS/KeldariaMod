/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features;

import fr.nathanael2611.keldaria.mod.block.BlockFlower;
import fr.nathanael2611.keldaria.mod.registry.KeldariaBlocks;
import net.minecraft.item.Item;

import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

public enum EnumFlower
{

    AIGREMOINE("aigremoine", "Aigremoine", getColor("B18C0C")),
    FENOUIL("fenouil", "Fenouil", getColor("9D5A20")),

    AGAVE_CHRYSANTHA("agave_chrysantha", "Agave Chrysantha", getColor("AA9300")),
    ROMARIN("romarin", "Romarin", getColor("6589E0")),
    AMINALIERE_YASHY("aminaliere_yashy", "Aminaliere Yashy", getColor("8D5A63")),
    BOUTON_DOR("bouton_dor", "Bouton d'Or", getColor("CE8F10")),
    AUGASTANOS("augastanos", "Augastanos", getColor("394AC9")),
    LYS("lys", "Lys", getColor("ABA7B1")),

    BELLADONE("belladone", "Belladone", getColor("A7A622")),
    PRIMEVERE("primevere", "Primevère", getColor("AB850D")),
    ITEAS("iteas", "Itéas", getColor("8A4A16")),
    CHICOREE("chicoree", "Chicorée", getColor("1B73A2")),
    ALTIOF_LIFUS("altiof_lifus", "Altiof Lifus", getColor("355865")),
    BENOITE("benoite", "Benoîte", getColor("906A10")),
    CYDONIA("cydonia", "Cydonia", getColor("A76E37")),
    BOUILLON_BLANC("bouillon_blanc", "Bouillon Blanc", getColor("D1AE88")),
    SAUGE("sauge", "Sauge", getColor("2D6685")),
    MAUVE("mauve", "Mauve", getColor("C591BC")),
    MILLEPERTUIS("millepertuis", "Millepertuis", getColor("EAC70B")),
    LYSOLIA("lysolia", "Lysolia", getColor("A27A00")),
    CELESTERE("celestere", "Célestère", getColor("CAB339")),
    MUGUET("muguet", "Muguet", getColor("E0DDD4")),
    POTENTILLE("potentille", "Potentille", getColor("E1C518")),
    PETIT_TOURNESOL("petit_tournesol", "Petit Tournesol", getColor("5F3A16")),
    CARCAPOTALIS("carcapotalis", "Carcapotalis", getColor("7D2D3A")),
    MOREA("morea", "Moréa", getColor("CECAA9")),
    ROSE_ETERNELLE("rose_eternelle", "Rose Eternelle", getColor("7B1216")),
    ROSE("rose", "Rose", getColor("5E0A0C")),
    ACONIT("aconit", "Aconit", getColor("6C47B6")),
    VIOLETTES("violettes", "Violettes", getColor("190929")),
    SPIREA("spirea", "Spiréa", getColor("B43B37")),
    EMIELLICITE("emiellicite", "Emiellicite", getColor("C44F89")),
    KYRGONIA("kyrgonia", "Kyrgonia", getColor("731818")),
    OCIMUM("ocimum", "Ocimum", getColor("00709F")),
    EUCALYSTE("eucalyste", "Eucalyste", getColor("360D0D")),
    AGASTACHE_FENOUIL("agastache_fenouil", "Agastache Fenouil", getColor("6C4A99")),
    SCABIEUSE("scabieuse", "Scabieuse", getColor("8A4F84")),
    PIMPRENELLE("pimprenelle", "Pimprenelle", getColor("5F4575")),
    FARENIUS("farenius", "Farenius", getColor("CCC9CF")),
    PIVOINE("pivoine", "Pivoine", getColor("851B12")),
    PAVOT("pavot", "Pavot", getColor("CFC2A2")),
    ANCOLIE("ancolie", "Ancolie", getColor("523E8B")),
    ALBIZIA("albizia", "Albizia", getColor("7B3236")),
    CLIFONIA("clifonia", "Clifonia", getColor("B36049")),
    VALIENTANIS("valientanis", "Valientanis", getColor("BA4FB9")),
    CYRILLIA("cyrillia", "Cyrillia", getColor("B05845")),
    VOLENIUS("volenius", "Volénius", getColor("95A5D5")),
    HEDERA("hedera", "Hedera", getColor("AB7730")),
    LAVANDE("lavande", "Lavande", getColor("9D4496")),
    STALIXIA("stalixia", "Stalixia", getColor("E365DF")),
    SOLPIRIA("solpiria", "Solpiria", getColor("A83343"));

    private String name;
    private String formattedName;
    private int color;
    private long growTime;

    EnumFlower(String name)
    {
        this(name, name);
    }

    EnumFlower(String name, String formattedName)
    {
        this(name, formattedName, 0);
    }

    EnumFlower(String name, String formattedName, int color)
    {
        this(name, formattedName, color, 172800000);
    }

    EnumFlower(String name, String formattedName, int color, long growTime)
    {
        this.name = name;
        this.formattedName = formattedName;
        this.color = color;
        this.growTime = growTime;
    }

    public long getGrowTime()
    {
        return growTime;
    }

    public String getName()
    {
        return name;
    }

    public String getFormattedName()
    {
        return formattedName;
    }

    public BlockFlower getBlock()
    {
        return KeldariaBlocks.FLOWERS.get(this.name);
    }

    public Item getItem()
    {
        return Item.getItemFromBlock(this.getBlock());
    }

    public void registerToList()
    {
        KeldariaBlocks.FLOWERS.put(this.name, new BlockFlower(this));
    }

    public static EnumFlower byName(String name)
    {
        Optional<EnumFlower> option = Arrays.stream(values()).filter(f -> f.name.equalsIgnoreCase(name)).findFirst();
        return option.orElse(null);
    }

    public int getColor()
    {
        return color;
    }

    public static int getColor(String color)
    {
        return Color.decode("#" + color.toLowerCase()).getRGB();
    }

}
