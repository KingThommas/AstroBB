package net.hectus.hectusblockbattles.watch;

import net.hectus.color.McColor;
import net.hectus.data.time.Time;
import net.hectus.data.time.Timer;
import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.match.Match;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class TurnWatch implements Timer {
    private Time time;
    private BukkitTask timer;
    private boolean paused;

    @Override
    public void start() {
        timer = Bukkit.getScheduler().runTaskTimer(HBB.getPlugin(HBB.class), () -> {
            if (!paused) {
                time.decrement();
                if (time.getAs(Time.Unit.SECONDS) == 0) {
                    time.increment(15);
                    Match.getPlacer().showTitle("", McColor.RED + "You were too slow. -10 Luck!", null);
                    Match.getOpponent().showTitle("", McColor.GREEN + "Your opponent was too slow, it's your turn now!", null);
                    Match.getPlacer().removeLuck(10);
                    Match.next();
                }
            }
        }, 0L, 20L);
    }

    @Override
    public void stop() {
        timer.cancel();
    }

    @Override
    public Time getLeft() {
        return time;
    }

    @Override
    public Time getDone() {
        return new Time(15 - time.getAs(Time.Unit.SECONDS));
    }

    @Override
    public boolean pause() {
        if (!paused) {
            paused = true;
            return true;
        } else
            return false;
    }

    @Override
    public boolean resume() {
        if (paused) {
            paused = false;
            return true;
        } else
            return false;
    }
}
