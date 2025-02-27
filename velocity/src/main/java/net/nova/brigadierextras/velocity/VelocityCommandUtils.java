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
    public static void register(Object plugin, Object... cmds) {
        for (Object command : cmds) {
            for (LiteralArgumentBuilder<CommandSource> cmd : CommandBuilder.buildCommand(CommandSource.class, command)) {
                BrigadierCommand brigadierCommand = new BrigadierCommand(cmd);

                VelocityBrigadierExtras.getInstance().proxy.getCommandManager().register(
                        VelocityBrigadierExtras.getInstance().proxy.getCommandManager().metaBuilder(brigadierCommand).plugin(plugin).build(),
                        brigadierCommand
                );
            }
        }
    }
}
