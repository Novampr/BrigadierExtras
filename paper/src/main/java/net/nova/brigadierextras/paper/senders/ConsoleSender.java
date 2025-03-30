package net.nova.brigadierextras.paper.senders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.nova.brigadierextras.Status;
import net.nova.brigadierextras.annotated.SenderConversion;
import net.nova.brigadierextras.annotated.SenderData;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleSender implements SenderConversion<CommandSourceStack, ConsoleCommandSender> {
    @Override
    public Class<CommandSourceStack> getSourceSender() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<ConsoleCommandSender> getResultSender() {
        return ConsoleCommandSender.class;
    }

    @Override
    public SenderData<ConsoleCommandSender> convert(CommandSourceStack sender) {
        CommandSender commandSender = sender.getExecutor();

        if (commandSender == null) {
            commandSender = sender.getSender();
        }

        if (commandSender instanceof ConsoleCommandSender consoleCommandSender) {
            return SenderData.ofSender(consoleCommandSender);
        } else {
            commandSender.sendMessage(Component.text("This command can only be executed as the console.").color(NamedTextColor.RED));
            return SenderData.ofFailed(Status.FAILURE);
        }
    }
}
