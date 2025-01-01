package net.nova.brigadierextras.fabric.resolvers;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.nova.brigadierextras.Resolver;

public class GameProfileResolver implements Resolver<GameProfile[], CommandSourceStack> {
    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<GameProfile[]> getArgumentClass() {
        return GameProfile[].class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, GameProfileArgument.gameProfile());
    }

    @Override
    public GameProfile[] getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return GameProfileArgument.getGameProfiles(context, name).toArray(new GameProfile[]{});
    }
}
