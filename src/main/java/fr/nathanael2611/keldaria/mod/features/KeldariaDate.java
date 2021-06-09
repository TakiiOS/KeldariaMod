/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features;

import fr.nathanael2611.keldaria.mod.season.Season;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

import java.util.concurrent.TimeUnit;

public class KeldariaDate
{

    public enum Unit
    {

        DAYS {
            @Override
            public long toMillis(long value)
            {
                return TimeUnit.HOURS.toMillis(12) * value;
            }
        },
        MONTH {
            @Override
            public long toMillis(long value)
            {
                return DAYS.toMillis(14) * value;
            }
        };

        public abstract long toMillis(long value);

    }

    public enum Month
    {
        CEFONE(0, "Cefône"),
        MIST(1, "Mist"),
        PAIS(2, "Païs"),
        ISTES(3, "Istès"),
        YAVANA(4, "Yavana"),
        MAGUIA(5, "Maguï'a"),
        LICH(6, "Lich"),
        FRISK(7, "Frisk");

        private int index;
        private String formattedName;

        Month(int index, String formattedName)
        {
            this.index = index;
            this.formattedName = formattedName;
        }

        public static Month byName(String arg)
        {
            for (Month value : values())
            {
                if(value.getFormattedName().equalsIgnoreCase(arg))
                {
                    return value;
                }
            }
            return null;
        }

        public String getFormattedName()
        {
            return formattedName;
        }

        public static Month byIndex(int index)
        {
            for (Month value : values())
            {
                if(value.index == index - 1)
                {
                    return value;
                }
            }
            return null;
        }

        public int getIndex()
        {
            return index;
        }

        @Override
        public String toString()
        {
            return this.formattedName;
        }
    }
    //public static final String[] MONTHS = new String[] {"Cefône", "Mist", "Païs", "Istès", "Yavana", "Maguï'a", "Lich", "Frisk"};

    public static KeldariaDateFormat lastDate = getKyrgonDate();

    public static KeldariaDateFormat getKyrgonDate()
    {
        return getKyrgonDate(System.currentTimeMillis());
    }

    public static KeldariaDateFormat getKyrgonDate(long millis)
    {
        long menoser = ((18100*24)*60)*60;

        long timeInSeconds = ((millis/1000) /* menoser start */ - menoser -  (60* 60 * (23 * 20)) - /* end of menoser */  (((5) * 60) * 60)   ) + ((5 * 60) * 60) + (86400 * 6 - 86400 / 2);


        timeInSeconds += 3600*12 + (3600 * 2);

        int rolePlayDays = 14;
        int rpDayTimeSec = 43200;

        int moonPhase = 0;
        for(; ;)
        {
            if (timeInSeconds > rpDayTimeSec)
            {
                timeInSeconds -= rpDayTimeSec;
                rolePlayDays++;
                moonPhase ++;
                if(moonPhase > 7) moonPhase = 0;
            } else break;
        }
        int rpYearTimeInDays = 112;
        int rolePlayYear = 4673;
        {
            int rolePlayDaysJustForDate = rolePlayDays;
            for (; ; )
            {
                if (rolePlayDaysJustForDate > rpYearTimeInDays)
                {
                    rolePlayDaysJustForDate -= rpYearTimeInDays;
                    rolePlayYear++;
                } else break;
            }
        }

        long rpDayInSeconds = timeInSeconds * 2;
        long rpDayInMinutes = rpDayInSeconds / 60;
        long rpDayInHours = rpDayInMinutes / 60;


        int rpMonthTimeInDay = 14;

        int rpMonths = 0;
        int rpDaysCount = rolePlayDays;
        for(; ;)
        {
            if(rpDaysCount > rpMonthTimeInDay)
            {
                rpDaysCount -= rpMonthTimeInDay;
                rpMonths ++;
            } else break;
        }
        int monthIndex = rpMonths+1;
        for(; ;)
        {
            if(monthIndex > 8)
            {
                monthIndex -= 8;
            } else break;
        }


        {
            long actualRPMinutesLong = (rpDayInMinutes - (rpDayInHours * 60));
            String actualRPMinutes = actualRPMinutesLong + "";
            if(actualRPMinutes.length() == 1) actualRPMinutes = "0" + actualRPMinutes;
            String actualRPHours = rpDayInHours + "";
            if(actualRPHours.length() == 1) actualRPHours = "0" + actualRPHours;
            String actualRPHour = actualRPHours + ":" + actualRPMinutes;
            //return rpDaysCount + "/" + monthIndex + "/" + rolePlayYear + " | " + actualRPHour;

            int h = Integer.parseInt(actualRPHour.split(":")[0]);

            if(h > 15)
            {
                moonPhase ++;
                if(moonPhase > 7) moonPhase = 0;
            }

            return lastDate = new KeldariaDateFormat(rolePlayDays, rpDaysCount, monthIndex, rolePlayYear, (int) rpDayInHours, (int) actualRPMinutesLong, actualRPHour, (rpDayInSeconds - (rpDayInMinutes * 60)), moonPhase);
        }
    }

    public static class KeldariaDateFormat
    {

        private int totalDaysInRP;
        private int monthIndex;
        private int hourIndex;
        private int minuteIndex;
        private String hour;
        private int dayIndex;
        private int year;
        private long seconds;
        private int moonPhase;

        public KeldariaDateFormat(int totalDaysInRP, int dayIndex, int monthIndex, int year, int hourIndex, int minuteIndex, String hour, long seconds, int moonPhase)
        {
            this.totalDaysInRP = totalDaysInRP;
            this.monthIndex = monthIndex;
            this.hourIndex = hourIndex;
            this.minuteIndex = minuteIndex;
            this.hour = hour;
            this.dayIndex = dayIndex;
            this.year = year;
            this.seconds = seconds;
            this.moonPhase = moonPhase;
        }

        public int getTotalDaysInRP()
        {
            return totalDaysInRP;
        }

        @Override
        public String toString() {
            return dayIndex + "/" + monthIndex + "/" + year + " | " + hour + "  /   " + seconds;
        }

        public String toFormattedString()
        {
            return this.dayIndex + " " + getMonthByIndex(this.monthIndex) + " " + this.year + " - " + this.hour;
        }



        public long toTicks()
        {
            String[] parts = this.hour.split(":");
            int hour = Integer.parseInt(parts[0]);
            final int minute = Integer.parseInt(parts[0]);
            final int second = (int) this.seconds;
            String strSecond;
            if (Integer.toString(second).length() == 1) strSecond = "0";
            else strSecond = Integer.toString(second).substring(0, 1);
            if (hour < 6) {
                hour += 24;
            }
            hour -= 6;
            String time;
            if (Integer.toString(hour).length() == 1) {
                time = "0" + hour;
            }
            else {
                time = Integer.toString(hour);
            }
            if (Integer.toString(minute).length() == 1) {
                time = time + "0" + minute;
            }
            else {
                time = time + minute;
            }
            time = time + strSecond;
            final long mcTime = Long.parseLong(time);
            return mcTime;
        }

        public Season getSeason() {
            switch (monthIndex - 1) {
                case 0: return Season.WINTER;
                case 1: return Season.SPRING;

                case 2: return Season.SPRING;
                case 3: return Season.SUMMER;
                case 4: return Season.SUMMER;

                case 5: return Season.AUTUMN;
                case 6: return Season.AUTUMN;
                case 7: return Season.WINTER;
            }
            return null;
        }

        public Month getMonth()
        {
            return getMonthByIndex(this.monthIndex);
        }

        public int getMoonPhase()
        {
            int h = Integer.parseInt(this.hour.split(":")[0]);
            switch (dayIndex)
            {
                case 0: return getMoonByHour(h, 7, 0);
                case 1: return getMoonByHour(h, 0, 1);
                case 2: return getMoonByHour(h, 1, 2);
                case 3: return getMoonByHour(h, 2, 3);
                case 4: return getMoonByHour(h, 3, 4);
                case 5: return getMoonByHour(h, 4, 5);
                case 6: return getMoonByHour(h, 5, 6);
                case 7: return getMoonByHour(h, 6, 7);
                case 8: return getMoonByHour(h, 7, 0);

                case 9: return getMoonByHour(h, 0, 1);
                case 10: return getMoonByHour(h, 1, 2);
                case 11: return getMoonByHour(h, 2, 3);
                case 12: return getMoonByHour(h, 3, 4);
                case 13: return getMoonByHour(h, 4, 5);
            }
            return 0;
        }

        public void updateWeather(World world)
        {
            if(true) return;
            WorldInfo worldinfo = world.getWorldInfo();
            int x = (int) (seconds / 10);
            if(x % 2 == 0)
            {
                worldinfo.setCleanWeatherTime(0);
                worldinfo.setRainTime(100);
                worldinfo.setThunderTime(100);
                worldinfo.setRaining(true);
                worldinfo.setThundering(false);
                world.setRainStrength(100);
            }
            else
            {
                worldinfo.setCleanWeatherTime(100);
                worldinfo.setRainTime(0);
                worldinfo.setThunderTime(0);
                worldinfo.setRaining(false);
                worldinfo.setThundering(false);
            }
        }

        public int getHour()
        {
            return this.hourIndex;
        }

        public int getMinute()
        {
            return minuteIndex;
        }
    }

    private static int getMoonByHour(int hour, int beforeMoon, int afterMoon)
    {
        if(hour < 15)
        {
            return beforeMoon;
        }
        return afterMoon;
    }


    public static Month getMonthByIndex(int index)
    {
        return Month.byIndex(index);
        //return MONTHS[index - 1];
    }

    public static int getIndexByMonth(Month month)
    {/*
        for (int i = 0; i < MONTHS.length; i++)
        {
            if(MONTHS[i].equalsIgnoreCase(month))
            {
                return i + 1;
            }
        }
        return -1;*/
        return month.getIndex();
    }


    public static class KeldariaBirthday
    {

        public static KeldariaBirthday getBirthDay(EntityPlayer player)
        {
            String birthDayString = player.world.isRemote ? ClientDatabases.getPersonalPlayerData().getString("Birthday") : Databases.getPlayerData(player).getString("Birthday");
            KeldariaBirthday invalid = new KeldariaBirthday(0, 0, 0);
            if(birthDayString == null) return invalid;
            String[] parts = birthDayString.split("/");
            try
            {
                if(parts.length == 3)
                {
                    int day = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int year = Integer.parseInt(parts[2]);
                    return new KeldariaBirthday(day, month, year);
                }
                return invalid;
            } catch(Exception ex)
            {
                return invalid;
            }
        }

        private int day;
        private int month;
        private int year;

        public KeldariaBirthday(int day, int month, int year)
        {
            this.day = Math.max(1, Math.min(day, 14));
            this.month = Math.max(1, Math.min(month, 8));
            this.year = year;
        }

        @Override
        public String toString()
        {
            return String.format("%s %s %s", day, getMonthByIndex(month), year);
        }

        public String toStorableString()
        {
            return String.format("%s/%s/%s", day, month, year);
        }

        public boolean isBirthdayNow(KeldariaDateFormat date)
        {
            return day == date.dayIndex && month == date.monthIndex;
        }

        public int getAge(KeldariaDateFormat date)
        {
            int age = date.year - year;
            if(month ==date.monthIndex)
            {
                if(day < date.dayIndex)
                    age --;
            }
            else if(month > date.monthIndex)
            {
                age --;
            }
            return age;
        }

        public boolean isValid()
        {
            return day > 0 && month > 0 && year > 0;
        }
    }
}
