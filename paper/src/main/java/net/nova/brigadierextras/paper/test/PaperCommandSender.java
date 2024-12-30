package net.nova.brigadierextras.paper.test;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;

public class PaperCommandSender {
    private final CommandSourceStack commandSourceStack;

    public PaperCommandSender(CommandSourceStack commandSourceStack) {
        this.commandSourceStack = commandSourceStack;
    }

    public void sendMessage(Component component) {
        commandSourceStack.getSender().sendMessage(component);
    }
}
