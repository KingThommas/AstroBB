package net.hectus.hectusblockbattles.structures.v2;

import net.hectus.hectusblockbattles.HBB;
import net.hectus.util.color.McColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class Algorithm {
    public static boolean running = false;
    private static final HashSet<Structure.BlockData> placed = new HashSet<>(); // All placed blocks
    private static final HashMap<Material, Integer> materialMap = new HashMap<>(); // All materials that were already placed
    private static HashMap<Structure, Double> possible = new HashMap<>(); // OutdatedStructures and their chances
    private static String placer; // The player who's currently placing blocks

    public static void start(Structure.BlockData block, String player) {
        HBB.LOGGER.info("The algorithm was started!");

        running = true;
        placed.add(block);
        materialMap.put(block.material(), 1);

        calculateChances();

        placer = player;
    }

    public static void addBlock(Structure.BlockData blockData) {
        placed.add(blockData);
        Material material = blockData.material();

        materialMap.put(material, materialMap.getOrDefault(material, 0) + 1);

        run();
    }

    public static void run() {
        // Store the updated chances
        HashMap<Structure, Double> updatedChances = new HashMap<>();

        calculateChances();

        // Update the possible map with the updated chances
        possible = updatedChances;

        sendStructureChances(Bukkit.getPlayerExact(placer));
    }

    private static void calculateChances() {
        for (Map.Entry<Structure, Double> entry : possible.entrySet()) {
            // Not done yet, I just committed now, so gradle is working for everyone
        }
    }

    private static Map<Material, Integer> getMaterialCounts() {
        Map<Material, Integer> materialCounts = new HashMap<>();

        for (Structure.BlockData blockData : Algorithm.placed) {
            Material material = blockData.material();
            materialCounts.put(material, materialCounts.getOrDefault(material, 0) + 1);
        }

        return materialCounts;
    }

    public static void sendStructureChances(Player player) {
        for (Map.Entry<Structure, Double> entry : possible.entrySet()) {
            Structure structure = entry.getKey();
            double chance = entry.getValue() * 100;

            String message = structure.name + ": " + String.format("%.2f%%", chance);

            player.sendMessage(Component.text(McColor.GRAY + message));
            System.out.println(message);
        }
    }
}
