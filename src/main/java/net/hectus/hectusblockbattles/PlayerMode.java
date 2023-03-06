package net.hectus.hectusblockbattles;

import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class PlayerMode {

    private final Map<Player, String> playerMode = new HashMap<>(); //default, blockbattles, practice, shopphase

    public String getPlayerMode(Player player) {
        String mode = playerMode.get(player);
        if (mode == null) {
            mode = "lobby";
            playerMode.put(player, "default");
        }
        return mode;
    }

    public void setPlayerMode(String s, Player player) {
        playerMode.put(player, s);
    }

    public void initializePlayerMode(Player player) {
        playerMode.put(player, "default");
    }
}
