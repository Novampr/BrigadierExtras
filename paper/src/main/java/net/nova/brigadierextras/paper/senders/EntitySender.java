package net.nova.brigadierextras.paper.senders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.nova.brigadierextras.Status;
import net.nova.brigadierextras.annotated.SenderConversion;
import net.nova.brigadierextras.annotated.SenderData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class EntitySender implements SenderConversion<CommandSourceStack, Entity> {
    @Override
    public Class<CommandSourceStack> getSourceSender() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<Entity> getResultSender() {
        return Entity.class;
    }

    @Override
    public SenderData<Entity> convert(CommandSourceStack sender) {
        CommandSender commandSender = sender.getExecutor();

        if (commandSender == null) {
            commandSender = sender.getSender();
        }

        if (commandSender instanceof Entity entity) {
            return SenderData.ofSender(entity);
        } else {
            commandSender.sendMessage(Component.text("This command can only be executed as an entity.").color(NamedTextColor.RED));
            return SenderData.ofFailed(Status.FAILURE);
        }
    }
}