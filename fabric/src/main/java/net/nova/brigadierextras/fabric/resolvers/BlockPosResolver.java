package net.nova.brigadierextras.fabric.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.nova.brigadierextras.Resolver;

public class BlockPosResolver implements Resolver<BlockPos, CommandSourceStack> {
    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<BlockPos> getArgumentClass() {
        return BlockPos.class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, BlockPosArgument.blockPos());
    }

    @Override
    public BlockPos getType(CommandContext<CommandSourceStack> context, String name) {
        return BlockPosArgument.getBlockPos(context, name);
    }
}
