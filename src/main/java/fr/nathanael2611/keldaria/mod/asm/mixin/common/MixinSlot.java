package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import fr.nathanael2611.keldaria.mod.asm.ISlotSwapAccessor;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Slot.class)
public abstract class MixinSlot implements ISlotSwapAccessor
{


    @Shadow public abstract void onSwapCraft(int p_190900_1_);

    @Override
    public void accessOnSwapCraft(int p_190900_1_) {
        onSwapCraft(p_190900_1_);
    }
}
