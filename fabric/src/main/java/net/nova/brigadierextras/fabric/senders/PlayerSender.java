package net.nova.brigadierextras.fabric.senders;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.nova.brigadierextras.annotated.SenderConversion;
import net.nova.brigadierextras.annotated.SenderData;

public class PlayerSender implements SenderConversion<CommandSourceStack, ServerPlayer> {
    @Override
    public Class<CommandSourceStack> getSourceSender() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<ServerPlayer> getResultSender() {
        return ServerPlayer.class;
    }

    @Override
    public SenderData<ServerPlayer> convert(CommandSourceStack sender) {
        return SenderData.ofSender(sender.getPlayer(), () -> {
            sender.sendSystemMessage(Component.literal("This command can only be executed as a player.").withStyle(ChatFormatting.RED));
            return SenderData.ofFailed(0);
        });
    }
}