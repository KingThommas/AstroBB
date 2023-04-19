package net.hectus.hectusblockbattles.match;

import net.hectus.hectusblockbattles.maps.GameMap;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public interface Match {
    boolean start();
    void stop(boolean isAbrupt);
    public Player getCurrentTurnPlayer();
    public void nextTurn();
    GameMap getGameMap();
    List<Player> getPlayers();
    boolean isRunning();
}
