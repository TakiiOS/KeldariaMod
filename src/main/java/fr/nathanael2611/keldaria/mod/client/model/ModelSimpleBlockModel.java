package fr.nathanael2611.keldaria.mod.client.model;

import fr.kerlann.tmt.ModelRendererTurbo;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelSimpleBlockModel extends ModelBase
{
	int textureX = 128;
	int textureY = 128;

	public ModelSimpleBlockModel()
	{
		simpleblockmodelModel = new ModelRendererTurbo[1];
		simpleblockmodelModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		simpleblockmodelModel[0].addBox(0F, 0F, 0F, 16, 16, 16, 0F); // Box 0
		simpleblockmodelModel[0].setRotationPoint(0F, -16F, 0F);


	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		for(int i = 0; i < 1; i++)
		{
			simpleblockmodelModel[i].render(f5);
		}
	}

	public void render(float scale)
	{
		for(int i = 0; i < 1; i++)
		{
			simpleblockmodelModel[i].render(scale);
		}
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
	{
	}

	public ModelRendererTurbo simpleblockmodelModel[];
}