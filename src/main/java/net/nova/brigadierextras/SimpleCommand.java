package net.nova.brigadierextras;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface SimpleCommand<S> {
    String getName();

    /**
     * Run when the command is executed
     *
     * @param context The context of the command
     * @param input The input of the command, if empty there was no input
     * @return The status code of the command
     * @see Status
     */
    Status executeCommand(@NotNull CommandContext<S> context, @NotNull String input);

    default CompletableFuture<Suggestions> getSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return Suggestions.empty();
    }

    default LiteralArgumentBuilder<S> build() {
        return getLiteral(getName())
                .executes(ctx -> executeCommand(ctx, "").getNum())
                .then(
                        getArgument("input", StringArgumentType.greedyString())
                                .executes((ctx) -> executeCommand(ctx, StringArgumentType.getString(ctx, "input")).getNum())
                                .suggests(this::getSuggestions)
                );
    }

    private LiteralArgumentBuilder<S> getLiteral(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    private <T> RequiredArgumentBuilder<S, T> getArgument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
}
