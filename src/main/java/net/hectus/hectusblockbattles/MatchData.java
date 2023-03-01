package net.hectus.hectusblockbattles;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class MatchData extends JavaPlugin {

    private final Map<World, MatchVariables> worldVariables = new HashMap<>();

    public MatchVariables getVariables(World world) {
        MatchVariables variables = worldVariables.get(world);
        if (variables == null) {
            variables = new MatchVariables();
            worldVariables.put(world, variables);
        }
        return variables;
    }

    public void initializeWorldVariables(World world) {
        worldVariables.put(world, new MatchVariables());
    }
}
