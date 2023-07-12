package net.hectus.hectusblockbattles;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.player.BBPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreBoard {
    public static final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

    public static void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(HBB.getPlugin(HBB.class), () -> {
            HBB.LOGGER.fine("Updating the scoreboard...");
            update(Match.p1);
            update(Match.p2);
        }, 0L, 5L);
    }

    public static void update(BBPlayer p) {
        final Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        final Objective o = scoreboard.registerNewObjective("bb", Criteria.DUMMY, Component.text(McColor.GREEN + "BlockBattles Alpha"));
        o.setDisplaySlot(DisplaySlot.SIDEBAR);

        String whosTurn = Match.getPlacer().player.getName().equalsIgnoreCase(p.player.getName()) ? "You" : "Opponent";

        Score blank1 = o.getScore(" ");
        Score user = o.getScore(McColor.GRAY + p.player.getName());
        Score opponent = o.getScore(McColor.GRAY + Match.getOpposite(p).player.getName());
        Score blank2 = o.getScore("  ");
        Score turn = o.getScore(McColor.GRAY + "Turn: " + McColor.WHITE + whosTurn);
        Score blank3 = o.getScore("   ");
        Score luck = o.getScore(McColor.GRAY + "Your Luck: " + McColor.WHITE + p.luck());
        Score turns = o.getScore(McColor.GRAY + "Cancer: " + McColor.WHITE + (p.hasCancer() ? "Stage " + p.cancerStage() : "No"));
        Score blank4 = o.getScore("    ");
        Score attacked = o.getScore(McColor.GRAY + "Attacked: " + (p.isAttacked() ? McColor.LIME : McColor.RED) + p.isAttacked());
        Score defended = o.getScore(McColor.GRAY + "Defended: " + (p.isDefended() ? McColor.LIME : McColor.RED) + p.isDefended());
        Score frozen = o.getScore(McColor.GRAY + "Frozen: " + ((!p.canMove()) ? McColor.LIME : McColor.RED) + !p.canMove());

        blank1.setScore(11); // Separation
        user.setScore(10); // Your username
        opponent.setScore(9); // Your opponent's username
        blank2.setScore(8); // Separation
        turn.setScore(7); // Who is currently turning
        blank3.setScore(6); // Separation
        luck.setScore(5); // Your luck
        turns.setScore(4); // Your extra turns
        blank4.setScore(3); // Separation
        attacked.setScore(2); // If you're attacked
        defended.setScore(1); // If you're defended
        frozen.setScore(0); // If you're frozen

        p.player.setScoreboard(scoreboard);

        HBB.LOGGER.finer("Scoreboard was updated for " + p.player.getName());
    }
}
