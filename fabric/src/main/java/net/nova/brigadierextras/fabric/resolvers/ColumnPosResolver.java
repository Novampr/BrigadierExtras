package net.nova.brigadierextras.fabric.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import net.minecraft.server.level.ColumnPos;
import net.nova.brigadierextras.Resolver;

public class ColumnPosResolver implements Resolver<ColumnPos, CommandSourceStack> {
    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<ColumnPos> getArgumentClass() {
        return ColumnPos.class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, ColumnPosArgument.columnPos());
    }

    @Override
    public ColumnPos getType(CommandContext<CommandSourceStack> context, String name) {
        return ColumnPosArgument.getColumnPos(context, name);
    }
}
