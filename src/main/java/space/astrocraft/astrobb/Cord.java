package space.astrocraft.astrobb;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Cord(int x, int y, int z) {
    @Contract("_ -> new")
    public static @NotNull Cord of(@NotNull Location loc) {
        return new Cord((int) loc.x(), (int) loc.y(), (int) loc.z());
    }

    @Contract(value = " -> new", pure = true)
    public @NotNull Location toLocation() {
        return new Location(AstroBB.WORLD, x, y, z);
    }

    @Contract(pure = true)
    @Override
    public String toString() {
        return String.format("X: %d, Y: %d, Z: %d", x, y, z);
    }
}
