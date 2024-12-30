package net.nova.brigadierextras.paper.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.nova.brigadierextras.Resolver;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityResolver implements Resolver<Entity, CommandSourceStack> {
    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<Entity> getArgumentClass() {
        return Entity.class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, ArgumentTypes.entity());
    }

    @Override
    public Entity getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return context.getArgument(name, EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
    }

    public static class Multiple implements Resolver<Entity[], CommandSourceStack> {
        @Override
        public Class<CommandSourceStack> getExpectedSenderClass() {
            return CommandSourceStack.class;
        }

        @Override
        public Class<Entity[]> getArgumentClass() {
            return Entity[].class;
        }

        @Override
        public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
            return RequiredArgumentBuilder.argument(name, ArgumentTypes.entities());
        }

        @Override
        public Entity[] getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
            return context.getArgument(name, EntitySelectorArgumentResolver.class).resolve(context.getSource()).toArray(new Entity[]{});
        }
    }
}
