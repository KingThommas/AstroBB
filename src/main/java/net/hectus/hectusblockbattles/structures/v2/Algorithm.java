package net.hectus.hectusblockbattles.structures.v2;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.events.StructurePlaceEvent;
import net.hectus.hectusblockbattles.match.NormalMatch;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;

public class Algorithm {
    public boolean running = false; // If the algorithm is already running
    public AlgorithmTimer timer = new AlgorithmTimer(this); // The timer for block placement detection
    public Player placer; // The player who's currently placing blocks
    private final HashSet<Structure.BlockData> placed = new HashSet<>(); // All placed blocks
    private final HashMap<Structure, Double> possible = new HashMap<>(); // Structures and their chances
    private Structure.Cord relative; // The 0 0 0 relative, used to compare with the structure relatives

    public void start(Player player) {
        HBB.LOGGER.info("The algorithm was started!");

        running = true;
        placer = player;
        timer.instance(this);

        StructureManager.loadedStructures.forEach(struct -> possible.put(struct, 0.0));
    }

    public void addBlock(Structure.BlockData blockData) {
        if (placed.isEmpty()) {
            relative = new Structure.Cord(blockData.x(), blockData.y(), blockData.z());
        } else {
            if (relative.x() > blockData.x()) relative = new Structure.Cord(blockData.x(), relative.y(), relative.z());
            if (relative.y() > blockData.y()) relative = new Structure.Cord(relative.x(), blockData.y(), relative.z());
            if (relative.z() > blockData.z()) relative = new Structure.Cord(relative.x(), relative.y(), blockData.z());
        }
        placed.add(blockData);

        timer.lap(this);
    }

    public void calculateChances() {
        Player opponent;
        if (isPlacer(NormalMatch.p1))
            opponent = NormalMatch.p2;
        else opponent = NormalMatch.p1;

        for (Structure structure : new HashSet<>(possible.keySet())) {
            int matchingBlocks = 0;
            int misplacedBlocks = 0;

            for (Structure.BlockData data : placed) {
                int rx = data.x() - relative.x();
                int ry = data.y() - relative.y();
                int rz = data.z() - relative.z();

                Structure.BlockData tmpData = new Structure.BlockData(data.material(), rx, ry, rz, data.direction(), data.blockBound(), data.open());

                // TODO: Add misplaces

                if (structure.blockData.contains(tmpData)) {
                    matchingBlocks++;
                } else {
                    misplacedBlocks++;
                }
            }

            double chanceWithoutMisplaces = (double) matchingBlocks / structure.blockData.size();

            double chance = (double) (matchingBlocks - misplacedBlocks) / structure.blockData.size();
            possible.put(structure, chance);

            if (chanceWithoutMisplaces >= 1.0) {
                if (chance >= 1.0) {
                    BlockBattleEvents.onStructurePlace(new StructurePlaceEvent(structure, relative, placer, opponent, possible));
                } else {
                    placer.showTitle(BlockBattleEvents.subtitle(McColor.RED + "Structure was misplaced!"));
                    opponent.sendActionBar(Component.text(placer.getName() + " misplaced his structure!"));
                }

                timer.stop();

                clear();
                start(opponent);
            }
        }
    }

    public void clear() {
        running = false;
        placed.clear();
        possible.clear();
        placer = null;
        relative = null;
    }

    public boolean isPlacer(@NotNull Player player) {
        if (running) {
            return player.getName().equals(placer.getName());
        } else {
            return false;
        }
    }
}
