package net.nova.brigadierextras.paper.senders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.nova.brigadierextras.Status;
import net.nova.brigadierextras.annotated.SenderConversion;
import net.nova.brigadierextras.annotated.SenderData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerSender implements SenderConversion<CommandSourceStack, Player> {
    @Override
    public Class<CommandSourceStack> getSourceSender() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<Player> getResultSender() {
        return Player.class;
    }

    @Override
    public SenderData<Player> convert(CommandSourceStack sender) {
        CommandSender commandSender = sender.getExecutor();

        if (commandSender == null) {
            commandSender = sender.getSender();
        }

        if (commandSender instanceof Player player) {
            return SenderData.ofSender(player);
        } else {
            commandSender.sendMessage(Component.text("This command can only be executed as a player.").color(NamedTextColor.RED));
            return SenderData.ofFailed(Status.FAILURE);
        }
    }
}
