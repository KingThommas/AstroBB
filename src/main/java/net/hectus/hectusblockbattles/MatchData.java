package net.hectus.hectusblockbattles;

import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import net.hectus.hectusblockbattles.MatchVariables;

public class MatchData {

    private final Map<World, MatchVariables> worldVariables = new HashMap<>();

    public MatchVariables getVariables(World world) {
        MatchVariables variables = worldVariables.get(world);
        if (variables == null) {
            variables = new MatchVariables();
            worldVariables.put(world, variables);
        }
        return variables;
    }

    public void setVariables(MatchVariables MV, World world) {
        worldVariables.put(world, MV);
    }

    public void initializeWorldVariables(World world) {
        worldVariables.put(world, new MatchVariables());
    }
}
