package net.nova.brigadierextras.velocity.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.VelocityBrigadierMessage;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.nova.brigadierextras.Resolver;
import net.nova.brigadierextras.velocity.VelocityBrigadierExtras;
import net.nova.brigadierextras.velocity.VelocityCommandHelpers;

import java.util.Optional;

public class PlayerResolver implements Resolver<Player, CommandSource> {
    @Override
    public Class<CommandSource> getExpectedSenderClass() {
        return CommandSource.class;
    }

    @Override
    public Class<Player> getArgumentClass() {
        return Player.class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSource, ?> generateArgumentBuilder(String name) {
        return VelocityCommandHelpers.getArgument(name)
                .suggests((ctx, builder) -> {
                    for (Player player : VelocityBrigadierExtras.getInstance().proxy.getAllPlayers()) {
                        if (player.getUsername().startsWith(builder.getRemaining())) {
                            builder.suggest(player.getUsername());
                        }
                    }

                    return builder.buildFuture();
                });
    }

    @Override
    public Player getType(CommandContext<CommandSource> context, String name) throws CommandSyntaxException {
        String playerName = context.getArgument(name, String.class);

        Optional<Player> optionalPlayer = VelocityBrigadierExtras.getInstance().proxy.getPlayer(playerName);

        if (optionalPlayer.isEmpty()) {
            throw new SimpleCommandExceptionType(VelocityBrigadierMessage.tooltip(Component.text("No player with that name found.").color(NamedTextColor.RED))).create();
        } else {
            return optionalPlayer.get();
        }
    }
}
