package net.nova.brigadierextras.fabric.test;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.nova.brigadierextras.CommandSender;

public class FabricCommandSender implements CommandSender<CommandSourceStack> {
    private final CommandSourceStack commandSourceStack;

    public FabricCommandSender(CommandSourceStack commandSourceStack) {
        this.commandSourceStack = commandSourceStack;
    }

    public void sendMessage(Component component) {
        commandSourceStack.sendSystemMessage(component);
    }
}
