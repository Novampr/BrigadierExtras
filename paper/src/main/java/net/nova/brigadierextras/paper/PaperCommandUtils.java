package net.nova.brigadierextras.paper;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.nova.brigadierextras.CommandBuilder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PaperCommandUtils {
    /**
     * Register a command with Paper's Brigadier, works best if done once
     * @param plugin The plugin registering this command
     * @param command The command to register
     * @see #register(Plugin, Object...)
     */
    public static void register(Plugin plugin, Object command) {
        LifecycleEventManager<@NotNull Plugin> manager = plugin.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();
            CommandBuilder.registerCommand(commands.getDispatcher(), CommandSourceStack.class, command);
        });
    }

    /**
     * Register multiple commands with Paper's Brigadier, registers multiple commands
     * @param plugin The plugin registering this command
     * @param cmds The commands to register
     * @see #register(Plugin, Object)
     */
    public static void register(Plugin plugin, Object... cmds) {
        LifecycleEventManager<@NotNull Plugin> manager = plugin.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();
            for (Object command : cmds) {
                CommandBuilder.registerCommand(commands.getDispatcher(), CommandSourceStack.class, command);
            }
        });
    }
}
