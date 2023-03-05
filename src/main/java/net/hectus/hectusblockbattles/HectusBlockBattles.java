package net.hectus.hectusblockbattles;

import net.hectus.hectusblockbattles.SpecialAbilities.GlassWalls;
import net.hectus.hectusblockbattles.SpecialAbilities.PumpkinWall;
import net.hectus.hectusblockbattles.SpecialAbilities.Warps;
import net.hectus.hectusblockbattles.MatchData;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;

public final class HectusBlockBattles extends JavaPlugin implements Listener {
    MatchData MD = new MatchData();
    @Override
    public void onEnable() {
        System.out.println("Heheheha!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean matchCheck(int x, int y, int z, World world, Material material, Player player) { //VERY NOT FINISHED!!!
        MatchVariables MV = MD.getVariables(world);
        boolean tjs = MV.didTurnJustStarted();
        PumpkinWall PW = MV.getPumpkinWall();
        GlassWalls GW = MV.getGlassWalls();
        Warps warps = MV.getWarps();
        boolean currentPlayer = MV.getCurrentTurnBoolean();
        if (material == Material.CARVED_PUMPKIN) {
            if (tjs && PW.didPlayerStartPlacingPumpkin(x, y, z, material, true, player)) {
                MV.setTurnJustStarted(false);
                return true;
            } else if (!tjs) {
                int result = PW.didPlayerContinuePlacingPumpkin(x, y, z, material, true, player);
                if (result == 1) {
                    return true;
                } else if (result == 2) {
                    MV.setLuckBoost(currentPlayer, MV.getLuckBoost(currentPlayer)+(MV.getLuckBoost(currentPlayer)*0.2));
                    return true;
                }
            }
        } else if (material == Material.GLASS || material.name().startsWith("STAINED_GLASS")) {
            if (tjs && GW.didPlayerStartPlacingGlass(x, y, z, material)) {
                return true;
            } else if (!tjs) {
                Material result = GW.didPlayerContinuePlacingGlass(x, y, z, material);
                if (result == Material.WHITE_CONCRETE) {
                    return true;
                } else if (result != Material.RED_CONCRETE) {
                    // do things after a glass wall is placed
                    return true;
                }
            }
        }
        else if (material == Material.NETHERRACK || material == Material.SHROOMLIGHT) {
            if (tjs && warps.didPlayerStartPlacing(x, y, z, material, player)) {
                return true;
            } else if (!tjs) {
                Material[] result = warps.didPlayerContinuePlacing(x,y,z,material,player);
                if (Arrays.equals(result, new Material[]{Material.WHITE_CONCRETE})) {
                    return true;
                } else if (!Arrays.equals(result, new Material[]{Material.RED_CONCRETE})) {
                    //do things after nether warp is placed
                    return true;
                }
            }
        }
        MD.setVariables(MV, world);
        return false;
    }
}
