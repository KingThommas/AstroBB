package net.hectus.hectusblockbattles.SpecialAbilities;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

//probably will need a lot of imports of classes
//test this
public class PumpkinWall {
    int currentX;
    int currentY;
    int currentZ;
    int step;
    // if returns 0 player misplaced a block and should lose, if returns 1 continue to run this function, if returns 2 player ended placing the pumpkin wall
    public int didPlayerContinuePlacing(int x, int y, int z, Material blockType, boolean facingCenter, Player player) { //direction 0: wall facing positive x; 1: facing negative x; 2: facing pos z; 3: neg z;
        if(blockType == Material.CARVED_PUMPKIN && player.getLocation().getYaw() > 50 && player.getLocation().getYaw() < 140) {
            if(y == currentY + (step%2 == 0?0:1) && x == currentX && z == currentZ + (step%2 == 0?-1:0) && facingCenter) {
                step += 1;
                if(step == 12) {
                    onNight(player.getWorld());
                    return 2;
                }
                return 1;
            }
        }
        return 0;
    }
    // Activate when player places a block on their turn, if returns true then go to the opponents turn, instead every time the player places a block run the function above
    public boolean didPlayerStartPlacing(int x, int y, int z, Material blockType, boolean facingCenter, Player player) {
        if(blockType == Material.CARVED_PUMPKIN && facingCenter && player.getLocation().getYaw() > 50 && player.getLocation().getYaw() < 140) {
            currentX = x;
            currentY = y;
            currentZ = z;
            step = 1;
            return true;
        }
        return false;
    }
    public void onNight(World world) {
        for (int i = 0; i < 48; i++) {
            for (int j = 0; j < 48; j++) {
                for (int k = 0; k < 48; k++) {
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
    }
}
