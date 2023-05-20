package net.hectus.hectusblockbattles.match;

import net.hectus.hectusblockbattles.maps.GameMap;
import org.bukkit.entity.Player;

import java.util.List;

public interface Match {
    boolean start();
    void stop(boolean isAbrupt);
    Player getCurrentTurnPlayer();
    void nextTurn(boolean wasSkipped);
    GameMap getGameMap();
    List<Player> getPlayers();
    boolean isRunning();
    double getGameScore();
    void setLuck(Player player, int amount);
    void addLuck(Player player, int amount);
    void removeLuck(Player player, int amount);

    int getLuck(Player player);

    boolean luckCheck(Player player, double chance);
}
