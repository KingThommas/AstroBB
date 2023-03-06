package net.hectus.hectusblockbattles.SpecialAbilities;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;

//probably will need a lot of imports of classes
//test this
public class PumpkinWall{
    int currentX;
    int currentY;
    int currentZ;
    int step;
    // if returns 0 player misplaced a block and should lose, if returns 1 continue to run this function, if returns 2 player ended placing the pumpkin wall
    public int didPlayerContinuePlacingPumpkin(int x, int y, int z, Material blockType, boolean facingCenter, Player player) {
        if(blockType == Material.CARVED_PUMPKIN) {
            if(y == currentY + (step%2 == 0?0:1) && x == currentX && z == currentZ + (step%2 == 0?-1:0) && facingCenter) {
                step += 1;
                if(step == 14) {
                    onNight(player.getWorld());
                    return 2;
                }
                return 1;
            }
        }
        return 0;
    }
    //activate when player places a block on their turn, if returns true then don't go to the opponents turn, instead every time the player places a block run the function above to check if the player successfully placed the wall
    public boolean didPlayerStartPlacingPumpkin(int x, int y, int z, Material blockType, Player player) {
        Directional blockDirectional = (Directional) player.getWorld().getBlockAt(x,y,z).getBlockData();
        if(blockType == Material.CARVED_PUMPKIN && x < 5 && blockDirectional.getFacing() == BlockFace.EAST) {
            currentX = x;
            currentY = y;
            currentZ = z;
            step = 1;
            return true;
        }
        return false;
    }
    public void onNight(World world) {
        for (int i = -48; i < 48; i++) {
            for (int j = -48; j < 48; j++) {
                for (int k = -48; k < 48; k++) {
                    if(world.getBlockAt(i, j, k).getType() == Material.LIGHT_BLUE_CONCRETE) {
                        world.getBlockAt(i, j, k).setType(Material.BLACK_CONCRETE);
                    }
                    if(world.getBlockAt(i, j, k).getType() == Material.BLUE_CONCRETE) {
                        world.getBlockAt(i, j, k).setType(Material.BLACK_CONCRETE);
                    }
                    if(world.getBlockAt(i, j, k).getType() == Material.YELLOW_CONCRETE) {
                        world.getBlockAt(i, j, k).setType(Material.LIGHT_GRAY_CONCRETE);
                    }
                }
            }
        }
        world.setTime(23000);
    }
}
