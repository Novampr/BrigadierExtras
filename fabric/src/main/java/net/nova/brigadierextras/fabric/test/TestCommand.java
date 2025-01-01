package net.nova.brigadierextras.fabric.test;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.nova.brigadierextras.Status;
import net.nova.brigadierextras.annotated.Command;
import net.nova.brigadierextras.annotated.Literal;
import net.nova.brigadierextras.annotated.Path;
import net.nova.brigadierextras.fabric.annotated.OP;

@Command("brigadierextras")
public class TestCommand {
    @Path
    public Status handle(FabricCommandSender fabricCommandSender) {
        fabricCommandSender.sendMessage(Component.literal("Working!"));
        return Status.SUCCESS;
    }

    @Path
    public Status handle(FabricCommandSender fabricCommandSender, Literal math, Float number1, Type type, Float number2) {
        fabricCommandSender.sendMessage(Component.literal(number1 + " " + type.getSign() + " " + number2 + " = " + type.getTask().apply(number1, number2)));
        return Status.SUCCESS;
    }

    @OP
    @Path
    public Status handle(FabricCommandSender fabricCommandSender, Literal op, Integer integer) {
        fabricCommandSender.sendMessage(Component.literal("Working!"));
        fabricCommandSender.sendMessage(Component.literal(integer + " is the number."));
        return Status.SUCCESS;
    }

    @Path
    public Status handle(FabricCommandSender fabricCommandSender, ServerPlayer player, Component component) {
        player.sendSystemMessage(component);
        fabricCommandSender.sendMessage(Component.literal("Sent player message."));
        return Status.SUCCESS;
    }

    private final AttributeModifier attributeModifier = new AttributeModifier(ResourceLocation.parse("be:freeze"), -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    @Path
    public Status handle(FabricCommandSender fabricCommandSender, Literal freeze, Entity[] entities) {
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                AttributeInstance speedAttribute = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
                AttributeInstance jumpAttribute = livingEntity.getAttribute(Attributes.JUMP_STRENGTH);

                if (speedAttribute.hasModifier(ResourceLocation.parse("be:freeze"))) {
                    speedAttribute.removeModifier(ResourceLocation.parse("be:freeze"));
                    jumpAttribute.removeModifier(ResourceLocation.parse("be:freeze"));
                } else {
                    speedAttribute.addPermanentModifier(attributeModifier);
                    jumpAttribute.addPermanentModifier(attributeModifier);
                }
            }
        }

        return Status.SUCCESS;
    }
}
