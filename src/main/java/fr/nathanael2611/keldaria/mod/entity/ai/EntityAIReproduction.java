package fr.nathanael2611.keldaria.mod.entity.ai;

import fr.nathanael2611.keldaria.mod.entity.animal.EntityKeldAnimal;
import fr.nathanael2611.keldaria.mod.entity.animal.Pregnancy;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class EntityAIReproduction extends EntityAIBase
{
    private final EntityKeldAnimal animal;
    private final Class<? extends EntityKeldAnimal> mateClass;
    private long lastSearch = -1;
    World world;
    private EntityKeldAnimal targetMate;
    int makeOutDelay;
    /**
     * The speed the creature moves at during mating behavior.
     */
    double moveSpeed;

    public EntityAIReproduction(EntityKeldAnimal animal, double speedIn)
    {
        this(animal, speedIn, animal.getClass());
    }

    public EntityAIReproduction(EntityKeldAnimal animal, double speed, Class<? extends EntityKeldAnimal> p_i47306_4_)
    {
        this.animal = animal;
        this.world = animal.world;
        this.mateClass = p_i47306_4_;
        this.moveSpeed = speed;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        //if (this.lastSearch == -1 || System.currentTimeMillis() - this.lastSearch > 10000)
        {
            if (!this.animal.isFertile())
            {
                return false;
            } else
            {
                this.targetMate = this.getNearbyMate();
                this.lastSearch = System.currentTimeMillis();
                return this.targetMate != null;
            }
        }
      //  return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return this.targetMate.isEntityAlive() && this.targetMate.isFertile() && this.makeOutDelay < 60;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.targetMate = null;
        this.makeOutDelay = 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {

        this.animal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, (float) this.animal.getVerticalFaceSpeed());
        this.animal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.makeOutDelay;

        if (this.makeOutDelay >= 60 && this.animal.getDistanceSq(this.targetMate) < 9.0D)
        {
            this.spawnParticles();
        }
    }

    /**
     * Loops through nearby animals and finds another animal of the same type that can be mated with. Returns the first
     * valid mate found.
     */
    private EntityKeldAnimal getNearbyMate()
    {
        List<EntityKeldAnimal> list = this.world.getEntitiesWithinAABB(this.mateClass, this.animal.getEntityBoundingBox().grow(8.0D));
        double d0 = Double.MAX_VALUE;
        EntityKeldAnimal entityanimal = null;

        for (EntityKeldAnimal entityanimal1 : list)
        {
            if (this.animal.canMateWith(entityanimal1) && this.animal.getDistanceSq(entityanimal1) < d0)
            {
                entityanimal = entityanimal1;
                d0 = this.animal.getDistanceSq(entityanimal1);
            }
        }

        return entityanimal;
    }

    /**
     * Spawns a baby animal of the same type.
     */
    private void spawnParticles()
    {
        this.getPregnant(this.animal);
        this.getPregnant(this.targetMate);

        Random random = this.animal.getRNG();

        for (int i = 0; i < 7; ++i)
        {
            double d0 = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            double d3 = random.nextDouble() * (double) this.animal.width * 2.0D - (double) this.animal.width;
            double d4 = 0.5D + random.nextDouble() * (double) this.animal.height;
            double d5 = random.nextDouble() * (double) this.animal.width * 2.0D - (double) this.animal.width;
            this.world.spawnParticle(EnumParticleTypes.HEART, this.animal.posX + d3, this.animal.posY + d4, this.animal.posZ + d5, d0, d1, d2);
        }
    }

    private void getPregnant(EntityKeldAnimal animal)
    {
        if (animal.canBePregnant())
        {
            EntityKeldAnimal baby = this.animal.createBaby(this.targetMate);
            long conceptionTime = baby.getConceptionTime();
            animal.getPregnant(new Pregnancy(System.currentTimeMillis(), conceptionTime, baby));
        }
        animal.makeOut();
    }
}
