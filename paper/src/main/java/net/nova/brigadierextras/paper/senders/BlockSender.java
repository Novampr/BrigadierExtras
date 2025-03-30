package net.nova.brigadierextras.paper.senders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.nova.brigadierextras.annotated.SenderConversion;
import net.nova.brigadierextras.annotated.SenderData;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;

public class BlockSender implements SenderConversion<CommandSourceStack, BlockCommandSender> {
    @Override
    public Class<CommandSourceStack> getSourceSender() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<BlockCommandSender> getResultSender() {
        return BlockCommandSender.class;
    }

    @Override
    public SenderData<BlockCommandSender> convert(CommandSourceStack sender) {
        CommandSender commandSender = sender.getExecutor();

        if (commandSender == null) {
            commandSender = sender.getSender();
        }

        if (commandSender instanceof BlockCommandSender blockCommandSender) {
            return SenderData.ofSender(blockCommandSender);
        }

        commandSender.sendMessage(Component.text("This command can only be executed as a block.").color(NamedTextColor.RED));
        return SenderData.ofFailed();
    }
}
