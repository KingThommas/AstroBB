package net.hectus.hectusblockbattles;

import org.bukkit.Material;

public class basicBlocks {
    private final Material[] groupBlocks = {Material.NETHER_WART_BLOCK, Material.NETHERRACK, Material.NETHER_GOLD_ORE, Material.NETHER_BRICKS, Material.RED_NETHER_BRICKS, Material.NETHERITE_BLOCK, //the first block of a group has a special ability, here it is removing opponents luck boost
                                            Material.SEA_LANTERN, Material.PRISMARINE, Material.PRISMARINE_BRICKS, Material.DARK_PRISMARINE, null, null, //tells the player who's winning
                                            Material.STONE_BRICKS, Material.STONE, Material.COBBLESTONE, Material.ANDESITE, Material.DIORITE, Material.GRANITE, //
                                            Material.WHITE_SHULKER_BOX, Material.SNOW_BLOCK, Material.WHITE_CONCRETE, Material.LIGHT_GRAY_CONCRETE, Material.GRAY_CONCRETE, Material.BLACK_WOOL,
                                            Material.SNOW_BLOCK, Material.ICE, Material.PACKED_ICE, Material.BLUE_ICE, null, null,
                                            Material.OAK_WOOD, Material.DIRT, Material.MOSS_BLOCK, Material.SMALL_DRIPLEAF, Material.BIG_DRIPLEAF, null};
    private final double[] groupPowers = {2.0, 1.0, 1.5, 2.0, 2.5, 3.2,
                                          1.0, 1.5, 2.0, 2.5, 0.0, 0.0,
                                          2.5, 1.5, 2.0, 2.5, 3.0, 0.0,
                                          2.0, 1.0, 1.5, 2.0, 2.5, 3.2,
                                          1.5, 1.5, 2.0, 2.5, 0.0, 0.0,
                                          2.0, 1.0, 1.5, 2.0, 3.0, 0.0};
    private final int[] groupCounters = {4,5,-1,-1,
                                         0,2,-1,-1,
                                         3,0,-1,-1,
                                         5,1,-1,-1,
                                         1,3,-1,-1,
                                         2,4,-1,-1}; //first 4 decide the counters of the first group the next 4 decide the counters of the second group etc.
    public double calculateGameScore(double currentGameScore, Material material, Material lastBlock, boolean turn) {
        double power = -1;
        int group = -1;
        int counterGroup = -1;
        boolean canCounter = false;
        for (int i = 0; i < groupBlocks.length; i++) {
            if(groupBlocks[i]==material) {
                power = groupPowers[i];
                group = (int) Math.floor((i+1)/6);
            }
            if(groupBlocks[i]==lastBlock) {
                counterGroup = (int) Math.floor((i+1)/6);
            }
        }
        for (int i = group*4; i<group*4+4; i++) {
            if (groupCounters[i]==counterGroup) {
                canCounter = true;
            }
        }
        boolean isCurrentPlayerLosing = (currentGameScore > 0 ? 1 : -1) != (turn ? 1 : -1);
        if(canCounter) {
            return currentGameScore + power - (isCurrentPlayerLosing ? currentGameScore / 4 : 0);
        }
        return isCurrentPlayerLosing ? currentGameScore * 1.5 : currentGameScore;
    }
}
