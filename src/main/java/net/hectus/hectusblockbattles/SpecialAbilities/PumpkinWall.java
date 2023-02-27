package net.hectus.hectusblockbattles.SpecialAbilities;

import org.bukkit.Material;
import org.bukkit.World;

//probably will need a lot of imports of classes
//test this
public class PumpkinWall {
    int currentX;
    int currentY;
    int currentZ;
    int step;
    int correctDirection;
    // if returns 0 player misplaced a block and should lose, if returns 1 continue to run this function, if returns 2 player ended placing the pumpkin wall
    public int didPlayerContinuePlacing(int x, int y, int z, Material blockType, boolean facingCenter, int direction, boolean placedBlockingTheSun, World world) { //direction 0: wall facing positive x; 1: facing negative x; 2: facing pos z; 3: neg z;
        if(blockType==Material.CARVED_PUMPKIN&&placedBlockingTheSun&&correctDirection==direction) {
            if(y==currentY+(step%2==0?0:1)&&x==currentX+(direction>1&&step%2==0?direction==2?1:-1:0)&&z==currentZ+(direction<2?direction==0?1:-1:0)&&facingCenter) {
                step+=1;
                if(step==10) {
                    onNight(world);
                    return 2;
                }
                return 1;
            }
        }
        return 0;
    }
    //activate when player places a block on their turn, if returns true then go to the opponents turn, instead every time the player places a block run the function above
    public boolean didPlayerStartPlacing(int x, int y, int z, Material blockType, boolean facingCenter, int direction, boolean placedBlockingTheSun) {
        if(blockType==Material.CARVED_PUMPKIN&&facingCenter&&placedBlockingTheSun) {
            currentX = x;
            currentY = y;
            currentZ = z;
            step = 1;
            correctDirection = direction;
            return true;
        }
        return false;
    }
    public void onNight(World world) {
        for (int i = 0; i < 48; i++) {
            for (int j = 0; j < 48; j++) {
                for (int k = 0; k < 48; k++) {
                    if(world.getBlockAt(i,j,k).getType()==Material.LIGHT_BLUE_CONCRETE) {
                        world.getBlockAt(i,j,k).setType(Material.BLACK_CONCRETE);
                    }
                }
            }
        }
    }
}
