/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PopMessages
{

    private static final ConcurrentLinkedDeque<PopMessage> QUEUE = new ConcurrentLinkedDeque<>();

    public static void doStuff()
    {
        long ts = System.currentTimeMillis();
        for (PopMessage message : QUEUE)
        {
            if (ts - message.timeStamp > message.getDuration())
            {
                QUEUE.remove(message);
            }
        }
    }

    public static void add(String message, int duration)
    {
        QUEUE.add(new PopMessage(message, duration));
    }

    public static List<String> getMessagesToDisplay()
    {
        ArrayList<String> strings = new ArrayList<>();
        QUEUE.forEach(popMessage -> strings.add(popMessage.getMessage()));
        return strings;
    }

    public static class PopMessage
    {
        private String message;
        private int duration;
        private long timeStamp;

        private PopMessage(String message, int duration)
        {
            this.message = message;
            this.duration = duration;
            this.timeStamp = new Date().getTime();
        }

        public String getMessage()
        {
            return message;
        }

        public int getDuration()
        {
            return duration;
        }

    }

}
