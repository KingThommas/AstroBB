package net.hectus.hectusblockbattles;

import net.hectus.hectusblockbattles.SpecialAbilities.GlassWalls;
import net.hectus.hectusblockbattles.SpecialAbilities.PumpkinWall;
import net.hectus.hectusblockbattles.SpecialAbilities.Warps;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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
        getServer().getPluginManager().registerEvents(this, this);
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
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLoc = player.getLocation();
        int x = playerLoc.getBlockX();
        int z = playerLoc.getBlockZ();
        MatchVariables MV = MD.getVariables(player.getWorld());
        if (Objects.equals(PM.getPlayerMode(player), "blockbattles")) {
            if (9 >= x && x >= 0 && 9 >= z && z >= 0) {
                matchEnd(MV.getPlayer(!MV.getTurnFromPlayer(player)), player, "stepped out of bounds");
            }
        }
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
        MatchVariables MV = MD.getVariables(world);
        Material lastBlock = MV.getLastBlock();
        boolean turn = MV.getTurn();
        Player notCurrentTurnPlayer = MV.getPlayer(!turn);
        double gameScore = MV.getGameScore();
        if(Objects.equals(PM.getPlayerMode(player), "blockbattles")) {
            if (!(MV.getCurrentTurnPlayer() == player)) {
                matchEnd(MV.getCurrentTurnPlayer(), player, "placed a block not in their turn");
                return;
            }
            if (!matchCheck(x, y, z, world, material, player)) {
                matchEnd(notCurrentTurnPlayer, player, "placed a block incorrectly");
                return;
            }
            if (!BB.blockCheck(material, MV.getCurrentWarp(), MV.getNight())) {
                matchEnd(notCurrentTurnPlayer, player, "placed an illegal block");
                return;
            }
            if (!(9 >= x && x >= 0 && 9 >= z && z >= 0)) {
                matchEnd(notCurrentTurnPlayer, player, "placed a block out of bounds");
                return;
            }
            if (MV.getTurnJustStarted()) {
                gameScore = BB.calculateGameScore(gameScore, material, lastBlock, turn, MV.getBlockBoosts());
            }
            MV.setGameScore(gameScore);
            MD.setVariables(MV, world);
            if (gameScore > 5) {
                matchEnd(MV.getPlayer(true), MV.getPlayer(false), "got a high score");
            } else if (gameScore < -5) {
                matchEnd(MV.getPlayer(false), MV.getPlayer(true), "got a high score");
            }
        }
    }

    public void matchEnd(Player won, Player lost, String cause) {
        PM.setPlayerMode("default", won);
        PM.setPlayerMode("default", lost);
    }

    public boolean matchCheck(int x, int y, int z, World world, Material material, Player player) { //VERY NOT FINISHED!!!
        MatchVariables MV = MD.getVariables(world);
        boolean tjs = MV.getTurnJustStarted();
        PumpkinWall PW = MV.getPumpkinWall();
        GlassWalls GW = MV.getGlassWalls();
        Warps warps = MV.getWarps();
        boolean turn = MV.getCurrentTurnBoolean();
        MV.setTurn(!MV.getTurn());
        if (material == Material.CARVED_PUMPKIN) {
            if (tjs && PW.didPlayerStartPlacingPumpkin(x, y, z, material, player)) {
                MV.setTurnJustStarted(false);
                return true;
            } else if (!tjs) {
                int result = PW.didPlayerContinuePlacingPumpkin(x, y, z, material, true, player);
                if (result == 1) {
                    MV.setTurn(MV.getTurn());
                    MD.setVariables(MV, world);
                    return true;
                } else if (result == 2) {
                    MV.setLuckBoost(turn, MV.getLuckBoost(turn)+(MV.getLuckBoost(turn)*0.2));
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
                    MV.setTurn(MV.getTurn());
                    MD.setVariables(MV, world);
                    return true;
                } else if (result != Material.RED_CONCRETE) {
                    if(result == Material.GREEN_STAINED_GLASS) {
                        MV.setLuckBoost(turn, MV.getLuckBoost(turn)*0.15);
                    } else if (result == Material.RED_STAINED_GLASS) {
                        MV.setLuckBoost(!turn, MV.getLuckBoost(!turn) - (MV.getLuckBoost(!turn) * 0.1));
                    }
                    MD.setVariables(MV, world);
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
                    MV.setTurn(MV.getTurn());
                    MD.setVariables(MV, world);
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
