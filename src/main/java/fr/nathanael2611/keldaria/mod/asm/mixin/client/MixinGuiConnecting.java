package fr.nathanael2611.keldaria.mod.asm.mixin.client;

import com.mojang.authlib.GameProfile;
import fr.nathanael2611.keldaria.mod.proxy.ClientProxy;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(GuiConnecting.class)
public abstract class MixinGuiConnecting extends GuiScreen
{

    @Shadow private NetworkManager networkManager;

    @Shadow @Final private static Logger LOGGER;

    @Shadow @Final private static AtomicInteger CONNECTION_ID;

    @Shadow @Final private GuiScreen previousGuiScreen;

    @Shadow private boolean cancel;

    /**
     * @author
     */
    @Overwrite
    private void connect(final String ip, final int port)
    {

        LOGGER.info("Connecting to {}, {}", ip, Integer.valueOf(port));
        //MixinClientHooks.startGuiConnectingThread(ip, port, this.previousGuiScreen, (GuiConnecting) (Object) this, this.networkManager, CONNECTION_ID);


        new Thread(() -> {
            InetAddress inetaddress = null;

            try
            {
                if (this.cancel)
                {
                    return;
                }

                inetaddress = InetAddress.getByName(ip);
                this.networkManager = NetworkManager.createNetworkManagerAndConnect(inetaddress, port, this.mc.gameSettings.isUsingNativeTransport());
                this.networkManager.setNetHandler(new NetHandlerLoginClient(this.networkManager, this.mc, this.previousGuiScreen));
                this.networkManager.sendPacket(new C00Handshake(ip, port, EnumConnectionState.LOGIN, true));
                //this.networkManager.sendPacket(new CPacketLoginStart(this.mc.getSession().getProfile()));
                GameProfile gp = new GameProfile(UUID.fromString(mc.getSession().getPlayerID()), ClientProxy.accessToken);
                networkManager.sendPacket(new CPacketLoginStart(gp));

            }
            catch (UnknownHostException unknownhostexception)
            {
                if (this.cancel)
                {
                    return;
                }

                LOGGER.error("Couldn't connect to server", (Throwable)unknownhostexception);
                this.mc.displayGuiScreen(new GuiDisconnected(this.previousGuiScreen, "connect.failed", new TextComponentTranslation("disconnect.genericReason", new Object[] {"Unknown host"})));
            }
            catch (Exception exception)
            {
                if (this.cancel)
                {
                    return;
                }

                LOGGER.error("Couldn't connect to server", (Throwable)exception);
                String s = exception.toString();

                if (inetaddress != null)
                {
                    String s1 = inetaddress + ":" + port;
                    s = s.replaceAll(s1, "");
                }

                this.mc.displayGuiScreen(new GuiDisconnected(this.previousGuiScreen, "connect.failed", new TextComponentTranslation("disconnect.genericReason", new Object[] {s})));
            }
        }).start();
    }


}
