package net.hectus.hectusblockbattles.playermode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerModeManager {

    private static final Map<UUID, PlayerMode> playerModeMap = new HashMap<>();

    public static PlayerMode getPlayerMode(@NotNull Player player) {
        UUID key = player.getUniqueId();
        return playerModeMap.putIfAbsent(key, PlayerMode.DEFAULT);
    }

    public static void setPlayerMode(PlayerMode mode, @NotNull Player player) {
        UUID key = player.getUniqueId();
        playerModeMap.put(key, mode);
    }

    public static void initializePlayerMode(Player player) {
        setPlayerMode(PlayerMode.DEFAULT, player);
    }
}
