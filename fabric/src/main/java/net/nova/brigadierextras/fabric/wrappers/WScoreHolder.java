package net.nova.brigadierextras.fabric.wrappers;

import net.minecraft.commands.arguments.ScoreHolderArgument;

/**
 * Allows for both types of ScoreHolder argument
 */
public class WScoreHolder {
    public record Single(ScoreHolderArgument.Result value) {}

    public record Multiple(ScoreHolderArgument.Result value) {}
}
