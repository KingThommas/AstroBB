package net.hectus.hectusblockbattles;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.player.BBPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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

        Score blank1 = o.getScore(" ");
        Score user = o.getScore(p.player.getName());
        Score opponent = o.getScore(Match.getOpposite(p).player.getName());
        Score blank2 = o.getScore("  ");
        Score luck = o.getScore("Your Luck: " + p.luck());
        Score turns = o.getScore("Extra Turns: " + p.getExtraTurns());
        Score blank3 = o.getScore("   ");
        Score attacked = o.getScore("Attacked: " + p.isAttacked());
        Score defended = o.getScore("Defended: " + p.isDefended());
        Score frozen = o.getScore("Frozen: " + !p.canMove());

        blank1.setScore(9);
        user.setScore(8); opponent.setScore(7);
        blank2.setScore(6);
        luck.setScore(5); turns.setScore(4);
        blank3.setScore(3);
        attacked.setScore(2); defended.setScore(1); frozen.setScore(0);

        p.player.setScoreboard(scoreboard);

        HBB.LOGGER.finer("Scoreboard was updated for " + p.player.getName());
    }
}
