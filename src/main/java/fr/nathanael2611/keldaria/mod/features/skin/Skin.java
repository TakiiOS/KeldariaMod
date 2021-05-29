package fr.nathanael2611.keldaria.mod.features.skin;

public class Skin
{
    private String name;
    private String link;

    public Skin(String name, String link)
    {
        this.name = name;
        this.link = link;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    @Override
    public String toString()
    {
        return name + "///" + link;
    }

    public boolean isDefault()
    {
        return (name.equalsIgnoreCase("") || name.equalsIgnoreCase("default")) && (link.equalsIgnoreCase("") || link.equalsIgnoreCase("default"));
    }

    public static Skin DEFAULT_SKIN = new Skin("default", "default");

    public static Skin fromString(String string)
    {
        String[] parts = string.split("///");
        if(parts.length == 2)
        {
            return new Skin(parts[0], parts[1]);
        }
        return DEFAULT_SKIN;
    }
}
