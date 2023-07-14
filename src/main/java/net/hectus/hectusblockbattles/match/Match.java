package net.hectus.hectusblockbattles.match;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.*;
import net.hectus.hectusblockbattles.player.BBPlayer;
import net.hectus.hectusblockbattles.structures.v2.Algorithm;
import net.hectus.hectusblockbattles.turn.Turn;
import net.hectus.hectusblockbattles.turn.TurnInfo;
import net.hectus.hectusblockbattles.warps.Warp;
import net.hectus.hectusblockbattles.warps.WarpSettings;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

public class Match {
    public static boolean hasStarted, shopPhase, isNight, rain, nextWarp100P, azureWasUsed, netherPortalAwaitIgnite, blazeDebuff;
    public static boolean nextCanMove = true;
    public static BBPlayer p1, p2;
    public static Warp currentWarp;
    public static HashSet<WarpSettings.Class> allowed = new HashSet<>(), disallowed = new HashSet<>();
    public static List<TurnInfo> turnHistory = new ArrayList<>();
    public static Algorithm algorithm = new Algorithm();
    public static int swapTimer = -3;

    public static void start(Player p1, Player p2) {
        hasStarted = true;

        Collections.addAll(allowed, Warp.DEFAULT.allow);
        Collections.addAll(disallowed, Warp.DEFAULT.deny);

        isNight = false;
        rain = false;
        HBB.WORLD.setTime(6000);
        HBB.WORLD.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

        Match.p1 = new BBPlayer(p1);
        Match.p2 = new BBPlayer(p2);

        currentWarp = Warp.DEFAULT;

        InGameShop.displayShop(p1, InGameShop.HotBar.NORMAL);
        InGameShop.displayShop(p2, InGameShop.HotBar.NORMAL);
        shopPhase = true;
    }

    public static BBPlayer getOpponent() {
        if (algorithm.isPlacer(p1.player)) {
            return p2;
        } else {
            return p1;
        }
    }

    public static BBPlayer getPlacer() {
        if (algorithm.isPlacer(p1.player)) {
            return p1;
        } else {
            return p2;
        }
    }

    @Contract(pure = true)
    public static BBPlayer getOpposite(@NotNull BBPlayer player) {
        if (p1.player.getUniqueId().toString().equalsIgnoreCase(player.player.getUniqueId().toString())) {
            return p2;
        } else {
            return p1;
        }
    }

    @Contract(pure = true)
    public static BBPlayer getPlayer(@NotNull Player player) {
        if (p1.player.getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            return p1;
        } else {
            return p2;
        }
    }

    public static void win() {
        if(getOpponent().hasRevive()){
            getOpponent().useRevive();
        }else{
            getOpponent().player.setHealth(0);
            getOpponent().showTitle("", McColor.RED + Translation.get("match.lose", getOpponent().locale()), null);

            getPlacer().player.setHealth(20.0);
            getPlacer().showTitle("", McColor.GREEN + Translation.get("match.win", getPlacer().locale()), null);
        }
    }

    public static void win(@NotNull BBPlayer player) {
        BBPlayer opponent;
        if (player == p1) {
            opponent = p2;
        } else {
            opponent = p1;
        }

        if (opponent.hasRevive()) {
            opponent.useRevive();
        } else {
            opponent.player.setHealth(0);
            opponent.showTitle("", McColor.RED + Translation.get("match.lose", opponent.locale()), null);

            player.player.setHealth(20.0);
            player.showTitle("", McColor.GREEN + Translation.get("match.win", player.locale()), null);
        }
    }

    public static void lose() {
        if(getPlacer().hasRevive()) {
            getPlacer().useRevive();
        } else {
            getPlacer().player.setHealth(0);
            getPlacer().showTitle("", McColor.RED + Translation.get("match.lose", getPlacer().locale()), null);

            getOpponent().player.setHealth(20.0);
            getOpponent().showTitle("", McColor.GREEN + Translation.get("match.win", getOpponent().locale()), null);
        }
    }

    public static void draw() {
        getPlacer().player.setHealth(0);
        getPlacer().showTitle("", McColor.GOLD + Translation.get("match.draw", getPlacer().locale()), null);

        getOpponent().player.setHealth(0);
        getOpponent().showTitle("", McColor.GOLD + Translation.get("match.draw", getOpponent().locale()), null);
    }

    public static void shopDone() {
        shopPhase = false;
        ScoreBoard.start();
        algorithm.start(p1.player);
        p1.swapHotbars();
        p2.swapHotbars();
    }

    public static void addTurn(TurnInfo turn) {
        turnHistory.add(turn);
    }

    public static void next() {
        swapTimer--;
        if (swapTimer == 0) {
            ItemStack[] hotbar1 = p1.player.getInventory().getContents().clone();
            ItemStack[] hotbar2 = p2.player.getInventory().getContents().clone();
            p1.player.getInventory().setContents(hotbar2);
            p1.player.getInventory().setContents(hotbar1);
        }

        if (!getPlacer().hasToDoubleCounterAttack()){
            algorithm.timer.stop();
            algorithm.clear();
            algorithm.start(getPlacer().player);
        } else {
            getPlacer().setDoubleCounterAttack(false);
        }

        p1.count();
        p2.count();

        Player player = getPlacer().player;
        boolean emptyHotbar = IntStream.range(0, 9)
                .mapToObj(s -> player.getInventory().getItem(s))
                .allMatch(i -> i == null || i.getType().isAir());
        boolean emptySecondary = IntStream.range(27, 36)
                .mapToObj(s -> player.getInventory().getItem(s))
                .allMatch(i -> i == null || i.getType().isAir());
        if (emptyHotbar) {
            if (emptySecondary) {
                Match.lose();
            } else {
                getPlayer(player).swapHotbars();
            }
        }
    }

    public static TurnInfo getLatestTurn() {
        return turnHistory.get(turnHistory.size() - 1);
    }

    public static boolean latestTurnIsClass(WarpSettings.Class @NotNull ... classes) {
        Turn latestTurn = getLatestTurn().turn();
        for (WarpSettings.Class clazz : classes) {
            if (latestTurn.clazz == clazz) return true;
        }
        return false;
    }

    public static boolean latestTurnIsUnder(@NotNull Cord cord) {
        Block underBlock = new Location(HBB.WORLD, cord.x(), cord.y(), cord.z()).getBlock();
        return Match.getLatestTurn().turn().material == underBlock.getType();
    }

    public static void setIsNight(boolean isNight) {
        Match.isNight = isNight;
        if (isNight) {
            HBB.WORLD.setTime(0);
        } else {
            HBB.WORLD.setTime(6000);
        }
    }

    public static void setRain(boolean rain) {
        Match.rain = rain;
        HBB.WORLD.setStorm(rain);
        HBB.WORLD.setWeatherDuration(-1);
    }

    public static void swapHotbars() {
        for (int i = 0; i < 9; i++) {
            ItemStack p1Slot = p1.player.getInventory().getItem(i);
            p1.player.getInventory().setItem(i, p2.player.getInventory().getItem(i));
            p2.player.getInventory().setItem(i, p1Slot);
        }
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
