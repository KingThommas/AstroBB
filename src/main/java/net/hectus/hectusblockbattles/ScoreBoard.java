package net.hectus.hectusblockbattles;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.player.BBPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class ScoreBoard {
    public static final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

    public static void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(HBB.getPlugin(HBB.class), () -> {
            update(Match.p1);
            update(Match.p2);
        }, 0L, 5L);
    }

    public static void update(@NotNull BBPlayer p) {
        final Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        final Objective o = scoreboard.registerNewObjective("bb", Criteria.DUMMY, Component.text(McColor.GREEN + "BlockBattles Alpha"));
        o.setDisplaySlot(DisplaySlot.SIDEBAR);

        Locale l = p.locale();

        String whosTurn = Match.getPlacer().player.getName().equalsIgnoreCase(p.player.getName()) ? Translation.get("scoreboard.turn.you", l) : Translation.get("scoreboard.turn.opponent", l);

        Score blank1 = o.getScore(" ");
        Score user = o.getScore(McColor.GRAY + p.player.getName());
        Score opponent = o.getScore(McColor.GRAY + Match.getOpposite(p).player.getName());
        Score blank2 = o.getScore("  ");
        Score turn = o.getScore(McColor.GRAY + Translation.get("scoreboard.turn", l) + McColor.WHITE + whosTurn);
        Score blank3 = o.getScore("   ");
        Score luck = o.getScore(McColor.GRAY + Translation.get("scoreboard.luck", l) + McColor.WHITE + p.luck());
        Score blank4 = o.getScore("    ");
        Score attacked = o.getScore(McColor.GRAY + Translation.get("scoreboard.attacked", l) + bool(p.isAttacked(), l));
        Score defended = o.getScore(McColor.GRAY + Translation.get("scoreboard.defended", l) + bool(p.isDefended(), l));
        Score frozen = o.getScore(McColor.GRAY + Translation.get("scoreboard.frozen", l) + bool(!p.canMove(), l));

        blank1.setScore(10); // Separation
        user.setScore(9); // Your username
        opponent.setScore(8); // Your opponent's username
        blank2.setScore(7); // Separation
        turn.setScore(6); // Who is currently turning
        blank3.setScore(5); // Separation
        luck.setScore(4); // Your luck
        blank4.setScore(3); // Separation
        attacked.setScore(2); // If you're attacked
        defended.setScore(1); // If you're defended
        frozen.setScore(0); // If you're frozen

        p.player.setScoreboard(scoreboard);
    }

    private static String bool(boolean bool, Locale loc) {
        return bool ? McColor.LIME + Translation.get("scoreboard.yes", loc) : McColor.RED + Translation.get("scoreboard.no", loc);
    }
}
