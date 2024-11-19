package net.nova.brigadierextras.annotated;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    int SUCCESS = 1;

    String value() default "";
}
