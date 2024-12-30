package net.nova.brigadierextras.fabric.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.TeamArgument;
import net.minecraft.world.scores.PlayerTeam;
import net.nova.brigadierextras.Resolver;

public class TeamResolver implements Resolver<PlayerTeam, CommandSourceStack> {
    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<PlayerTeam> getArgumentClass() {
        return PlayerTeam.class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, TeamArgument.team());
    }

    @Override
    public PlayerTeam getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return TeamArgument.getTeam(context, name);
    }
}
