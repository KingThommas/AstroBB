package net.hectus.hectusblockbattles.Match;

import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MatchManager {

    private static final Map<UUID, Match> worldMatchMap = new HashMap<>();
    public static Match getMatch(World world) {
        UUID key = world.getUID();

        return worldMatchMap.get(key);
    }

    public static void addMatch(Match match, World world) {
        UUID key = world.getUID();
        worldMatchMap.put(key, match);
    }
}
