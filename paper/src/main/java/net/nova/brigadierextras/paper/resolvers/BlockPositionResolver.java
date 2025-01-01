package net.nova.brigadierextras.paper.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.math.BlockPosition;
import net.nova.brigadierextras.Resolver;

public class BlockPositionResolver implements Resolver<BlockPosition, CommandSourceStack> {
    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<BlockPosition> getArgumentClass() {
        return BlockPosition.class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, ArgumentTypes.blockPosition());
    }

    @Override
    public BlockPosition getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return null;
    }
}
