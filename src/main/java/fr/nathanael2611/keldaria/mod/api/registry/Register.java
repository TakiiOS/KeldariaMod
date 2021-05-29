package fr.nathanael2611.keldaria.mod.api.registry;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Register
{

    String name();

    boolean defaultModel() default true;

    boolean hasItem() default true;

}
