package net.nova.brigadierextras.fabric.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.world.scores.ScoreHolder;
import net.nova.brigadierextras.Resolver;

public class ScoreHolderResolver implements Resolver<ScoreHolder, CommandSourceStack> {
    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<ScoreHolder> getArgumentClass() {
        return ScoreHolder.class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, ScoreHolderArgument.scoreHolder());
    }

    @Override
    public ScoreHolder getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return ScoreHolderArgument.getName(context, name);
    }

    public static class Multiple implements Resolver<ScoreHolder[], CommandSourceStack> {
        @Override
        public Class<CommandSourceStack> getExpectedSenderClass() {
            return CommandSourceStack.class;
        }

        @Override
        public Class<ScoreHolder[]> getArgumentClass() {
            return ScoreHolder[].class;
        }

        @Override
        public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
            return RequiredArgumentBuilder.argument(name, ScoreHolderArgument.scoreHolders());
        }

        @Override
        public ScoreHolder[] getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
            return ScoreHolderArgument.getNames(context, name).toArray(new ScoreHolder[]{});
        }
    }
}
