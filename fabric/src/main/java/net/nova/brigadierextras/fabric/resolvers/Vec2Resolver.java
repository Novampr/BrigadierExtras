package net.nova.brigadierextras.fabric.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.world.phys.Vec2;
import net.nova.brigadierextras.Resolver;

public class Vec2Resolver implements Resolver<Vec2, CommandSourceStack> {
    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<Vec2> getArgumentClass() {
        return Vec2.class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, Vec2Argument.vec2());
    }

    @Override
    public Vec2 getType(CommandContext<CommandSourceStack> context, String name) {
        return Vec2Argument.getVec2(context, name);
    }
}
