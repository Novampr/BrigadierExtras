package net.nova.brigadierextras.fabric;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.*;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.*;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.numbers.NumberFormatType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.rcon.RconConsoleSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.util.valueproviders.FloatProviderType;
import net.minecraft.util.valueproviders.IntProviderType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SlotRange;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.nova.brigadierextras.BrigadierExtras;
import net.nova.brigadierextras.CommandBuilder;
import net.nova.brigadierextras.annotated.*;
import net.nova.brigadierextras.fabric.annotated.OP;
import net.nova.brigadierextras.fabric.annotated.Permission;
import net.nova.brigadierextras.fabric.senders.*;
import net.nova.brigadierextras.fabric.test.FabricCommandSender;
import net.nova.brigadierextras.fabric.test.ShowcaseCommand;
import net.nova.brigadierextras.fabric.test.TestCommand;
import net.nova.brigadierextras.fabric.resolvers.*;
import net.nova.brigadierextras.fabric.wrappers.*;

import java.util.UUID;

public class FabricBrigadierExtras implements ModInitializer {
    public static CommandBuildContext context;

    private MinecraftServer minecraftServer;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> minecraftServer = server);

        BrigadierExtras.init();

        FabricBrigadierExtras.context = Commands.createValidationContext(VanillaRegistries.createLookup());

        CommandBuilder.registerArgument(AngleArgument.SingleAngle.class, AngleArgument.angle());
        CommandBuilder.registerArgument(ChatFormatting.class, ColorArgument.color());
        CommandBuilder.registerArgument(Component.class, ComponentArgument.textComponent(context));
        CommandBuilder.registerArgument(CompoundTag.class, CompoundTagArgument.compoundTag());
        CommandBuilder.registerArgument(Dimension.class, ResourceLocation.class, DimensionArgument.dimension(), Dimension::new);
        CommandBuilder.registerArgument(EntityAnchorArgument.Anchor.class, EntityAnchorArgument.anchor());
        CommandBuilder.registerArgument(GameType.class, GameModeArgument.gameMode());
        CommandBuilder.registerArgument(Heightmap.Types.class, HeightmapTypeArgument.heightmap());
        CommandBuilder.registerArgument(MessageArgument.Message.class, MessageArgument.message());
        CommandBuilder.registerArgument(NbtPathArgument.NbtPath.class, NbtPathArgument.nbtPath());
        CommandBuilder.registerArgument(Tag.class, NbtTagArgument.nbtTag());
        CommandBuilder.registerArgument(ObjectiveCriteria.class, ObjectiveCriteriaArgument.criteria());
        CommandBuilder.registerArgument(Objective.class, String.class, ObjectiveArgument.objective(), Objective::new);
        CommandBuilder.registerArgument(OperationArgument.Operation.class, OperationArgument.operation());
        CommandBuilder.registerArgument(ParticleOptions.class, ParticleArgument.particle(context));

        // yay, more registries but in Fabric now!!!
        CommandBuilder.registerResolver(new ResourceResolver<>(GameEvent.class, BuiltInRegistries.GAME_EVENT.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(SoundEvent.class, BuiltInRegistries.SOUND_EVENT.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(Fluid.class, BuiltInRegistries.FLUID.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(MobEffect.class, BuiltInRegistries.MOB_EFFECT.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(Block.class, BuiltInRegistries.BLOCK.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(EntityType.class, BuiltInRegistries.ENTITY_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(Item.class, BuiltInRegistries.ITEM.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(Potion.class, BuiltInRegistries.POTION.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(ParticleType.class, BuiltInRegistries.PARTICLE_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(BlockEntityType.class, BuiltInRegistries.BLOCK_ENTITY_TYPE.key()));
        CommandBuilder.registerResolver(new CustomStatResolver());
        CommandBuilder.registerResolver(new ResourceResolver<>(ChunkStatus.class, BuiltInRegistries.CHUNK_STATUS.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(RuleTestType.class, BuiltInRegistries.RULE_TEST.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(RuleBlockEntityModifierType.class, BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(PosRuleTestType.class, BuiltInRegistries.POS_RULE_TEST.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(MenuType.class, BuiltInRegistries.MENU.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(RecipeType.class, BuiltInRegistries.RECIPE_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(RecipeSerializer.class, BuiltInRegistries.RECIPE_SERIALIZER.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(Attribute.class, BuiltInRegistries.ATTRIBUTE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(PositionSourceType.class, BuiltInRegistries.POSITION_SOURCE_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(ArgumentTypeInfo.class, BuiltInRegistries.COMMAND_ARGUMENT_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(StatType.class, BuiltInRegistries.STAT_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(VillagerType.class, BuiltInRegistries.VILLAGER_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(VillagerProfession.class, BuiltInRegistries.VILLAGER_PROFESSION.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(PoiType.class, BuiltInRegistries.POINT_OF_INTEREST_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(MemoryModuleType.class, BuiltInRegistries.MEMORY_MODULE_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(SensorType.class, BuiltInRegistries.SENSOR_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(Schedule.class, BuiltInRegistries.SCHEDULE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(Activity.class, BuiltInRegistries.ACTIVITY.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(LootPoolEntryType.class, BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(LootItemFunctionType.class, BuiltInRegistries.LOOT_FUNCTION_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(LootItemConditionType.class, BuiltInRegistries.LOOT_CONDITION_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(LootNumberProviderType.class, BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(LootNbtProviderType.class, BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(LootScoreProviderType.class, BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(FloatProviderType.class, BuiltInRegistries.FLOAT_PROVIDER_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(IntProviderType.class, BuiltInRegistries.INT_PROVIDER_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(HeightProviderType.class, BuiltInRegistries.HEIGHT_PROVIDER_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(BlockPredicateType.class, BuiltInRegistries.BLOCK_PREDICATE_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(WorldCarver.class, BuiltInRegistries.CARVER.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(Feature.class, BuiltInRegistries.FEATURE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(StructurePlacementType.class, BuiltInRegistries.STRUCTURE_PLACEMENT.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(StructurePieceType.class, BuiltInRegistries.STRUCTURE_PIECE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(StructureType.class, BuiltInRegistries.STRUCTURE_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(PlacementModifierType.class, BuiltInRegistries.PLACEMENT_MODIFIER_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(BlockStateProviderType.class, BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(FoliagePlacerType.class, BuiltInRegistries.FOLIAGE_PLACER_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(TrunkPlacerType.class, BuiltInRegistries.TRUNK_PLACER_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(RootPlacerType.class, BuiltInRegistries.ROOT_PLACER_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(TreeDecoratorType.class, BuiltInRegistries.TREE_DECORATOR_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(FeatureSizeType.class, BuiltInRegistries.FEATURE_SIZE_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(StructureProcessorType.class, BuiltInRegistries.STRUCTURE_PROCESSOR.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(StructurePoolElementType.class, BuiltInRegistries.STRUCTURE_POOL_ELEMENT.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(DecoratedPotPattern.class, BuiltInRegistries.DECORATED_POT_PATTERN.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(CreativeModeTab.class, BuiltInRegistries.CREATIVE_MODE_TAB.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(CriterionTrigger.class, BuiltInRegistries.TRIGGER_TYPES.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(NumberFormatType.class, BuiltInRegistries.NUMBER_FORMAT_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(DataComponentType.class, BuiltInRegistries.DATA_COMPONENT_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(DataComponentPredicate.Type.class, BuiltInRegistries.DATA_COMPONENT_PREDICATE_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(MapDecorationType.class, BuiltInRegistries.MAP_DECORATION_TYPE.key()));
        CommandBuilder.registerResolver(new EnchantmentEffectResolver());
        CommandBuilder.registerResolver(new ResourceResolver<>(ConsumeEffect.Type.class, BuiltInRegistries.CONSUME_EFFECT_TYPE.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(RecipeDisplay.Type.class, BuiltInRegistries.RECIPE_DISPLAY.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(SlotDisplay.Type.class, BuiltInRegistries.SLOT_DISPLAY.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(RecipeBookCategory.class, BuiltInRegistries.RECIPE_BOOK_CATEGORY.key()));
        CommandBuilder.registerResolver(new ResourceResolver<>(TicketType.class, BuiltInRegistries.TICKET_TYPE.key()));

        CommandBuilder.registerArgument(ResourceLocation.class, ResourceLocationArgument.id());
        CommandBuilder.registerArgument(DisplaySlot.class, ScoreboardSlotArgument.displaySlot());
        CommandBuilder.registerArgument(Slot.class, Integer.class, SlotArgument.slot(), Slot::new);
        CommandBuilder.registerArgument(SlotRange.class, SlotsArgument.slots());
        CommandBuilder.registerArgument(Style.class, StyleArgument.style(context));
        CommandBuilder.registerArgument(Mirror.class, TemplateMirrorArgument.templateMirror());
        CommandBuilder.registerArgument(net.minecraft.world.level.block.Rotation.class, TemplateRotationArgument.templateRotation());
        CommandBuilder.registerArgument(UUID.class, UuidArgument.uuid());
        CommandBuilder.registerArgument(BlockPredicateArgument.Result.class, BlockPredicateArgument.blockPredicate(context));
        CommandBuilder.registerArgument(BlockInput.class, BlockStateArgument.block(context));
        CommandBuilder.registerArgument(Rotation.class, Coordinates.class, RotationArgument.rotation(), Rotation::new);
        CommandBuilder.registerArgument(ItemInput.class, ItemArgument.item(context));
        CommandBuilder.registerArgument(ItemPredicateArgument.Result.class, ItemPredicateArgument.itemPredicate(context));
        CommandBuilder.registerArgument(Time.class, Integer.class, TimeArgument.time(), Time::new);

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
                        (argumentBuilder, op) ->
                                argumentBuilder.requires(
                                        sender -> {
                                            int opValue;

                                            if (minecraftServer != null && minecraftServer.isDedicatedServer()) {
                                                opValue = ((DedicatedServer) minecraftServer).getProperties().opPermissionLevel;
                                            } else {
                                                opValue = 4;
                                            }

                                            return ((CommandSourceStack) sender).hasPermission(op.value() < 0 ? opValue : op.value());
                                        }
                                )
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

        CommandBuilder.registerSenderConversion(new EntitySender());
        CommandBuilder.registerSenderConversion(new PlayerSender());

        CommandBuilder.registerSenderConversion(new SourceSender<>(MinecraftServer.class, "This command can only be executed as the console."));
        CommandBuilder.registerSenderConversion(new SourceSender<>(BaseCommandBlock.class, "This command can only be executed as a block."));
        CommandBuilder.registerSenderConversion(new SourceSender<>(RconConsoleSource.class, "This command can only be executed as an RCON sender."));

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
                CommandBuilder.registerSenderConversion(new SenderConversion<CommandSourceStack, FabricCommandSender>() {
                    @Override
                    public Class<CommandSourceStack> getSourceSender() {
                        return CommandSourceStack.class;
                    }

                    @Override
                    public Class<FabricCommandSender> getResultSender() {
                        return FabricCommandSender.class;
                    }

                    @Override
                    public SenderData<FabricCommandSender> convert(CommandSourceStack sender) {
                        return SenderData.ofSender(new FabricCommandSender(sender));
                    }
                });

                CommandBuilder.registerCommand(commandDispatcher, CommandSourceStack.class, new TestCommand());
                CommandBuilder.registerCommand(commandDispatcher, CommandSourceStack.class, new ShowcaseCommand());
            });
        }
    }
}
