package net.hectus.hectusblockbattles.warps;

import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.util.Randomizer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WarpManager {
    public static boolean warp(Warp warp, Player activator, Player otherPlayer) {
        if (Randomizer.boolByChance(warp.chance * 100)) {
            Structure.Cord cord = warp.middle;
            activator.teleport(new Location(activator.getWorld(), cord.x() - 2, cord.y(), cord.z()));
            otherPlayer.teleport(new Location(activator.getWorld(), cord.x() + 2, cord.y(), cord.z()));
            return true;
        } else {
            return false;
        }
    }
}
