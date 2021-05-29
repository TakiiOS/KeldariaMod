package fr.nathanael2611.keldaria.mod.util.spy;

import fr.nathanael2611.keldaria.mod.log.PlayerSpy;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketSpyRequest;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketSendLog;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketSpyReply;
import fr.nathanael2611.keldaria.mod.util.spy.imgur.Uploader;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.IntBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8_REV;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class ScreenshotManager
{

    public static final ExecutorService SERVICE = Executors.newSingleThreadExecutor();

    private static IntBuffer buffer = null;

    public static void silentScreenAndSend(String requester)
    {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            BufferedImage image = takeScreenshot();
            SERVICE.execute(() -> {
                String link = Uploader.uploadAndGetLink(image);
                StringBuilder builder = new StringBuilder();
                builder.append(String.format("§f[%s] §7%s", Minecraft.getMinecraft().player.getName(), link));
                TextComponentString text = new TextComponentString(String.format("§8[KeldariaSpy] §7screen: %s", builder.toString()));
                text.setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link)));
                KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketSpyReply(PacketSpyRequest.ID_PACKS, requester, text));
            });
        });
    }

    public static BufferedImage takeScreenshot()
    {
        Minecraft mc = Minecraft.getMinecraft();
        int width = mc.displayWidth;
        int height = mc.displayHeight;

        glPixelStorei(GL_PACK_ALIGNMENT, 1);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        int count = width * height;
        if(buffer == null || buffer.capacity() < count)
        {
            buffer = BufferUtils.createIntBuffer(count);
        }
        buffer.clear();

        glReadPixels(0, 0, width, height, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] imgData = ((DataBufferInt) img.getWritableTile(0, 0).getDataBuffer()).getData();

        for(int row = height; row > 0;)
        {
            buffer.get(imgData, --row * width, width);
        }

        return img;
    }

    public static void sendScreenshotToServer(BufferedImage image)
    {
        SERVICE.execute(() -> {
            String link = Uploader.uploadAndGetLink(image);
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketSendLog(PlayerSpy.SCREENSHOTS_LOG,"**"+ Minecraft.getMinecraft().player.getName() + "**: " + link));
        });
    }



}
