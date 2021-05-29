package fr.nathanael2611.keldaria.mod.proxy;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CommonProxy
{

    private static final ScheduledExecutorService EXECUTE_AFTER = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("execute-after").build());

    public void preInitialization(FMLPreInitializationEvent event)
    {
    }

    public void initialization(FMLInitializationEvent event)
    {

    }

    public static void executeAfter(Runnable toRun, int ms)
    {
        EXECUTE_AFTER.schedule(toRun, ms, TimeUnit.MILLISECONDS);
    }

}
