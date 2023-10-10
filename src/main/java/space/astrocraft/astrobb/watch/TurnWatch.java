package space.astrocraft.astrobb.watch;

import net.hectus.data.time.Time;
import net.hectus.data.time.Timer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import space.astrocraft.astrobb.AstroBB;
import space.astrocraft.astrobb.match.GameFlow;
import space.astrocraft.astrobb.match.Match;

public class TurnWatch implements Timer {
    private Time time;
    private BukkitTask timer;
    private boolean paused;

    @Override
    public void start() {
        timer = Bukkit.getScheduler().runTaskTimer(AstroBB.getPlugin(AstroBB.class), () -> {
            if (!paused) {
                time.decrement();
                Match.getPlacer().sendActionBar(time.getOneUnitFormatted());
                if (time.getAs(Time.Unit.SECONDS) == 0) GameFlow.lose(GameFlow.LoseReason.TIME_OVER);
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

    public void lap() {
        time = new Time(15);
    }
}
