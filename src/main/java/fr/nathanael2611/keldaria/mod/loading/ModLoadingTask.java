/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.loading;

import fr.nathanael2611.keldaria.mod.util.Helpers;

public class ModLoadingTask
{

    private final String name;
    private final Runnable TODO;
    private final LoadingStep loadAfter;
    private final Thread thread;
    private long timeStarted;
    private boolean alreadyRun = false;

    public ModLoadingTask(String name, Runnable todo, LoadingStep step)
    {
        this.timeStarted = System.currentTimeMillis();
        this.name = name;
        this.TODO = todo;
        this.loadAfter = step;
        this.thread = new Thread(name)
        {
            @Override
            public void run()
            {
                TODO.run();
                Helpers.log(String.format("Finished loading-thread %s in %s", name, System.currentTimeMillis() - timeStarted + "milliseconds"));
            }

        };
    }

    public boolean canLoad(LoadingStep actualStep)
    {
        return !alreadyRun && (actualStep == LoadingStep.SCRATCH || this.loadAfter.ordinal() <= actualStep.ordinal());
    }

    public void runTask()
    {
        alreadyRun = true;
        Helpers.log(String.format("Starting loading-thread: %s", this.name));
        this.thread.start();
    }

    public String getName()
    {
        return name;
    }

    public Runnable getTodo()
    {
        return TODO;
    }
}
