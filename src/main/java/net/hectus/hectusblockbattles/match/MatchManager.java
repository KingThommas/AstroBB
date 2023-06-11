package net.hectus.hectusblockbattles.match;

import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class MatchManager {
    private static final Set<Match> matches = new HashSet<>();

    public static @Nullable Match getMatch(World world) {
        for (Match match : matches) {
            if (match.getGameMap().getWorld() == world) return match;
        }
        return null;
    }

    public static void addMatch(Match match) {
        matches.add(match);
    }
}
