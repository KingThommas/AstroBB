package net.hectus.hectusblockbattles;

import net.hectus.hectusblockbattles.SpecialAbilities.GlassWalls;
import net.hectus.hectusblockbattles.SpecialAbilities.PumpkinWall;
import net.hectus.hectusblockbattles.SpecialAbilities.Warps;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;

public final class HectusBlockBattles extends JavaPlugin implements Listener {
    MatchData MD = new MatchData();
    PlayerMode PM = new PlayerMode();
    BasicBlocks BB = new BasicBlocks();
    @Override
    public void onEnable() {
        System.out.println("Hectus plugin started.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PM.initializePlayerMode(player);
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        Block block = event.getBlock();
        Material material = block.getType();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        MatchVariables worldVars = MD.getVariables(world);
        Material lastBlock = worldVars.getLastBlock();
        boolean turn = worldVars.getTurn();
        if(Objects.equals(PM.getPlayerMode(player), "blockbattles")) {
            if(worldVars.getPlayerFromTurn()==player
            && matchCheck(x, y, z, world, material, player)
            && BB.blockCheck(material, worldVars.getCurrentWarp(), worldVars.getNight())) {
                double gameScore = BB.calculateGameScore(worldVars.getGameScore(), material, lastBlock, turn, worldVars.getBlockBoosts());
                worldVars.setGameScore(gameScore);
                MD.setVariables(worldVars, world);
                if(gameScore > 5) {
                    matchEnd(worldVars.getPlayer(true), worldVars.getPlayer(false));
                } else if (gameScore < -5) {
                    matchEnd(worldVars.getPlayer(false), worldVars.getPlayer(true));
                }
            } else {
                matchEnd(worldVars.getPlayer(!turn), worldVars.getPlayer(turn));
            }
        }
    }

    public void matchEnd(Player won, Player lost) {
        PM.setPlayerMode("default", won);
        PM.setPlayerMode("default", lost);
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
                    MV.setNight(true);
                    MD.setVariables(MV, world);
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
                    if (Math.random() < 0.3 * MV.getLuckBoost(MV.getTurn())) {
                        MV.setCurrentWarp("nether");
                        MV.setBlockBoosts(0, 1.5);
                        MD.setVariables(MV, world);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
