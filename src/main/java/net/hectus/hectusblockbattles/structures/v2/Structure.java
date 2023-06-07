package net.hectus.hectusblockbattles.structures.v2;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.HashMap;
import java.util.HashSet;

public class Structure {
    public final String name; // The OutdatedStructure's name
    public final HashMap<Material, Integer> materials; // For Stage 1 filtering the StructureManager
    public final HashSet<BlockData> blockData; // Data of all blocks that are in the structure and where they are

    public Structure(String name, HashMap<Material, Integer> materials, HashSet<BlockData> blockData) {
        this.name = name;
        this.materials = materials;
        this.blockData = blockData;
    }

    public Structure(String name) {
        this.name = name;
        materials = new HashMap<>();
        blockData = new HashSet<>();
    }

    public record Cord(int x, int y, int z) {}

    public record BlockData(Material material, int relativeX, int relativeY, int relativeZ) {}

    public static Structure save(Cord c1, Cord c2, String name, World world) {
        int hX, lX, hY, lY, hZ, lZ;

        hX = Math.max(c1.x, c2.x);
        lX = Math.min(c1.x, c2.x);
        hY = Math.max(c1.y, c2.y);
        lY = Math.min(c1.y, c2.y);
        hZ = Math.max(c1.z, c2.z);
        lZ = Math.min(c1.z, c2.z);

        Structure structure = new Structure(name);

        Cord startCord = new Cord(lX, lY, lZ);

        for (int x = lX; x <= hX; x++) {
            for (int y = lY; y <= hY; y++) {
                for (int z = lZ; z <= hZ; z++) {
                    org.bukkit.block.Block block = new Location(world, x, y, z).getBlock();
                    if (!block.getType().isAir()) {
                        structure.blockData.add(new BlockData(block.getType(), x - startCord.x, y - startCord.y, z - startCord.z));

                        if (structure.materials.containsKey(block.getType())) {
                            structure.materials.put(block.getType(), structure.materials.get(block.getType()) + 1);
                        } else {
                            structure.materials.put(block.getType(), 1);
                        }
                    }
                }
            }
        }

        return structure;
    }
}
