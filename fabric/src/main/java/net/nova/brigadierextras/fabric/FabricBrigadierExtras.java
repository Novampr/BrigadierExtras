package net.nova.brigadierextras.fabric;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.*;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.*;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.inventory.SlotRange;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.nova.brigadierextras.BrigadierExtras;
import net.nova.brigadierextras.CommandBuilder;
import net.nova.brigadierextras.annotated.AnnotationModifier;
import net.nova.brigadierextras.annotated.BranchModifier;
import net.nova.brigadierextras.annotated.Command;
import net.nova.brigadierextras.annotated.RootModifier;
import net.nova.brigadierextras.fabric.annotated.OP;
import net.nova.brigadierextras.fabric.annotated.Permission;
import net.nova.brigadierextras.fabric.test.FabricCommandSender;
import net.nova.brigadierextras.fabric.test.TestCommand;
import net.nova.brigadierextras.fabric.resolvers.*;
import net.nova.brigadierextras.fabric.wrappers.Dimension;
import net.nova.brigadierextras.fabric.wrappers.Rotation;
import net.nova.brigadierextras.fabric.wrappers.Slot;
import net.nova.brigadierextras.fabric.wrappers.Time;

import java.util.UUID;

/**
 * <b><i>Minecraft</i></b>
 */
public class FabricBrigadierExtras implements ModInitializer {
    private MinecraftServer minecraftServer;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> minecraftServer = server);

        BrigadierExtras.init();

        CommandBuildContext context = Commands.createValidationContext(VanillaRegistries.createLookup());

        CommandBuilder.registerArgument(AngleArgument.SingleAngle.class, AngleArgument.angle());
        CommandBuilder.registerArgument(ChatFormatting.class, ColorArgument.color());
        CommandBuilder.registerArgument(Component.class, ComponentArgument.textComponent(context));
        CommandBuilder.registerArgument(CompoundTag.class, CompoundTagArgument.compoundTag());
        CommandBuilder.registerArgument(Dimension.class, DimensionArgument.dimension(), Dimension::new);
        CommandBuilder.registerArgument(EntityAnchorArgument.Anchor.class, EntityAnchorArgument.anchor());
        CommandBuilder.registerArgument(GameType.class, GameModeArgument.gameMode());
        CommandBuilder.registerArgument(Heightmap.Types.class, HeightmapTypeArgument.heightmap());
        CommandBuilder.registerArgument(MessageArgument.Message.class, MessageArgument.message());
        CommandBuilder.registerArgument(NbtPathArgument.NbtPath.class, NbtPathArgument.nbtPath());
        CommandBuilder.registerArgument(Tag.class, NbtTagArgument.nbtTag());
        CommandBuilder.registerArgument(ObjectiveCriteria.class, ObjectiveCriteriaArgument.criteria());
        CommandBuilder.registerArgument(OperationArgument.Operation.class, OperationArgument.operation());
        CommandBuilder.registerArgument(ParticleOptions.class, ParticleArgument.particle(context));
        CommandBuilder.registerArgument(ResourceLocation.class, ResourceLocationArgument.id());
        CommandBuilder.registerArgument(DisplaySlot.class, ScoreboardSlotArgument.displaySlot());
        CommandBuilder.registerArgument(Slot.class, SlotArgument.slot(), Slot::new);
        CommandBuilder.registerArgument(SlotRange.class, SlotsArgument.slots());
        CommandBuilder.registerArgument(Style.class, StyleArgument.style(context));
        CommandBuilder.registerArgument(Mirror.class, TemplateMirrorArgument.templateMirror());
        CommandBuilder.registerArgument(net.minecraft.world.level.block.Rotation.class, TemplateRotationArgument.templateRotation());
        CommandBuilder.registerArgument(UUID.class, UuidArgument.uuid());
        CommandBuilder.registerArgument(BlockPredicateArgument.Result.class, BlockPredicateArgument.blockPredicate(context));
        CommandBuilder.registerArgument(BlockInput.class, BlockStateArgument.block(context));
        CommandBuilder.registerArgument(Rotation.class, RotationArgument.rotation(), Rotation::new);
        CommandBuilder.registerArgument(ItemInput.class, ItemArgument.item(context));
        CommandBuilder.registerArgument(ItemPredicateArgument.Result.class, ItemPredicateArgument.itemPredicate(context));
        CommandBuilder.registerArgument(Time.class, TimeArgument.time(), Time::new);

        CommandBuilder.registerResolver(new BlockPosResolver());
        CommandBuilder.registerResolver(new ColumnPosResolver());
        CommandBuilder.registerResolver(new Vec2Resolver());
        CommandBuilder.registerResolver(new Vec3Resolver());
        CommandBuilder.registerResolver(new TeamResolver());
        CommandBuilder.registerResolver(new ObjectiveResolver());
        CommandBuilder.registerResolver(new ScoreHolderResolver());
        CommandBuilder.registerResolver(new ScoreHolderResolver.Multiple());
        CommandBuilder.registerResolver(new SelectorResolver.Player());
        CommandBuilder.registerResolver(new SelectorResolver.Players());
        CommandBuilder.registerResolver(new SelectorResolver.Entity());
        CommandBuilder.registerResolver(new SelectorResolver.Entities());
        CommandBuilder.registerResolver(new GameProfileResolver());
        CommandBuilder.registerResolver(new FunctionResolver());

        CommandBuilder.registerAnnotationModifier(
                new AnnotationModifier<>(
                        0,
                        OP.class,
                        (argumentBuilder, op) -> {
                            int opValue;

                            try {
                                if (minecraftServer instanceof DedicatedServer dedicatedServer) {
                                    opValue = dedicatedServer.getProperties().opPermissionLevel;
                                } else {
                                    opValue = 4;
                                }
                            } catch (Exception e) { // Honestly not sure what happens on the Integrated Server if I referance Dedi, lets not find out
                                opValue = 4;
                            }

                            int finalOpValue = opValue;
                            return argumentBuilder.requires(
                                    sender -> ((CommandSourceStack) sender).hasPermission(op.value() < 0 ? finalOpValue : op.value())
                            );
                        }
                )
        );

        CommandBuilder.registerAnnotationModifier(
                new AnnotationModifier<>(
                        1,
                        Permission.class,
                        (argumentBuilder, permission) ->
                                argumentBuilder.requires(
                                        sender ->
                                                Permissions.check(
                                                        (CommandSourceStack) sender,
                                                        permission.value()
                                                )
                                )
                )
        );

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
                CommandBuilder.registerCommand(commandDispatcher, FabricCommandSender.class, CommandSourceStack.class, FabricCommandSender::new, new TestCommand());
            });
        }
    }
}
