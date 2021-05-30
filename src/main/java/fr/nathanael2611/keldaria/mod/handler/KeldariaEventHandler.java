/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.handler;

import com.google.common.collect.Maps;
import de.mennomax.astikorcarts.entity.AbstractDrawn;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.animation.Animation;
import fr.nathanael2611.keldaria.mod.animation.AnimationUtils;
import fr.nathanael2611.keldaria.mod.asm.IKeldariaAnimal;
import fr.nathanael2611.keldaria.mod.asm.IKeldariaPlayer;
import fr.nathanael2611.keldaria.mod.asm.ILivingHorse;
import fr.nathanael2611.keldaria.mod.command.CommandDebugMode;
import fr.nathanael2611.keldaria.mod.features.*;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.features.cleanliness.CleanilessManager;
import fr.nathanael2611.keldaria.mod.features.combat.BlockingSystem;
import fr.nathanael2611.keldaria.mod.features.combat.EntityStats;
import fr.nathanael2611.keldaria.mod.features.combat.EnumAttackType;
import fr.nathanael2611.keldaria.mod.features.combatstats.WeaponStat;
import fr.nathanael2611.keldaria.mod.features.containment.Containment;
import fr.nathanael2611.keldaria.mod.features.cookingfurnace.BurntAliments;
import fr.nathanael2611.keldaria.mod.features.lockpick.Lock;
import fr.nathanael2611.keldaria.mod.features.lockpick.LocksHelper;
import fr.nathanael2611.keldaria.mod.features.rot.ExpiredFoods;
import fr.nathanael2611.keldaria.mod.features.rot.capability.Rot;
import fr.nathanael2611.keldaria.mod.features.skill.EnumJob;
import fr.nathanael2611.keldaria.mod.features.thirst.Thirst;
import fr.nathanael2611.keldaria.mod.features.writable.WritablePaper;
import fr.nathanael2611.keldaria.mod.inventory.InventoryArmor;
import fr.nathanael2611.keldaria.mod.inventory.InventoryQuiver;
import fr.nathanael2611.keldaria.mod.item.*;
import fr.nathanael2611.keldaria.mod.item.accessory.ItemQuiver;
import fr.nathanael2611.keldaria.mod.log.PlayerSpy;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketOpenPaperWriting;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketSendPopMessage;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketUpdateClimbSystem;
import fr.nathanael2611.keldaria.mod.proxy.CommonProxy;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.season.Season;
import fr.nathanael2611.keldaria.mod.server.WaitingHorses;
import fr.nathanael2611.keldaria.mod.server.WhitelistManager;
import fr.nathanael2611.keldaria.mod.server.chat.voicechatbridge.KeldariaVoices;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityWallpaper;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.keldaria.mod.util.math.Vector2i;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;
import java.sql.ParameterMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class KeldariaEventHandler
{

    private Keldaria keldaria;

    public KeldariaEventHandler(Keldaria keldaria)
    {
        this.keldaria = keldaria;
    }

    private HashMap<String, Double> lastReachs = Maps.newHashMap();

    /**
     * This event is used to resize the player's collision box according to his
     * size as well as the animation he plays
     */
    @SubscribeEvent
    public void playerTickEventResize(TickEvent.PlayerTickEvent e)
    {
        EntityPlayer player = e.player;
        if (player == null) return;

        /* Thirst logic */
        if (!player.world.isRemote)
        {
            Thirst.getThirst(player).onUpdate(player, e.phase);

            {
                AxisAlignedBB axisAlignedBB = player.getEntityBoundingBox().grow(2.0D, 1D, 2.0D);


                List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, axisAlignedBB);

                for (Entity entity : list)
                {
                    if (entity instanceof EntityItem && !entity.isDead)
                    {
                        entity.onCollideWithPlayer(player);
                    }
                }
            }

        }
        /* Thirst logic end */

        {
            double reach = (player.isRiding() ? 4 : 3) * PlayerSizes.get(player);
            ItemStack stack = player.getHeldItemMainhand();
            if (stack.getItem() instanceof ItemTool) reach *= 1.2;
            else if (stack.getItem() instanceof ItemSword)
            {
                if (stack.getItem() instanceof ItemDagger)
                {
                    reach *= 0.8;
                } else if (stack.getItem() instanceof ItemSpear)
                {
                    reach *= 1.4;
                } else reach *= 1.01;
            }
            if (player.isCreative()) reach = 6;
            if (player.isSneaking()) reach += 0.5;

            if(player.getHorizontalFacing() == EnumFacing.SOUTH)
            {
                reach += 0.2;
            }

            player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(reach);
        }

        int agility = EnumAptitudes.AGILITY.getPoints(player);
        if (!player.world.isRemote)
        {
            double speed = 0.1D - 0.010;
            speed += agility * 0.009;
            IKeldariaPlayer keldariaPlayer = (IKeldariaPlayer) player;
            {
                double armorSpeedModifier = 0;
                for (int i = 0; i < keldariaPlayer.getInventoryArmor().getSizeInventory(); i++)
                {
                    ItemStack armorStar = keldariaPlayer.getInventoryArmor().getStackInSlot(i);
                    if (armorStar.getItem() instanceof ItemArmorPart)
                    {
                        ItemArmorPart part = (ItemArmorPart) armorStar.getItem();
                        armorSpeedModifier -= part.getSpeedModifier();
                    }
                }
                armorSpeedModifier = armorSpeedModifier * (1 - (0.1 * (EnumAptitudes.STRENGTH.getPoints(player) + EnumAptitudes.RESISTANCE.getPoints(player))));
                speed += armorSpeedModifier;
            }
            if (WalkMode.isPlayerIsWalkingMode(player))
            {
                speed = 0.07;
            }
            IAttributeInstance pSpeed = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            if (!player.isRiding())
            {
                pSpeed.setBaseValue(speed);
            }
            if (player.isRiding())
            {
                Entity entity = player.getRidingEntity();
                if (entity instanceof AbstractHorse)
                {
                    AbstractHorse horse = (AbstractHorse) entity;
                    IAttributeInstance instance = horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                    if (WalkMode.isPlayerIsWalkingMode(player))
                    {
                        if (!instance.hasModifier(AbstractDrawn.PULL_SLOWLY_MODIFIER))
                        {
                            instance.applyModifier(AbstractDrawn.PULL_SLOWLY_MODIFIER);
                        }
                    } else
                    {
                        if (instance.hasModifier(AbstractDrawn.PULL_SLOWLY_MODIFIER))
                        {
                            instance.removeModifier(AbstractDrawn.PULL_SLOWLY_MODIFIER);
                        }
                    }
                }
            }
        }

        Animation animation = AnimationUtils.getPlayerHandledAnimation(player);

        /* TEMPORARY START
         * This code will not persist here
         */
        if (player.world.isRemote)
        {
            if (player.getName().equals(Minecraft.getMinecraft().player.getName()))
            {
                if (animation.isElongated())
                {
                    Minecraft.getMinecraft().player.movementInput.sneak = true;
                }
            }
        }
        /* TEMPORARY END
         */

        float scale = (float) PlayerSizes.get(player);
        float width = animation.getWidth(scale);
        float height = animation.getHeight(scale);
        AxisAlignedBB maxBound = animation.getAnimationHitbox(player, scale);
        float futureEyeHeight = 0;
        boolean client = player.world.isRemote;
        EntityLivingBase entityLivingBase = MorphingManager.getMorphAsEntity(player, client);
        //System.out.println(player.world.isRemote + " : " + entityLivingBase);
        //System.out.println(player.world.isRemote + " : " + entityLivingBase);

        if (MorphingManager.isMorphed(player, client) && entityLivingBase != null)
        {
            //entityLivingBase.setPosition(player.posX, player.posY, player.posZ);
            width = entityLivingBase.width * scale;
            height = entityLivingBase.height * scale;
            maxBound = Animation.createHitboxByDimensions(player, width, height);
        } else
        {
            if (player.isSneaking() && animation.isSneakModifyHitbox())
            {
                maxBound = animation.getAnimationSneakingHitbox(player, scale);
                height = height - height / 5;
                futureEyeHeight = animation.getSneakingEyeHeight(scale);
            } else
            {
                if (player.world.isRemote && player.world.collidesWithAnyBlock(new AxisAlignedBB(maxBound.minX, maxBound.minY + animation.getEyeHeight(scale), maxBound.minZ, maxBound.maxX, maxBound.maxY, maxBound.maxZ)))
                {

                    if (player.world.isRemote)
                    {
                        if (player.getName().equals(Minecraft.getMinecraft().player.getName()))
                        {
                            if (!player.isRiding())
                            {
                                Minecraft.getMinecraft().player.movementInput.sneak = true;
                            }
                        }
                    }

                } else
                {
                    futureEyeHeight = animation.getEyeHeight(scale);
                }
            }
        }

        if (MorphingManager.isMorphed(player, client) && entityLivingBase != null)
        {
            player.eyeHeight = entityLivingBase.getEyeHeight() * scale;
        } else
        {
            if (player.eyeHeight > futureEyeHeight)
            {
                player.eyeHeight = Math.max(player.eyeHeight - /*0.045f*/ player.eyeHeight / 68, animation.getSneakingEyeHeight(scale));
            } else if (player.eyeHeight < futureEyeHeight)
            {
                player.eyeHeight = Math.min(player.eyeHeight + player.eyeHeight / 40, animation.getEyeHeight(scale));
            }
        }

        player.setEntityBoundingBox(maxBound);
        player.width = width;
        player.height = height;
        player.getEntityBoundingBox().setMaxY(maxBound.maxY);
    }


    @SubscribeEvent
    public void onRepair(AnvilUpdateEvent event)
    {

        System.out.println(event.getMaterialCost());
        if(true)
        {//TODO: YEET
            return;
        }
        if(event.getLeft().getItem() instanceof ItemArmorPart)
        {
            if(event.getRight().getItem() == Items.IRON_INGOT)
            {
                ItemStack repaired = event.getLeft().copy();
                repaired.setItemDamage(0);
                event.setCost(1);

                event.setOutput(repaired);
            }
        }

    }

    @SubscribeEvent
    public void onRepair(AnvilRepairEvent event)
    {

    }

    /**
     * Just unlock the position where the door is placed
     *
     * @param e the event
     */
    @SubscribeEvent
    public void onDoorPlace(BlockEvent.PlaceEvent e)
    {
        if (KeldariaVoices.isTheoricallyTalking(e.getPlayer()))
        {
            if (!KeldariaVoices.hearTroughWalls(e.getWorld()))
            {
                KeldariaVoices.reloadHearList(e.getPlayer());
            }
        }
        if (e.getPlacedBlock().getBlock() instanceof BlockDoor)
        {
            BlockPos pos = !(e.getWorld().getBlockState(e.getPos().up()).getBlock() instanceof BlockDoor) ? e.getPos().down() : e.getPos();
            LocksHelper.unLockDoor(pos);
        }
    }

    /**
     * Unlock door and spawn the lock's
     *
     * @param e the event
     */
    @SubscribeEvent
    public void onDoorRemove(BlockEvent.BreakEvent e)
    {
        if (KeldariaVoices.isTheoricallyTalking(e.getPlayer()))
        {
            if (!KeldariaVoices.hearTroughWalls(e.getWorld()))
            {
                KeldariaVoices.reloadHearList(e.getPlayer());
            }
        }
        if (e.getState().getBlock() instanceof BlockDoor)
        {
            BlockPos pos = !(e.getWorld().getBlockState(e.getPos().up()).getBlock() instanceof BlockDoor) ? e.getPos().down() : e.getPos();
            Lock lock = LocksHelper.getLockFromPos(pos);
            if (!lock.isLocked) return;
            ItemStack stackToDrop = LocksHelper.createWithId(KeldariaItems.LOCK, lock.getKeyid());
            EntityItem item = new EntityItem(e.getWorld(), e.getPlayer().posX, e.getPlayer().posY + 0.5, e.getPlayer().posZ, stackToDrop);
            e.getWorld().spawnEntity(item);
            LocksHelper.unLockDoor(pos);
        }
        if (SharpeningHelpers.canBeSharped(e.getPlayer().getHeldItemMainhand()))
        {
            SharpeningHelpers.decrementSharpness(e.getPlayer().getHeldItemMainhand(), Helpers.RANDOM.nextInt(5));
        }


    }



    @SubscribeEvent
    public void onUse(LivingEntityUseItemEvent.Tick event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            //Yeet
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack stack = event.getItem();
            if (stack.getItem() instanceof ItemCrossbow)
            {
                int i = stack.getMaxItemUseDuration() - player.getItemInUseCount();
                if (i > 20)
                {
                    player.stopActiveHand();
                    player.resetActiveHand();
                    player.getCooldownTracker().setCooldown(stack.getItem(), 10);
                }
            }
        }
    }

    @SubscribeEvent
    public void onUse(LivingEntityUseItemEvent.Start event)
    {
        ItemStack stack = event.getItem();
        if (stack.getItem() instanceof ItemFood)
        {
            if (event.getEntityLiving() instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                IKeldariaPlayer keldariaPlayer = (IKeldariaPlayer) player;
                if (keldariaPlayer.getInventoryArmor().isWearingHelm())
                {
                    if (!player.world.isRemote)
                    {
                        Helpers.sendPopMessage((EntityPlayerMP) player, "§cImpossible de manger avec un heaume", 2000);
                        if (CommandDebugMode.isInDebugMode(player.getName()))
                        {
                            player.getFoodStats().setFoodLevel(40);
                            player.getFoodStats().setFoodSaturationLevel(40);
                        }
                    }
                    event.setCanceled(true);
                }
            }
        }


    }

    @SubscribeEvent
    public void onEat(LivingEntityUseItemEvent.Finish e)
    {
        if (e.getEntityLiving().world.isRemote) return;
        ItemStack stack = e.getItem();
        if (stack.getItem() instanceof ItemFood)
        {
            if (e.getEntityLiving() instanceof EntityPlayer)
            {
                ItemFood food = (ItemFood) stack.getItem();
                EntityPlayer player = (EntityPlayer) e.getEntityLiving();
                for (Item foodItem : KeldariaItems.getFoodItems())
                {
                    player.getCooldownTracker().setCooldown(foodItem, 20 * food.getHealAmount(stack));
                }
            }

            EntityLivingBase living = e.getEntityLiving();
            if (living instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) living;
                EntityPlayerMP playerMP = (EntityPlayerMP) player;
                int amount = ((ItemFood) stack.getItem()).getHealAmount(stack);
                int baseAmount = amount;
                if(!ExpiredFoods.isExpired(stack))
                {
                    Rot rot = ExpiredFoods.getRot(stack);
                    int percent = rot.getRotPercent(stack);
                    amount *= 1 - (percent * 0.01);
                    if(amount == 0) amount = 1;
                }
                if (BurntAliments.isBurned(stack))
                {
                    KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketSendPopMessage("Vous avez un goût de cramé dans la bouche...", 3000), playerMP);
                    player.attackEntityFrom(DamageSource.GENERIC, 1);
                    amount /= 2;
                } else if (ExpiredFoods.isExpired(stack))
                {
                    player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 500, 2));
                    Helpers.sendPopMessage((EntityPlayerMP) player, "Vous venez de manger de la nourriture périmée. :)", 3000);
                    player.attackEntityFrom(DamageSource.GENERIC, 1);
                } else if (BurntAliments.isFumed(stack))
                {
                    player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 100, 0));
                }
                player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - baseAmount + amount);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!event.player.world.isRemote)
        {
            EntityPlayerMP mp = (EntityPlayerMP) event.player;
            if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer() && !WhitelistManager.canJoin(mp.getName()))
            {
                for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
                {
                    player.sendMessage(new TextComponentString("§c[Keldaria] §6" + event.player.getName() + "§e a essayé de rejoindre le serveur. Mais n'est pas whitelist."));
                }
            }
            Databases.getPlayerData(mp).setString("Ip", String.valueOf(mp.connection.netManager.getRemoteAddress()));
            Helpers.markPlayerConnection(mp);
            KeldariaVoices.setSpeakStrength(mp, 15);
            KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketUpdateClimbSystem(), mp);
        }
    }

    private int growTimer = 0;

    @SubscribeEvent
    public void onCropGrow(BlockEvent.CropGrowEvent.Pre e)
    {
        e.setResult(Event.Result.DENY);
        if (Season.getSeason(e.getWorld()) == Season.WINTER)
        {
            return;
        }
    }



    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load e)
    {
        if(Season.isWinter(e.getWorld())) return;
        Chunk chunk = e.getChunk();
        if (chunk != null && !e.getWorld().getGameRules().getBoolean("disableAutoGrowing"))
        {
            //GrowHandler.makeGrow(chunk);
        }
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event)
    {
        if (!event.getEntityPlayer().world.isRemote)
        {
            EntityPlayerMP player = Helpers.getPlayerMP(event.getEntityPlayer());
            ItemStack mainHandStack = player.getHeldItemMainhand();
            if (mainHandStack.getItem() == Items.PAPER || mainHandStack.getItem() == KeldariaItems.WRITED_PAPER)
            {
                String title = WritablePaper.getTitle(mainHandStack);
                String content = WritablePaper.getContent(mainHandStack);
                boolean isSigned = WritablePaper.isSigned(mainHandStack);
                if (mainHandStack.getItem() == KeldariaItems.WRITED_PAPER && !isSigned)
                {
                    player.sendMessage(new TextComponentString(TextFormatting.RED + " �? Papier invalide"));
                    return;
                }
                PacketOpenPaperWriting packet = isSigned ? new PacketOpenPaperWriting(title, content, true) : new PacketOpenPaperWriting(content);
                KeldariaPacketHandler.getInstance().getNetwork().sendTo(packet, player);
            }
        }
    }

    private HashMap<EntityPlayer, Boolean> canLockHorse = Maps.newHashMap();

    @SubscribeEvent
    public void onRightClickEntity(PlayerInteractEvent.EntityInteract event)
    {
        Entity target = event.getTarget();
        EntityPlayer player = event.getEntityPlayer();
        if (event.getWorld().isRemote)
        {
            if (target instanceof AbstractHorse)
            {
                AbstractHorse horse = (AbstractHorse) target;
                horse.rotationYaw = player.rotationYaw;
                horse.rotationPitch = player.rotationPitch;
            }
            return;
        }
        //event.setCanceled(true);
        if (event.getHand() != EnumHand.MAIN_HAND)
        {
            return;
        }
        if (target instanceof AbstractHorse)
        {
            AbstractHorse horse = (AbstractHorse) target;
            ILivingHorse livingHorse = (ILivingHorse) horse;

            ItemStack stack = player.getHeldItemMainhand();
            if (stack.getItem() == Items.CARROT || stack.getItem() == Items.WHEAT || stack.getItem() == Items.APPLE || stack.getItem() == KeldariaItems.ORGE || stack.getItem() == Items.BEETROOT)
            {
                boolean isCarrot = stack.getItem() == Items.CARROT;
                if (isCarrot && horse.isTame() && horse.getGrowingAge() == 0 && !horse.isInLove())
                {
                    horse.setInLove(player);
                }
                livingHorse.incrementFood(15);
                stack.shrink(1);
                event.setCanceled(true);
                return;
            } else if (stack.getItem() == KeldariaItems.HORSE_BRUSH)
            {
                livingHorse.incrementCleaniless(5);
                player.playSound(SoundEvents.BLOCK_CLOTH_BREAK, 1, 1);
                stack.damageItem(1, horse);
                event.setCanceled(true);
                return;
            } else if (stack.getItem() == Items.WATER_BUCKET)
            {
                livingHorse.incrementThirst(40);
                stack.shrink(1);
                player.addItemStackToInventory(new ItemStack(Items.BUCKET));
                player.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1, 1);
                event.setCanceled(true);
                return;
            }

            if (canLockHorse.getOrDefault(player, true))
            {
                canLockHorse.put(player, false);
                CommonProxy.executeAfter(() -> canLockHorse.put(player, true), 1000);
                if (player.isSneaking())
                {
                    event.setCanceled(true);
                    if (WaitingHorses.isWaiting(horse))
                    {
                        WaitingHorses.WaitingHorse waitingHorse = WaitingHorses.getWaitingHorse(horse);
                        if (waitingHorse.ownerId.equals(player.getGameProfile().getId()) || player.isCreative())
                        {
                            WaitingHorses.unWait(horse);
                            Helpers.sendPopMessage((EntityPlayerMP) player, "§aVous venez de dé-verouiller ce cheval" + (player.isCreative() ? " parce que vous êtes en créatif " : "") + ".", 1200);
                        }
                    } else
                    {
                        WaitingHorses.setWait(horse, player.getGameProfile().getId());
                        Helpers.sendPopMessage((EntityPlayerMP) player, "§aVous venez de verouiller ce cheval.", 1200);
                    }
                } else
                {

                    if (WaitingHorses.isWaiting(horse))
                    {
                        event.setCanceled(true);
                        String pluser = WaitingHorses.getWaitingHorse(horse).ownerId.equals(player.getGameProfile().getId()) ? " par vous" : "";
                        Helpers.sendPopMessage((EntityPlayerMP) player, "§cCe cheval est verrouillé" + pluser + "!", 1200);
                    } else
                    {
                        //event.setCanceled(false);
                        if (horse.getPassengers().size() == 1)
                        {
                            player.startRiding(horse, true);
                        }
                    }
                }

            } else event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock e)
    {

        IBlockState state = e.getWorld().getBlockState(e.getPos());
        if (state.getBlock() instanceof BlockBed)
        {
            e.setCanceled(true);
        }

        TileEntity te = e.getWorld().getTileEntity(e.getPos());
        boolean hasTE = te != null;
        if (hasTE)
        {
            PlayerSpy.TE_LOG.log(e.getEntityPlayer().getName() + " interacted with `" + te.getClass().getSimpleName() + "` at `" + Helpers.blockPosToString(e.getPos()) + "`");
        }

        if(hasTE && te instanceof TileEntityWallpaper)
        {
            WallPaperUnroller.select(e.getEntityPlayer(), te.getPos());
        }

        if(e.getHand() == EnumHand.MAIN_HAND && !e.getWorld().isRemote)
        {
            if(WallPaperUnroller.hasSelectedWallpaper(e.getEntityPlayer()))
            {
                TileEntity tile = e.getWorld().getTileEntity(WallPaperUnroller.getSelectedWallpaper(e.getEntityPlayer()));
                if(tile instanceof TileEntityWallpaper)
                {
                    ((TileEntityWallpaper) tile).unroll(e.getPos(), e.getFace());
                }
            }
        }

    }

    @SubscribeEvent
    public void onMount(EntityMountEvent e)
    {
        if (e.getWorldObj().isRemote) return;
        Entity entityMounting = e.getEntityMounting();
        Entity entityMounted = e.getEntityBeingMounted();
        if (entityMounted instanceof AbstractHorse)
        {
            if (entityMounting instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) entityMounting;
                AbstractHorse horse = (AbstractHorse) entityMounted;
                PlayerSpy.HORSE_LOG.log(player.getName() + " has " + (e.isDismounting() ? "dismounted" : "mounted") + " a horse " + (horse.hasCustomName() ? "named `" + horse.getName() + "`" : "") + " at `" + Helpers.blockPosToString(horse.getPosition()) + "`");
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event)
    {
        IKeldariaPlayer old = (IKeldariaPlayer) event.getOriginal();
        IKeldariaPlayer actual = (IKeldariaPlayer) event.getEntityPlayer();
        for (int i = 0; i < old.getInventoryCloths().getSizeInventory(); i++)
        {
            actual.getInventoryCloths().setInventorySlotContents(i, old.getInventoryCloths().getStackInSlot(i));
        }

        for (int i = 0; i < old.getInventoryAccessories().getSizeInventory(); i++)
        {
            actual.getInventoryAccessories().setInventorySlotContents(i, old.getInventoryAccessories().getStackInSlot(i));
        }

        for (int i = 0; i < old.getInventoryArmor().getSizeInventory(); i++)
        {
            actual.getInventoryArmor().setInventorySlotContents(i, old.getInventoryArmor().getStackInSlot(i));
        }
    }

    private long lastUpdate = -1;
    private long lastPigeonsCheck = -1;

    @SubscribeEvent
    public void onPlayerTickEvent(TickEvent.ServerTickEvent event)
    {
        long actual = System.currentTimeMillis();
        if (lastUpdate == -1)
        {
            lastUpdate = actual - 5500;
        }
        if (actual - lastUpdate > 5000)
        {
            lastUpdate = actual;
            for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
            {
                ArrayList<ItemStack> stacks = new ArrayList<>(player.inventory.mainInventory);
                stacks.addAll(player.inventory.armorInventory);
                stacks.addAll(player.inventory.offHandInventory);
                stacks.forEach(itemStack ->
                {
                    if (SharpeningHelpers.canBeSharped(itemStack))
                    {
                        if (!SharpeningHelpers.hasSharpness(itemStack))
                        {
                            SharpeningHelpers.initSharpness(itemStack);
                        }
                    }
                });

                EnumAptitudes.updateModifiers(player);
                if (player.interactionManager.getGameType() == GameType.SURVIVAL)
                {
                    CleanilessManager.updateCleaniless(player);
                }
            }
        }

        if(lastPigeonsCheck == -1)
        {
            lastPigeonsCheck = actual - 10000;
        }
        if(actual - lastPigeonsCheck > 10000)
        {
            lastPigeonsCheck  = System.currentTimeMillis();
            this.keldaria.getPigeonTravels().checkAndSpawn();
            //System.out.println(FMLCommonHandler.instance().getMinecraftServerInstance().worlds[0].getChunk(-43, -13).isLoaded());
        }

    }

    @SubscribeEvent
    public void onCritical(CriticalHitEvent event)
    {
        if (event.getEntityPlayer().world.isRemote) return;
        WeaponStat stat = WeaponStat.getAttackStat(event.getEntityPlayer());
        if (stat == null) return;
        if (event.getDamageModifier() == 1)
        {
            double percent = Helpers.getPercent((Helpers.getPercent(Math.min(15, stat.getLevel(event.getEntityPlayer())), 15)), 200);
            if (Helpers.randomDouble(0, 100) <= percent)
            {
                event.setResult(Event.Result.ALLOW);
            }
        }
        event.setDamageModifier((float) (event.getDamageModifier() * (1 + ((Math.max(0, stat.getLevel(event.getEntityPlayer()) - 15) * 1 / 5)))));
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event)
    {
        if (event.getSource().getTrueSource() instanceof EntityLivingBase)
        {
            EntityLivingBase killer = (EntityLivingBase) event.getSource().getTrueSource();
            EntityLivingBase base = event.getEntityLiving();
            String prefix = ":red_circle:";
            if (base instanceof EntityPlayer)
            {
                prefix = ":orange_circle:";
            }
            PlayerSpy.DEATH_LOG.log("\n**Position:** " + Helpers.blockPosToString(event.getEntityLiving().getPosition()) +prefix + " **Tueur:** " + killer.getName() + " | **Tué:** " + base.getName() + "|" + "\nCoordonnées: " + Helpers.blockPosToString(base.getPosition()));
        }
    }


    @SubscribeEvent
    public void onDamage(LivingDamageEvent event)
    {
        double finalEarnedXP = 0;
        DamageSource source = event.getSource();
        {
            if (event.getSource().getTrueSource() != null)
            {
                EntityLivingBase attackedEntity = event.getEntityLiving();
                if (event.getSource().getTrueSource() instanceof EntityPlayer)
                {
                    EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();


                    double earned = Helpers.randomDouble(0.0008, 0.002);


                    float amount = event.getAmount();

                    earned *= amount / 2;

                    ItemStack mainHand = player.getHeldItemMainhand();
                    if (!event.getSource().isProjectile() && SharpeningHelpers.canBeSharped(mainHand.getItem()))
                    {
                        float percent = Helpers.getPercent(SharpeningHelpers.getSharpness(mainHand), SharpeningHelpers.MAX_SHARPENING);
                        float val = percent / 100;
                        event.setAmount(amount * val);
                        int lvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, mainHand);
                        int bound = 8 / (1 + lvl);
                        if (bound <= 0) bound = 1;
                        SharpeningHelpers.decrementSharpness(mainHand, Helpers.RANDOM.nextInt(bound));
                    }


                    if (!event.getSource().isProjectile())
                    {
                        double distance = Math.max(0, player.getDistance(attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ) - (attackedEntity.width));
                        if (!CommandDebugMode.isInDebugMode(player.getName()))
                        {
                            {
                                event.setAmount(Math.max(0, (float) (event.getAmount() - (distance / (mainHand.getItem() instanceof ItemSpear ? 2.5 : 1.25)))));
                            }
                        }

                        //earned /= (distance / 2);
                    }

                    if (event.getSource().isProjectile())
                    {
                        double distance = player.getDistance(attackedEntity);
                        WeaponStat.ARCHER.naturalIncrement(player, distance * 0.002);
                    }
                    EnumAttackType type = EnumAttackType.getAttackType(player);
                    event.setAmount((float) (event.getAmount() * EnumAttackType.getMultiplier(mainHand, type)));
                    type.applyCooldown(player);
                    {
                        double statPercent = WeaponStat.getStatPercent(event.getAmount(), source, player, attackedEntity);
                        double strengthPercent = EnumAptitudes.STRENGTH.getPoints(player) * 5;
                        double random = Helpers.randomDouble(0, 50 - strengthPercent);
                        double totalPercent = statPercent + strengthPercent + random;

                        event.setAmount(event.getAmount() / 2 + Helpers.crossMult(totalPercent, 100, event.getAmount()));
                    }

                    if (attackedEntity.isActiveItemStackBlocking())
                    {
                        earned *= 2;
                    }

                    WeaponStat stat = WeaponStat.getAttackStat(player);
                    if (stat != null)
                    {
                        double attackerStat = stat.getLevel(player);
                        if (attackedEntity instanceof EntityPlayer)
                        {
                            double enemyStat = stat.getLevel((EntityPlayer) attackedEntity);
                            earned *= (enemyStat / 3);
                        }
                        stat.naturalIncrement(player, earned);
                        finalEarnedXP = earned;
                    }

                }
                if (attackedEntity instanceof EntityPlayer && attackedEntity.isActiveItemStackBlocking())
                {
                    event.setAmount(BlockingSystem.reduceDamages(event.getAmount(), event.getSource().getTrueSource(), (EntityPlayer) attackedEntity, attackedEntity.getActiveItemStack()));
                }
            } else if (source == DamageSource.FALL)
            {
                if (event.getEntityLiving() instanceof EntityPlayer)
                {
                    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                    /*double diviser = 1 + (EnumAptitudes.AGILITY.getPoints(player) * 0.2);
                    float amount = (float) (event.getAmount() / diviser);*/
                    double multiplier = 2.5 - EnumAptitudes.AGILITY.getPoints(player) * 0.2;
                    float amount = (float) (event.getAmount() * multiplier);
                    if(player.isSneaking())
                    {
                        amount *= 0.8f;

                    }
                    player.sendMessage(new TextComponentString("" + amount));
                    event.setAmount(amount);
                }
            }
        }

        float damage = event.getAmount();
        EnumAttackType attackType = EnumAttackType.getAttackType(source);
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            IKeldariaPlayer keldariaPlayer = (IKeldariaPlayer) player;
            int damageReduce = Helpers.crossMult(keldariaPlayer.getInventoryArmor().getProtectionPercent(attackType), 100, 80);
            damage -= Helpers.crossMult(damageReduce, 100, damage);
            InventoryArmor armor = keldariaPlayer.getInventoryArmor();
            if (!armor.isArmorEmpty())
            {
                for (; ; )
                {
                    ItemStack partToDamage = armor.getStackInSlot(Helpers.randomInteger(0, keldariaPlayer.getInventoryArmor().getSizeInventory()) - 1);
                    if (!partToDamage.isEmpty())
                    {
                        if (partToDamage.getItem() instanceof ItemArmorPart && ((ItemArmorPart) partToDamage.getItem()).canDamageArmor(source))
                        {
                            partToDamage.damageItem((int) damage + 1, player);
                        }
                        break;
                    }
                }
            }
        } else if (event.getEntity() instanceof EntityLivingBase)
        {
            EntityStats stats = EntityStats.getStats(event.getEntity());
            int damageReduce = Helpers.crossMult(stats.getProtectionPercent(attackType), 100, 100);
            double d = (damageReduce * damage / 100);
            damage -= d;
        }
        event.setAmount(damage);
        {
            String killerName = "";
            ItemStack weapon = ItemStack.EMPTY;
            String killed = event.getEntityLiving().getName();
            if (event.getSource().getTrueSource() instanceof EntityLivingBase)
            {
                EntityLivingBase killer = (EntityLivingBase) event.getSource().getTrueSource();
                killerName = killer.getName();
                weapon = killer.getHeldItemMainhand();

            } else
            {
                killerName = event.getSource().damageType;
            }
            PlayerSpy.PUNCH_LOG.log("\n**Position:** " + Helpers.blockPosToString(event.getEntityLiving().getPosition()) + "" + "\n** :crossed_swords: Attaquant: ** " + killerName + "\n" + "** :shield: Attaqué: ** " + killed + "\n" + (weapon != ItemStack.EMPTY ? "** :bow_and_arrow: Arme: **" + weapon.getDisplayName() + "\n" : "") + "** :boom: Dégats: **" + event.getAmount() + "\n" + (finalEarnedXP != 0 ? "** :sparkles: XP gagné:** " + finalEarnedXP : ""));
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityLlama)
        {
            event.setCanceled(true);
        }
        if (event.getEntity() instanceof EntityPlayerMP)
        {
            if (event.getWorld().isRemote) return;
            EntityPlayerMP mp = (EntityPlayerMP) event.getEntity();
            if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer() && !WhitelistManager.canJoin(mp.getName()))
            {
                mp.connection.disconnect(new TextComponentString("§cVous n'êtes pas whitelisté sur Keldaria!\n" + "§6Faites votre candidature et rejoignez le discord\n" + "§6sur §ehttp://keldaria.fr\n\n" + "§7S'il sagit d'une erreur et que vous\n" + "§7êtes déjà whitelisté, contactez\n" + "§7nous sur Discord!"));
            }
        } else if (event.getEntity() instanceof EntityArrow)
        {
            EntityArrow arrow = (EntityArrow) event.getEntity();

            if (arrow.shootingEntity instanceof EntityPlayer && !event.getWorld().isRemote)
            {
                EntityPlayer player = (EntityPlayer) arrow.shootingEntity;
                IKeldariaPlayer keldariaPlayer = (IKeldariaPlayer) player;

                ItemStack quiver = keldariaPlayer.getInventoryAccessories().getAccessory(EntityEquipmentSlot.CHEST);
                if (quiver.getItem() instanceof ItemQuiver)
                {
                    InventoryQuiver inventoryQuiver = new InventoryQuiver(player, quiver);
                    for (int i = 0; i < inventoryQuiver.getSizeInventory(); i++)
                    {
                        ItemStack potentielArrow = inventoryQuiver.getStackInSlot(i);
                        if (potentielArrow.getItem() instanceof ItemArrow)
                        {
                            inventoryQuiver.decrStackSize(i, 1);
                            break;
                        }
                    }
                }

                double motX = arrow.motionX;
                double motY = arrow.motionY;
                double motZ = arrow.motionZ;
                double errorRange = 1.01;
                double skillValue = (Helpers.getPercent(Math.min(15, WeaponStat.ARCHER.getLevel(player)), 15)) * 0.01;
                errorRange -= skillValue;
                WeaponStat.ARCHER.naturalIncrement(player, 0.01);
                double xPluser = (errorRange / 2) - (Helpers.randomDouble(0, errorRange));
                double yPluser = (errorRange / 2) - (Helpers.randomDouble(0, errorRange));
                double zPluser = (errorRange / 2) - (Helpers.randomDouble(0, errorRange));
                motX += xPluser;
                motY += yPluser;
                motZ += zPluser;
                arrow.motionX = motX;
                arrow.motionY = motY;
                arrow.motionZ = motZ;
                //arrow.setVelocity(motX, motY, motZ);
            }
        }
    }

    public static Field ticksSinceLastSwing;

    static
    {
        (KeldariaEventHandler.ticksSinceLastSwing = EntityLivingBase.class.getDeclaredFields()[24]).setAccessible(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerAttackTarget(AttackEntityEvent e)
    {
        if (CommandDebugMode.isInDebugMode(e.getEntityPlayer().getName()))
        {
            e.getEntityPlayer().ticksSinceLastSwing = 1000;
        }
    }


    @SubscribeEvent
    public void tickPlayer(final TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            if (event.player.getHealth() < event.player.getMaxHealth())
            {
                if (event.player.getFoodStats().getFoodLevel() >= /*MinHunger*/ 10)
                {
                    if (event.player.getEntityWorld().getTotalWorldTime() % (/* HealTime */ 100) == 0L)
                    {
                        float res = EnumAptitudes.RESISTANCE.getPoints(event.player);
                        event.player.heal(1.0f + (res * 0.1f));
                        event.player.getFoodStats().addExhaustion((float) (2 - (res * 0.5)));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void event(final WorldEvent.Load event)
    {
        event.getWorld().getGameRules().setOrCreateGameRule("naturalRegeneration", "false");
    }





    public final HashMap<String, BlockPos> LAST_POS = Maps.newHashMap();
    public final HashMap<String, Vector2i> LAST_CHUNK_POS = Maps.newHashMap();

    private final HashMap<String, Integer> LAST_ITEMS = Maps.newHashMap();


    @SubscribeEvent
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
    {
        if (event.player.world.isRemote) return;
        if (event.phase == TickEvent.Phase.END)
        {
            EntityPlayer player = (EntityPlayer) event.player;


            if (LAST_ITEMS.getOrDefault(player.getName(), 0) != player.inventory.currentItem)
            {
                EnumAttackType.setAttackType(player, EnumAttackType.getPreferedAttackType(player.getHeldItemMainhand()));
                LAST_ITEMS.put(player.getName(), player.inventory.currentItem);
            }

            if ((int) player.prevPosX != (int) player.posX || (int) player.prevPosY != (int) player.posY || (int) player.prevPosZ != player.posZ)
            {
                if (KeldariaVoices.isTheoricallyTalking(player))
                {
                    if (!KeldariaVoices.hearTroughWalls(player.world))
                    {
                        KeldariaVoices.reloadHearList(player);
                    }
                }
                Chunk chunkk = player.world.getChunk((int) player.posX, (int) player.posZ);
                Vector2i chunkPos = new Vector2i(chunkk.x, chunkk.z);
                Vector2i lastChunkPos = LAST_CHUNK_POS.get(player.getName());
                if (!LAST_CHUNK_POS.containsKey(player.getName()))
                {
                    LAST_CHUNK_POS.put(player.getName(), chunkPos);
                } else
                {
                    if (!chunkPos.equals(lastChunkPos))
                    {
                        if (!Containment.canMoveOn(player, chunkk))
                        {
                            if (player.isRiding())
                            {
                                Entity riding = player.getRidingEntity();
                                player.dismountRidingEntity();
                                riding.setPosition(player.prevPosX - player.getForward().x *2, player.prevPosY, player.prevPosZ -player.getForward().z*2);
                            } else
                            {
                                player.setPositionAndUpdate(player.prevPosX - player.getForward().x *2, player.prevPosY, player.prevPosZ - player.getForward().z*2);

                            }
                            Helpers.sendPopMessage((EntityPlayerMP) player, "§cVous ne pouvez pas aller au delà d'ici sans supervision.", 3000);
                            return;
                        }
                        LAST_CHUNK_POS.put(player.getName(), chunkPos);
                    }
                }
            }

            player.prevPosX = player.posX;
            player.prevPosY = player.posY;
            player.prevPosZ = player.posZ;
        }
    }

    @SubscribeEvent
    public void onEntityTick(LivingEvent.LivingUpdateEvent event)
    {
        Entity entity = event.getEntity();
        if (entity.world.isRemote) return;

        if (entity instanceof EntityAnimal)
        {
            EntityAnimal animal = (EntityAnimal) entity;
            IKeldariaAnimal keldariaAnimal = (IKeldariaAnimal) animal;
            if (keldariaAnimal.getTimeCreated() == -1)
            {
                if (animal.getGrowingAge() == 0)
                {
                    keldariaAnimal.setTimeCreated(1291935600000L);
                } else keldariaAnimal.setTimeCreated(new Date().getTime());
            }
            if (keldariaAnimal.needGrow(animal))
            {
                if (animal.getGrowingAge() < 0)
                {
                    animal.setGrowingAge(0);
                }
            } else
            {
                animal.setGrowingAge(-10);
            }
        } else if (entity instanceof EntityItem)
        {

        }
    }

    @SubscribeEvent
    public void onAnimalTame(AnimalTameEvent event)
    {
        World world = event.getAnimal().world;
        if (world.isRemote) return;
        EntityPlayerMP mp = (EntityPlayerMP) event.getTamer();
        if (!EnumJob.PEASANT.has(mp))
        {
            event.setCanceled(true);
            Helpers.sendPopMessage(mp, "Vous n'avez pas les compétences pour apprivoiser un " + event.getAnimal().getName(), 3000);
        }
    }

    @SubscribeEvent
    public void onHarvestBlock(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event)
    {
        IBlockState state = event.getEntityPlayer().world.getBlockState(event.getPos());
        float speed = event.getOriginalSpeed();

        speed *= 0.5 + EnumAptitudes.STRENGTH.getPoints(event.getEntityPlayer()) * 0.1;

        ItemStack tool = event.getEntityPlayer().getHeldItemMainhand();
        if (tool.getItem() instanceof ItemTool && SharpeningHelpers.canBeSharped(tool))
        {
            if ((tool.getItem() instanceof ItemAxe && state.getMaterial() == Material.WOOD) || (tool.getItem() instanceof ItemPickaxe && (state.getMaterial() == Material.ROCK || state.getMaterial() == Material.IRON)))
            {
                double sharpness = SharpeningHelpers.getSharpness(tool);
                speed *= (0.25 + ((sharpness * 100 / 2000) * 0.01));
            }
        }
        event.setNewSpeed(speed);
    }

    @SubscribeEvent
    public void onProjectileImpact(ProjectileImpactEvent.Arrow event)
    {
        EntityArrow arrow = event.getArrow();
        Entity shootingEntity = arrow.shootingEntity;
        if (shootingEntity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) shootingEntity;
            int strength = EnumAptitudes.STRENGTH.getPoints(player);
            arrow.setDamage(arrow.getDamage() + ((strength * 0.1) * 2));
        }
    }

    @SubscribeEvent
    public void onArrowLoose(ArrowLooseEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (!CommandDebugMode.isInDebugMode(event.getEntityPlayer().getName()))
        {
            int ticks = (event.getBow().getItem() instanceof ItemLongbow ? 13 : event.getBow().getItem() instanceof ItemCrossbow ? 16 : 10) * 6 - (8 * EnumAptitudes.STRENGTH.getPoints(event.getEntityPlayer()));
            ItemStack quiver = (player.world.isRemote ? Keldaria.getInstance().getSyncedAccessories().getAccessories(player).getInventory() : ((IKeldariaPlayer) player).getInventoryAccessories()).getAccessory(EntityEquipmentSlot.CHEST);
            if (!ItemQuiver.isEmpty(player, quiver))
            {
                ticks = (int) (ticks * 0.5);
            }
            player.getCooldownTracker().setCooldown(event.getBow().getItem(), ticks);
        }
    }

    @SubscribeEvent
    public void onKnockback(LivingKnockBackEvent event)
    {
        if (event.getAttacker() instanceof EntityPlayer)
        {
            EntityPlayer attacker = (EntityPlayer) event.getAttacker();
            ItemStack mainHand = attacker.getHeldItemMainhand();
            Item itemMainHand = mainHand.getItem();
            event.setStrength((float) (event.getStrength() * (1 + (EnumAptitudes.STRENGTH.getPoints(attacker) * 0.05))));
            if (itemMainHand instanceof ItemSpear)
            {
                if(EnumAttackType.getAttackType(attacker) != EnumAttackType.THRUST)
                {
                    event.setStrength(event.getStrength() * 1.4f);
                }
                else
                {
                    event.setStrength(0.3f);
                }
            } else if (itemMainHand instanceof ItemShield)
            {
                event.setStrength(event.getStrength() * 2.5f);
            }
        }
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer defenser = (EntityPlayer) event.getEntityLiving();
            event.setStrength((float) (event.getStrength() * (1 - (EnumAptitudes.RESISTANCE.getPoints(defenser) * 0.05))));
        }
    }

    public static final HashMap<String, Integer> PICKUP_NEEDED = Maps.newHashMap();

    @SubscribeEvent
    public void itemPickupEvent(EntityItemPickupEvent event)
    {
        EntityItem item = event.getItem();
        EntityPlayer player = event.getEntityPlayer();
        if (PICKUP_NEEDED.getOrDefault(player.getName(), 0) != item.getEntityId())
        {
            event.setResult(Event.Result.ALLOW);
        } else
        {
        }
    }


    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event)
    {
        Helpers.log("Saving HomingPigeons");
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList list = this.keldaria.getPigeonTravels().serializeNBT();
        System.out.println(list);
        compound.setTag("list", list);
        Databases.getDatabase("keldaria:serverInfos").setString("PigeonTravels", compound.toString());
    }

}