package fr.nathanael2611.keldaria.mod.util;

import com.google.common.collect.Lists;
import com.google.gson.JsonParser;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketSendPopMessage;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketDisplayInChatOf;
import fr.nathanael2611.keldaria.mod.util.math.Vector2i;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.StringBuilderWriter;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class contain a lot of useful features used in a lot of mod parts
 *
 * @author Nathanael2611
 */
public class Helpers
{

    public static String readResponse(URL url) throws IOException
    {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    /**
     * Used for read the content of a file and return a string.
     */
    public static String readFileToString(File file)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            reader.close();
            return stringBuilder.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return "ERROR";
    }

    /**
     * Useful for easily create a List<String> by enter a String Collection in the constructor
     */
    public static List<String> createListFrilStrings(String... str)
    {
        return new ArrayList<>(Arrays.asList(str));
    }

    public static final int EOF = -1;

    public static byte[] toByteArray(final InputStream input) throws IOException
    {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static int copy(final InputStream input, final OutputStream output) throws IOException
    {
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE)
        {
            return -1;
        }
        return (int) count;
    }

    public static long copy(final InputStream input, final OutputStream output, final int bufferSize) throws IOException
    {
        return copyLarge(input, output, new byte[bufferSize]);
    }

    public static long copyLarge(final InputStream input, final OutputStream output) throws IOException
    {
        return copy(input, output, DEFAULT_BUFFER_SIZE);
    }

    public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer) throws IOException
    {
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer)))
        {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;


    public static byte[] readFileToByteArray(final File file) throws IOException
    {
        InputStream in = null;
        try
        {
            in = openInputStream(file);
            return IOUtils.toByteArray(in); // Do NOT use file.length() - see IO-453
        } finally
        {
            IOUtils.closeQuietly(in);
        }
    }

    public static FileInputStream openInputStream(final File file) throws IOException
    {
        if (file.exists())
        {
            if (file.isDirectory())
            {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false)
            {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else
        {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    public static FileOutputStream openOutputStream(final File file, final boolean append) throws IOException
    {
        if (file.exists())
        {
            if (file.isDirectory())
            {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false)
            {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else
        {
            final File parent = file.getParentFile();
            if (parent != null)
            {
                if (!parent.mkdirs() && !parent.isDirectory())
                {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }

    public static int copy(final Reader input, final Writer output) throws IOException
    {
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE)
        {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(final Reader input, final Writer output) throws IOException
    {
        return copyLarge(input, output, new char[DEFAULT_BUFFER_SIZE]);
    }

    public static long copyLarge(final Reader input, final Writer output, final char[] buffer) throws IOException
    {
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer)))
        {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void writeByteArrayToFile(final File file, final byte[] data) throws IOException
    {
        writeByteArrayToFile(file, data, false);
    }

    public static void writeByteArrayToFile(final File file, final byte[] data, final boolean append) throws IOException
    {
        writeByteArrayToFile(file, data, 0, data.length, append);
    }

    public static void writeByteArrayToFile(final File file, final byte[] data, final int off, final int len) throws IOException
    {
        writeByteArrayToFile(file, data, off, len, false);
    }

    public static void writeByteArrayToFile(final File file, final byte[] data, final int off, final int len, final boolean append) throws IOException
    {
        OutputStream out = null;
        try
        {
            out = openOutputStream(file, append);
            out.write(data, off, len);
            out.close(); // don't swallow close Exception if copy completes normally
        } finally
        {
            IOUtils.closeQuietly(out);
        }
    }

    public static String IOtoString(final Reader input) throws IOException
    {
        final StringBuilderWriter sw = new StringBuilderWriter();
        copy(input, sw);
        return sw.toString();
    }


    /**
     * Return the distance between two positions without paying intention to Y axis.
     */
    public static double getDistanceWithNoY(BlockPos pos1, BlockPos pos2)
    {
        pos1 = new BlockPos(pos1.getX(), 0, pos1.getZ());
        pos2 = new BlockPos(pos2.getX(), 0, pos2.getZ());
        return pos1.getDistance(pos2.getX(), 0, pos2.getZ());
    }

    /**
     * Parse a String to a BlockPos.
     * The String has to be like that: "123,31,3891"
     * For example, "123,31,3891" will return new BlockPos(123, 31, 3891)
     */
    public static BlockPos parseBlockPosFromString(String stringPos)
    {
        String[] xyz = stringPos.split(",");
        if (xyz.length == 3)
        {
            int x = Integer.parseInt(xyz[0]);
            int y = Integer.parseInt(xyz[1]);
            int z = Integer.parseInt(xyz[2]);
            return new BlockPos(x, y, z);
        } else
        {
            System.err.println("The parsed BlockPos value has to be like that: \"x, y, z\"");
        }
        return BlockPos.ORIGIN;
    }

    public static String blockPosToString(BlockPos pos)
    {
        return pos.getX() + "," + pos.getY() + "," + pos.getZ();
    }

    public static String readJsonFromUrl(String url) throws IOException, IOException
    {
        URL urlObject;
        URLConnection uc;
        StringBuilder parsedContentFromUrl = new StringBuilder();
        urlObject = new URL(url);
        uc = urlObject.openConnection();
        uc.connect();
        uc = urlObject.openConnection();
        uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        uc.getInputStream();
        InputStream is = uc.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        int ch;
        while ((ch = in.read()) != -1)
        {
            parsedContentFromUrl.append((char) ch);
        }
        return parsedContentFromUrl.toString();
    }

    /*
    This function is for convert an ItemStack to a String. It is useful for configuration files.
 */
    public static String getStringFromItemStack(ItemStack stack)
    {
        return stack.serializeNBT().toString();
    }


    /*

     */
    public static ItemStack getItemStackFromString(String stack)
    {
        try
        {
            return new ItemStack(JsonToNBT.getTagFromJson(stack));
        } catch (NBTException e)
        {
            e.printStackTrace();
        }
        return ItemStack.EMPTY;
    }

    /**
     * Just send a pop-message
     */
    public static void sendPopMessage(EntityPlayerMP player, String message, int duration)
    {
        KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketSendPopMessage(message, duration), player);
    }

    public static boolean isValidJson(String json)
    {
        try
        {
            return new JsonParser().parse(json).getAsJsonObject() != null;
        } catch (Throwable ignored)
        {
        }
        return false;
    }

    public static boolean isOP(String playerName)
    {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (!server.isDedicatedServer()) return true;
        for (String oppedPlayerName : server.getPlayerList().getOppedPlayerNames())
        {
            if (oppedPlayerName.equalsIgnoreCase(playerName)) return true;
        }
        return false;
    }

    public static String toRawUnformattedString(ITextComponent component)
    {
        String comp = component.getUnformattedText();
        for (TextFormatting value : TextFormatting.values()) comp = comp.replace(value.toString(), "");
        return comp;
    }

    public static void sendToAll(IMessage message)
    {
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(player -> KeldariaPacketHandler.getInstance().getNetwork().sendTo(message, player));
    }

    public static void spawnEntityItem(ItemStack stack, World world, BlockPos pos)
    {
        if (world.isRemote) return;
        EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        world.spawnEntity(item);
    }

    public static EntityPlayerMP getPlayerMP(EntityPlayer player)
    {
        if(player instanceof EntityPlayerMP ) return (EntityPlayerMP) player;
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(player.getName());
    }

    public static EntityPlayerMP getPlayerByUsername(String name)
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(name);
    }

    public static EntityPlayerMP getPlayerByEntityId(int entityId)
    {
        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
        {
            if(player.getEntityId() == entityId)
            {
                return player;
            }
        }
        return null;
    }

    public static String spaceListWithComa(List<String> list)
    {
        StringBuilder builder = new StringBuilder();
        list.forEach(string -> builder.append(string).append(","));
        return builder.toString().substring(0, builder.toString().length() - 1);
    }

    public static BlockPos getRelative(BlockPos pos, EnumFacing face)
    {
        return getRelative(pos, face, 1);
    }

    public static BlockPos getRelative(BlockPos pos, EnumFacing face, int distance)
    {
        return pos.offset(face, distance);
    }

    public static String dropFirstString(String[] str)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < str.length; i++) builder.append(" ").append(str[i]);
        return (builder.toString().substring(1));
    }

    public static boolean isSameTag(NBTTagCompound one, NBTTagCompound other)
    {
        return one.equals(other);
    }

    public static int countItemInInventory(EntityPlayer player, Item type, int meta)
    {
        int number = 0;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack.getItem() == type && (stack.getMetadata() == meta || meta == -1))
            {
                number += stack.getCount();
            }
        }
        return number;
    }

    public static void removeQuantityOfItem(EntityPlayer player, Item item, int meta, int quantity)
    {
        int least = quantity;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            if (least <= 0) return;
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack.getItem() == item && (stack.getMetadata() == meta || meta == -1))
            {
                int count = stack.getCount();
                if (least > count)
                {
                    stack.setCount(0);
                } else if (least < count)
                {
                    stack.setCount(count - least);
                } else
                {
                    player.inventory.removeStackFromSlot(i);
                }
                least -= count;
            }
        }
    }


    public static void clientSendMessageTo(String playerName, String msg)
    {
        KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketDisplayInChatOf(playerName, msg));
    }

    public static void sendInRadius(BlockPos pos, ITextComponent component, double radius)
    {
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(p ->
        {
            double d = pos.getDistance((int) p.posX, (int) p.posY, (int) p.posZ);
            if (d <= radius)
            {
                p.sendMessage(component);
            }
        });
    }

    public static Date getLastPlayerConnection(String playerName)
    {
        Database playerData = Databases.getPlayerData(playerName);
        long timestamp = 0;
        try
        {
            timestamp = Long.valueOf(playerData.getString("LastConnection"));
        } catch (Exception e)
        {
        }
        if (timestamp != 0)
        {
            Date date = new Date(timestamp);
            return date;
        }
        return null;
    }

    public static void markPlayerConnection(EntityPlayer player)
    {
        Database playerData = Databases.getPlayerData(player);
        long timestamp = new Date().getTime();
        playerData.setString("LastConnection", String.valueOf(timestamp));
    }

    public static boolean isDebugEnable()
    {
        return ClientDatabases.getPersonalPlayerData().getInteger("DebugMode") == 1;
    }

    public static double getPercent(double value, double max)
    {
        return value * 100 / max;
    }

    public static float getPercent(float value, float max)
    {
        return value * 100 / max;
    }
    public static int getPercent(int value, int max)
    {
        return value * 100 / max;
    }


    public static final Random RANDOM = new Random();
    public static double randomDouble(double min, double max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return min + (max - min) * RANDOM.nextDouble();
    }
    public static int randomInteger(int min, int max)
    {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return RANDOM.nextInt((max - min) + 1) + min;
    }

    public static int crossMult(double value, double max, double factor)
    {
        return (int) (value * factor / max);
    }

    public static NBTTagCompound getCompoundTag(ItemStack stack)
    {
        return stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
    }

    public static boolean parseOrFalse(String str)
    {
        try
        {
            return Boolean.parseBoolean(str);
        }
        catch(Exception ex)
        {
            return false;
        }
    }

    public static int parseOrZero(String str)
    {
        try
        {
            return Integer.parseInt(str);
        }
        catch(Exception ex)
        {
            return 0;
        }
    }
    public static double parseDoubleOrZero(String str)
    {
        try
        {
            return Double.parseDouble(str);
        }
        catch(Exception ex)
        {
            return 0;
        }
    }

    public static float parseFloatOrZero(String str)
    {
        try
        {
            return Float.parseFloat(str);
        }
        catch(Exception ex)
        {
            return 0;
        }
    }


    public static double getChunkDistance(int x1, int z1, int x2, int z2)
    {
        System.out.println(x1 + "|" + z1 + "  /  " + x2 + "|" + z2);
        double d0 = (double)(x1 - x2);
        double d2 = (double)(z1 - z2);
        return Math.sqrt(d0 * d0 + d2 * d2);
    }

    public static Vector2i stringToChunkPos(String str)
    {
        String[] parts = str.split(",");
        if(parts.length == 2)
        {
            return new Vector2i(parseOrZero(parts[0]), parseOrZero(parts[1]));
        }
        return new Vector2i(0, 0);
    }

    public static String chunkPosToString(Vector2i chunkPos)
    {
        return chunkPos.x + "," + chunkPos.y;
    }

    public static String chunkPosToString(int x, int z)
    {
        return x + "," + z;
    }


    /**
     * Only supports vanilla
     */
    public static Material getMat(String s)
    {
        s = s.toUpperCase();
        if(s.equals("AIR"))
            return Material.AIR;
        else if(s.equals("ANVIL"))
            return Material.ANVIL;
        else if(s.equals("BARRIER"))
            return Material.BARRIER;
        else if(s.equals("CACTUS"))
            return Material.CACTUS;
        else if(s.equals("CAKE"))
            return Material.CAKE;
        else if(s.equals("CIRCUITS"))
            return Material.CIRCUITS;
        else if(s.equals("CARPET"))
            return Material.CARPET;
        else if(s.equals("CLAY"))
            return Material.CLAY;
        else if(s.equals("CLOTH"))
            return Material.CLOTH;
        else if(s.equals("CORAL"))
            return Material.CORAL;
        else if(s.equals("CRAFTED_SNOW"))
            return Material.CRAFTED_SNOW;
        else if(s.equals("DRAGON_EGG"))
            return Material.DRAGON_EGG;
        else if(s.equals("FIRE"))
            return Material.FIRE;
        else if(s.equals("GLASS"))
            return Material.GLASS;
        else if(s.equals("GOURD"))
            return Material.GOURD;
        else if(s.equals("GRASS"))
            return Material.GRASS;
        else if(s.equals("GROUND"))
            return Material.GROUND;
        else if(s.equals("ICE"))
            return Material.ICE;
        else if(s.equals("IRON"))
            return Material.IRON;
        else if(s.equals("LAVA"))
            return Material.LAVA;
        else if(s.equals("LEAVES"))
            return Material.LEAVES;
        else if(s.equals("PACKED_ICE"))
            return Material.PACKED_ICE;
        else if(s.equals("PISTON"))
            return Material.PISTON;
        else if(s.equals("PLANTS"))
            return Material.PLANTS;
        else if(s.equals("PORTAL"))
            return Material.PORTAL;
        else if(s.equals("REDSTONE_LIGHT"))
            return Material.REDSTONE_LIGHT;
        else if(s.equals("ROCK"))
            return Material.ROCK;
        else if(s.equals("SAND"))
            return Material.SAND;
        else if(s.equals("SNOW"))
            return Material.SNOW;
        else if(s.equals("SPONGE"))
            return Material.SPONGE;
        else if(s.equals("STRUCTURE_VOID"))
            return Material.STRUCTURE_VOID;
        else if(s.equals("TNT"))
            return Material.TNT;
        else if(s.equals("VINE"))
            return Material.VINE;
        else if(s.equals("WATER"))
            return Material.WATER;
        else if(s.equals("WEB"))
            return Material.WEB;
        else if(s.equals("WOOD"))
            return Material.WOOD;

        return null;
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm");

    public static void log(String log)
    {
        System.out.println(String.format("[%s] [Keldaria] %s", DATE_FORMAT.format(new Date()), log));
    }

    public static void handleMinimapCheck(FMLPostInitializationEvent e)
    {
        if (e.getSide() == Side.CLIENT)
        {
            for (ModContainer modContainer : Loader.instance().getModList())
            {
                if (modContainer.getModId().toLowerCase().contains("map"))
                {
                    JOptionPane.showMessageDialog(null, "Va te faire souiller avec ton mod de minimap :)");
                    System.exit(0);
                }
            }
        }
    }

    public static BlockPos getBlockInFrontOf(Entity player)
    {
        BlockPos base = new BlockPos(player.posX, player.posY, player.posZ);
        IBlockState state = player.world.getBlockState(base);
        if(state.getBlock().getCollisionBoundingBox(state, player.world, base) != null)
        {
            return base;
        }
        return base.offset(player.getHorizontalFacing());
    }


    public static Collection<Chunk> getLoadedChunks(World world)
    {
        if (world.getChunkProvider() instanceof ChunkProviderServer)
        {
            ChunkProviderServer chunkProvider = (ChunkProviderServer) world.getChunkProvider();
            return chunkProvider.getLoadedChunks();
        }
        return Lists.newArrayList();
    }

    public static Vec3d processNormalizeColor(Vec3d actual, Vec3d destination, double i)
    {
        if(actual.x != destination.x)
        {
            if(actual.x > destination.x)
                actual = actual.add(-i, 0, 0);
            else actual = actual.add(i, 0, 0);
        }
        if(actual.y != destination.y)
        {
            if(actual.y > destination.y)
                actual = actual.add(0, -i, 0);
            else actual = actual.add(0, i, 0);
        }
        if(actual.z != destination.z)
        {
            if(actual.z > destination.z)
                actual = actual.add(0, 0, -i);
            else actual = actual.add(0, 0, i);
        }
        return actual;
    }

    public static ITextComponent readTextComponent(ByteBuf buf)
    {
        return ITextComponent.Serializer.jsonToComponent(ByteBufUtils.readUTF8String(buf));
    }

    public static void writeTextComponent(ByteBuf buf, ITextComponent component)
    {
        ByteBufUtils.writeUTF8String(buf, ITextComponent.Serializer.componentToJson(component));
    }

    public static NBTTagCompound serializeVec3d(Vec3d vec)
    {
        NBTTagCompound compound = new NBTTagCompound();
        if(vec != null)
        {
            compound.setDouble("x", vec.x);
            compound.setDouble("y", vec.y);
            compound.setDouble("z", vec.z);
        }
        return compound;
    }

    public static Vec3d deserialize(NBTTagCompound compound)
    {
        if(!compound.hasKey("x")) return null;
        return new Vec3d(compound.getDouble("x"), compound.getDouble("y"), compound.getDouble("z"));
    }


    public static float getYaw(Entity entity)
    {
        if(entity instanceof EntityPlayer)
        {
            if(!entity.world.isRemote)
            {
                System.out.println("YeE " + ((EntityPlayer) entity).prevRenderYawOffset);
            }
            return ((EntityPlayer) entity).prevRenderYawOffset;
        }
        return entity.rotationYaw;
    }

    public static boolean motionPassed(Vec3d mot, Vec3d max)
    {
        boolean xOke = valuePassed(mot.x, max.x);
        boolean yOke = valuePassed(mot.y, max.y);
        boolean zOke = valuePassed(mot.z, max.z);
        return xOke && yOke && zOke;
    }

    public static boolean valuePassed(double val, double max)
    {
        if(val == 0 && max == 0) return true;
        boolean oke = false;
        if(max < 0)
        {
            oke = val <= max;
        }
        else {
            oke = val >= max;
        }
        return oke;
    }

}
