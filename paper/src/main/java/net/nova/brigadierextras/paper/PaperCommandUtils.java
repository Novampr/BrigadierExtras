package net.nova.brigadierextras.paper;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.nova.brigadierextras.CommandBuilder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class PaperCommandUtils {
    /**
     * Register 1 or more commands with Paper's Brigadier
     * @param plugin The plugin registering this command
     * @param cmds The commands to register
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

    /**
     * Register 1 or more commands with Paper's Brigadier
     * @param plugin The plugin registering this command
     * @param senderClass The custom sender
     * @param translate The method to create a custom sender from a {@link CommandSourceStack}
     * @param cmds The commands to register
     */
    public static <T> void register(Plugin plugin, Class<T> senderClass, Function<CommandSourceStack, T> translate, Object... cmds) {
        LifecycleEventManager<@NotNull Plugin> manager = plugin.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();
            for (Object command : cmds) {
                CommandBuilder.registerCommand(commands.getDispatcher(), senderClass, CommandSourceStack.class, translate, command);
            }
        });
    }
}
