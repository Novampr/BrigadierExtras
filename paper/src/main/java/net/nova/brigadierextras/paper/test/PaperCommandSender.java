package net.nova.brigadierextras.paper.test;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.nova.brigadierextras.CommandSender;

public class PaperCommandSender implements CommandSender<CommandSourceStack> {
    private final CommandSourceStack commandSourceStack;

    public PaperCommandSender(CommandSourceStack commandSourceStack) {
        this.commandSourceStack = commandSourceStack;
    }

    public void sendMessage(Component component) {
        commandSourceStack.getSender().sendMessage(component);
    }
}
