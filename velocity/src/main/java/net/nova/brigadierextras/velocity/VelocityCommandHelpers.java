package net.nova.brigadierextras.velocity;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.velocitypowered.api.command.CommandSource;

public class VelocityCommandHelpers {
    public static RequiredArgumentBuilder<CommandSource, ?> getArgument(String name) {
        return RequiredArgumentBuilder.argument(name, StringArgumentType.word());
    }
}
