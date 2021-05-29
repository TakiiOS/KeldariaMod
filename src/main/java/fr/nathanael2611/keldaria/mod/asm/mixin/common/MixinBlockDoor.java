package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import fr.nathanael2611.keldaria.mod.features.lockpick.Lock;
import fr.nathanael2611.keldaria.mod.features.lockpick.LocksHelper;
import fr.nathanael2611.keldaria.mod.features.skill.EnumComplement;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketOpenGui;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockDoor.class)
public abstract class MixinBlockDoor extends Block
{

    @Shadow @Final public static PropertyEnum<BlockDoor.EnumDoorHalf> HALF;

    @Shadow @Final public static PropertyBool OPEN;

    @Shadow protected abstract int getOpenSound();

    @Shadow protected abstract int getCloseSound();

    public MixinBlockDoor(Material materialIn) {
        super(materialIn);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) return true;
        if(hand != playerIn.getActiveHand()) return false;

        BlockPos blockpos = state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
        IBlockState iblockstate = pos.equals(blockpos) ? state : worldIn.getBlockState(blockpos);

        if(playerIn.getDistance(pos.getX(), pos.getY(), pos.getZ()) > 2.5)
        {
            Helpers.sendPopMessage((EntityPlayerMP) playerIn, "§cLa porte est trop loin.", 1200);
            return false;
        }


        if(!(worldIn.getBlockState(pos.up()).getBlock() instanceof BlockDoor)) pos = pos.down();
        {
            if(iblockstate.getBlock() != this) return false;
            if(!iblockstate.getValue(OPEN))
            {
                final Lock lock = LocksHelper.getLockFromPos(pos);

                ItemStack stack = playerIn.getHeldItemMainhand();
                if (lock.isLocked)
                {
                    if ((stack.getItem() == KeldariaItems.KEY && LocksHelper.hasKeyId(stack)) || stack.getItem() == KeldariaItems.KEYRING)
                    {
                        if (/*LocksHelper.getKeyId(stack) != lock.getKeyid()*/!LocksHelper.isKeyValid(stack, lock.getKeyid()))
                        {
                            Helpers.sendPopMessage((EntityPlayerMP) playerIn, "§cMauvaise clef", 1200);
                            return false;
                        }
                        else
                        {
                            if(playerIn.isSneaking())
                            {
                                ItemStack stackToDrop = LocksHelper.createWithId(KeldariaItems.LOCK, lock.getKeyid());
                                EntityItem item = new EntityItem(playerIn.world, playerIn.posX, playerIn.posY + 0.5, playerIn.posZ, stackToDrop);
                                playerIn.world.spawnEntity(item);
                                LocksHelper.unLockDoor(pos);
                                Helpers.sendPopMessage((EntityPlayerMP) playerIn, "§aCadenas retiré", 1200);
                                return true;
                            }
                        }
                    }
                    else if(stack.getItem() == KeldariaItems.LOCKPICK)
                    {
                        if(EnumComplement.PICKING.has(playerIn))
                        {
                            KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketOpenGui(PacketOpenGui.LOCKPICK, Helpers.blockPosToString(pos)), Helpers.getPlayerMP(playerIn));
                        }
                        else
                        {
                            Helpers.sendPopMessage(Helpers.getPlayerMP(playerIn), "§cVous n'avez pas le complément Crochetage.", 2000);
                        }
                        return false;
                    }
                    else
                    {
                        Helpers.sendPopMessage((EntityPlayerMP) playerIn, "§cCette porte est fermée", 1200);
                        return false;
                    }
                }
                else
                {
                    if (stack.getItem() == KeldariaItems.LOCK)
                    {
                        if (LocksHelper.hasKeyId(stack))
                        {
                            LocksHelper.lockDoor(pos, LocksHelper.getKeyId(stack));
                            stack.shrink(1);
                            Helpers.sendPopMessage((EntityPlayerMP) playerIn, "§aCadenas posé sur la porte.", 1200);
                        }
                        return true;
                    }
                }
            }

            state = iblockstate.cycleProperty(OPEN);
            worldIn.setBlockState(blockpos, state, 10);
            worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
            worldIn.playEvent(playerIn, state.getValue(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
            return true;
        }
    }
}