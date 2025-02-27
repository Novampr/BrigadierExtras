package net.nova.brigadierextras.paper.senders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.nova.brigadierextras.annotated.SenderConversion;
import net.nova.brigadierextras.annotated.SenderData;
import org.bukkit.command.CommandSender;

public class CommandSenderSender implements SenderConversion<CommandSourceStack, CommandSender> {
    @Override
    public Class<CommandSourceStack> getSourceSender() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<CommandSender> getResultSender() {
        return CommandSender.class;
    }

    @Override
    public SenderData<CommandSender> convert(CommandSourceStack sender) {
        CommandSender commandSender = sender.getExecutor();

        if (commandSender == null) {
            commandSender = sender.getSender();
        }

        return SenderData.ofSender(commandSender);
    }
}
