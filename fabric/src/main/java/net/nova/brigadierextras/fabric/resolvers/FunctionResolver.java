package net.nova.brigadierextras.fabric.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.functions.CommandFunction;
import net.nova.brigadierextras.Resolver;

public class FunctionResolver implements Resolver<CommandFunction[], CommandSourceStack> {
    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<CommandFunction[]> getArgumentClass() {
        return CommandFunction[].class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, FunctionArgument.functions());
    }

    @Override
    public CommandFunction<CommandSourceStack>[] getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return FunctionArgument.getFunctions(context, name).toArray(new CommandFunction[]{});
    }
}
