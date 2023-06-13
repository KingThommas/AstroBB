package net.hectus.hectusblockbattles.match;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.InGameShop;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.structures.v2.Algorithm;
import org.bukkit.entity.Player;

public class NormalMatch {
    public static Player p1;
    public static Player p2;
    public static boolean shopPhase;
    public static Algorithm algorithm = new Algorithm();

    public static void start(Player p1, Player p2) {
        NormalMatch.p1 = p1;
        NormalMatch.p2 = p2;

        InGameShop.displayShop(p1, InGameShop.HotBar.NORMAL);
        InGameShop.displayShop(p2, InGameShop.HotBar.NORMAL);
    }

    public static Player getOpponent() {
        if (algorithm.isPlacer(p1)) return p2;
        else return p1;
    }

    public static void lose() {
        getOpponent().setHealth(-1.0);
        getOpponent().showTitle(BlockBattleEvents.subtitle(McColor.RED + "You lost!"));

        algorithm.placer.setHealth(20.0);
        algorithm.placer.showTitle(BlockBattleEvents.subtitle(McColor.GREEN + "You won!"));
    }

    public static void shopDone() {
        shopPhase = false;
        algorithm.start(p1);
    }
}
