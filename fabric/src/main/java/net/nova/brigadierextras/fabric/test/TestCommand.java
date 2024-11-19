package net.nova.brigadierextras.fabric.test;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.nova.brigadierextras.annotated.Command;
import net.nova.brigadierextras.annotated.Literal;
import net.nova.brigadierextras.annotated.Path;
import net.nova.brigadierextras.fabric.annotated.OP;

@Command("brigadierextras")
public class TestCommand {
    @Path
    public int handle(CommandSourceStack commandSourceStack) {
        commandSourceStack.sendSystemMessage(Component.literal("Working!"));
        return Command.SUCCESS;
    }

    @Path
    public int handle(CommandSourceStack commandSourceStack, Literal addition, Long long1, Integer integer2) {
        commandSourceStack.sendSystemMessage(Component.literal("Working!"));
        commandSourceStack.sendSystemMessage(Component.literal(long1 + " + " + integer2 + " = " + (long1 + integer2)));
        return Command.SUCCESS;
    }

    @Path
    public int handle(CommandSourceStack commandSourceStack, Literal subtract, Integer integer1, Integer integer2) {
        commandSourceStack.sendSystemMessage(Component.literal("Working!"));
        commandSourceStack.sendSystemMessage(Component.literal(integer1 + " - " + integer2 + " = " + (integer1 - integer2)));
        return Command.SUCCESS;
    }

    @OP
    @Path
    public int handle(CommandSourceStack commandSourceStack, Literal op, Integer integer) {
        commandSourceStack.sendSystemMessage(Component.literal("Working!"));
        commandSourceStack.sendSystemMessage(Component.literal(integer + " is the number."));
        return Command.SUCCESS;
    }
}
