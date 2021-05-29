package fr.nathanael2611.keldaria.mod.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemHookBlade extends Item {

    private float attackDamage;
    private final ToolMaterial material;
    private static final String __OBFID = "CL_00000072";
    boolean printed;
    private int bedZ;
    public int enemyFacing;
    public int ownerFacing;

    public ItemHookBlade(ToolMaterial material)
    {
        this.material = material;
        this.maxStackSize = 1;
        setMaxDamage(material.getMaxUses());
        this.attackDamage = 8.0F + material.getAttackDamage();
    }



    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase enemy, EntityLivingBase owner)
    {
        this.ownerFacing = MathHelper.floor((owner.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;

        if (this.ownerFacing == 1)
        {
            if (owner.isSprinting()) {

                owner.motionX = -2.0D;
                owner.motionY = (enemy.height / 4.0F);
                enemy.motionX = 1.0D;
            }
            else {

                owner.motionX = -1.0D;
                owner.motionY = (enemy.height / 4.0F);
                enemy.motionX = 1.0D;
            }
        }

        if (this.ownerFacing == 2)
        {
            if (owner.isSprinting()) {

                owner.motionZ = -2.0D;
                owner.motionY = (enemy.height / 4.0F);
                enemy.motionZ = 1.0D;
            }
            else {

                owner.motionZ = -1.0D;
                owner.motionY = (enemy.height / 4.0F);
                enemy.motionZ = 1.0D;
            }
        }

        if (this.ownerFacing == 3)
        {
            if (owner.isSprinting()) {

                owner.motionX = 2.0D;
                owner.motionY = (enemy.height / 4.0F);
                enemy.motionX = -1.0D;
            }
            else {

                owner.motionX = 1.0D;
                owner.motionY = (enemy.height / 4.0F);
                enemy.motionX = -1.0D;
            }
        }

        if (this.ownerFacing == 0)
        {
            if (owner.isSprinting()) {

                owner.motionZ = 2.0D;
                owner.motionY = (enemy.height / 4.0F);
                enemy.motionZ = -1.0D;
            }
            else {

                owner.motionZ = 1.0D;
                owner.motionY = (enemy.height / 4.0F);
                enemy.motionZ = -1.0D;
            }
        }

        itemstack.damageItem(1, owner);
        return true;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        if (playerIn.collidedHorizontally && !playerIn.isSneaking()) {

            playerIn.addExhaustion(0.8F);
            this.ownerFacing = MathHelper.floor((playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;

            if (this.ownerFacing == 1)
            {
                if (playerIn.isSprinting()) {

                    playerIn.motionX = -2.0D;
                    playerIn.motionY = 1.0D;
                }
                else {

                    playerIn.motionX = -1.0D;
                    playerIn.motionY = 0.6000000238418579D;
                }
            }

            if (this.ownerFacing == 2)
            {
                if (playerIn.isSprinting()) {

                    playerIn.motionZ = -2.0D;
                    playerIn.motionY = 1.0D;
                }
                else {

                    playerIn.motionZ = -1.0D;
                    playerIn.motionY = 0.6000000238418579D;
                }
            }

            if (this.ownerFacing == 3)
            {
                if (playerIn.isSprinting()) {

                    playerIn.motionX = 2.0D;
                    playerIn.motionY = 1.0D;
                }
                else {

                    playerIn.motionX = 1.0D;
                    playerIn.motionY = 0.6000000238418579D;
                }
            }

            if (this.ownerFacing == 0)
            {
                if (playerIn.isSprinting()) {

                    playerIn.motionZ = 2.0D;
                    playerIn.motionY = 1.0D;
                }
                else {

                    playerIn.motionZ = 1.0D;
                    playerIn.motionY = 0.6000000238418579D;
                }
            }

            playerIn.getHeldItemMainhand().damageItem(1, playerIn);
        }
        return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItemMainhand());
    }










    public float getDamageVsEntity() { return this.material.getAttackDamage(); }



    public float getStrVsBlock(ItemStack stack, Block block) {
        if (block == Blocks.WEB)
        {
            return 15.0F;
        }


        Material material = block.getMaterial(null);
        return (material != Material.PLANTS && material != Material.VINE && material != Material.CORAL && material != Material.LEAVES && material != Material.GOURD) ? 1.0F : 1.5F;
    }


    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (worldIn.getBlockState(pos).getBlock().getBlockHardness(null, worldIn, pos) != 0.0D)
        {
            stack.damageItem(2, entityLiving);
        }

        return true;
    }







    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() { return false; }







    @Override
    public EnumAction getItemUseAction(ItemStack stack) { return EnumAction.BLOCK; }






    @Override
    public int getMaxItemUseDuration(ItemStack stack) { return 72000; }


    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return (blockIn == Blocks.WEB);
    }







    @Override
    public int getItemEnchantability() { return this.material.getEnchantability(); }










    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        ItemStack mat = this.material.getRepairItemStack();
        if (mat != null && OreDictionary.itemMatches(mat, repair, false)) return true;
        return super.getIsRepairable(toRepair, repair);
    }


    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap multimap = getItemAttributeModifiers(null);
        multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.attackDamage, 0));
        return multimap;
    }




}
