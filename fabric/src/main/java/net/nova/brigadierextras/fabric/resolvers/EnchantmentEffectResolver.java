package net.nova.brigadierextras.fabric.resolvers;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.nova.brigadierextras.Resolver;
import net.nova.brigadierextras.fabric.FabricBrigadierExtras;
import net.nova.brigadierextras.fabric.wrappers.CustomStat;
import net.nova.brigadierextras.fabric.wrappers.EnchantmentEffect;

public class EnchantmentEffectResolver implements Resolver<EnchantmentEffect, CommandSourceStack> {
    @Override
    public Class<CommandSourceStack> getExpectedSenderClass() {
        return CommandSourceStack.class;
    }

    @Override
    public Class<EnchantmentEffect> getArgumentClass() {
        return EnchantmentEffect.class;
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
        return RequiredArgumentBuilder.argument(name, ResourceArgument.resource(FabricBrigadierExtras.context, BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE.key()));
    }

    @Override
    public EnchantmentEffect getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return new EnchantmentEffect(ResourceArgument.getResource(context, name, (ResourceKey<Registry<DataComponentType<?>>>) BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE.key()).value());
    }
}