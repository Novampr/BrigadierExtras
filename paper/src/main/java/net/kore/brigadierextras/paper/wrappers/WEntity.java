package net.kore.brigadierextras.paper.wrappers;

import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;

public class WEntity {
    public record Single(EntitySelectorArgumentResolver entitySelectorArgumentResolver) {}

    public record Multiple(EntitySelectorArgumentResolver entitySelectorArgumentResolver) {}
}
