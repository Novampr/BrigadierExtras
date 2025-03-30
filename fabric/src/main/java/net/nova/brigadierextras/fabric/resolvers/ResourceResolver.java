package net.nova.brigadierextras.fabric.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.nova.brigadierextras.Resolver;
import net.nova.brigadierextras.fabric.FabricBrigadierExtras;

public class ResourceResolver<T> implements Resolver<T, CommandSourceStack> {
    private final Class<T> registeryClass;
    private final ResourceKey<Registry<T>> key;


    public ResourceResolver(Class<T> registeryClass, ResourceKey<? extends Registry<? extends T>> key) {
        this.registeryClass = registeryClass;
        this.key = (ResourceKey<Registry<T>>) key;
    }

    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<T> getArgumentClass() {
        return registeryClass;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, ResourceArgument.resource(FabricBrigadierExtras.context, key));
    }

    @Override
    public T getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return ResourceArgument.getResource(context, name, key).value();
    }
}
