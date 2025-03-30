package net.nova.brigadierextras.velocity.senders;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.nova.brigadierextras.annotated.SenderConversion;
import net.nova.brigadierextras.annotated.SenderData;

public class ConsoleSender implements SenderConversion<CommandSource, ConsoleCommandSource> {
    @Override
    public Class<CommandSource> getSourceSender() {
        return CommandSource.class;
    }

    @Override
    public Class<ConsoleCommandSource> getResultSender() {
        return ConsoleCommandSource.class;
    }

    @Override
    public SenderData<ConsoleCommandSource> convert(CommandSource sender) {
        if (sender instanceof ConsoleCommandSource consoleCommandSource) {
            return SenderData.ofSender(consoleCommandSource);
        }

        sender.sendMessage(Component.text("This command can only be executed as the console.").color(NamedTextColor.RED));
        return SenderData.ofFailed(0);
    }
}
