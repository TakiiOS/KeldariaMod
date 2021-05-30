/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.clothe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.asm.IKeldariaPlayer;
import fr.nathanael2611.keldaria.mod.features.accessories.InventoryAccessories;
import fr.nathanael2611.keldaria.mod.features.cleanliness.CleanilessManager;
import fr.nathanael2611.keldaria.mod.inventory.InventoryArmor;
import fr.nathanael2611.keldaria.mod.item.ItemClothe;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketSendClothes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.List;

public class ClothesManager implements INBTSerializable<NBTTagCompound>
{

    public HashMap<String, Clothes> playerClothes = Maps.newHashMap();

    private boolean server = false;

    public ClothesManager(Side side)
    {
        this.server = side == Side.SERVER;
    }

    public ClothesManager collect()
    {
        /*if(this.server)*/
        {
            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(player ->
            {
                IKeldariaPlayer keldariaPlayer = (IKeldariaPlayer) player;
                List<String> urls = Lists.newArrayList();

                double cleaniless = CleanilessManager.getCleanilessValue(player);

                if(cleaniless < 20)
                {
                    urls.add("http://keldaria.fr/skinhosting/dirt_skin_20.png");
                }
                else if(cleaniless < 40) urls.add("http://keldaria.fr/skinhosting/dirt_skin_40.png");
                else if(cleaniless < 60) urls.add("http://keldaria.fr/skinhosting/dirt_skin_60.png");
                else if(cleaniless < 80) urls.add("http://keldaria.fr/skinhosting/dirt_skin_80.png");


                for (int i = keldariaPlayer.getInventoryCloths().getSizeInventory(); i >= 9; i --)
                {
                    ItemStack stack = keldariaPlayer.getInventoryCloths().getStackInSlot(i);
                    if(ItemClothe.isClothValid(stack))
                    {
                        urls.add(ItemClothe.getClothURL(stack));
                    }
                }



                boolean[] armorOverrides = new boolean[4];
                {
                    ItemStack helm = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                    ItemStack chestplate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                    ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
                    ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                    armorOverrides[0] = ItemClothe.isClothValid(helm);
                    armorOverrides[1] = ItemClothe.isClothValid(chestplate);
                    armorOverrides[2] = ItemClothe.isClothValid(leggings);
                    armorOverrides[3] = ItemClothe.isClothValid(boots);
                    if(armorOverrides[2])
                    {
                        urls.add(ItemClothe.getClothURL(leggings));
                    }
                    if(armorOverrides[1])
                    {
                        urls.add(ItemClothe.getClothURL(chestplate));
                    }
                    if(armorOverrides[3])
                    {
                        urls.add(ItemClothe.getClothURL(boots));
                    }
                    if(armorOverrides[0])
                    {
                        urls.add(ItemClothe.getClothURL(helm));
                    }
                }

                {
                    InventoryArmor inventoryArmor = keldariaPlayer.getInventoryArmor();
                    for (ItemStack stack : inventoryArmor.getArmor(EntityEquipmentSlot.LEGS))
                    {
                        if(ItemClothe.isClothValid(stack)) urls.add(ItemClothe.getClothURL(stack));
                    }
                    for (ItemStack stack : inventoryArmor.getArmor(EntityEquipmentSlot.CHEST))
                    {
                        if(ItemClothe.isClothValid(stack)) urls.add(ItemClothe.getClothURL(stack));
                    }
                    for (ItemStack stack : inventoryArmor.getArmor(EntityEquipmentSlot.FEET))
                    {
                        if(ItemClothe.isClothValid(stack)) urls.add(ItemClothe.getClothURL(stack));
                    }
                    for (ItemStack stack : inventoryArmor.getArmor(EntityEquipmentSlot.HEAD))
                    {
                        if(ItemClothe.isClothValid(stack)) urls.add(ItemClothe.getClothURL(stack));
                    }
                }

                for (int i = keldariaPlayer.getInventoryCloths().getSizeInventory() / 2; i >= 0; i --)
                {
                    ItemStack stack = keldariaPlayer.getInventoryCloths().getStackInSlot(i);
                    if(ItemClothe.isClothValid(stack))
                    {
                        urls.add(ItemClothe.getClothURL(stack));
                    }
                }

                InventoryAccessories acc = ((IKeldariaPlayer)player).getInventoryAccessories();
                if(!acc.canSee())
                {


                }
                if(ItemClothe.isClothValid(acc.getAccessory(EntityEquipmentSlot.HEAD)))
                {
                    urls.add(ItemClothe.getClothURL(acc.getAccessory(EntityEquipmentSlot.HEAD)));
                }
                if(ItemClothe.isClothValid(acc.getAccessory(EntityEquipmentSlot.CHEST)))
                {
                    urls.add(ItemClothe.getClothURL(acc.getAccessory(EntityEquipmentSlot.CHEST)));
                }
                if(ItemClothe.isClothValid(acc.getAccessory(EntityEquipmentSlot.LEGS)))
                {
                    urls.add(ItemClothe.getClothURL(acc.getAccessory(EntityEquipmentSlot.LEGS)));
                }
                if(ItemClothe.isClothValid(acc.getAccessory(EntityEquipmentSlot.FEET)))
                {
                    urls.add(ItemClothe.getClothURL(acc.getAccessory(EntityEquipmentSlot.FEET)));
                }

                this.playerClothes.put(player.getName(), new Clothes(player.getName(), urls, armorOverrides));
            });
        }
        return this;
    }

    public ClothesManager sync()
    {
        /*if(this.server)*/
        {
            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(player -> KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketSendClothes(), player));
        }
        return this;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        this.playerClothes.forEach((playerName, clothe) ->
        {
            list.appendTag(clothe.serializeNBT());
        });
        compound.setTag("Clothes", list);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.playerClothes.clear();
        NBTTagList list = nbt.getTagList("Clothes", Constants.NBT.TAG_COMPOUND);
        list.forEach(nbtBase ->
        {
            NBTTagCompound compound = (NBTTagCompound) nbtBase;
            Clothes clothes = new Clothes(compound);
            this.playerClothes.put(clothes.getPlayerName(), clothes);
        });
    }

    public Clothes getClothes(String playerName)
    {
        return this.playerClothes.getOrDefault(playerName, empty(playerName));
    }

    private Clothes empty(String playerName)
    {
        return new Clothes(playerName, Lists.newArrayList(), new boolean[]{false, false, false, false});
    }
}
