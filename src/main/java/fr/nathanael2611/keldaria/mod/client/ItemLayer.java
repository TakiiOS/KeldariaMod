package fr.nathanael2611.keldaria.mod.client;

import fr.nathanael2611.keldaria.mod.client.model.ModelCustomItemTexture;
import fr.nathanael2611.keldaria.mod.client.model.custom.Transforms;

public class ItemLayer
{

    public Transforms transforms;
    public String url;

    public ItemLayer(String url, Transforms transforms)
    {
        this.transforms = transforms;
        this.url = url;
    }

    public void apply()
    {
        if(this.url == null) return;
        this.transforms.apply();
        ModelCustomItemTexture model = NewItemTextureCache.getModel(url, null);
        if(model != null)
        {

            model.render();
        }
    }

    public static ItemLayer from(String str)
    {
        String[] parts = str.split("=");
        String url = "";
        Transforms transforms = new Transforms();
        if(parts.length == 2)
        {
            url = parts[0];
            transforms = new Transforms(parts[1]);
        }
        return new ItemLayer(url, transforms);
    }

}
