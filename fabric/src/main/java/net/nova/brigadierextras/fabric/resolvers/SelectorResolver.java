package net.nova.brigadierextras.fabric.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.nova.brigadierextras.Resolver;

public class SelectorResolver {
    public static class Player implements Resolver<ServerPlayer, CommandSourceStack> {
        @Override
        public Class<CommandSourceStack> getExpectedSenderClass() {
            return CommandSourceStack.class;
        }

        @Override
        public Class<ServerPlayer> getArgumentClass() {
            return ServerPlayer.class;
        }

        @Override
        public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
            return RequiredArgumentBuilder.argument(name, EntityArgument.player());
        }

        @Override
        public ServerPlayer getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
            return EntityArgument.getPlayer(context, name);
        }
    }

    public static class Players implements Resolver<ServerPlayer[], CommandSourceStack> {
        @Override
        public Class<CommandSourceStack> getExpectedSenderClass() {
            return CommandSourceStack.class;
        }

        @Override
        public Class<ServerPlayer[]> getArgumentClass() {
            return ServerPlayer[].class;
        }

        @Override
        public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
            return RequiredArgumentBuilder.argument(name, EntityArgument.players());
        }

        @Override
        public ServerPlayer[] getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
            return EntityArgument.getPlayers(context, name).toArray(new ServerPlayer[]{});
        }
    }

    public static class Entity implements Resolver<net.minecraft.world.entity.Entity, CommandSourceStack> {
        @Override
        public Class<CommandSourceStack> getExpectedSenderClass() {
            return CommandSourceStack.class;
        }

        @Override
        public Class<net.minecraft.world.entity.Entity> getArgumentClass() {
            return net.minecraft.world.entity.Entity.class;
        }

        @Override
        public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
            return RequiredArgumentBuilder.argument(name, EntityArgument.entity());
        }

        @Override
        public net.minecraft.world.entity.Entity getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
            return EntityArgument.getEntity(context, name);
        }
    }

    public static class Entities implements Resolver<net.minecraft.world.entity.Entity[], CommandSourceStack> {
        @Override
        public Class<CommandSourceStack> getExpectedSenderClass() {
            return CommandSourceStack.class;
        }

        @Override
        public Class<net.minecraft.world.entity.Entity[]> getArgumentClass() {
            return net.minecraft.world.entity.Entity[].class;
        }

        @Override
        public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
            return RequiredArgumentBuilder.argument(name, EntityArgument.entities());
        }

        @Override
        public net.minecraft.world.entity.Entity[] getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
            return EntityArgument.getEntities(context, name).toArray(new net.minecraft.world.entity.Entity[]{});
        }
    }
}
