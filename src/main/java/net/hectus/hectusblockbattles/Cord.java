package net.hectus.hectusblockbattles;

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
        return new Location(HBB.WORLD, x, y, z);
    }
}
