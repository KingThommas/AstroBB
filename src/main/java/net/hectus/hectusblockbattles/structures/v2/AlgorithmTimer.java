package net.hectus.hectusblockbattles.structures.v2;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.match.NormalMatch;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Contract;

public class AlgorithmTimer {
    public int ticks = 10;
    public int laps = 0;
    public BukkitTask task;
    public Algorithm algorithm;

    @Contract(pure = true)
    public AlgorithmTimer(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void instance(Algorithm algorithm) {
        this.algorithm = algorithm;

        task = Bukkit.getScheduler().runTaskTimer(HBB.getPlugin(HBB.class), () -> {
            ticks--;

            algorithm.placer.sendActionBar(Component.text(McColor.GRAY + String.valueOf(ticks)));

            if (ticks <= 0) {
                ticks = 10;
                laps += 1;
                algorithm.calculateChances();
            }
            if (laps >= 20) {
                Player opponent;
                if (NormalMatch.algorithm.isPlacer(NormalMatch.p1.player))
                    opponent = NormalMatch.p2.player;
                else opponent = NormalMatch.p1.player;

                NormalMatch.algorithm.timer.stop();

                NormalMatch.algorithm.clear();
                NormalMatch.algorithm.start(opponent);

                HBB.WORLD.showTitle(BlockBattleEvents.subtitle(McColor.GOLD + NormalMatch.algorithm.placer.getName() + " was too slow!"));
            }
        }, 0L, 1);
    }

    public void lap(Algorithm algorithm) {
        stop();
        instance(algorithm);
    }

    public void stop() {
        task.cancel();
        ticks = 10;
        laps = 0;
    }
}
