package fr.nathanael2611.keldaria.mod.features;

public class TimeBasedSaturation
{

    public static double getMultiplier()
    {
        return getMultiplier(KeldariaDate.lastDate);
    }

    public static double getMultiplier(KeldariaDate.KeldariaDateFormat date)
    {
        switch (date.getHour())
        {
            case 0: return 1;
            case 1: return 1.05;
            case 2: return 1.1;
            case 3: return 1.15;
            case 4: return 1.2;
            case 5: return 1.3;
            case 6: return 1.35;
            case 7: return 1.4;
            case 8: return 1.55;
            case 9: return 1.7;
            case 10: return 1.9;
            case 11: return 2.4;
            case 12: return 3.2;
            case 13: return 2.5;
            case 14: return 2.1;
            case 15: return 1.8;
            case 16: return 1.5;
            case 17: return 1.25;
            case 18: return 1.6;
            case 19: return 2.55;
            case 20: return 1.9;
            case 21: return 1.7;
            case 22: return 1.4;
            case 23: return 1.15;
            default: return 1;
        }
    }

}
