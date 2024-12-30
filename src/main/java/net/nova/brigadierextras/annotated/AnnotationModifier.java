package net.nova.brigadierextras.annotated;

import com.mojang.brigadier.builder.ArgumentBuilder;

import java.lang.annotation.Annotation;

public record AnnotationModifier<A extends Annotation>(int priority, Class<A> annotationClass, Handler<A> handler) {
    @FunctionalInterface
    public interface Handler<A extends Annotation> {
        ArgumentBuilder<?, ?> modify(ArgumentBuilder<?, ?> argumentBuilder, A annotation);
    }
}
