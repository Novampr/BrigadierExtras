package net.nova.brigadierextras.velocity.senders;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.nova.brigadierextras.annotated.SenderConversion;
import net.nova.brigadierextras.annotated.SenderData;

public class PlayerSender implements SenderConversion<CommandSource, Player> {
    @Override
    public Class<CommandSource> getSourceSender() {
        return CommandSource.class;
    }

    @Override
    public Class<Player> getResultSender() {
        return Player.class;
    }

    @Override
    public SenderData<Player> convert(CommandSource sender) {
        if (sender instanceof Player player) {
            return SenderData.ofSender(player);
        }

        sender.sendMessage(Component.text("This command can only be executed as a player.").color(NamedTextColor.RED));
        return SenderData.ofFailed(0);
    }
}
