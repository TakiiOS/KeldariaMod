
package fr.kerlann.tmt;

public class ModelBlock extends Model<Object>
{
    public ModelRendererTurbo[] bodyModel = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] model = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] bodyDoorOpenModel = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] bodyDoorCloseModel = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] turretModel = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] barrelModel = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] frontWheelModel = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] backWheelModel = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] leftFrontWheelModel = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] rightFrontWheelModel = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] leftBackWheelModel = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] rightBackWheelModel = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] rightTrackModel = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] leftTrackModel = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] rightTrackWheelModels = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] leftTrackWheelModels = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] trailerModel = new ModelRendererTurbo[0];
    public ModelRendererTurbo[] steeringWheelModel = new ModelRendererTurbo[0];

    public void render()
    {
        render(this.bodyModel);
        render(this.model);
        render(this.bodyDoorCloseModel);
        render(this.turretModel);
        render(this.barrelModel);
        render(this.frontWheelModel);
        render(this.backWheelModel);
        render(this.leftFrontWheelModel);
        render(this.rightFrontWheelModel);
        render(this.leftBackWheelModel);
        render(this.rightBackWheelModel);
        render(this.rightTrackModel);
        render(this.leftTrackModel);
        render(this.rightTrackWheelModels);
        render(this.leftTrackWheelModels);
        render(this.trailerModel);
        render(this.steeringWheelModel);
    }


    public void translateAll(float x, float y, float z)
    {
        translate(this.bodyModel, x, y, z);
        translate(this.model, x, y, z);
        translate(this.bodyDoorOpenModel, x, y, z);
        translate(this.bodyDoorCloseModel, x, y, z);
        translate(this.turretModel, x, y, z);
        translate(this.barrelModel, x, y, z);
        translate(this.frontWheelModel, x, y, z);
        translate(this.backWheelModel, x, y, z);
        translate(this.leftFrontWheelModel, x, y, z);
        translate(this.rightFrontWheelModel, x, y, z);
        translate(this.leftBackWheelModel, x, y, z);
        translate(this.rightBackWheelModel, x, y, z);
        translate(this.rightTrackModel, x, y, z);
        translate(this.leftTrackModel, x, y, z);
        translate(this.rightTrackWheelModels, x, y, z);
        translate(this.leftTrackWheelModels, x, y, z);
        translate(this.trailerModel, x, y, z);
        translate(this.steeringWheelModel, x, y, z);
    }

    public void rotateAll(float x, float y, float z)
    {
        rotate(this.bodyModel, x, y, z);
        rotate(this.model, x, y, z);
        rotate(this.bodyDoorOpenModel, x, y, z);
        rotate(this.bodyDoorCloseModel, x, y, z);
        rotate(this.turretModel, x, y, z);
        rotate(this.barrelModel, x, y, z);
        rotate(this.frontWheelModel, x, y, z);
        rotate(this.backWheelModel, x, y, z);
        rotate(this.leftFrontWheelModel, x, y, z);
        rotate(this.rightFrontWheelModel, x, y, z);
        rotate(this.leftBackWheelModel, x, y, z);
        rotate(this.rightBackWheelModel, x, y, z);
        rotate(this.rightTrackModel, x, y, z);
        rotate(this.leftTrackModel, x, y, z);
        rotate(this.rightTrackWheelModels, x, y, z);
        rotate(this.leftTrackWheelModels, x, y, z);
        rotate(this.trailerModel, x, y, z);
        rotate(this.steeringWheelModel, x, y, z);
    }

    public void flipAll()
    {
        flip(this.bodyModel);
        flip(this.model);
        flip(this.bodyDoorOpenModel);
        flip(this.bodyDoorCloseModel);
        flip(this.turretModel);
        flip(this.barrelModel);
        flip(this.frontWheelModel);
        flip(this.backWheelModel);
        flip(this.leftFrontWheelModel);
        flip(this.rightFrontWheelModel);
        flip(this.leftBackWheelModel);
        flip(this.rightBackWheelModel);
        flip(this.rightTrackModel);
        flip(this.leftTrackModel);
        flip(this.rightTrackWheelModels);
        flip(this.leftTrackWheelModels);
        flip(this.trailerModel);
        flip(this.steeringWheelModel);
    }
}