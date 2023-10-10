package space.astrocraft.astrobb.match;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import space.astrocraft.astrobb.*;
import space.astrocraft.astrobb.player.BBPlayer;
import space.astrocraft.astrobb.structures.Algorithm;
import space.astrocraft.astrobb.turn.Turn;
import space.astrocraft.astrobb.turn.TurnInfo;
import space.astrocraft.astrobb.warps.Warp;
import space.astrocraft.astrobb.warps.WarpSettings;

import java.util.*;

public class Match {
    public static boolean hasStarted, shopPhase, isNight, rain, nextWarp100P, azureWasUsed, netherPortalAwaitIgnite, blazeDebuff;
    public static BBPlayer p1, p2;
    public static Warp currentWarp;
    public static HashSet<WarpSettings.Class> allowed = new HashSet<>(), disallowed = new HashSet<>();
    public static List<TurnInfo> turnHistory = new ArrayList<>();
    public static Algorithm algorithm = new Algorithm();
    public static int swapTimer = -3;

    public static void start(Player p1, Player p2) {
        System.out.println("Match.start(" + "p1 = " + p1.getName() + ", p2 = " + p2.getName() + ") by: " + Trace.last());

        p1.teleport(new Location(AstroBB.WORLD, -275.5, 59, 144.5, 0, 0));
        p2.teleport(new Location(AstroBB.WORLD, -275.5, 59, 152.5, 180, 0));

        hasStarted = true;

        Collections.addAll(allowed, Warp.DEFAULT.allow);
        Collections.addAll(disallowed, Warp.DEFAULT.deny);

        isNight = false;
        rain = false;
        AstroBB.WORLD.setTime(6000);

        Match.p1 = new BBPlayer(p1);
        Match.p2 = new BBPlayer(p2);

        currentWarp = Warp.DEFAULT;

        InGameShop.displayShop(p1, InGameShop.HotBar.NORMAL);
        InGameShop.displayShop(p2, InGameShop.HotBar.NORMAL);
        shopPhase = true;
    }

    public static void stop() {
        System.out.println("Match.stop() by: " + Trace.last());

        hasStarted = false;
        shopPhase = false;
        nextWarp100P = false;
        azureWasUsed = false;
        netherPortalAwaitIgnite = false;
        blazeDebuff = false;
        isNight = false;
        rain = false;
        swapTimer = -3;
        p1 = null;
        p2 = null;
        allowed.clear();
        disallowed.clear();
        turnHistory.clear();
        algorithm.clear();
        algorithm = new Algorithm();
        currentWarp = Warp.DEFAULT;

        AstroBB.WORLD.setTime(6000);
    }

    public static BBPlayer getOpponent() {
        if (algorithm.isPlacer(p1.player)) {
            System.out.println("Match.getOpponent() - return: " + p2.player.getName() + " by: " + Trace.last());
            return p2;
        } else {
            System.out.println("Match.getOpponent() - return: " + p1.player.getName() + " by: " + Trace.last());
            return p1;
        }
    }

    public static BBPlayer getPlacer() {
        if (algorithm.isPlacer(p1.player)) {
            System.out.println("Match.getPlacer() - return: " + p1.player.getName() + " by: " + Trace.last());
            return p1;
        } else {
            System.out.println("Match.getPlacer() - return: " + p2.player.getName() + " by: " + Trace.last());
            return p2;
        }
    }

    @Contract(pure = true)
    public static BBPlayer getOpposite(@NotNull BBPlayer player) {
        if (p1.player.getUniqueId() == player.player.getUniqueId()) {
            System.out.println("Match.getOpposite(" + "player = " + player.player.getName() + ") - return: " + p2.player.getName() + " by: " + Trace.last());
            return p2;
        } else {
            System.out.println("Match.getOpposite(" + "player = " + player.player.getName() + ") - return: " + p1.player.getName() + " by: " + Trace.last());
            return p1;
        }
    }

    @Contract(pure = true)
    public static BBPlayer getPlayer(@NotNull Player player) {
        if (p1.player.getUniqueId() == player.getUniqueId()) {
            System.out.println("Match.getPlayer(" + "player = " + player.getName() + ") - return: " + p1.player.getName() + " by: " + Trace.last());
            return p1;
        } else {
            System.out.println("Match.getPlayer(" + "player = " + player.getName() + ") - return: " + p2.player.getName() + " by: " + Trace.last());
            return p2;
        }
    }

    public static void shopDone() {
        System.out.println("Match.shopDone()");

        shopPhase = false;

        if (GameFlow.next == 1) {
            algorithm.start(p1.player);
            GameFlow.next = 2;
        } else {
            algorithm.start(p2.player);
            GameFlow.next = 1;
        }

        p1.swapHotbars();
        p2.swapHotbars();

        ScoreBoard.update(p1);
        ScoreBoard.update(p2);
    }

    public static void addTurn(TurnInfo turn) {
        System.out.println("Match.addTurn(" + "turn = " + turn + ")");
        turnHistory.add(turn);
    }

    public static TurnInfo getLatestTurn() {
        System.out.println("Match.getLatestTurn() by: " + Trace.last());
        return turnHistory.get(turnHistory.size() - 1);
    }

    public static boolean latestTurnIsClass(WarpSettings.Class @NotNull ... classes) {
        Turn latestTurn = getLatestTurn().turn();
        for (WarpSettings.Class clazz : classes) {
            if (latestTurn.clazz == clazz) {
                System.out.println("Match.latestTurnIsClass(" + "classes = " + Arrays.toString(classes) + ") - return: true by: " + Trace.last());
                return true;
            }
        }
        System.out.println("Match.latestTurnIsClass(" + "classes = " + Arrays.toString(classes) + ") - return: false by: " + Trace.last());
        return false;
    }

    public static boolean latestTurnIsUnder(@NotNull Cord cord) {
        Block underBlock = new Location(AstroBB.WORLD, cord.x(), cord.y(), cord.z()).getBlock();
        System.out.println("Match.latestTurnIsUnder(" + "cord = " + cord + ") - return: " + (Match.getLatestTurn().turn().material == underBlock.getType()) + " by: " + Trace.last());
        return Match.getLatestTurn().turn().material == underBlock.getType();
    }

    public static void setIsNight(boolean isNight) {
        System.out.println("Match.setIsNight(" + "isNight = " + isNight + ") by: " + Trace.last());

        Match.isNight = isNight;
        if (isNight) {
            AstroBB.WORLD.setTime(0);
        } else {
            AstroBB.WORLD.setTime(6000);
        }
    }

    public static void setRain(boolean rain) {
        System.out.println("Match.setRain(" + "rain = " + rain + ") by: " + Trace.last());

        Match.rain = rain;
        AstroBB.WORLD.setStorm(rain);
        AstroBB.WORLD.setWeatherDuration(-1);
    }

    public static void swapHotbars() {
        System.out.println("Match.swapHotbars()");

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
