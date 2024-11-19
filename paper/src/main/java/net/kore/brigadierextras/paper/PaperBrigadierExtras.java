package net.kore.brigadierextras.paper;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
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
import net.kore.brigadierextras.paper.annotated.OP;
import net.kore.brigadierextras.paper.annotated.Permission;
import net.kore.brigadierextras.paper.wrappers.WEntity;
import net.kore.brigadierextras.paper.wrappers.WPlayer;
import net.kore.brigadierextras.paper.wrappers.WTime;
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
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public final class PaperBrigadierExtras extends JavaPlugin {
    @Override
    public void onEnable() {
        BrigadierExtras.init();

        CommandBuilder.registerArgument(WEntity.Single.class, ArgumentTypes.entity(), WEntity.Single::new);
        CommandBuilder.registerArgument(WEntity.Multiple.class, ArgumentTypes.entities(), WEntity.Multiple::new);
        CommandBuilder.registerArgument(WPlayer.Single.class, ArgumentTypes.player(), WPlayer.Single::new);
        CommandBuilder.registerArgument(WPlayer.Multiple.class, ArgumentTypes.players(), WPlayer.Multiple::new);
        CommandBuilder.registerArgument(PlayerProfileListResolver.class, ArgumentTypes.playerProfiles());
        CommandBuilder.registerArgument(BlockPositionResolver.class, ArgumentTypes.blockPosition());
        CommandBuilder.registerArgument(FinePositionResolver.class, ArgumentTypes.finePosition());
        CommandBuilder.registerArgument(BlockState.class, ArgumentTypes.blockState());
        CommandBuilder.registerArgument(ItemStack.class, ArgumentTypes.itemStack());
        CommandBuilder.registerArgument(ItemStackPredicate.class, ArgumentTypes.itemPredicate());
        CommandBuilder.registerArgument(NamedTextColor.class, ArgumentTypes.namedColor());
        CommandBuilder.registerArgument(Component.class, ArgumentTypes.component());
        CommandBuilder.registerArgument(Style.class, ArgumentTypes.style());
        CommandBuilder.registerArgument(SignedMessageResolver.class, ArgumentTypes.signedMessage());
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
        CommandBuilder.registerArgument(WTime.class, ArgumentTypes.time(), WTime::new);
        CommandBuilder.registerArgument(Mirror.class, ArgumentTypes.templateMirror());
        CommandBuilder.registerArgument(StructureRotation.class, ArgumentTypes.templateRotation());

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
