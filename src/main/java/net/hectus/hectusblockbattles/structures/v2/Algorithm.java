package net.hectus.hectusblockbattles.structures.v2;

import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.events.StructurePlaceEvent;
import net.hectus.util.Formatter;
import net.hectus.util.color.McColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Algorithm {
    public static boolean running = false;
    private static final HashSet<Structure.BlockData> placed = new HashSet<>(); // All placed blocks
    private static final HashMap<Structure, Double> possible = new HashMap<>(); // Structures and their chances
    private static Player placer; // The player who's currently placing blocks
    private static Structure.Cord relative; // The 0 0 0 relative, used to compare with the structure relatives

    public static void start(Player player) {
        HBB.LOGGER.info("The algorithm was started!");
        running = true;
        for (Structure structure : StructureManager.loadedStructures) {
            possible.put(structure, 0.0);
        }
        placer = player;
        calculateChances();
    }

    public static void addBlock(Structure.BlockData blockData) {
        if (placed.isEmpty()) {
            relative = new Structure.Cord(blockData.x(), blockData.y(), blockData.z());
        } else {
            if (relative.x() > blockData.x()) relative = new Structure.Cord(blockData.x(), relative.y(), relative.z());
            if (relative.y() > blockData.y()) relative = new Structure.Cord(relative.x(), blockData.y(), relative.z());
            if (relative.z() > blockData.z()) relative = new Structure.Cord(relative.x(), relative.y(), blockData.z());
        }
        placed.add(blockData);

        calculateChances();
    }

    private static void calculateChances() {
        for (Structure structure : new HashSet<>(possible.keySet())) {
            int matchingBlocks = 0;

            for (Structure.BlockData data : placed) {
                int rx = data.x() - relative.x();
                int ry = data.y() - relative.y();
                int rz = data.z() - relative.z();

                Structure.BlockData tmpData = new Structure.BlockData(data.material(), rx, ry, rz);

                // TODO: Add misplaces

                if (structure.blockData.contains(tmpData)) matchingBlocks++;
            }

            double chance = (double) matchingBlocks / structure.blockData.size();
            possible.put(structure, chance);
        }
        chanceOutput();
    }

    private static void chanceOutput() {
        for (int i = 0; i < 32; i++) placer.sendMessage(Component.empty());

        placer.sendMessage(Component.text("===== Structure Chances ====="));

        for (Map.Entry<Structure, Double> entry : possible.entrySet()) {
            if (entry.getValue() > .0){
                double pct = entry.getValue() * 100.0;
                placer.sendMessage(Component.text(Formatter.toPascalCase(entry.getKey().name) + " : " + pct + "%"));
            }

            if (entry.getValue() >= 100.0) { // If the player placed a whole structure
                BlockBattleEvents.onStructurePlace(new StructurePlaceEvent(entry.getKey(), relative, placer, placer, possible));
                clear();
            }
        }
    }

    public static void clear() {
        running = false;
        placed.clear();
        possible.clear();
        placer = null;
        relative = null;
    }
}
