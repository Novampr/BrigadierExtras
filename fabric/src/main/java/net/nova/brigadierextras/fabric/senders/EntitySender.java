package net.nova.brigadierextras.fabric.senders;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.nova.brigadierextras.annotated.SenderConversion;
import net.nova.brigadierextras.annotated.SenderData;

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
        return SenderData.ofSender(sender.getEntity(), () -> {
            sender.sendSystemMessage(Component.literal("This command can only be executed as an entity.").withStyle(ChatFormatting.RED));
            return SenderData.ofFailed(0);
        });
    }
}
