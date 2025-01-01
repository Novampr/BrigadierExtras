package net.nova.brigadierextras.paper.resolvers;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.resolvers.ArgumentResolver;
import net.nova.brigadierextras.Resolver;

import java.util.function.Function;

/**
 * What? Yes, PaperMC has resolver types, so we can resolve those.
 */
public class ResolverResolver<T, O> implements Resolver<O, CommandSourceStack> {
    private final ArgumentType<? extends ArgumentResolver<T>> type;
    private final Class<? extends ArgumentResolver<T>> argumentTypeClass;
    private final Class<O> clazz;
    private final Function<T, O> transformer;

    public ResolverResolver(ArgumentType<? extends ArgumentResolver<T>> type, Class<? extends ArgumentResolver<T>> argumentTypeClass, Class<T> clazz) {
        this(type, argumentTypeClass, (Class<O>) clazz, t -> (O) t);
    }

    public ResolverResolver(ArgumentType<? extends ArgumentResolver<T>> type, Class<? extends ArgumentResolver<T>> argumentTypeClass, Class<O> clazz, Function<T, O> transformer) {
        this.clazz = clazz;
        this.argumentTypeClass = argumentTypeClass;
        this.type = type;
        this.transformer = transformer;
    }

    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<O> getArgumentClass() {
        return clazz;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    @Override
    public O getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return transformer.apply(context.getArgument(name, argumentTypeClass).resolve(context.getSource()));
    }
}
