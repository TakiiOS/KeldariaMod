package fr.nathanael2611.obfuscate.remastered.client;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public abstract class ModelPlayerEvent extends PlayerEvent
{
    private ModelPlayer modelPlayer;
    private float partialTicks;

    private ModelPlayerEvent(final EntityPlayer player, final ModelPlayer modelPlayer, final float partialTicks) {
        super(player);
        this.modelPlayer = modelPlayer;
        this.partialTicks = partialTicks;
    }

    public ModelPlayer getModelPlayer() {
        return this.modelPlayer;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    ModelPlayerEvent(final EntityPlayer x0, final ModelPlayer x1, final float x2, final ModelPlayerEvent x3) {
        this(x0, x1, x2);
    }

    @Cancelable
    public static class SetupAngles extends ModelPlayerEvent
    {
        private SetupAngles(final EntityPlayer player, final ModelPlayer modelPlayer, final float partialTicks) {
            super(player, modelPlayer, partialTicks, null);
        }

        SetupAngles(final EntityPlayer x0, final ModelPlayer x1, final float x2, final ModelPlayerEvent x3) {
            this(x0, x1, x2);
        }

        public static class Pre extends SetupAngles
        {
            public Pre(final EntityPlayer player, final ModelPlayer modelPlayer, final float partialTicks) {
                super(player, modelPlayer, partialTicks);
            }
        }

        public static class Post extends SetupAngles
        {
            public Post(final EntityPlayer player, final ModelPlayer modelPlayer, final float partialTicks) {
                super(player, modelPlayer, partialTicks);
            }

            public boolean isCancelable() {
                return false;
            }
        }
    }

    @Cancelable
    public static class Render extends ModelPlayerEvent
    {
        private Render(final EntityPlayer player, final ModelPlayer modelPlayer, final float partialTicks) {
            super(player, modelPlayer, partialTicks, null);
        }

        Render(final EntityPlayer x0, final ModelPlayer x1, final float x2, final ModelPlayerEvent x3)
        {
            this(x0, x1, x2);
        }

        public static class Pre extends Render
        {
            public Pre(final EntityPlayer player, final ModelPlayer modelPlayer, final float partialTicks)
            {
                super(player, modelPlayer, partialTicks);
            }
        }

        public static class Post extends Render
        {
            public Post(final EntityPlayer player, final ModelPlayer modelPlayer, final float partialTicks) {
                super(player, modelPlayer, partialTicks);
            }

            public boolean isCancelable() {
                return false;
            }
        }
    }
}