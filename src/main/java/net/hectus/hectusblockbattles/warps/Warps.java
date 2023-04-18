package net.hectus.hectusblockbattles.warps;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Warps {
    int pos;
    int step;
    Material outerMaterial;
    Material[] outerBlocks = {Material.MAGMA_BLOCK};
    Material[] innerBlocks = {Material.SHROOMLIGHT};
    public boolean didPlayerStartPlacing(int x, int y, int z, Material material, Player player) {
        Location pl = player.getLocation();
        for (Material m : innerBlocks) {
            if (m == material && (pl.getBlockX() + 1 == x ^ pl.getBlockX() - 1 == x ^ pl.getBlockZ() + 1 == z ^ pl.getBlockZ() - 1 == z) && pl.getBlockY() - 1 == y) {
                pos = pl.getBlockX() + 1 == x ? 0 : pl.getBlockZ() + 1 == z ? 1 : pl.getBlockX() - 1 == x ? 2 : 3;
                step = 1;
                outerMaterial = material;
                return true;
            }
        }
        return false;
    }
    public Material[] didPlayerContinuePlacing(int x, int y, int z, Material material, Player player) {
        Location pl = player.getLocation();
        if(step==4) {
            for (int i = 0; i< innerBlocks.length; i++) {
                if (outerMaterial == outerBlocks[i] && material == innerBlocks[i] && x == pl.getBlockX() && z == pl.getBlockZ() && y == pl.getBlockY() - 2) {
                    return new Material[]{outerMaterial, material}; //first is outer material second is the material in the middle
                }
            }
        }
        else if(material == outerMaterial && (pos + 1 == 4 ? 0 : pos + 1) == ( pl.getBlockX() + 1 == x ? 0 : pl.getBlockZ() + 1 == z ? 1 : pl.getBlockX() - 1 == x ? 2 : 3)) {
            return new Material[]{Material.WHITE_CONCRETE}; //white concrete: continue checking if player finishes building the warp
        }
        return new Material[]{Material.RED_CONCRETE}; //red concrete: player failed to place a block
    }
}
