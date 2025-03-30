package net.nova.brigadierextras.fabric.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.nova.brigadierextras.Resolver;
import net.nova.brigadierextras.fabric.FabricBrigadierExtras;
import net.nova.brigadierextras.fabric.wrappers.CustomStat;

public class CustomStatResolver implements Resolver<CustomStat, CommandSourceStack> {
    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<CustomStat> getArgumentClass() {
        return CustomStat.class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, ResourceArgument.resource(FabricBrigadierExtras.context, BuiltInRegistries.CUSTOM_STAT.key()));
    }

    @Override
    public CustomStat getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return new CustomStat(ResourceArgument.getResource(context, name, (ResourceKey<Registry<ResourceLocation>>) BuiltInRegistries.CUSTOM_STAT.key()).value());
    }
}
