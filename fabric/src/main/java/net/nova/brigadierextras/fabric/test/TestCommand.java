package net.nova.brigadierextras.fabric.test;

import net.minecraft.network.chat.Component;
import net.nova.brigadierextras.Status;
import net.nova.brigadierextras.annotated.Command;
import net.nova.brigadierextras.annotated.Literal;
import net.nova.brigadierextras.annotated.Path;
import net.nova.brigadierextras.fabric.annotated.OP;

@Command("brigadierextras")
public class TestCommand {
    @Path
    public Status handle(FabricCommandSender fabricCommandSender) {
        fabricCommandSender.sendMessage(Component.literal("Working!"));
        return Status.SUCCESS;
    }

    @Path
    public Status handle(FabricCommandSender fabricCommandSender, Literal addition, Long long1, Integer integer2) {
        fabricCommandSender.sendMessage(Component.literal("Working!"));
        fabricCommandSender.sendMessage(Component.literal(long1 + " + " + integer2 + " = " + (long1 + integer2)));
        return Status.SUCCESS;
    }

    @Path
    public Status handle(FabricCommandSender fabricCommandSender, Literal subtract, Integer integer1, Integer integer2) {
        fabricCommandSender.sendMessage(Component.literal("Working!"));
        fabricCommandSender.sendMessage(Component.literal(integer1 + " - " + integer2 + " = " + (integer1 - integer2)));
        return Status.SUCCESS;
    }

    @OP
    @Path
    public Status handle(FabricCommandSender fabricCommandSender, Literal op, Integer integer) {
        fabricCommandSender.sendMessage(Component.literal("Working!"));
        fabricCommandSender.sendMessage(Component.literal(integer + " is the number."));
        return Status.SUCCESS;
    }
}
