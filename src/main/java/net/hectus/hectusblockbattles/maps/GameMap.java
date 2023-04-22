package net.hectus.hectusblockbattles.maps;

import net.hectus.hectusblockbattles.warps.Warp;
import org.bukkit.World;

public interface GameMap {
    boolean load();
    void unload();
    boolean restoreFromSource();

    boolean isLoaded();
    World getWorld();
    World getSourceWorld();
    boolean isNight();
    void setNight(boolean isNight);
    Warp currentWarp();
    boolean setWarp(Warp warp);
}
