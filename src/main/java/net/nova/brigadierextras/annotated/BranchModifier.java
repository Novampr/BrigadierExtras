package net.nova.brigadierextras.annotated;

import com.mojang.brigadier.builder.ArgumentBuilder;

import java.lang.reflect.Method;

public record BranchModifier(int priority, Handler function) {
    @FunctionalInterface
    public interface Handler {
        <T> ArgumentBuilder<T, ?> modify(ArgumentBuilder<T, ?> argumentBuilder, Method method, Class<?> clazz);
    }
}
