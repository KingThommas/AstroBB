package net.hectus.hectusblockbattles.Structures;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;

import java.util.HashSet;
import java.util.Set;

public class Structure {
    public final String name;
    public final HashSet<Material> materials;
    public transient Set<Block> blocks; // Represents the blocks in a world for manipulation
    public final HashSet<PlacedBlock> placedBlocks; // Set of blocks used for comparisons
    public final Boundary boundary;

    public Structure(String name, HashSet<Material> materials, HashSet<PlacedBlock> placedBlocks, Set<Block> blocks, Boundary boundary) {
        this.name = name;
        this.materials = materials;
        this.placedBlocks = placedBlocks;
        this.blocks = blocks;
        this.boundary = boundary;
    }

    public Structure(World world, String name, int x1, int y1, int z1, int x2, int y2, int z2) {
        this(name, new HashSet<>(), new HashSet<>(), new HashSet<>(), new Boundary());

        BoundingBox tempBox = new BoundingBox(x1, y1, z1, x2, y2, z2);

        for (int x = (int) tempBox.getMinX(); x <= tempBox.getMaxX(); x++) {
            for (int z = (int) tempBox.getMinZ(); z <= tempBox.getMaxZ(); z++) {
                for (int y = (int) tempBox.getMinY(); y <= tempBox.getMaxY(); y++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.AIR)
                        continue;

                    blocks.add(block);
                    materials.add(block.getType());
                    placedBlocks.add(new PlacedBlock(block.getType(), (int) (x - tempBox.getMinX()), (int) (y - tempBox.getMinY()), (int) (z - tempBox.getMinZ())));
                }
            }
        }

        boundary.setMaxX((int) (tempBox.getMaxX() - tempBox.getMinX()));
        boundary.setMaxY((int) (tempBox.getMaxY() - tempBox.getMinY()));
        boundary.setMaxZ((int) (tempBox.getMaxZ() - tempBox.getMinZ()));
    }

    public Structure(String name, Set<Block> blocks) {
        this(name, new HashSet<>(), new HashSet<>(), blocks, new Boundary());
        int minX, minY, minZ, maxX, maxY, maxZ;
        minX = maxX = blocks.iterator().next().getX();
        minY = maxY = blocks.iterator().next().getY();
        minZ = maxZ = blocks.iterator().next().getZ();

        HashSet<PlacedBlock> tempPlacedBlocks = new HashSet<>();
        for (Block block : blocks) {
            if (block.getType() == Material.AIR)
                continue;
            getMaterials().add(block.getType());

            int x = block.getX(), y = block.getY(), z = block.getZ();
            tempPlacedBlocks.add(new PlacedBlock(block.getType(), x, y, z));
            minX = Math.min(x, minX);
            minY = Math.min(y, minY);
            minZ = Math.min(z, minZ);
            maxX = Math.max(x, maxX);
            maxY = Math.max(y, maxY);
            maxZ = Math.max(z, maxZ);
        }

        getBoundary().setMaxX(maxX - minX);
        getBoundary().setMaxY(maxY - minY);
        getBoundary().setMaxZ(maxZ - minZ);

        for (PlacedBlock block : tempPlacedBlocks) {
            getPlacedBlocks().add(new PlacedBlock(block.material(), block.x() - minX, block.y() - minY, block.z() - minZ));
        }
    }

    /**
     * @param toCompare The structure to compare
     * @return          Whether the structure contains the structure toCompare
     */
    public boolean hasSubset(Structure toCompare) {
        // Material comparison
        if (!materials.containsAll(toCompare.materials)) {
            return false;
        }

        // Compare for all 4 rotations
        Set<Structure> rotations = new HashSet<>();
        rotations.add(toCompare);
        for (int i = 0; i < 3; i++) {
            Structure rotated = toCompare.rotateBy90();
            rotations.add(rotated);
            toCompare = rotated;
        }

        for (Structure structure : rotations) {
            // Comparison of offsets in all 3 dimensions
            int dx = getBoundary().getMaxX() - structure.getBoundary().getMaxX();
            int dz = getBoundary().getMaxZ() - structure.getBoundary().getMaxZ();
            int dy = getBoundary().getMaxY() - structure.getBoundary().getMaxY();
            if (dx < 0 || dy < 0 || dz < 0) {
                continue;
            }

            // Block by block comparison
            for (int x = 0; x <= dx; x++) {
                for (int z = 0; z <= dz; z++) {
                    for (int y = 0; y <= dy; y++) {
                        if (compareWithOffset(getPlacedBlocks(), structure.getPlacedBlocks(), x, y, z)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public String getName() {
        return name;
    }

    public HashSet<Material> getMaterials() {
        return materials;
    }

    public Set<Block> getBlocks() {
        return blocks;
    }

    public HashSet<PlacedBlock> getPlacedBlocks() {
        return placedBlocks;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public record PlacedBlock(Material material, int x, int y, int z) {}

    public static class Boundary {
        int maxX;
        int maxY;
        int maxZ;

        public Boundary(int maxX, int maxY, int maxZ) {
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
        }

        public Boundary() {
            this(0, 0, 0);
        }

        public int getMaxX() {
            return maxX;
        }

        public void setMaxX(int maxX) {
            this.maxX = maxX;
        }

        public int getMaxY() {
            return maxY;
        }

        public void setMaxY(int maxY) {
            this.maxY = maxY;
        }

        public int getMaxZ() {
            return maxZ;
        }

        public void setMaxZ(int maxZ) {
            this.maxZ = maxZ;
        }
    }

    private boolean compareWithOffset(Set<PlacedBlock> original, Set<PlacedBlock> toCompare, int dx, int dy, int dz) {
        for (PlacedBlock block : toCompare) {
            PlacedBlock offsetBlock = new PlacedBlock(block.material(), block.x() + dx, block.y() + dy, block.z() + dz);
            if (!(original.contains(offsetBlock))) {
                return false;
            }
        }
        return true;
    }

    private Structure rotateBy90() {
        int xOffset = getBoundary().getMaxZ();

        HashSet<PlacedBlock> newPlacedBlocks = new HashSet<>();
        for (PlacedBlock block : getPlacedBlocks()) {
            PlacedBlock newBlock = new PlacedBlock(block.material(), (-1 * block.z()) + xOffset, block.y(), block.x());
            newPlacedBlocks.add(newBlock);
        }

        return new Structure(getName(), getMaterials(), newPlacedBlocks, getBlocks(),  new Boundary(getBoundary().getMaxZ(), getBoundary().getMaxY(), getBoundary().getMaxX()));
    }
}
