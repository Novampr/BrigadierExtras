package net.nova.brigadierextras.fabric.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.world.phys.Vec3;
import net.nova.brigadierextras.Resolver;

public class Vec3Resolver implements Resolver<Vec3, CommandSourceStack> {
    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<Vec3> getArgumentClass() {
        return Vec3.class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, Vec3Argument.vec3());
    }

    @Override
    public Vec3 getType(CommandContext<CommandSourceStack> context, String name) {
        return Vec3Argument.getVec3(context, name);
    }
}
