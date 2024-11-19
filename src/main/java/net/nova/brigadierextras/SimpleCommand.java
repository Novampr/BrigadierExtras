package net.nova.brigadierextras;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

public interface SimpleCommand<S> {
    String getName();

    int noArgumentExecute(CommandContext<S> context);

    int executeCommand(CommandContext<S> context, String input);

    default CompletableFuture<Suggestions> getSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return Suggestions.empty();
    }

    default LiteralArgumentBuilder<S> build() {
        return getLiteral(getName())
                .executes(this::noArgumentExecute)
                .then(
                        getArgument("input", StringArgumentType.greedyString())
                                .executes((ctx) -> executeCommand(ctx, StringArgumentType.getString(ctx, "input")))
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
