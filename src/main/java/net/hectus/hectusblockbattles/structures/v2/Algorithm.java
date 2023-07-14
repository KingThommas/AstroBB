package net.hectus.hectusblockbattles.structures.v2;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.Cord;
import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.Translation;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.events.StructurePlaceEvent;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.player.BBPlayer;
import net.hectus.storing.pair.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Algorithm {
    public boolean running = false; // If the algorithm is already running
    public AlgorithmTimer timer = new AlgorithmTimer(this); // The timer for block placement detection
    public BBPlayer placer; // The player who's currently placing blocks
    private final HashSet<Structure.BlockData> placed = new HashSet<>(); // All placed blocks
    private final HashMap<Structure, Double> possible = new HashMap<>(); // Structures and their chances
    private Cord relative; // The 0 0 0 relative, used to compare with the structure relatives

    public void start(Player player) {
        HBB.LOGGER.info("The algorithm was started!");

        Match.p1.showTitle(Translation.get("match.start_player", Match.p1.locale(), player.getName()), "", null);
        Match.p2.showTitle(Translation.get("match.start_player", Match.p2.locale(), player.getName()), "", null);

        running = true;
        placer = Match.getPlayer(player);
        StructureManager.loadedStructures.forEach(struct -> possible.put(struct, 0.0));
        timer.instance(this);
    }

    public void addBlock(Structure.BlockData blockData) {
        if (placed.isEmpty()) {
            relative = new Cord(blockData.x(), blockData.y(), blockData.z());
        } else {
            if (relative.x() > blockData.x()) relative = new Cord(blockData.x(), relative.y(), relative.z());
            if (relative.y() > blockData.y()) relative = new Cord(relative.x(), blockData.y(), relative.z());
            if (relative.z() > blockData.z()) relative = new Cord(relative.x(), relative.y(), blockData.z());
        }
        placed.add(blockData);

        timer.lap(this);
    }

    public void calculateChances() {
        BBPlayer opponent = Match.getOpponent();

        for (Structure structure : new HashSet<>(possible.keySet())) {
            HashMap<Material, Integer> materials = new HashMap<>();
            for (Structure.BlockData structureData : structure.blockData) {
                Material mat = structureData.material();

                int oldCount = 0;
                if (materials.containsKey(mat)) oldCount = materials.get(mat);

                materials.put(mat, oldCount + 1);
            }

            int matchingBlocks = 0;
            int misplacedBlocks = 0;

            int misplacedMaterials = 0;

            for (Structure.BlockData data : placed) {
                Material mat = data.material();

                int rx = data.x() - relative.x();
                int ry = data.y() - relative.y();
                int rz = data.z() - relative.z();

                Structure.BlockData tmpData = new Structure.BlockData(mat, rx, ry, rz, data.direction(), data.blockBound(), data.open());

                if (structure.blockData.contains(tmpData)) {
                    matchingBlocks++;
                } else {
                    misplacedBlocks++;
                }

                if (materials.containsKey(mat)) {
                    int count = materials.get(mat);

                    if (count <= 1) {
                        materials.remove(mat);
                    } else {
                        materials.put(mat, count - 1);
                    }
                } else {
                    misplacedMaterials--;
                }
            }

            double noMisChance = (double) matchingBlocks / structure.blockData.size();

            if (noMisChance >= 1.0) {
                double chance = (double) (matchingBlocks - misplacedBlocks) / structure.blockData.size();
                possible.put(structure, chance);

                if (chance >= 1.0) {
                    BlockBattleEvents.onStructurePlace(new StructurePlaceEvent(structure, relative, placer.player, opponent.player, possible));
                } else {
                    placer.showTitle("", McColor.RED + Translation.get("structure.misplace", placer.locale()), null);
                    opponent.sendActionBar(Translation.get("structure.misplace.opponent", opponent.locale(), placer.player.getName()));
                }
                timer.stop();
                clear();
                start(opponent.player);
            } else {
                if (misplacedMaterials > 0) {
                    placer.showTitle("", McColor.RED + Translation.get("turn.misplace", placer.locale()), null);
                    opponent.sendActionBar(Translation.get("turn.misplace.opponent", opponent.locale(), placer.player.getName()));
                }
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
            return player.getName().equals(placer.player.getName());
        } else {
            return false;
        }
    }

    public Pair<Structure, Double> getHighestChance() {
        Pair<Structure, Double> pair = new Pair<>();
        for (Map.Entry<Structure, Double> entry : possible.entrySet()) {
            if (pair.isEmpty()) {
                pair.set(entry.getValue(), entry.getKey());
            } else if (entry.getValue() > pair.right()) {
                pair.set(entry.getValue(), entry.getKey());
            }
        }
        return pair;
    }
}
