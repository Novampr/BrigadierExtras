package net.nova.brigadierextras.paper;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.nova.brigadierextras.CommandBuilder;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("UnstableApiUsage")
public class PaperCommandUtils {
    /**
     * Register 1 or more commands with Paper's Brigadier
     * @param context The bootstrap registering this command
     * @param cmds The commands to register
     */
    public static void register(BootstrapContext context, Object... cmds) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();
            for (Object command : cmds) {
                CommandBuilder.registerCommand(commands.getDispatcher(), CommandSourceStack.class, command);
            }
        });
    }

    /**
     * Register 1 or more commands with Paper's Brigadier
     * @param plugin The plugin registering this command
     * @param cmds The commands to register
     */
    public static void register(Plugin plugin, Object... cmds) {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();
            for (Object command : cmds) {
                CommandBuilder.registerCommand(commands.getDispatcher(), CommandSourceStack.class, command);
            }
        });
    }
}
