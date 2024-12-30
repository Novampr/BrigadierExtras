package net.nova.brigadierextras;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public interface Resolver<T, S> {
    Class<S> getExpectedSenderClass();

    Class<T> getArgumentClass();

    RequiredArgumentBuilder<S, ?> generateArgumentBuilder(String name);

    T getType(CommandContext<S> context, String name) throws CommandSyntaxException;
}
