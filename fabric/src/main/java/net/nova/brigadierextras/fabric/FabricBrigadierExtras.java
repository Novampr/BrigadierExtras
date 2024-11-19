package net.nova.brigadierextras.fabric;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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
import net.minecraft.world.inventory.SlotRange;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.nova.brigadierextras.BrigadierExtras;
import net.nova.brigadierextras.CommandBuilder;
import net.nova.brigadierextras.annotated.BranchModifier;
import net.nova.brigadierextras.annotated.RootModifier;
import net.nova.brigadierextras.fabric.annotated.OP;
import net.nova.brigadierextras.fabric.annotated.Permission;
import net.nova.brigadierextras.fabric.test.TestCommand;
import net.nova.brigadierextras.fabric.wrappers.*;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * <b><i>Minecraft</i></b>
 */
public class FabricBrigadierExtras implements ModInitializer {
    @Override
    public void onInitialize() {
        BrigadierExtras.init();

        CommandBuildContext context = Commands.createValidationContext(VanillaRegistries.createLookup());

        CommandBuilder.registerArgument(AngleArgument.SingleAngle.class, AngleArgument.angle());
        CommandBuilder.registerArgument(ChatFormatting.class, ColorArgument.color());
        CommandBuilder.registerArgument(Component.class, ComponentArgument.textComponent(context));
        CommandBuilder.registerArgument(CompoundTag.class, CompoundTagArgument.compoundTag());
        CommandBuilder.registerArgument(WDimension.class, DimensionArgument.dimension(), WDimension::new);
        CommandBuilder.registerArgument(EntityAnchorArgument.Anchor.class, EntityAnchorArgument.anchor());
        CommandBuilder.registerArgument(WSelector.Player.class, EntityArgument.player(), WSelector.Player::new);
        CommandBuilder.registerArgument(WSelector.Players.class, EntityArgument.players(), WSelector.Players::new);
        CommandBuilder.registerArgument(WSelector.Entity.class, EntityArgument.entity(), WSelector.Entity::new);
        CommandBuilder.registerArgument(WSelector.Entities.class, EntityArgument.entities(), WSelector.Entities::new);
        CommandBuilder.registerArgument(GameType.class, GameModeArgument.gameMode());
        CommandBuilder.registerArgument(GameProfileArgument.Result.class, GameProfileArgument.gameProfile());
        CommandBuilder.registerArgument(Heightmap.Types.class, HeightmapTypeArgument.heightmap());
        CommandBuilder.registerArgument(MessageArgument.Message.class, MessageArgument.message());
        CommandBuilder.registerArgument(NbtPathArgument.NbtPath.class, NbtPathArgument.nbtPath());
        CommandBuilder.registerArgument(Tag.class, NbtTagArgument.nbtTag());
        CommandBuilder.registerArgument(WObjective.class, ObjectiveArgument.objective(), WObjective::new);
        CommandBuilder.registerArgument(ObjectiveCriteria.class, ObjectiveCriteriaArgument.criteria());
        CommandBuilder.registerArgument(OperationArgument.Operation.class, OperationArgument.operation());
        CommandBuilder.registerArgument(ParticleOptions.class, ParticleArgument.particle(context));
        CommandBuilder.registerArgument(ResourceLocation.class, ResourceLocationArgument.id());
        CommandBuilder.registerArgument(DisplaySlot.class, ScoreboardSlotArgument.displaySlot());
        CommandBuilder.registerArgument(WScoreHolder.Single.class, ScoreHolderArgument.scoreHolder(), WScoreHolder.Single::new);
        CommandBuilder.registerArgument(WScoreHolder.Multiple.class, ScoreHolderArgument.scoreHolders(), WScoreHolder.Multiple::new);
        CommandBuilder.registerArgument(WSlot.class, SlotArgument.slot(), WSlot::new);
        CommandBuilder.registerArgument(SlotRange.class, SlotsArgument.slots());
        CommandBuilder.registerArgument(Style.class, StyleArgument.style(context));
        CommandBuilder.registerArgument(WTeam.class, TeamArgument.team(), WTeam::new);
        CommandBuilder.registerArgument(Mirror.class, TemplateMirrorArgument.templateMirror());
        CommandBuilder.registerArgument(Rotation.class, TemplateRotationArgument.templateRotation());
        CommandBuilder.registerArgument(UUID.class, UuidArgument.uuid());
        CommandBuilder.registerArgument(BlockPredicateArgument.Result.class, BlockPredicateArgument.blockPredicate(context));
        CommandBuilder.registerArgument(BlockInput.class, BlockStateArgument.block(context));
        CommandBuilder.registerArgument(WCoordinates.BlockPos.class, BlockPosArgument.blockPos(), WCoordinates.BlockPos::new);
        CommandBuilder.registerArgument(WCoordinates.ColumnPos.class, ColumnPosArgument.columnPos(), WCoordinates.ColumnPos::new);
        CommandBuilder.registerArgument(WCoordinates.Rotation.class, RotationArgument.rotation(), WCoordinates.Rotation::new);
        CommandBuilder.registerArgument(WCoordinates.Vec2.class, Vec2Argument.vec2(), WCoordinates.Vec2::new);
        CommandBuilder.registerArgument(WCoordinates.Vec3.class, Vec3Argument.vec3(), WCoordinates.Vec3::new);
        CommandBuilder.registerArgument(FunctionArgument.Result.class, FunctionArgument.functions());
        CommandBuilder.registerArgument(ItemInput.class, ItemArgument.item(context));
        CommandBuilder.registerArgument(ItemPredicateArgument.Result.class, ItemPredicateArgument.itemPredicate(context));

        CommandBuilder.registerRootModifier(new RootModifier(1, new RootModifier.Handler() {
            @Override
            public <T> LiteralArgumentBuilder<T> modify(LiteralArgumentBuilder<T> argumentBuilder, Class<?> clazz) {
                if (clazz.isAnnotationPresent(Permission.class)) {
                    return argumentBuilder.requires(
                            sender ->
                                    Permissions.check(
                                            (CommandSourceStack) sender,
                                            clazz.getAnnotation(Permission.class).value()
                                    )
                    );
                }

                if (clazz.isAnnotationPresent(OP.class)) {
                    return argumentBuilder.requires(
                            sender ->
                                    ((CommandSourceStack) sender).hasPermission(clazz.getAnnotation(OP.class).value())
                    );
                }

                return argumentBuilder;
            }
        }));

        CommandBuilder.registerBuilderModifier(new BranchModifier(1, new BranchModifier.Handler() {
            @Override
            public <T> ArgumentBuilder<T, ?> modify(ArgumentBuilder<T, ?> argumentBuilder, Method method, Class<?> clazz) {
                if (method.isAnnotationPresent(Permission.class)) {
                    return argumentBuilder.requires(
                            sender ->
                                    Permissions.check(
                                            (CommandSourceStack) sender,
                                            method.getAnnotation(Permission.class).value()
                                    )
                    );
                }

                if (method.isAnnotationPresent(OP.class)) {
                    return argumentBuilder.requires(
                            sender ->
                                    ((CommandSourceStack) sender).hasPermission(method.getAnnotation(OP.class).value())
                    );
                }

                return argumentBuilder;
            }
        }));

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
                CommandBuilder.registerCommand(commandDispatcher, CommandSourceStack.class, new TestCommand());
            });
        }
    }
}
