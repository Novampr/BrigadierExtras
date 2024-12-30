package net.nova.brigadierextras.fabric.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.world.scores.Objective;
import net.nova.brigadierextras.Resolver;

public class ObjectiveResolver implements Resolver<Objective, CommandSourceStack> {
    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<Objective> getArgumentClass() {
        return Objective.class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, ObjectiveArgument.objective());
    }

    @Override
    public Objective getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return ObjectiveArgument.getObjective(context, name);
    }
}
