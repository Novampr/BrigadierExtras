package net.nova.brigadierextras.fabric.wrappers;

import net.minecraft.commands.arguments.selector.EntitySelector;

/**
 * Entity selectors, can have player or entity and choose if there's more than one
 */
public class WSelector {
    public record Player(EntitySelector value) {}

    public record Players(EntitySelector value) {}

    public record Entity(EntitySelector value) {}

    public record Entities(EntitySelector value) {}
}
