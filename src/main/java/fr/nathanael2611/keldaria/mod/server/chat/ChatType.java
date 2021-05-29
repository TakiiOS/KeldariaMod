package fr.nathanael2611.keldaria.mod.server.chat;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.server.ChatUsernameAssets;
import fr.nathanael2611.keldaria.mod.server.RolePlayNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;

public abstract class ChatType
{

    private static final HashMap<String, ChatType> REGISTRY = Maps.newHashMap();

    public static final ChatType SPEAK = new ChatType(15) {
        @Override
        public ITextComponent format(EntityPlayer player, String usedPrefix, String message)
        {
            return new TextComponentString(TextFormatting.WHITE + " ● " + TextFormatting.GRAY + RolePlayNames.getName(player) + TextFormatting.WHITE + " dit: " + message.substring(usedPrefix.length()));
        }
    };

    public static final ChatType LOW_SPEAK = new ChatType(6) {
        @Override
        public ITextComponent format(EntityPlayer player, String usedPrefix, String message)
        {
            return new TextComponentString(TextFormatting.WHITE + " ● " + TextFormatting.GRAY + RolePlayNames.getName(player) + TextFormatting.WHITE + " parle bas: " + message.substring(usedPrefix.length()));
        }
    };

    public static final ChatType LOUD_SPEAK = new ChatType(24) {
        @Override
        public ITextComponent format(EntityPlayer player, String usedPrefix, String message)
        {
            return new TextComponentString(TextFormatting.WHITE + " ● " + TextFormatting.GRAY + RolePlayNames.getName(player) + TextFormatting.WHITE + " parle fort: " + message.substring(usedPrefix.length()));
        }
    };

    public static final ChatType SHOUT = new ChatType(35) {
        @Override
        public ITextComponent format(EntityPlayer player, String usedPrefix, String message)
        {
            return new TextComponentString(TextFormatting.RED + " ● " + TextFormatting.GRAY + RolePlayNames.getName(player) + TextFormatting.RED + " crie: " + message.substring(usedPrefix.length()));
        }
    };

    public static final ChatType WHISP = new ChatType(4) {
        @Override
        public ITextComponent format(EntityPlayer player, String usedPrefix, String message)
        {
            return new TextComponentString(TextFormatting.DARK_GREEN + " ● " + TextFormatting.GRAY + RolePlayNames.getName(player) + TextFormatting.DARK_GREEN + " chuchote: " + message.substring(usedPrefix.length()));
        }
    };


    public static final ChatType ACTION = new ChatType(15) {
        @Override
        public ITextComponent format(EntityPlayer player, String usedPrefix, String message)
        {
            return new TextComponentString(TextFormatting.LIGHT_PURPLE + " * " + TextFormatting.GRAY + "" + TextFormatting.ITALIC + RolePlayNames.getName(player) + " " + TextFormatting.LIGHT_PURPLE + "" + TextFormatting.ITALIC + message.substring(usedPrefix.length()));
        }

        @Override
        public boolean isVoice()
        {
            return false;
        }
    };

    public static final ChatType SHORT_ACTION = new ChatType(3) {
        @Override
        public ITextComponent format(EntityPlayer player, String usedPrefix, String message)
        {
            return new TextComponentString(TextFormatting.DARK_PURPLE + " * " + TextFormatting.GRAY + "" + TextFormatting.ITALIC + RolePlayNames.getName(player) + " " + TextFormatting.DARK_PURPLE + "" + TextFormatting.ITALIC + message.substring(usedPrefix.length()));
        }

        @Override
        public boolean isVoice()
        {
            return false;
        }
    };

    public static final ChatType LOCAL_NARR = new ChatType(15) {
        @Override
        public ITextComponent format(EntityPlayer player, String usedPrefix, String message)
        {
            return new TextComponentString(TextFormatting.DARK_PURPLE + " ● " + TextFormatting.LIGHT_PURPLE + "§o " + message.substring(usedPrefix.length()));
        }

        @Override
        public boolean isVoice()
        {
            return false;
        }
    };

    public static final ChatType GLOBAL_NARR = new ChatType(0) {
        @Override
        public ITextComponent format(EntityPlayer player, String usedPrefix, String message)
        {
            return new TextComponentString(TextFormatting.DARK_RED + " ● " + TextFormatting.RED + "§o " + message.substring(usedPrefix.length()));
        }

        @Override
        public boolean isVoice()
        {
            return false;
        }
    };

    public static final ChatType HRP = new ChatType() {
        @Override
        public ITextComponent format(EntityPlayer player, String usedPrefix, String message)
        {
            message = message.substring(usedPrefix.length());
            String o = TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC;
            return new TextComponentString( o+ " ( §7§o" + ChatUsernameAssets.getFormattedUsername(player) + o + " : " + message + o + " )");
        }

        @Override
        public boolean isVoice()
        {
            return false;
        }
    };

    public static final ChatType LOCAL_HRP = new ChatType(20) {
        @Override
        public ITextComponent format(EntityPlayer player, String usedPrefix, String message)
        {
            message = message.substring(usedPrefix.length());
            String o = TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC;
            return new TextComponentString( " §f● " + "§7" + RolePlayNames.getName(player) + " §f[HRP-LOCAL] §8§o(" +  o + "" + message + o + ")");
        }

        @Override
        public boolean isVoice()
        {
            return false;
        }
    };



    static
    {

        REGISTRY.put("*$", SHORT_ACTION);
        REGISTRY.put("[", LOCAL_HRP);
        REGISTRY.put("(", HRP);
        REGISTRY.put(")", HRP);
        REGISTRY.put("#", LOCAL_NARR);
        REGISTRY.put("%", GLOBAL_NARR);
        REGISTRY.put("!", SHOUT);
        REGISTRY.put("$", WHISP);
        REGISTRY.put("*", ACTION);
        REGISTRY.put("+", LOUD_SPEAK);
        REGISTRY.put("-", LOW_SPEAK);
    }

    public static Map.Entry<String, ChatType> getFor(String message)
    {
        for (Map.Entry<String, ChatType> entry : REGISTRY.entrySet())
        {
            if(message.startsWith(entry.getKey())) return entry;
        }
        return new Map.Entry<String, ChatType>() {
            @Override
            public String getKey() {
                return "";
            }

            @Override
            public ChatType getValue() {
                return SPEAK;
            }

            @Override
            public ChatType setValue(ChatType value) {
                return null;
            }
        };
    }

    private int distance;

    public ChatType()
    {
        this(0);
    }

    public ChatType(int distance)
    {
        this.distance = distance;
    }

    public int getDistance()
    {
        return distance;
    }

    public boolean hasLimitedRange()
    {
        return distance > 0;
    }

    public abstract ITextComponent format(EntityPlayer player, String usedPrefix, String message);

    public boolean isVoice()
    {
        return true;
    }

}
