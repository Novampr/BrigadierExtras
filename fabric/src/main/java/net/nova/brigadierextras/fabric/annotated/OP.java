package net.nova.brigadierextras.fabric.annotated;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OP {
    int value() default -1;
}
