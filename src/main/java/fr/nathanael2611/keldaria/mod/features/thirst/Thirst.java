/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.thirst;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.DatabaseReadOnly;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector3d;
import java.util.HashMap;

public class Thirst
{

    private static HashMap<EntityPlayer, Thirst> CACHE = new HashMap<>();

    private boolean client;

    private int thirstLevel = 20;
    private float hydration = 5.0F;
    private float thirstExhaustionLevel;
    private int timer;

    private Vector3d lastPos;

    private Thirst(EntityPlayer player)
    {
        this.client = player.world.isRemote;
        this.read(player);
    }

    public static Thirst getThirst(EntityPlayer player)
    {
        if(CACHE.containsKey(player) && CACHE.get(player) != null)
        {
            return CACHE.get(player);
        }
        CACHE.put(player, new Thirst(player));
        return CACHE.get(player);
    }

    @SideOnly(Side.CLIENT)
    public static Thirst getPersonnalThirst()
    {
        return new Thirst(Minecraft.getMinecraft().player);
    }

    public int getThirstLevel()
    {
        return thirstLevel;
    }

    public float getHydration()
    {
        return hydration;
    }

    public float getThirstExhaustionLevel()
    {
        return thirstExhaustionLevel;
    }

    public void addExhaustion(float exhaustion)
    {
        this.thirstExhaustionLevel = Math.min(this.thirstExhaustionLevel + exhaustion, 40.0F);
    }

    public boolean isThirsty()
    {
        return this.thirstLevel < 20;
    }

    public void setThirstLevel(int thirstLevel)
    {
        this.thirstLevel = thirstLevel;
    }

    public void setHydration(float hydration)
    {
        this.hydration = hydration;
    }

    public void onUpdate(EntityPlayer player, TickEvent.PlayerTickEvent.Phase phase)
    {
        if(player.isCreative() || player.isSpectator()) return;
        EnumDifficulty enumdifficulty = player.world.getDifficulty();
        //this.prevFoodLevel = this.foodLevel;

        if(phase == TickEvent.Phase.START)
        {
            if(this.lastPos != null)
            {
                Vector3d vec = new Vector3d(
                        player.posX, player.posY, player.posZ
                );
                vec.sub(this.lastPos); vec.absolute();
                int distance = (int) Math.round(vec.length() * 100);
                if(distance > 0)
                {
                    this.applyMovementExhaustion(player, distance);
                }
            }
        }
        else if(phase == TickEvent.Phase.END)
        {
            this.lastPos = new Vector3d(
                    player.prevPosX, player.prevPosY, player.prevPosZ
            );
            if (this.thirstExhaustionLevel > 4.0F)
            {
                this.thirstExhaustionLevel -= 4.0F;

                if (this.hydration > 0.0F)
                {
                    this.hydration = Math.max(this.hydration - 1.0F, 0.0F);
                }
                else if (enumdifficulty != EnumDifficulty.PEACEFUL)
                {
                    this.thirstLevel = Math.max(this.thirstLevel - 1, 0);
                }
            }

            if (this.thirstLevel <= 0)
            {
                ++this.timer;

                if (this.timer >= 80)
                {
                    if (player.getHealth() > 10.0F || enumdifficulty == EnumDifficulty.HARD || player.getHealth() > 1.0F && enumdifficulty == EnumDifficulty.NORMAL)
                    {
                        player.attackEntityFrom(DamageSource.STARVE, 1.0F);
                        player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 120));
                    }

                    this.timer = 0;
                }
            }
            else
            {
                this.timer = 0;
            }

            save(player);
        }
        else
        {
            this.timer = 0;
        }
        if (player.isSprinting() && thirstLevel <= 6)
        {
            player.setSprinting(false);
        }
    }

    private void applyMovementExhaustion(EntityPlayer player, double distance)
    {
        if (player.isInsideOfMaterial(Material.WATER))
        {
            this.addExhaustion(0.015F * (float)distance * 0.01F);
        }
        else if (player.isInWater())
        {
            this.addExhaustion(0.015F * (float)distance * 0.01F);
        }
        else if (player.onGround)
        {
            if (player.isSprinting())
            {
                this.addExhaustion(0.099999994F * (float)distance * 0.01F);
            }
            else
            {
                this.addExhaustion(0.01F * (float)distance * 0.01F);
            }
        }
    }

    public void drink(int thirstLevel, float hydration)
    {
        this.thirstLevel = Math.min(thirstLevel + this.thirstLevel, 20);
        this.hydration = Math.min(this.hydration + (float)thirstLevel * hydration * 2.0F, (float)this.thirstLevel);
    }

    private static final String THIRST_LEVEL = Keldaria.MOD_ID + ":ThirstLevel";
    private static final String THIRST_EXHAUSTION = Keldaria.MOD_ID + ":ThirstExhaustion";
    private static final String HYDRATION = Keldaria.MOD_ID + ":Hydration";
    private static final String THIRST_TIMER = Keldaria.MOD_ID + ":ThirstTimer";


    private void read(EntityPlayer player)
    {
        DatabaseReadOnly db = this.client ? ClientDatabases.getPersonalPlayerData() : Databases.getPlayerData(player);
        if(db.isInteger(THIRST_LEVEL)) this.thirstLevel = db.getInteger(THIRST_LEVEL);
        if(db.isFloat(THIRST_EXHAUSTION)) this.thirstExhaustionLevel = db.getFloat(THIRST_EXHAUSTION);
        if(db.isFloat(HYDRATION)) this.hydration = db.getFloat(HYDRATION);
        if(db.isInteger(THIRST_TIMER)) this.timer = db.getInteger(THIRST_TIMER);
    }

    private void save(EntityPlayer player)
    {
        Database db = Databases.getPlayerData(player);
        if(db.getInteger(THIRST_LEVEL) != this.thirstLevel) db.setInteger(THIRST_LEVEL, this.thirstLevel);
        if(db.getInteger(THIRST_EXHAUSTION) != this.thirstExhaustionLevel) db.setFloat(THIRST_EXHAUSTION, this.thirstExhaustionLevel);
        if(db.getInteger(HYDRATION) != this.hydration) db.setFloat(HYDRATION, this.hydration);
        if(db.getInteger(THIRST_TIMER) != this.timer) db.setInteger(THIRST_TIMER, this.timer);
    }

}
