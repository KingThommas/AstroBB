package net.hectus.hectusblockbattles.specialabilities;

import org.bukkit.Material;

public class GlassWalls{
    int currentX;
    int currentY;
    int currentZ;
    int step;
    // same as pumpkin wall
    public Material didPlayerContinuePlacingGlass(int x, int y, int z, Material blockType) {
        if(blockType == Material.GLASS || blockType.name().startsWith("STAINED_GLASS")) {
            if(y == currentY + (step % 2 == 0 ? 0 : 1) && ( x == currentX + ( step % 2 == 1 ? 1 : 0 ) ^ z == currentZ + ( step % 2 == 0 ? 1 : 0 ) ^ x == currentX + ( step % 2 == 1 ? -1 : 0 ) ^ z == currentZ + ( step % 2 == 0 ? -1 : 0 ) ) ) {
                step += 1;
                if(step == 12) {
                    return blockType; //player successfully placed the wall and returned the glass block used
                }
                return Material.WHITE_CONCRETE; //continue checking
            }
        }
        return Material.RED_CONCRETE; //player failed
    }
    public boolean didPlayerStartPlacingGlass(int x, int y, int z, Material blockType) {
        if(blockType == Material.GLASS || blockType.name().startsWith("STAINED_GLASS")) {
            currentX = x;
            currentY = y;
            currentZ = z;
            step = 1;
            return true;
        }
        return false;
    }
}
