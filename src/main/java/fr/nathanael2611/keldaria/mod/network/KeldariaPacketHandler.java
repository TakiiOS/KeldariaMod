/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.network.animation.PacketHandleAnimationRequest;
import fr.nathanael2611.keldaria.mod.network.animation.PacketHandleAnimationResponse;
import fr.nathanael2611.keldaria.mod.network.canvas.PacketOpenCanvasGui;
import fr.nathanael2611.keldaria.mod.network.canvas.PacketSaveCanvas;
import fr.nathanael2611.keldaria.mod.network.toclient.*;
import fr.nathanael2611.keldaria.mod.network.toserver.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class KeldariaPacketHandler
{

    /**
     * Define the mod-network, we will use it to send packets ! ;D
     */
    private SimpleNetworkWrapper network;

    private static KeldariaPacketHandler instance;

    private int nextID = 0;

    public static KeldariaPacketHandler getInstance()
    {
        if(instance == null) instance = new KeldariaPacketHandler();
        return instance;
    }

    public SimpleNetworkWrapper getNetwork()
    {
        return network;
    }

    /**
     * This method will register all our packets.
     */
    public void registerPackets()
    {
        this.network = NetworkRegistry.INSTANCE.newSimpleChannel(Keldaria.MOD_ID.toUpperCase());

        this.registerPacket(PacketSendPopMessage.Message.class, PacketSendPopMessage.class, Side.CLIENT);
        this.registerPacket(PacketHandleAnimationRequest.Message.class, PacketHandleAnimationRequest.class, Side.SERVER);
        this.registerPacket(PacketHandleAnimationResponse.Message.class, PacketHandleAnimationResponse.class, Side.CLIENT);
        this.registerPacket(PacketSendWritingUpdate.Message.class, PacketSendWritingUpdate.class, Side.SERVER);
        this.registerPacket(PacketSyncPlayerWriting.Message.class, PacketSyncPlayerWriting.class, Side.CLIENT);
        this.registerPacket(PacketChangeRolePlayName.Message.class, PacketChangeRolePlayName.class, Side.SERVER);
        this.registerPacket(PacketActionWardrobe.Message.class, PacketActionWardrobe.class, Side.SERVER);
        this.registerPacket(PacketOpenGui.Message.class, PacketOpenGui.class, Side.CLIENT);
        this.registerPacket(PacketInitGui.Message.class, PacketInitGui.class, Side.CLIENT);
        this.registerPacket(PacketLockpickDoor.Message.class, PacketLockpickDoor.class, Side.SERVER);
        this.registerPacket(PacketCopyStringToClipboard.Message.class, PacketCopyStringToClipboard.class, Side.CLIENT);
        this.registerPacket(PacketOpenClothsWardrobe.Message.class, PacketOpenClothsWardrobe.class, Side.SERVER);
        this.registerPacket(PacketOpenAccessories.Message.class, PacketOpenAccessories.class, Side.SERVER);
        this.registerPacket(PacketSendClothes.Handler.class, PacketSendClothes.class, Side.CLIENT);
        this.registerPacket(PacketUpdateWeapons.Handler.class, PacketUpdateWeapons.class, Side.CLIENT);
        this.registerPacket(PacketUpdateAccessories.Handler.class, PacketUpdateAccessories.class, Side.CLIENT);
        this.registerPacket(PacketOpenCanvasGui.Message.class, PacketOpenCanvasGui.class, Side.CLIENT);
        this.registerPacket(PacketSaveCanvas.Message.class, PacketSaveCanvas.class, Side.SERVER);
        this.registerPacket(PacketOpenPaperWriting.Message.class, PacketOpenPaperWriting.class, Side.CLIENT);
        this.registerPacket(PacketSavePaper.Message.class, PacketSavePaper.class, Side.SERVER);
        this.registerPacket(PacketOpenCraftManager.Message.class, PacketOpenCraftManager.class, Side.CLIENT);
        this.registerPacket(PacketCraft.Message.class, PacketCraft.class, Side.SERVER);
        this.registerPacket(PacketProcessEmptyRightClick.Message.class, PacketProcessEmptyRightClick.class, Side.SERVER);
        this.registerPacket(PacketDisplayInChatOf.Message.class, PacketDisplayInChatOf.class, Side.SERVER);
        this.registerPacket(PacketOpenCratfingAnvil.Message.class, PacketOpenCratfingAnvil.class, Side.SERVER);
        this.registerPacket(PacketToggleAThing.Message.class, PacketToggleAThing.class, Side.SERVER);
        this.registerPacket(PacketUpdateHRPSign.Message.class, PacketUpdateHRPSign.class, Side.SERVER);
        this.registerPacket(PacketUpdateFatBookPages.Message.class, PacketUpdateFatBookPages.class, Side.SERVER);
        this.registerPacket(PacketOpenHorseStorage.Message.class, PacketOpenHorseStorage.class, Side.SERVER);
        this.registerPacket(PacketSetSkins.Handler.class, PacketSetSkins.class, Side.CLIENT);
        this.registerPacket(PacketUpdateSnowManager.Handler.class, PacketUpdateSnowManager.class, Side.CLIENT);
        this.registerPacket(PacketReloadChunks.Handler.class, PacketReloadChunks.class, Side.CLIENT);
        this.registerPacket(PacketChooseArmPose.Message.class, PacketChooseArmPose.class, Side.SERVER);
        this.registerPacket(PacketSetArms.Message.class, PacketSetArms.class, Side.CLIENT);
        this.registerPacket(PacketNeedPickupItem.Message.class, PacketNeedPickupItem.class, Side.SERVER);
        this.registerPacket(PacketChangeVoiceStrength.Message.class, PacketChangeVoiceStrength.class, Side.SERVER);
        this.registerPacket(PacketUpdateClimbSystem.Handler.class, PacketUpdateClimbSystem.class, Side.CLIENT);
        this.registerPacket(PacketOpenHomingPigeon.Handler.class, PacketOpenHomingPigeon.class, Side.CLIENT);
        this.registerPacket(PacketActionHomingPigeon.Handler.class, PacketActionHomingPigeon.class, Side.SERVER);
        this.registerPacket(PacketSendLog.Message.class, PacketSendLog.class, Side.SERVER);
        this.registerPacket(PacketSpyRequest.Message.class, PacketSpyRequest.class, Side.CLIENT);
        this.registerPacket(PacketSpyReply.Message.class, PacketSpyReply.class, Side.SERVER);
        this.registerPacket(PacketUpdateZone.Handler.class, PacketUpdateZone.class, Side.CLIENT);
        //this.registerPacket(PacketFightControl.Message.class, PacketFightControl.class, Side.CLIENT);
        //this.registerPacket(PacketProcessAttack.Handler.class, PacketProcessAttack.class, Side.SERVER);
    }

    public <REQ extends IMessage, REPLY extends IMessage> void registerPacket(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side)
    {
        network.registerMessage(messageHandler, requestMessageType, nextID, side);
        nextID++;
    }

}
