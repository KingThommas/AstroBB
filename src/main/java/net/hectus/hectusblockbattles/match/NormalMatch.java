package net.hectus.hectusblockbattles.match;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.structures.v2.Algorithm;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class NormalMatch {
    public static Player p1;
    public static Player p2;
    public static Algorithm algorithm = new Algorithm();

    public static void start(Player p1, Player p2) {
        NormalMatch.p1 = p1;
        NormalMatch.p2 = p2;

        algorithm.start(p1);
    }

    public static boolean addBlock(Structure.BlockData blockData, Player player) {
        if (algorithm.isPlacer(player)) {
            algorithm.addBlock(blockData);
            return true;
        } else {
            player.sendMessage(Component.text(McColor.RED + "It isn't your turn right now!"));
            return false;
        }
    }
}
