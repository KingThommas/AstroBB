package net.hectus.hectusblockbattles.match;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;

public interface Match {
    boolean start();
    void stop(boolean isAbrupt);
    World getWorld();
    Set<Player> getPlayers();
    boolean isRunning();
}
