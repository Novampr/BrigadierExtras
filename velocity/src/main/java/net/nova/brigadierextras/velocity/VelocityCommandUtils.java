package net.nova.brigadierextras.velocity;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import net.nova.brigadierextras.CommandBuilder;

import java.util.function.Function;

public class VelocityCommandUtils {
    /**
     * Register 1 or more commands with Velocity's Brigadier
     * @param cmds The commands to register, contains metadata for velocity
     */
    public static void register(CommandAndData... cmds) {
        for (CommandAndData command : cmds) {
            for (LiteralArgumentBuilder<CommandSource> cmd : CommandBuilder.buildCommand(CommandSource.class, command.command())) {
                VelocityBrigadierExtras.getInstance().proxy.getCommandManager().register(command.meta(), new BrigadierCommand(cmd));
            }
        }
    }

    /**
     * Register 1 or more commands with Velocity's Brigadier
     * @param senderClass The custom sender
     * @param translate The method to create a custom sender from a {@link CommandSource}
     * @param cmds The commands to register, contains metadata for velocity
     */
    public static <T> void register(Class<T> senderClass, Function<CommandSource, T> translate, CommandAndData... cmds) {
        for (CommandAndData command : cmds) {
            for (LiteralArgumentBuilder<CommandSource> cmd : CommandBuilder.buildCommand(senderClass, CommandSource.class, translate, command.command())) {
                VelocityBrigadierExtras.getInstance().proxy.getCommandManager().register(command.meta(), new BrigadierCommand(cmd));
            }
        }
    }

    public record CommandAndData(CommandMeta meta, Object command) {}
}
