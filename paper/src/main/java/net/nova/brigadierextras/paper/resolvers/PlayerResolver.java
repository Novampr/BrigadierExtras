package net.nova.brigadierextras.paper.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.nova.brigadierextras.Resolver;
import org.bukkit.entity.Player;

public class PlayerResolver implements Resolver<Player, CommandSourceStack> {
    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<Player> getArgumentClass() {
        return Player.class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, ArgumentTypes.player());
    }

    @Override
    public Player getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return context.getArgument(name, PlayerSelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
    }

    public static class Multiple implements Resolver<Player[], CommandSourceStack> {
        @Override
        public Class<CommandSourceStack> getExpectedSenderClass() {
            return CommandSourceStack.class;
        }

        @Override
        public Class<Player[]> getArgumentClass() {
            return Player[].class;
        }

        @Override
        public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
            return RequiredArgumentBuilder.argument(name, ArgumentTypes.players());
        }

        @Override
        public Player[] getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
            return context.getArgument(name, PlayerSelectorArgumentResolver.class).resolve(context.getSource()).toArray(new Player[]{});
        }
    }
}
