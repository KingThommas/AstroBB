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
        String whosTurn = "";
        if(Match.getPlacer().player.getName().equalsIgnoreCase(p.player.getName())){
            whosTurn = "You";
        }else{
            whosTurn = "Opponent";
        }

        Score blank1 = o.getScore(" ");
        Score user = o.getScore(McColor.GRAY + p.player.getName());
        Score opponent = o.getScore(McColor.GRAY + Match.getOpposite(p).player.getName());
        Score blank2 = o.getScore("  ");
        Score turn = o.getScore(McColor.GRAY + "Turn: " + McColor.WHITE + whosTurn);
        Score blank3 = o.getScore("   ");
        Score luck = o.getScore(McColor.GRAY + "Your Luck: " + McColor.WHITE + p.luck());
        Score turns = o.getScore(McColor.GRAY + "Extra Turns: " + McColor.WHITE + p.getExtraTurns());
        Score blank4 = o.getScore("    ");
        McColor color = McColor.RED;
        if(p.isAttacked()){
            color = McColor.LIME;
        }
        Score attacked = o.getScore(McColor.GRAY + "Attacked: " + color + p.isAttacked());
        if(p.isDefended()){
            color = McColor.LIME;
        }
        Score defended = o.getScore(McColor.GRAY + "Defended: " + color + p.isDefended());
        if(p.canMove()){
            color = McColor.LIME;
        }
        Score frozen = o.getScore(McColor.GRAY + "Frozen: " + color + !p.canMove());

        blank1.setScore(11);
        user.setScore(10);
        opponent.setScore(9);
        blank2.setScore(8);
        turn.setScore(7);
        blank3.setScore(6);
        luck.setScore(5);
        turns.setScore(4);
        blank4.setScore(3);
        attacked.setScore(2);
        defended.setScore(1);
        frozen.setScore(0);

        p.player.setScoreboard(scoreboard);

        HBB.LOGGER.finer("Scoreboard was updated for " + p.player.getName());
    }
}
