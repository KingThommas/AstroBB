package net.hectus.hectusblockbattles.maps;

import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

public class MapWorldGenerator extends ChunkGenerator {

        // Generates void world with bedrock layer at the bottom.
        // Used for loaded maps to generate the world around.
        // TODO: Maybe change this to use new API, because this is deprecated.
        @Override
        public @NotNull ChunkData generateChunkData(org.bukkit.@NotNull World world, java.util.@NotNull Random random, int x, int z, ChunkGenerator.@NotNull BiomeGrid biome) {
                ChunkData chunk = createChunkData(world);
                chunk.setRegion(0, 0, 0, 16, 1, 16, org.bukkit.Material.BEDROCK);
                return chunk;
        }
}
