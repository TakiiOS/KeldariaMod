package fr.nathanael2611.keldaria.mod.util.spy;

import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketSpyRequest;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketSpyReply;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Loader;

public class ModsAndPacks
{

    public static void collectAndSendMods(String requester)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("[%s]", Minecraft.getMinecraft().player.getName()));
        Loader.instance().getModList().forEach(modContainer -> {
            builder.append("\n - §7").append(modContainer.getModId()).append("§f:").append(modContainer.getName()).append("");
        });
        KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketSpyReply(PacketSpyRequest.ID_MODS, requester, new TextComponentTranslation(String.format("§8[KeldariaSpy] §7mod: §f%s", builder.toString()))));
    }

    public static void collectAndSendPacks(String requester)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("§f[%s]\n", Minecraft.getMinecraft().player.getName()));
        {// Utilisés
            StringBuilder useds = new StringBuilder();
            Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntries().forEach(entry -> useds.append("\n - ").append(entry.getResourcePackName()).append(", "));
            builder.append(String.format("  -  §8Actuellement utilisés: §7%s", useds.toString())).append("\n");
        }
        {//ALL
            StringBuilder all = new StringBuilder();
            Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntriesAll().forEach(entry -> all.append("§7").append(entry.getResourcePackName()).append("§f, "));
            builder.append(String.format("  -  §8Tous: §7%s", all.toString()));
        }

        KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketSpyReply(PacketSpyRequest.ID_PACKS, requester, new TextComponentTranslation(String.format("§8[KeldariaSpy] §7resources-packs: %s", builder.toString()))));
    }

}
