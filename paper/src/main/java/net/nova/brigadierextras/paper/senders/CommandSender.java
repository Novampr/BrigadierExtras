package net.nova.brigadierextras.paper.senders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.nova.brigadierextras.annotated.SenderConversion;
import net.nova.brigadierextras.annotated.SenderData;

public class CommandSender implements SenderConversion<CommandSourceStack, org.bukkit.command.CommandSender> {
    @Override
    public Class<CommandSourceStack> getSourceSender() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<org.bukkit.command.CommandSender> getResultSender() {
        return org.bukkit.command.CommandSender.class;
    }

    @Override
    public SenderData<org.bukkit.command.CommandSender> convert(CommandSourceStack sender) {
        org.bukkit.command.CommandSender commandSender = sender.getExecutor();

        if (commandSender == null) {
            commandSender = sender.getSender();
        }

        return SenderData.ofSender(commandSender);
    }
}
