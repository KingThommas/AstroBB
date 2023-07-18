package net.hectus.hectusblockbattles.match;

import net.hectus.color.Ansi;
import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.ScoreBoard;
import net.hectus.hectusblockbattles.Trace;
import net.hectus.hectusblockbattles.Translation;
import net.hectus.hectusblockbattles.player.BBPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.stream.IntStream;

public class GameFlow {
    public static void next() {
        System.out.println(Ansi.RED + "GameFlow.next() by: " + Trace.last() + Ansi.GREEN);

        System.out.println("++++++++++++++++++++++++++ NEXT ++++++++++++++++++++++++++");

        Match.swapTimer--;
        if (Match.swapTimer == 0) {
            ItemStack[] hotbar1 = Match.p1.player.getInventory().getContents().clone();
            ItemStack[] hotbar2 = Match.p2.player.getInventory().getContents().clone();
            Match.p1.player.getInventory().setContents(hotbar2);
            Match.p1.player.getInventory().setContents(hotbar1);
        }

        if (!Match.getPlacer().hasToDoubleCounterAttack()){
            Match.algorithm.clear();
            Match.algorithm.start(Match.getPlacer().player);
        } else {
            Match.getPlacer().setDoubleCounterAttack(false);
        }

        Match.p1.count();
        Match.p2.count();

        boolean emptyHotbar = IntStream.range(0, 9)
                .mapToObj(s -> Match.getPlacer().player.getInventory().getItem(s))
                .allMatch(i -> i == null || i.getType().isAir());
        boolean emptySecondary = IntStream.range(27, 36)
                .mapToObj(s -> Match.getPlacer().player.getInventory().getItem(s))
                .allMatch(i -> i == null || i.getType().isAir());
        if (emptyHotbar) {
            if (emptySecondary) {
                GameFlow.lose(LoseReason.NO_ITEMS);
            } else {
                Match.getPlacer().sendActionBar(McColor.RED + "Switching to your Overtime Hotbar!");
                Match.getPlacer().swapHotbars();
            }
        }
        System.out.println("-------------------------- NEXT --------------------------" + Ansi.DARK_GRAY);
        System.out.println("++++++++++ SCOREBOARD ++++++++++");
        ScoreBoard.update(Match.p1);
        ScoreBoard.update(Match.p2);
        System.out.println("---------- SCOREBOARD ----------" + Ansi.RESET);
    }

    public static void win(BBPlayer player, WinReason reason) {
        System.out.println("GameFlow.win(" + "player = " + player + ", reason = " + reason + ") by: " + Trace.last());

        BBPlayer opponent = Match.getOpposite(player);

        if (opponent.hasRevive()) {
            opponent.useRevive();
        } else {
            opponent.player.setHealth(0);
            opponent.showTitle("", McColor.RED + Translation.get("match.lose", opponent.player.locale()), null);

            player.player.setHealth(20.0);
            player.showTitle("", McColor.RED + Translation.get("match.win", player.player.locale()), null);

            Match.stop();

            for (Player tempPlayer : HBB.WORLD.getPlayers()) {
                tempPlayer.sendMessage(Component.text(reason.get(tempPlayer)));
            }
        }
    }
    public static void win(WinReason reason) {
        System.out.println("GameFlow.win(" + "reason = " + reason + ") by: " + Trace.last());
        win(Match.getPlacer(), reason);
    }

    public static void lose(BBPlayer player, LoseReason reason) {
        System.out.println("GameFlow.lose(" + "player = " + player + ", reason = " + reason + ") by: " + Trace.last());

        BBPlayer opponent = Match.getOpposite(player);

        if (player.hasRevive()) {
            player.useRevive();
            opponent.sendActionBar(McColor.RED + player.player.getName() + " had a revive, so he didn't lose.");
        } else {
            player.player.setHealth(0);
            player.showTitle("", McColor.RED + Translation.get("match.lose", player.player.locale()), null);

            opponent.player.setHealth(20.0);
            opponent.showTitle("", McColor.GREEN + Translation.get("match.win", opponent.player.locale()), null);

            Match.stop();

            for (Player tempPlayer : HBB.WORLD.getPlayers()) {
                tempPlayer.sendMessage(Component.text(reason.get(tempPlayer)));
            }
        }
    }
    public static void lose(LoseReason reason) {
        System.out.println("GameFlow.lose(" + "reason = " + reason + ") by: " + Trace.last());
        lose(Match.getPlacer(), reason);
    }

    public static void draw(@NotNull DrawReason reason) {
        System.out.println("GameFlow.draw(" + "reason = " + reason + ") by: " + Trace.last());

        BBPlayer placer = Match.getPlacer();
        BBPlayer opponent = Match.getOpponent();

        placer.player.setHealth(0);
        placer.showTitle("", McColor.RED + Translation.get("match.draw", Match.getPlacer().locale()), null);

        opponent.player.setHealth(0);
        opponent.showTitle("", McColor.RED + Translation.get("match.draw", Match.getOpponent().locale()), null);

        Match.stop();

        for (Player player : HBB.WORLD.getPlayers()) {
            player.sendMessage(Component.text(reason.get(player.locale())));
        }
    }

    public enum WinReason {
        PURPLE_WOOL,
        TNT,
        PINK_SHEEP,
        BLUE_AXOLOTL;

        public @NotNull String get(@NotNull Player player) {
            System.out.println("WinReason.get(" + "player = " + player + ") by: " + Trace.last());
            return Translation.get(key(), player.locale(), player.getName());
        }

        public @NotNull String key() {
            return "game_flow.win_reason." + name().toLowerCase();
        }
    }

    public enum LoseReason {
        GAVE_UP,
        MOVEMENT,
        BURNING,
        DEATH_COUNTER,
        PLAYER_OUT_OF_BOUNDS,
        TURN_OUT_OF_BOUNDS,
        DISALLOWED_TURN,
        WARP_ON_FIRST_TURN,
        TNT,
        NO_COUNTER,
        NO_ITEMS,
        TIME_OVER;

        public @NotNull String get(@NotNull Player player) {
            System.out.println("LoseReason.get(" + "player = " + player + ") by: " + Trace.last());

            return Translation.get(key(), player.locale(), player.getName());
        }

        public @NotNull String key() {
            return "game_flow.lose_reason." + name().toLowerCase();
        }
    }

    public enum DrawReason {
        TNT;

        public @NotNull String get(Locale locale) {
            System.out.println("DrawReason.get(" + "locale = " + locale + ") by: " + Trace.last());
            return Translation.get(key(), locale);
        }

        public @NotNull String key() {
            return "game_flow.draw_reason." + name().toLowerCase();
        }
    }
}
