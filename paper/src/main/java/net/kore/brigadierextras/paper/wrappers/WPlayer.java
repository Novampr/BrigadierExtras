package net.kore.brigadierextras.paper.wrappers;

import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;

public class WPlayer {
    public record Single(PlayerSelectorArgumentResolver playerSelectorArgumentResolver) {}

    public record Multiple(PlayerSelectorArgumentResolver playerSelectorArgumentResolver) {}
}
