package net.nova.brigadierextras.paper;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.SignedMessageResolver;
import io.papermc.paper.command.brigadier.argument.predicate.ItemStackPredicate;
import io.papermc.paper.command.brigadier.argument.range.DoubleRangeProvider;
import io.papermc.paper.command.brigadier.argument.range.IntegerRangeProvider;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.chat.SignedMessage;
import net.nova.brigadierextras.Resolver;
import net.nova.brigadierextras.annotated.AnnotationModifier;
import net.nova.brigadierextras.paper.annotated.OP;
import net.nova.brigadierextras.paper.annotated.Permission;
import net.nova.brigadierextras.paper.resolvers.EntityResolver;
import net.nova.brigadierextras.paper.resolvers.PlayerResolver;
import net.nova.brigadierextras.paper.resolvers.ResolverResolver;
import net.nova.brigadierextras.paper.test.PaperCommandSender;
import net.nova.brigadierextras.paper.test.TestCommand;
import net.nova.brigadierextras.paper.wrappers.Time;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.nova.brigadierextras.BrigadierExtras;
import net.nova.brigadierextras.CommandBuilder;
import net.nova.brigadierextras.annotated.BranchModifier;
import net.nova.brigadierextras.annotated.RootModifier;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockState;
import org.bukkit.block.BlockType;
import org.bukkit.block.banner.PatternType;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.map.MapCursor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public final class PaperBrigadierExtras extends JavaPlugin {
    @Override
    public void onEnable() {
        BrigadierExtras.init();

        CommandBuilder.registerArgument(BlockState.class, ArgumentTypes.blockState());
        CommandBuilder.registerArgument(ItemStack.class, ArgumentTypes.itemStack());
        CommandBuilder.registerArgument(ItemStackPredicate.class, ArgumentTypes.itemPredicate());
        CommandBuilder.registerArgument(NamedTextColor.class, ArgumentTypes.namedColor());
        CommandBuilder.registerArgument(Component.class, ArgumentTypes.component());
        CommandBuilder.registerArgument(Style.class, ArgumentTypes.style());
        CommandBuilder.registerArgument(DisplaySlot.class, ArgumentTypes.scoreboardDisplaySlot());
        CommandBuilder.registerArgument(NamespacedKey.class, ArgumentTypes.namespacedKey());
        CommandBuilder.registerArgument(Key.class, ArgumentTypes.key());
        CommandBuilder.registerArgument(IntegerRangeProvider.class, ArgumentTypes.integerRange());
        CommandBuilder.registerArgument(DoubleRangeProvider.class, ArgumentTypes.doubleRange());
        CommandBuilder.registerArgument(World.class, ArgumentTypes.world());
        CommandBuilder.registerArgument(GameMode.class, ArgumentTypes.gameMode());
        CommandBuilder.registerArgument(HeightMap.class, ArgumentTypes.heightMap());
        CommandBuilder.registerArgument(UUID.class, ArgumentTypes.uuid());
        CommandBuilder.registerArgument(Criteria.class, ArgumentTypes.objectiveCriteria());
        CommandBuilder.registerArgument(LookAnchor.class, ArgumentTypes.entityAnchor());
        CommandBuilder.registerArgument(Time.class, ArgumentTypes.time(), Time::new);
        CommandBuilder.registerArgument(Mirror.class, ArgumentTypes.templateMirror());
        CommandBuilder.registerArgument(StructureRotation.class, ArgumentTypes.templateRotation());

        CommandBuilder.registerArgument(GameEvent.class, ArgumentTypes.resource(RegistryKey.GAME_EVENT));
        CommandBuilder.registerArgument(StructureType.class, ArgumentTypes.resource(RegistryKey.STRUCTURE_TYPE));
        CommandBuilder.registerArgument(PotionEffectType.class, ArgumentTypes.resource(RegistryKey.MOB_EFFECT));
        CommandBuilder.registerArgument(BlockType.class, ArgumentTypes.resource(RegistryKey.BLOCK));
        CommandBuilder.registerArgument(ItemType.class, ArgumentTypes.resource(RegistryKey.ITEM));
        CommandBuilder.registerArgument(Cat.Type.class, ArgumentTypes.resource(RegistryKey.CAT_VARIANT));
        CommandBuilder.registerArgument(Frog.Variant.class, ArgumentTypes.resource(RegistryKey.FROG_VARIANT));
        CommandBuilder.registerArgument(Villager.Profession.class, ArgumentTypes.resource(RegistryKey.VILLAGER_PROFESSION));
        CommandBuilder.registerArgument(Villager.Type.class, ArgumentTypes.resource(RegistryKey.VILLAGER_TYPE));
        CommandBuilder.registerArgument(MapCursor.Type.class, ArgumentTypes.resource(RegistryKey.MAP_DECORATION_TYPE));
        CommandBuilder.registerArgument(MenuType.class, ArgumentTypes.resource(RegistryKey.MENU));
        CommandBuilder.registerArgument(Attribute.class, ArgumentTypes.resource(RegistryKey.ATTRIBUTE));
        CommandBuilder.registerArgument(Fluid.class, ArgumentTypes.resource(RegistryKey.FLUID));
        CommandBuilder.registerArgument(Sound.class, ArgumentTypes.resource(RegistryKey.SOUND_EVENT));
        CommandBuilder.registerArgument(DataComponentType.class, ArgumentTypes.resource(RegistryKey.DATA_COMPONENT_TYPE));
        CommandBuilder.registerArgument(Biome.class, ArgumentTypes.resource(RegistryKey.BIOME));
        CommandBuilder.registerArgument(Structure.class, ArgumentTypes.resource(RegistryKey.STRUCTURE));
        CommandBuilder.registerArgument(TrimMaterial.class, ArgumentTypes.resource(RegistryKey.TRIM_MATERIAL));
        CommandBuilder.registerArgument(TrimPattern.class, ArgumentTypes.resource(RegistryKey.TRIM_PATTERN));
        CommandBuilder.registerArgument(DamageType.class, ArgumentTypes.resource(RegistryKey.DAMAGE_TYPE));
        CommandBuilder.registerArgument(Wolf.Variant.class, ArgumentTypes.resource(RegistryKey.WOLF_VARIANT));
        CommandBuilder.registerArgument(Enchantment.class, ArgumentTypes.resource(RegistryKey.ENCHANTMENT));
        CommandBuilder.registerArgument(JukeboxSong.class, ArgumentTypes.resource(RegistryKey.JUKEBOX_SONG));
        CommandBuilder.registerArgument(PatternType.class, ArgumentTypes.resource(RegistryKey.BANNER_PATTERN));
        CommandBuilder.registerArgument(Art.class, ArgumentTypes.resource(RegistryKey.PAINTING_VARIANT));
        CommandBuilder.registerArgument(MusicInstrument.class, ArgumentTypes.resource(RegistryKey.INSTRUMENT));
        CommandBuilder.registerArgument(EntityType.class, ArgumentTypes.resource(RegistryKey.ENTITY_TYPE));
        CommandBuilder.registerArgument(Particle.class, ArgumentTypes.resource(RegistryKey.PARTICLE_TYPE));
        CommandBuilder.registerArgument(PotionType.class, ArgumentTypes.resource(RegistryKey.POTION));

        CommandBuilder.registerResolver(new ResolverResolver<>(
                ArgumentTypes.playerProfiles(),
                PlayerProfileListResolver.class,
                PlayerProfile[].class,
                collection -> collection.toArray(new PlayerProfile[]{})
        ));

        CommandBuilder.registerResolver(new ResolverResolver<>(
                ArgumentTypes.blockPosition(),
                BlockPositionResolver.class,
                BlockPosition.class
        ));

        CommandBuilder.registerResolver(new ResolverResolver<>(
                ArgumentTypes.finePosition(),
                FinePositionResolver.class,
                FinePosition.class
        ));

        CommandBuilder.registerResolver(new Resolver<SignedMessage, CommandSourceStack>() {
            @Override
            public Class<CommandSourceStack> getExpectedSenderClass() {
                return CommandSourceStack.class;
            }

            @Override
            public Class<SignedMessage> getArgumentClass() {
                return SignedMessage.class;
            }

            @Override
            public RequiredArgumentBuilder<CommandSourceStack, ?> generateArgumentBuilder(String name) {
                return RequiredArgumentBuilder.argument(name, ArgumentTypes.signedMessage());
            }

            @Override
            public SignedMessage getType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
                return context.getArgument(name, SignedMessageResolver.class).resolveSignedMessage(name, context).join();
            }
        });

        CommandBuilder.registerResolver(new PlayerResolver());
        CommandBuilder.registerResolver(new PlayerResolver.Multiple());
        CommandBuilder.registerResolver(new EntityResolver());
        CommandBuilder.registerResolver(new EntityResolver.Multiple());

        CommandBuilder.registerAnnotationModifier(
                new AnnotationModifier<>(
                        0,
                        OP.class,
                        (argumentBuilder, permission) ->
                                argumentBuilder.requires(
                                        sender -> ((CommandSourceStack) sender).getSender().isOp()
                                )
                )
        );

        CommandBuilder.registerAnnotationModifier(
                new AnnotationModifier<>(
                        1,
                        Permission.class,
                        (argumentBuilder, permission) ->
                                argumentBuilder.requires(
                                        sender -> ((CommandSourceStack) sender).getSender().hasPermission(permission.value())
                                )
                )
        );

        if (System.getProperty("be.test", "nope").equals("TESTMEPLEASE")) {
            PaperCommandUtils.register(this, PaperCommandSender.class, PaperCommandSender::new, new TestCommand());
        }
    }

    @Override
    public void onDisable() {

    }
}
