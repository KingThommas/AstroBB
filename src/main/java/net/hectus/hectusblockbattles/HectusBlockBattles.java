package net.hectus.hectusblockbattles;

import net.hectus.hectusblockbattles.SpecialAbilities.GlassWalls;
import net.hectus.hectusblockbattles.SpecialAbilities.PumpkinWall;
import net.hectus.hectusblockbattles.SpecialAbilities.Warps;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
        if (material == Material.CARVED_PUMPKIN) {
            if (tjs && PW.didPlayerStartPlacingPumpkin(x, y, z, material, true, player)) {
                return true;
            } else if (!tjs) {
                int result = PW.didPlayerContinuePlacingPumpkin(x, y, z, material, true, player);
                if (result == 1) {
                    return true;
                } else if (result == 2) {
                    // do things after player placed a pumpkin wall maybe a luck boost setting to night is already done in the class
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
                } else if (result == Material.RED_CONCRETE) {
                    return false;
                } else {
                    // do things after a glass wall is placed
                    return true;
                }
            }
        }
        return false;
    }
}
