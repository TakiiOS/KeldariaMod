package fr.nathanael2611.keldaria.mod.tileentity;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.features.rot.ExpiredFoods;
import fr.nathanael2611.keldaria.mod.features.cookingfurnace.BurntAliments;
import fr.nathanael2611.keldaria.mod.features.cookingfurnace.CookingRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;

public class TileEntityCookingFurnace extends TileEntity implements ITickable, IInventory
{

    private NonNullList<ItemStack> furnaceItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);

    private int lastTotalCookTime;
    private ItemStack lastEntry = ItemStack.EMPTY;

    private int furnaceCookTime;

    private int burnTime;

    private CookingRegistry cookingRegistry = Keldaria.getInstance().getRegistry().getCookingRegistry();

    public int totalBurnTime;

    @Override
    public void update()
    {
        //if(world.isRemote) return;
        if(getEntryCopy().isEmpty() && furnaceItemStacks.get(2).isEmpty()) furnaceCookTime = 0;
        if(isBurning())
        {
            burnTime --;

            if(this.cookingRegistry.containsCookEntry(this.getLastEntryCopy().getItem()))
            {

                this.furnaceCookTime ++;

                if(this.furnaceCookTime >= this.getItemCookTimeWithBurn(this.getLastEntryCopy())) this.furnaceCookTime = this.getItemCookTimeWithBurn(this.getLastEntryCopy());

                if(this.furnaceCookTime == this.getItemCookTime(this.getLastEntryCopy())) this.cook();

                if (this.furnaceCookTime >= this.getItemCookTimeWithBurn(this.getLastEntryCopy()))
                {
                    if(!this.isResultBurnt()) this.burnt();
                }
            }
            if(!this.getEntryCopy().isEmpty()) this.lastEntry = getEntryCopy();
        } else {
            if(TileEntityFurnace.isItemFuel(getFuelCopy()) && (!getStackInSlot(2).isEmpty() || this.cookingRegistry.containsCookEntry(getEntryCopy().getItem())))
            {
                int b = TileEntityFurnace.getItemBurnTime(this.getFuelCopy());
                this.burnTime += b * 1.2;
                this.totalBurnTime = this.burnTime;
                ItemStack stack = this.furnaceItemStacks.get(1);
                stack.shrink(1);
                this.furnaceItemStacks.set(1, stack);
            }
        }
        NBTTagCompound actualNBT = writeToNBT(new NBTTagCompound());
        if(!this.world.isRemote && !lastSendedNBT.toString().equals(actualNBT.toString()))
        {
            lastSendedNBT = actualNBT;
            markDirty();
            IBlockState state = world.getBlockState(this.pos);
            this.world.notifyBlockUpdate(this.pos, state, state, 3);
            //this.sendTopStacksPacket();
        }
    }

    private NBTTagCompound lastSendedNBT = new NBTTagCompound();

    public void cook()
    {
        if(this.cookingRegistry.containsCookEntry(getEntryCopy().getItem()))
        {
            ItemStack result = this.cookingRegistry.getCookableItem(getEntryCopy().getItem()).getResult();
            ExpiredFoods.create(result);
            furnaceItemStacks.set(2, result);
            furnaceItemStacks.set(0, ItemStack.EMPTY);
        }
    }

    public void burnt()
    {
        ItemStack burned = furnaceItemStacks.get(2);
        if(burned.getItem() instanceof ItemFood)
        {
            NBTTagCompound compound = burned.getTagCompound();
            if (compound == null) compound = new NBTTagCompound();
            compound.setBoolean(BurntAliments.BURNT_KEY, true);
            burned.setTagCompound(compound);
            furnaceItemStacks.set(2, burned);
            this.world.playEvent(2004, this.pos, 0);
            this.world.playEvent(1009, this.pos, 0);
        }
    }

    public boolean isResultBurnt()
    {
        ItemStack result = furnaceItemStacks.get(2).copy();
        if(result.hasTagCompound() && result.getTagCompound() != null)
        {
            if(result.getTagCompound().hasKey(BurntAliments.BURNT_KEY))
            {
                return result.getTagCompound().getBoolean(BurntAliments.BURNT_KEY);
            }
        }
        return false;
    }

    public boolean isBurning()
    {
        return burnTime > 0;
    }

    public ItemStack getEntryCopy()
    {
        return furnaceItemStacks.get(0).copy();
    }

    public ItemStack getLastEntryCopy()
    {
        return lastEntry.copy();
    }

    public ItemStack getFuelCopy()
    {
        return furnaceItemStacks.get(1).copy();
    }


    public int getFurnaceCookTime()
    {
        return furnaceCookTime;
    }

    public int getBurnTime()
    {
        return burnTime;
    }

    public int getItemCookTime(ItemStack stack)
    {
        if(this.cookingRegistry.containsCookEntry(stack.getItem()))
        {
            return this.cookingRegistry.getCookableItem(stack.getItem()).getCookingTime();
        }
        return 200;
    }

    public int getItemCookTimeWithBurn(ItemStack stack)
    {
        return this.getItemCookTime(stack) + ((/*20 **/ this.getItemCookTime(stack)) / 2);
    }

    /*
     * All the IInventory methods
     */

    @Override
    public int getSizeInventory() {
        return furnaceItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.furnaceItemStacks) {
            if (!itemstack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.furnaceItemStacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.furnaceItemStacks, index, count);
    }

    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.furnaceItemStacks, index);
    }
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.furnaceItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.furnaceItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            //this.totalCookTime = this.getFurnaceCookTime(stack);
            this.markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        if (index == 2)
        {
            return false;
        }
        else if (index != 1)
        {
            return true;
        }
        else
        {
            ItemStack itemstack = this.furnaceItemStacks.get(1);
            return TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && itemstack.getItem() != Items.BUCKET;
        }
    }
    @Override
    public int getField(int id) { return 0; }
    @Override
    public void setField(int id, int value) { }
    @Override
    public int getFieldCount() { return 0; }
    @Override
    public void clear() { this.furnaceItemStacks.clear(); }
    @Override
    public String getName() { return "Four de cuisine"; }
    @Override
    public boolean hasCustomName() { return false; }


    /**
     * Saving and reading
     */


    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.clear();
        this.furnaceItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.furnaceItemStacks);
        this.burnTime = compound.getInteger("BurnTime");
        this.furnaceCookTime = compound.getInteger("CookTime");
        this.totalBurnTime = compound.getInteger("TotalBurnTime");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", this.burnTime);
        compound.setInteger("CookTime", (short)this.furnaceCookTime);
        compound.setInteger("TotalBurnTime", this.totalBurnTime);
        ItemStackHelper.saveAllItems(compound, this.furnaceItemStacks);
        return compound;
    }

    protected void sendTopStacksPacket()
    {
        //NonNullList<ItemStack> stacks = this.buildItemStackDataList();
        //KeldariaPacketHandler.getInstance().getNetwork().sendToAllAround(new PacketSyncCookingFurnace(this, stacks), new NetworkRegistry.TargetPoint(world.provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128));
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity packet)
    {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    public NonNullList<ItemStack> buildItemStackDataList()
    {

        NonNullList<ItemStack> sortList = NonNullList.<ItemStack> withSize(this.furnaceItemStacks.size(), ItemStack.EMPTY);

        int pos = 0;

        for (ItemStack is : this.furnaceItemStacks)
        {
            if (!is.isEmpty())
            {
                sortList.set(pos, is.copy());
            }
            else {
                sortList.set(pos, ItemStack.EMPTY);
            }

            pos++;
        }

        return sortList;
    }

    public void receiveMessageFromServer(NonNullList<ItemStack> topStacks)
    {
        this.furnaceItemStacks = topStacks;
    }

}
