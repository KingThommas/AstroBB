package net.hectus.hectusblockbattles.structures.v2;

import net.hectus.color.Ansi;
import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.Cord;
import net.hectus.hectusblockbattles.Trace;
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
    public BBPlayer placer; // The player who's currently placing blocks
    private final HashSet<Structure.BlockData> placed = new HashSet<>(); // All placed blocks
    private final HashMap<Structure, Double> possible = new HashMap<>(); // Structures and their chances
    private Cord relative; // The 0 0 0 relative, used to compare with the structure relatives

    public void start(Player player) {
        System.out.println("Algorithm.start(" + "player = " + player.getName() + ") by: " + Trace.last());

        running = true;
        placer = Match.getPlayer(player);

        Match.getOpponent().sendMessage(McColor.GOLD + "Your turn is over");
        Match.getPlacer().sendMessage(McColor.LIME + "It's your turn now!");

        StructureManager.loadedStructures.forEach(struct -> possible.put(struct, 0.0));
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
        calculateChances();
    }

    public void calculateChances() {
        System.out.println(Ansi.DARK_GRAY + "Algorithm.calculateChances()" + Ansi.RESET);
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
        System.out.println("Algorithm.clear() by: " + Trace.last());

        running = false;
        placed.clear();
        possible.clear();
        placer = null;
        relative = null;
    }

    public boolean isPlacer(@NotNull Player player) {
        System.out.println("Algorithm.isPlacer(" + "player = " + player + ") - return: " + (running && player.getName().equals(placer.player.getName())) + " by: " + Trace.last());
        return running && player.getUniqueId() == placer.player.getUniqueId();
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
