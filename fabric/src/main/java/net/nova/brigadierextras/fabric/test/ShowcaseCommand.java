package net.nova.brigadierextras.fabric.test;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.nova.brigadierextras.Status;
import net.nova.brigadierextras.annotated.Command;
import net.nova.brigadierextras.annotated.Literal;
import net.nova.brigadierextras.annotated.Path;

@Command("mycommand")
public class ShowcaseCommand {
    @Path
    public Status handle(CommandSourceStack sender) {
        sender.sendSystemMessage(Component.literal("Hello there!"));

        return Status.SUCCESS;
    }

    @Path
    public Status handle(CommandSourceStack sender, Literal epicParameter) {
        sender.sendSystemMessage(Component.literal("You triggered the ultimate command :O"));

        return Status.SUCCESS;
    }
}
