/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.entity.AnimalStat;
import fr.nathanael2611.keldaria.mod.entity.animal.Pregnancy;
import fr.nathanael2611.keldaria.mod.features.AnimalGender;
import fr.nathanael2611.keldaria.mod.features.food.ExpiredFoods;
import fr.nathanael2611.keldaria.mod.features.PlayerSizes;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.features.combat.BlockingSystem;
import fr.nathanael2611.keldaria.mod.features.combatstats.WeaponStat;
import fr.nathanael2611.keldaria.mod.features.cookingfurnace.BurntAliments;
import fr.nathanael2611.keldaria.mod.features.food.FoodQuality;
import fr.nathanael2611.keldaria.mod.features.food.capability.Rot;
import fr.nathanael2611.keldaria.mod.item.ItemSmeltedIngot;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.command.ICommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public class MixinHooks
{


    public static int colorMultiplier(java.util.Map<net.minecraftforge.registries.IRegistryDelegate<Item>, IItemColor> itemColorMap, ItemStack stack, int tintIndex)
    {
        IItemColor iitemcolor = itemColorMap.get(stack.getItem().delegate);

        int color = iitemcolor == null ? -1 : iitemcolor.colorMultiplier(stack, tintIndex);

        if(stack.getItem() instanceof ItemSmeltedIngot)
        {
            Color c = new Color(color);
            color = new Color(
                    (int) Math.max(0, Math.min(c.getRed(), 255)),
                    (int) Math.max(0, Math.min(c.getGreen()/ 1.7f, 255)),
                    (int) Math.max(0, Math.min(c.getBlue() / 2, 255))).getRGB();
        }
        else if(BurntAliments.isFumed(stack))
        {
            Color c = new Color(color);
            color = new Color(
                    (int) Math.max(0, Math.min(c.getRed() / 1.5f, 255)),
                    (int) Math.max(0, Math.min(c.getGreen() / 2, 255)),
                    (int) Math.max(0, Math.min(c.getBlue() / 2, 255))).getRGB();
        }
        if(BurntAliments.isBurned(stack))
        {
            color = new Color(color).darker().darker().darker().darker().darker().getRGB();
        }
        else if(stack.getItem() instanceof ItemFood)
        {
            if(ExpiredFoods.isExpired(stack) && ExpiredFoods.getRot(stack).getCreatedDay() != 0)
            {
                Color c = new Color(color);
                color = new Color(
                        (int) Math.max(0, Math.min(c.getRed() / 1.2f, 255)),
                        (int) Math.max(0, Math.min(c.getGreen(), 255)),
                        (int) Math.max(0, Math.min(c.getBlue() / 1.2, 255))).getRGB();
            }
        }

        return color;
    }

    public static float getHorsePassengerOffset(int passenger)
    {
        if(passenger == 0) return -0.35F;
        return -0.75f;
    }

    public static boolean canBlockDamageSource(EntityLivingBase base, DamageSource damageSourceIn)
    {
        if (!damageSourceIn.isUnblockable() && base.isActiveItemStackBlocking())
        {
            Vec3d vec3d = damageSourceIn.getDamageLocation();
            if (vec3d != null)
            {
                Vec3d vec3d1 = base.getLook(1.0F);
                Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(base.posX, base.posY, base.posZ)).normalize();
                vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

                double earned = Helpers.randomDouble(0.006, 0.04);

                int rand = Helpers.RANDOM.nextInt(100);
                //int chances = base.getActiveItemStack().getItem() instanceof ItemShield ? 60 : 30;
                int chances = base instanceof EntityPlayer ? BlockingSystem.getBlockChances((EntityPlayer) base, damageSourceIn.getTrueSource() ) : 0;
                if(base instanceof EntityPlayer)
                {
                    int resistance = EnumAptitudes.RESISTANCE.getPoints((EntityPlayer) base);
                    chances += resistance * 10;
                }
                if(damageSourceIn.getTrueSource() instanceof EntityPlayer)
                {
                    EntityPlayer player = (EntityPlayer) damageSourceIn.getTrueSource();
                    int strength = EnumAptitudes.STRENGTH.getPoints(player);
                    chances -= strength * 3;


                }

                if (vec3d2.dotProduct(vec3d1) < 0.0D && rand < chances)
                {
                    if(!(base.getActiveItemStack().getItem() instanceof ItemShield))
                    {
                        int amount = 2;
                        if(damageSourceIn.getTrueSource() instanceof EntityPlayer)
                        {
                            EntityPlayer player = (EntityPlayer) damageSourceIn.getTrueSource();
                            int strength = EnumAptitudes.STRENGTH.getPoints(player);
                            amount = 1 + strength;

                            if(base instanceof EntityPlayer)
                            {
                                WeaponStat stat = WeaponStat.getBlockingStat((EntityPlayer) base);
                                double defenserStat = stat.getLevel((EntityPlayer) base);
                                double attackStat = stat.getLevel(player);
                                earned *= (attackStat / 3);

                                stat.naturalIncrement((EntityPlayer) base, earned);
                            }
                        }
                        base.getActiveItemStack().damageItem(amount, base);
                    }
                    return true;
                }
            }
        }

        return false;
    }



    public static final DataSerializer<AnimalGender> GENDER = new DataSerializer<AnimalGender>()
    {
        public void write(PacketBuffer buf, AnimalGender value)
        {
            buf.writeInt(value.getId());
        }
        public AnimalGender read(PacketBuffer buf) throws IOException
        {
            return AnimalGender.byId(buf.readInt());
        }
        public DataParameter<AnimalGender> createKey(int id)
        {
            return new DataParameter<>(id, this);
        }
        public AnimalGender copyValue(AnimalGender value)
        {
            return value;
        }
    };

    public static final DataSerializer<AnimalStat> ANIMAL_STATS = new DataSerializer<AnimalStat>()
    {
        public void write(PacketBuffer buf, AnimalStat value)
        {
            ByteBufUtils.writeTag(buf, value.serializeNBT());
        }
        public AnimalStat read(PacketBuffer buf) throws IOException
        {
            return new AnimalStat(ByteBufUtils.readTag(buf));
        }
        public DataParameter<AnimalStat> createKey(int id)
        {
            return new DataParameter<>(id, this);
        }
        public AnimalStat copyValue(AnimalStat value)
        {
            return value;
        }
    };

    public static final DataSerializer<Pregnancy> PREGNANCY = new DataSerializer<Pregnancy>()
    {
        public void write(PacketBuffer buf, Pregnancy value)
        {
            ByteBufUtils.writeTag(buf, value.serializeNBT());
        }
        public Pregnancy read(PacketBuffer buf) throws IOException
        {
            return new Pregnancy(ByteBufUtils.readTag(buf));
        }
        public DataParameter<Pregnancy> createKey(int id)
        {
            return new DataParameter<>(id, this);
        }
        public Pregnancy copyValue(Pregnancy value)
        {
            return value;
        }
    };

    public static final DataSerializer<Long> LONG = new DataSerializer<Long>()
    {
        public void write(PacketBuffer buf, Long value)
        {
            buf.writeLong(value.longValue());
        }
        public Long read(PacketBuffer buf) throws IOException
        {
            return buf.readLong();
        }
        public DataParameter<Long> createKey(int id)
        {
            return new DataParameter<Long>(id, this);
        }
        public Long copyValue(Long value)
        {
            return value;
        }
    };
    public static final DataSerializer<Double> DOUBLE = new DataSerializer<Double>()
    {
        public void write(PacketBuffer buf, Double value)
        {
            buf.writeDouble(value.doubleValue());
        }
        public Double read(PacketBuffer buf) throws IOException
        {
            return buf.readDouble();
        }
        public DataParameter<Double> createKey(int id)
        {
            return new DataParameter<Double>(id, this);
        }
        public Double copyValue(Double value)
        {
            return value;
        }
    };

    public static final DataParameter<Double> HORSE_FOOD = EntityDataManager.<Double>createKey(EntityHorse.class, MixinHooks.DOUBLE);
    public static final DataParameter<Double> HORSE_THIRST = EntityDataManager.<Double>createKey(EntityHorse.class, MixinHooks.DOUBLE);
    public static final DataParameter<Double> HORSE_CLEANILESS = EntityDataManager.<Double>createKey(EntityHorse.class, MixinHooks.DOUBLE);
    public static final DataParameter<Integer> HORSE_GENDER = EntityDataManager.<Integer>createKey(EntityHorse.class, DataSerializers.VARINT);
    public static final DataParameter<ItemStack> HORSE_STORAGE_1 = EntityDataManager.<ItemStack>createKey(EntityHorse.class, DataSerializers.ITEM_STACK);
    public static final DataParameter<ItemStack> HORSE_STORAGE_2 = EntityDataManager.<ItemStack>createKey(EntityHorse.class, DataSerializers.ITEM_STACK);
    public static final DataParameter<ItemStack> HORSE_STORAGE_3 = EntityDataManager.<ItemStack>createKey(EntityHorse.class, DataSerializers.ITEM_STACK);
    public static final DataParameter<ItemStack> HORSE_STORAGE_4 = EntityDataManager.<ItemStack>createKey(EntityHorse.class, DataSerializers.ITEM_STACK);


    static
    {
        //DataSerializers.registerSerializer(DOUBLE);
    }

    public static boolean isCommandHide(EntityPlayer sender, ICommand icommand)
    {
        return                                     Databases.getPlayerData(sender.getName()).isString("hidedCommands") &&
                Databases.getPlayerData(sender.getName()).getString("hidedCommands").contains(icommand.getName());
    }

    public static GenericFutureListener<? extends Future<? super Void>> createNetHandlerNessecity(NetHandlerLoginServer login)
    {

        return new ChannelFutureListener()
        {
            public void operationComplete(ChannelFuture p_operationComplete_1_) throws Exception
            {
                login.networkManager.setCompressionThreshold(login.server.getNetworkCompressionThreshold());
            }
        };

    }

    public static GameProfile isLoggedIn(String token)
    {
        URL obj = null;
        try
        {
            obj = new URL(Keldaria.API_URL + "?action=checkConnection&accessToken=" + token);
            JsonObject json = new JsonParser().parse(Helpers.readResponse(obj)).getAsJsonObject();
            String state = json.get("state").getAsString();
            if(state.equalsIgnoreCase("success"))
            {
                return new GameProfile(
                        UUID.fromString(json.get("uuid").getAsString()),
                        json.get("id").getAsString()
                );
            }
            else
            {
                return null;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;

    }

    public static GameProfile getSinglePlayerProfile()
    {
        return Minecraft.getMinecraft().getSession().getProfile();
    }

    public static void applyHeldItemRotates(EntityLivingBase base, ItemStack stack, ItemCameraTransforms.TransformType yay)
    {
        if(base instanceof EntityPlayer)
        {
            double scale = PlayerSizes.get((EntityPlayer) base);
            GlStateManager.scale(1/scale, 1/scale, 1/scale);
        }
        /*
        if(base instanceof EntityPlayer)
        {
            FightControl<EntityPlayer> control = FightHandler.getFightControl(base);
            if (control != null && control.isAttacking())
            {
                GlStateManager.rotate(-90, 1, 0, 0);
                if(control.getAttack().getSwingTypes() == SwingTypes.BY_UP_RIGHT || control.getAttack().getSwingTypes() == SwingTypes.BY_UP_LEFT)
                {
                    GlStateManager.rotate(50, 1, 0, 0);

                }
                GlStateManager.translate(0, 0, -0.2);
                GlStateManager.translate(-0.1, 0, 0);
            }
        }*/

    }

    public static void onFoodUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(entityIn instanceof EntityPlayer)
        {
            if(!FoodQuality.isDefined(stack))
            {
                FoodQuality.init((EntityPlayer) entityIn, stack);
            }
        }
        Rot rot = ExpiredFoods.getRot(stack);
        if (rot.getCreatedDay() == 0)
        {
            ExpiredFoods.create(stack);
        }
        if(rot.isInBag())
        {
            rot.extractFromBag();
        }

    }
}
