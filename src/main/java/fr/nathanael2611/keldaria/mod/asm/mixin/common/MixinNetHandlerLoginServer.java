/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.asm.MixinHooks;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Mixin(NetHandlerLoginServer.class)
public abstract class MixinNetHandlerLoginServer implements INetHandlerLoginServer, ITickable
{

    @Shadow
    private NetHandlerLoginServer.LoginState currentLoginState;

    @Shadow
    @Final
    public NetworkManager networkManager;

    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    @Final
    private byte[] verifyToken;

    @Shadow
    private GameProfile loginGameProfile;

    @Shadow
    public EntityPlayerMP player;

    @Shadow
    public abstract void disconnect(ITextComponent reason);

    @Shadow
    public abstract void onDisconnect(ITextComponent reason);

    @Shadow
    public abstract void tryAcceptPlayer();

    @Shadow
    public int connectionTimer;
    @Shadow @Final public String serverId;
    String disconnect = null;

    public void update()
    {
        if (disconnect != null)
        {
            this.disconnect(new TextComponentString(disconnect));
        }
        if (this.currentLoginState == NetHandlerLoginServer.LoginState.READY_TO_ACCEPT)
        {
            this.tryAcceptPlayer();
        } else if (this.currentLoginState == NetHandlerLoginServer.LoginState.DELAY_ACCEPT)
        {
            EntityPlayerMP entityplayermp = this.server.getPlayerList().getPlayerByUUID(this.loginGameProfile.getId());

            if (entityplayermp == null)
            {
                this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                net.minecraftforge.fml.common.network.internal.FMLNetworkHandler.fmlServerHandshake(this.server.getPlayerList(), this.networkManager, this.player);
                this.player = null;
            }
        }


        if (this.connectionTimer++ == 600)
        {
            this.disconnect(new TextComponentTranslation("multiplayer.disconnect.slow_login", new Object[0]));
        }
    }

    public void processLoginStart(CPacketLoginStart packetIn)
    {
        server.getPlayerList().setWhiteListEnabled(false);
        Validate.validState(this.currentLoginState == NetHandlerLoginServer.LoginState.HELLO, "Unexpected hello packet");
        this.loginGameProfile = packetIn.getProfile();

        if(this.server.isSinglePlayer())
        {
            this.loginGameProfile = MixinHooks.getSinglePlayerProfile();
            this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
        }
        else
        {
            GameProfile profile = MixinHooks.isLoggedIn(this.loginGameProfile.getName());
            if (profile == null)
            {
                disconnect = "§cDésolé, vous n'êtes pas authentifié.\n§7La session est invalide.\n\n§7Essayez de vous ré-authentifier avec vos identitifiants Keldaria.\n§7Appuyez sur §néchap dans le menu principal.§r§7";
            }
            this.loginGameProfile = profile;
            this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
        }
    }

    public GameProfile getOfflineProfile(GameProfile original)
    {
        //UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + original.getName()).getBytes(StandardCharsets.UTF_8));
        try
        {
            JsonObject json = new JsonParser().parse(Helpers.readResponse(new URL(Keldaria.API_URL + "?action=getUuid&id=" + original.getName()))).getAsJsonObject();
            if (json.get("state").getAsString().equalsIgnoreCase("success"))
            {
                return new GameProfile(UUID.fromString(json.get("uuid").getAsString()), original.getName());
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return new GameProfile(UUID.randomUUID(), original.getName());
    }

}
