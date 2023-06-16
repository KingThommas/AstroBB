package net.hectus.hectusblockbattles.match;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.BBPlayer;
import net.hectus.hectusblockbattles.InGameShop;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.structures.v2.Algorithm;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class NormalMatch {
    public static BBPlayer p1;
    public static BBPlayer p2;
    public static boolean shopPhase;
    public static Algorithm algorithm = new Algorithm();

    public static void start(Player p1, Player p2) {
        NormalMatch.p1 = new BBPlayer(p1);
        NormalMatch.p2 = new BBPlayer(p2);

        InGameShop.displayShop(p1, InGameShop.HotBar.NORMAL);
        InGameShop.displayShop(p2, InGameShop.HotBar.NORMAL);
    }

    public static BBPlayer getOpponent() {
        if (algorithm.isPlacer(p1.player)) return p2;
        else return p1;
    }

    public static void win() {
        getOpponent().player.setHealth(0);
        getOpponent().showTitle("", McColor.RED + "You lost!", null);

        algorithm.placer.setHealth(20.0);
        algorithm.placer.showTitle(BlockBattleEvents.subtitle(McColor.GREEN + "You won!"));
    }

    public static void lose() {
        algorithm.placer.setHealth(0);
        algorithm.placer.showTitle(BlockBattleEvents.subtitle(McColor.RED + "You lost!"));

        getOpponent().player.setHealth(20.0);
        getOpponent().showTitle("", McColor.GREEN + "You won!", null);
    }

    public static void shopDone() {
        shopPhase = false;
        algorithm.start(p1.player);
    }

    public enum PlayerState {
        NONE,
        SHOP_NORMAL,
        SHOP_NORMAL_FINISHED,
        SHOP_OVERTIME,
        SHOP_OVERTIME_FINISHED,
        GAME
    }
}
