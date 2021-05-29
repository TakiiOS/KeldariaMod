package fr.nathanael2611.keldaria.mod.features.accessories;

import fr.nathanael2611.keldaria.mod.inventory.InventoryArmor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class Accessories implements INBTSerializable<NBTTagCompound>
{

    private String playerName;
    private InventoryAccessories accessories;
    private InventoryArmor armor;

    public Accessories(String playerName, InventoryAccessories accessories, InventoryArmor armor)
    {
        this.playerName = playerName;
        this.accessories = accessories;
        this.armor = armor != null ? armor : new InventoryArmor();
    }

    public Accessories(String playerName)
    {
        this(playerName, new InventoryAccessories(), new InventoryArmor());
    }

    public Accessories(NBTTagCompound compound)
    {
        this("deserialize");
        deserializeNBT(compound);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("PlayerName", this.playerName);
        compound.setTag("Accessories", accessories.saveInventoryToNBT());
        compound.setTag("Armor", armor.saveInventoryToNBT());
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        playerName = nbt.getString("PlayerName");
        accessories = new InventoryAccessories();
        accessories.loadInventoryFromNBT(nbt.getCompoundTag("Accessories"));
        armor = new InventoryArmor();
        armor.loadInventoryFromNBT(nbt.getCompoundTag("Armor"));
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public InventoryAccessories getInventory()
    {
        return accessories;
    }

    public InventoryArmor getArmor()
    {
        return armor;
    }
}
