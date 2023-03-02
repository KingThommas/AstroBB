package net.hectus.hectusblockbattles;

import org.bukkit.Material;

public class basicBlocks {
    private final Material[] groupBlocks = {Material.NETHER_WART_BLOCK, Material.NETHERRACK, Material.NETHER_GOLD_ORE, Material.NETHER_BRICKS, Material.RED_NETHER_BRICKS, Material.NETHERITE_BLOCK, //the first block of a group has a special ability, here it is removing opponents luck boost
                                            Material.SEA_LANTERN, Material.PRISMARINE, Material.PRISMARINE_BRICKS, Material.DARK_PRISMARINE, null, null, //tells the player who's winning
                                            Material.STONE_BRICKS, Material.DIRT, Material.COBBLESTONE, Material.ANDESITE, Material.DIORITE, Material.GRANITE, //
                                            Material.WHITE_SHULKER_BOX, Material.SNOW_BLOCK, Material.WHITE_CONCRETE, Material.LIGHT_GRAY_CONCRETE, Material.GRAY_CONCRETE, Material.BLACK_WOOL};
    private final double[] groupPowers = {2.0, 1.0, 1.5, 2.0, 2.5, 3.2,
                                          1.0, 1.5, 2.0, 2.5, 0.0, 0.0,
                                          2.7, 1.4, 1.9, 2.4, 2.9, 0.0,
                                          2.2, 1.5, 1.8, 2.2, 2.7, 3,2};
    private final int[] groupCounters = {1,-1,-1,-1,0,-1,-1,-1}; //first 4 decide the counters of the first group the next 4 decide the counters of the second group etc.
    public double calculateGameScore(double currentGameScore, Material material, Material lastBlock) {
        return 0;
    }
}
