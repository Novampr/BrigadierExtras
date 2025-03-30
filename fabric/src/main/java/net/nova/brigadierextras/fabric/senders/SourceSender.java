package net.nova.brigadierextras.fabric.senders;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.nova.brigadierextras.annotated.SenderConversion;
import net.nova.brigadierextras.annotated.SenderData;
import net.nova.brigadierextras.fabric.mixin.CommandSourceStackAccessor;

public class SourceSender<SoruceClass extends CommandSource> implements SenderConversion<CommandSourceStack, SoruceClass> {
    private final Class<SoruceClass> soruceClass;
    private final String message;

    public SourceSender(Class<SoruceClass> soruceClass, String message) {
        this.soruceClass = soruceClass;
        this.message = message;
    }

    @Override
    public Class<CommandSourceStack> getSourceSender() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<SoruceClass> getResultSender() {
        return soruceClass;
    }

    @Override
    public SenderData<SoruceClass> convert(CommandSourceStack sender) {
        CommandSource source = ((CommandSourceStackAccessor) sender).getSource();
        if (source.getClass().isAssignableFrom(soruceClass)) {
            return SenderData.ofSender(((SoruceClass) source));
        }

        sender.sendSystemMessage(Component.literal(message).withStyle(ChatFormatting.RED));
        return SenderData.ofFailed(0);
    }
}
