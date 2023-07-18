package net.hectus.hectusblockbattles;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.player.BBPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScoreBoard {
    public static final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

    public static void update(@NotNull BBPlayer p) {
        final Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        final Objective o = scoreboard.registerNewObjective("bb", Criteria.DUMMY, Component.text(McColor.GREEN + "BlockBattles " + McColor.RED + "Alpha"));
        o.setDisplaySlot(DisplaySlot.SIDEBAR);

        Locale l = p.locale();

        List<Score> scores = new ArrayList<>();

        scores.add(o.getScore(" "));
        scores.add(o.getScore(McColor.GRAY + p.player.getName()));
        scores.add(o.getScore(McColor.GRAY + Match.getOpposite(p).player.getName()));
        scores.add(o.getScore("  "));
        scores.add(o.getScore(McColor.GRAY + Translation.get("scoreboard.turn", l) + McColor.WHITE + Match.algorithm.placer.player.getName()));
        scores.add(o.getScore("   "));
        scores.add(o.getScore(McColor.GRAY + Translation.get("scoreboard.luck", l) + McColor.WHITE + p.luck()));
        scores.add(o.getScore("    "));
        scores.add(o.getScore(McColor.GRAY + Translation.get("scoreboard.attacked", l) + bool(p.isAttacked(), l)));
        scores.add(o.getScore(McColor.GRAY + Translation.get("scoreboard.defended", l) + bool(p.isDefended(), l)));
        scores.add(o.getScore(McColor.GRAY + Translation.get("scoreboard.frozen", l) + bool(p.cantMove(), l)));

        // This code just iterates backwards through 'scores' and sets the score.
        // Same a the old way, but you can add more scores without having to add a score.setScore(...) each time.
        int scoreValue = scores.size() - 1;
        for (int i = scoreValue; i >= 0; i--) {
            scores.get(i).setScore(scoreValue - i);
        }

        p.player.setScoreboard(scoreboard);
    }

    private static @NotNull String bool(boolean bool, Locale loc) {
        return bool ? McColor.LIME + Translation.get("scoreboard.yes", loc) : McColor.RED + Translation.get("scoreboard.no", loc);
    }
}
