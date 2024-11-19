package net.nova.brigadierextras.paper.test;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.nova.brigadierextras.Status;
import net.nova.brigadierextras.paper.annotated.OP;
import net.kyori.adventure.text.Component;
import net.nova.brigadierextras.annotated.Command;
import net.nova.brigadierextras.annotated.Literal;
import net.nova.brigadierextras.annotated.Path;

@Command("brigadierextras")
public class TestCommand {
    @Path
    public Status handle(PaperCommandSender commandSender) {
        commandSender.sendMessage(Component.text("Working!"));
        return Status.SUCCESS;
    }

    @Path
    public Status handle(PaperCommandSender commandSender, Literal addition, Long long1, Integer integer2) {
        commandSender.sendMessage(Component.text("Working!"));
        commandSender.sendMessage(Component.text(long1 + " + " + integer2 + " = " + (long1 + integer2)));
        return Status.SUCCESS;
    }

    @Path
    public Status handle(PaperCommandSender commandSender, Literal subtract, Integer integer1, Integer integer2) {
        commandSender.sendMessage(Component.text("Working!"));
        commandSender.sendMessage(Component.text(integer1 + " - " + integer2 + " = " + (integer1 - integer2)));
        return Status.SUCCESS;
    }

    @OP
    @Path
    public Status handle(PaperCommandSender commandSender, Literal op, Integer integer) {
        commandSender.sendMessage(Component.text("Working!"));
        commandSender.sendMessage(Component.text(integer + " is the number."));
        return Status.SUCCESS;
    }
}
