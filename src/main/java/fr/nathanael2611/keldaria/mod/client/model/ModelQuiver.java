/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.model;

import fr.kerlann.tmt.ModelRendererTurbo;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelQuiver extends ModelBase
{

    int textureX = 64;
    int textureY = 64;

    public ModelQuiver()
    {
        yourclassnameModel = new ModelRendererTurbo[4];
        yourclassnameModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 3
        yourclassnameModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 4
        yourclassnameModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 7
        yourclassnameModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 8

        yourclassnameModel[0].addShapeBox(0F, 0F, 0F, 3, 13, 1, 0F, 3F, -2F, 0F, -2F, 0F, 0F, -2F, 0F, -1F, 3F, -2F, -1F, -3F, 0F, 0F, 4F, -2F, 0F, 4F, -2F, 0F, -3F, 0F, 0F); // Box 3
        yourclassnameModel[0].setRotationPoint(-3F, -12F, 2F);

        yourclassnameModel[1].addShapeBox(0F, 0F, 0F, 3, 13, 1, 0F, 3F, -2F, -1F, -2F, 0F, -1F, -2F, 0F, 0F, 3F, -2F, 0F, -3F, 0F, 1F, 4F, -2F, 1F, 4F, -2F, 0F, -3F, 0F, 0F); // Box 4
        yourclassnameModel[1].setRotationPoint(-3F, -12F, 4F);

        yourclassnameModel[2].addShapeBox(0F, 0F, 0F, 3, 13, 1, 0F, -5F, -3F, 2F, 2F, -2F, 2F, 2F, -2F, 0F, -5F, -3F, 0F, -11F, 0F, 2F, 8F, 0F, 2F, 8F, 0F, 0F, -11F, 0F, 0F); // Box 7
        yourclassnameModel[2].setRotationPoint(-11F, -12F, 4F);

        yourclassnameModel[3].addShapeBox(0F, 0F, 0F, 3, 13, 1, 0F, -3F, -5F, 2F, 1F, -6F, 2F, 1F, -6F, 0F, -3F, -5F, 0F, -9F, 3F, 2F, 6F, 3F, 2F, 6F, 3F, 0F, -9F, 3F, 0F); // Box 8
        yourclassnameModel[3].setRotationPoint(-5F, -17F, 4F);


    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        for(int i = 0; i < 4; i++)
        {
            yourclassnameModel[i].render(f5);
        }
    }

    public void render(float scale)
    {
        for(int i = 0; i < 4; i++)
        {
            yourclassnameModel[i].render(scale);
        }
    }


    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
    }

    public ModelRendererTurbo yourclassnameModel[];

}
