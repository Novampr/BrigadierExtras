package net.nova.brigadierextras.fabric.wrappers;

import net.minecraft.resources.ResourceLocation;

/**
 * Wrapper for a dimension argument
 * @param id The id in ResourceLocation form
 */
public record Dimension(ResourceLocation id) {
}
