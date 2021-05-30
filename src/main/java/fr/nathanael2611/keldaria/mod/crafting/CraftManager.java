/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.crafting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.crafting.storage.KnownRecipesStorage;
import fr.nathanael2611.keldaria.mod.crafting.storage.api.IKnownRecipes;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.features.skill.EnumComplement;
import fr.nathanael2611.keldaria.mod.features.skill.EnumJob;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketOpenCraftManager;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class CraftManager implements INBTSerializable<NBTTagCompound>
{

    public static final HashMap<String, CraftManager> MANAGERS = Maps.newHashMap();

    public static void createManager(String managerName)
    {
        MANAGERS.put(managerName, new CraftManager(managerName));
    }

    public static void openManager(String managerName, EntityPlayerMP player)
    {
        if (MANAGERS.containsKey(managerName))
        {
            CraftManager manager = MANAGERS.get(managerName);
            PlayerCrafts crafts = PlayerCrafts.retrieve(manager, player);
            KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketOpenCraftManager(crafts), player);
        }
    }

    public static IKnownRecipes getRecipes(EntityPlayer player)
    {
        IKnownRecipes recipes = player.getCapability(KnownRecipesStorage.CAPABILITY_KNOWN_RECIPES, null);
        return recipes;
    }

    public static List<String> getKnownRecipes(EntityPlayer player)
    {
        IKnownRecipes recipes = getRecipes(player);
        return recipes != null ? recipes.getKnownKeys() : Lists.newArrayList();
    }


    public static CraftManager getManager(String manager)
    {
        if (MANAGERS.containsKey(manager))
        {
            return MANAGERS.get(manager);
        }
        return null;
    }


    private String name;
    private HashMap<Item, Item> itemsContainers = Maps.newHashMap();
    private HashMap<String, CraftEntry> craftEntries = Maps.newHashMap();
    private HashMap<String, CraftEntry> supCraftEntries = Maps.newHashMap();

    private HashMap<String, List<String>> infoMap = Maps.newHashMap();

    private CraftManager(String name)
    {
        this.name = name;
        this.craftEntries = Maps.newHashMap();
        this.reload();
    }

    public void registerCraftEntry(CraftEntry entry)
    {
        supCraftEntries.put(entry.getKey(), entry);
        craftEntries.put(entry.getKey(), entry);
    }

    public void reload()
    {
        this.craftEntries.clear();
        File file = new File(Keldaria.getInstance().getFiles().CONFIG_DIR, "/craft/" + this.name + ".json");
        if (file.exists())
        {
            String json = Helpers.readFileToString(file);
            JsonObject object = new JsonParser().parse(json).getAsJsonObject();
            { /* Read all itemContainers */
                if (object.has("containers"))
                {
                    JsonObject obj = object.get("containers").getAsJsonObject();
                    obj.entrySet().forEach(entry ->
                    {
                        Item prev = Item.REGISTRY.getObject(new ResourceLocation(entry.getKey()));
                        Item container = Item.REGISTRY.getObject(new ResourceLocation(entry.getValue().getAsString()));
                        this.itemsContainers.put(prev, container);

                    });
                }
            }

            { /* Read all crafts across the yeet */
                JsonObject crafts = object.get("crafts").getAsJsonObject();
                crafts.entrySet().forEach(entry ->
                {
                    String cat = entry.getKey();
                    this.infoMap.put(cat, Lists.newArrayList());
                    if (entry.getValue().isJsonObject())
                    {
                        JsonObject craftListObj = entry.getValue().getAsJsonObject();
                        craftListObj.entrySet().forEach(craftEntry ->
                        {
                            if (craftEntry.getValue().isJsonObject())
                            {
                                JsonObject craftObj = craftEntry.getValue().getAsJsonObject();
                                String key = String.format("%s/%s/%s", this.getName(), cat, craftEntry.getKey());
                                this.infoMap.get(cat).add(craftEntry.getKey());
                                JsonArray ingredientsArray = craftObj.getAsJsonArray("ingredients");
                                HashMap<CraftIngredient, Integer> ingredients = Maps.newHashMap();
                                ingredientsArray.forEach(jsonElement1 ->
                                {
                                    String str = jsonElement1.getAsString();
                                    String[] divised1 = str.split("/");
                                    if (divised1.length == 2)
                                    {
                                        String[] divised2 = divised1[0].split("=");
                                        ingredients.put(new CraftIngredient(Item.REGISTRY.getObject(new ResourceLocation(divised2[0])), Integer.parseInt(divised2[1])), Integer.parseInt(divised1[1]));
                                    }
                                });
                                ItemStack result = Helpers.getItemStackFromString(craftObj.get("result").getAsString());
                                int toolDamage = craftObj.has("toolDamage") ? craftObj.get("toolDamage").getAsInt() : 0;
                                String description = craftObj.has("description") ? craftObj.get("description").getAsString() : "";

                                JsonArray aptitudesArray = craftObj.getAsJsonArray("aptitudes");
                                HashMap<EnumAptitudes, Integer> aptitudes = Maps.newHashMap();
                                if (aptitudesArray != null)
                                {
                                    aptitudesArray.forEach(jsonElement1 ->
                                    {
                                        String str = jsonElement1.getAsString();
                                        String[] parts = str.split("=");
                                        if (parts.length == 2)
                                        {
                                            EnumAptitudes aptitude = EnumAptitudes.byName(parts[0]);
                                            if (aptitude != null) aptitudes.put(aptitude, Integer.parseInt(parts[1]));
                                        }
                                    });
                                }


                                JsonArray possibleSkillsArray = craftObj.getAsJsonArray("possibleSkills");
                                List<PossibleWith> possibleSkills = Lists.newArrayList();
                                if (possibleSkillsArray != null)
                                {
                                    for (JsonElement jsonElement : possibleSkillsArray)
                                    {

                                        String yeet = jsonElement.getAsString();
                                        String[] split = yeet.split(":");
                                        if (split.length == 2)
                                        {
                                            EnumJob job = EnumJob.byName(split[0]);
                                            EnumJob.Level level = EnumJob.Level.byId(Helpers.parseOrZero(split[1]));
                                            if (job != null)
                                            {
                                                possibleSkills.add(new PossibleWith(job, level));
                                            }
                                        } else
                                        {
                                            EnumComplement complement = EnumComplement.byName(yeet);
                                            if (complement != null)
                                            {
                                                possibleSkills.add(new PossibleWith(complement));
                                            }
                                        }

                                    }
                                }

                                this.craftEntries.put(key, new CraftEntry(key, aptitudes, ingredients, result, toolDamage, description, possibleSkills));


                            }
                        });

                    }
                });
            }
            /*
            JsonArray array = object.get("crafts").getAsJsonArray();
            array.forEach(jsonElement ->
            {
                JsonObject craftObj = jsonElement.getAsJsonObject();
                JsonArray ingredientsArray = craftObj.getAsJsonArray("ingredients");
                HashMap<CraftIngredient, Integer> ingredients = Maps.newHashMap();
                ingredientsArray.forEach(jsonElement1 ->
                {
                    String str = jsonElement1.getAsString();
                    String[] divised1 = str.split("/");
                    if (divised1.length == 2)
                    {
                        String[] divised2 = divised1[0].split("=");
                        ingredients.put(new CraftIngredient(Item.REGISTRY.getObject(new ResourceLocation(divised2[0])), Integer.parseInt(divised2[1])), Integer.parseInt(divised1[1]));
                    }
                });
                ItemStack result = Helpers.getItemStackFromString(craftObj.get("result").getAsString());
                int toolDamage = craftObj.get("toolDamage").getAsInt();
                String description = craftObj.has("description") ? craftObj.get("description").getAsString() : "";

                JsonArray aptitudesArray = craftObj.getAsJsonArray("aptitudes");
                HashMap<EnumAptitudes, Integer> aptitudes = Maps.newHashMap();
                if (aptitudesArray != null)
                {
                    aptitudesArray.forEach(jsonElement1 ->
                    {
                        String str = jsonElement1.getAsString();
                        String[] parts = str.split("=");
                        if (parts.length == 2)
                        {
                            EnumAptitudes aptitude = EnumAptitudes.byName(parts[0]);
                            if (aptitude != null) aptitudes.put(aptitude, Integer.parseInt(parts[1]));
                        }
                    });
                }

                this.craftEntries.add(new CraftEntry(aptitudes, ingredients, result, toolDamage, description));

            });*/
        }
        this.craftEntries.putAll(this.supCraftEntries);
    }

    public CraftManager()
    {
    }

    public String getName()
    {
        return name;
    }

    public HashMap<String, CraftEntry> getSupCraftEntries()
    {
        return supCraftEntries;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("Name", this.name);

        StringBuilder containersBuilder = new StringBuilder();
        this.itemsContainers.forEach((item, container) ->
        {
            containersBuilder.append(item.getRegistryName().toString()).append("=").append(container.getRegistryName().toString()).append("/");
        });
        compound.setString("ItemContainers", containersBuilder.toString().substring(0, containersBuilder.toString().length() - 1));


        NBTTagList list = new NBTTagList();
        //craftEntries.forEach(entry -> list.appendTag(entry.serializeNBT()));
        craftEntries.forEach((key, entry) -> list.appendTag(entry.serializeNBT()));
        compound.setTag("Entries", list);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound)
    {
        this.name = compound.getString("Name");

        String[] itemContainersString = compound.getString("ItemContainers").split("/");
        HashMap<Item, Item> itemContainersMap = Maps.newHashMap();
        for (String s : itemContainersString)
        {
            String[] parts = s.split("=");
            Item prev = Item.REGISTRY.getObject(new ResourceLocation(parts[0]));
            Item container = Item.REGISTRY.getObject(new ResourceLocation(parts[1]));
            itemContainersMap.put(prev, container);
        }
        this.itemsContainers = itemContainersMap;

        NBTTagList list = compound.getTagList("Entries", Constants.NBT.TAG_COMPOUND);
        list.forEach(nbtBase ->
        {
            if (nbtBase instanceof NBTTagCompound)
            {
                CraftEntry entry = new CraftEntry();
                entry.deserializeNBT((NBTTagCompound) nbtBase);
                this.craftEntries.put(entry.getKey(), entry);
            }
        });
    }

    public HashMap<String, CraftEntry> getCraftEntries()
    {
        return craftEntries;
    }

    public CraftManager copy()
    {
        CraftManager manager = new CraftManager();
        manager.deserializeNBT(this.serializeNBT());
        return manager;
    }

    public ItemStack getContainer(Item item)
    {
        return new ItemStack(this.itemsContainers.getOrDefault(item, Items.AIR));
    }

    public CraftEntry getCraft(String key)
    {
        return this.craftEntries.getOrDefault(key, null);
    }

    public HashMap<String, List<String>> getInfoMap()
    {
        return infoMap;
    }
}
