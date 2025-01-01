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
import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.FinePosition;
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
import org.bukkit.GameMode;
import org.bukkit.HeightMap;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
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

        CommandBuilder.registerRootModifier(new RootModifier(1, new RootModifier.Handler() {
            @Override
            public <T> LiteralArgumentBuilder<T> modify(LiteralArgumentBuilder<T> argumentBuilder, Class<?> clazz) {
                if (clazz.isAnnotationPresent(Permission.class)) {
                    return argumentBuilder.requires(
                            sender -> ((CommandSourceStack) sender).getSender().hasPermission(clazz.getAnnotation(Permission.class).value())
                    );
                }

                if (clazz.isAnnotationPresent(OP.class)) {
                    return argumentBuilder.requires(
                            sender -> ((CommandSourceStack) sender).getSender().isOp()
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
                            sender -> ((CommandSourceStack) sender).getSender().hasPermission(method.getAnnotation(Permission.class).value())
                    );
                }

                if (method.isAnnotationPresent(OP.class)) {
                    return argumentBuilder.requires(
                            sender -> ((CommandSourceStack) sender).getSender().isOp()
                    );
                }

                return argumentBuilder;
            }
        }));

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
