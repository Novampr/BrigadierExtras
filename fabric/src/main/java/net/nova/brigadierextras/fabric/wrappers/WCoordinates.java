package net.nova.brigadierextras.fabric.wrappers;

import net.minecraft.commands.arguments.coordinates.Coordinates;

public class WCoordinates {
    public record BlockPos(Coordinates value) {}

    public record ColumnPos(Coordinates value) {}

    public record Rotation(Coordinates value) {}

    public record Vec2(Coordinates value) {}

    public record Vec3(Coordinates value) {}
}
