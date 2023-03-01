package net.hectus.hectusblockbattles;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.hectus.hectusblockbattles.SpecialAbilities.PumpkinWall;

public final class HectusBlockBattles extends JavaPlugin implements Listener {
    MatchData MD = new MatchData();
    @Override
    public void onEnable() {
        System.out.println("Heheheha!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
