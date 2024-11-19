package net.nova.brigadierextras.annotated;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public record RootModifier(int priority, Handler function) {
    @FunctionalInterface
    public interface Handler {
        <T> LiteralArgumentBuilder<T> modify(LiteralArgumentBuilder<T> argumentBuilder, Class<?> clazz);
    }
}
