package net.nova.brigadierextras.velocity.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.VelocityBrigadierMessage;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.nova.brigadierextras.Resolver;
import net.nova.brigadierextras.velocity.VelocityBrigadierExtras;
import net.nova.brigadierextras.velocity.VelocityCommandHelpers;

import java.util.Optional;

public class RegisteredServerResolver implements Resolver<RegisteredServer, CommandSource> {
    @Override
    public Class<CommandSource> getExpectedSenderClass() {
        return CommandSource.class;
    }

    @Override
    public Class<RegisteredServer> getArgumentClass() {
        return RegisteredServer.class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSource, ?> generateArgumentBuilder(String name) {
        return VelocityCommandHelpers.getArgument(name)
                .suggests((ctx, builder) -> {
                    for (RegisteredServer server : VelocityBrigadierExtras.getInstance().proxy.getAllServers()) {
                        if (server.getServerInfo().getName().startsWith(builder.getRemaining())) {
                            builder.suggest(server.getServerInfo().getName());
                        }
                    }

                    return builder.buildFuture();
                });
    }

    @Override
    public RegisteredServer getType(CommandContext<CommandSource> context, String name) throws CommandSyntaxException {
        String serverName = context.getArgument(name, String.class);

        Optional<RegisteredServer> optionalRegisteredServer = VelocityBrigadierExtras.getInstance().proxy.getServer(serverName);

        if (optionalRegisteredServer.isEmpty()) {
            throw new SimpleCommandExceptionType(VelocityBrigadierMessage.tooltip(Component.text("No server with that name found.").color(NamedTextColor.RED))).create();
        } else {
            return optionalRegisteredServer.get();
        }
    }
}
