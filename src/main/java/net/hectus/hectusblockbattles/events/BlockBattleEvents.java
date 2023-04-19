package net.hectus.hectusblockbattles.events;

import net.hectus.hectusblockbattles.BasicBlocks;
import net.hectus.hectusblockbattles.match.LocalMatchSingles;
import net.hectus.hectusblockbattles.match.MatchManager;
import net.hectus.hectusblockbattles.playermode.PlayerMode;
import net.hectus.hectusblockbattles.playermode.PlayerModeManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class BlockBattleEvents  implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerModeManager.initializePlayerMode(player);
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLoc = player.getLocation();
        int x = playerLoc.getBlockX();
        int z = playerLoc.getBlockZ();

        LocalMatchSingles localMatchSingles = MatchManager.getMatch(player.getWorld());
        if (localMatchSingles == null) {
            return;
        }

        if (PlayerModeManager.getPlayerMode(player) != PlayerMode.BLOCK_BATTLES) {
            return;
        }

        if (x < 0 || x >= 9 || z >= 9 || z < 0) {
            localMatchSingles.end(localMatchSingles.getOppositeTurnPlayer(), player,  player, "stepped out of bounds");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player placerPlayer = event.getPlayer();

        if (PlayerModeManager.getPlayerMode(placerPlayer) != PlayerMode.BLOCK_BATTLES) {
            return;
        }

        Block block = event.getBlock();
        Material material = block.getType();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        LocalMatchSingles localMatchSingles = MatchManager.getMatch(placerPlayer.getWorld());
        if (localMatchSingles == null) {
            localMatchSingles = new LocalMatchSingles();
            MatchManager.addMatch(localMatchSingles, placerPlayer.getWorld());
        }

        Player currentTurnPlayer = localMatchSingles.getCurrentTurnPlayer();
        Player oppositeTurnPlayer = localMatchSingles.getOppositeTurnPlayer();

        Material lastBlock = localMatchSingles.getLastBlock();
        boolean turn = localMatchSingles.getTurn();
        double gameScore = localMatchSingles.getGameScore();
        if (currentTurnPlayer != placerPlayer) {
            localMatchSingles.end(oppositeTurnPlayer, placerPlayer, placerPlayer,"placed a block not in their turn");
            return;
        }
        if (!localMatchSingles.check(x, y, z, material, placerPlayer)) {
            localMatchSingles.end(oppositeTurnPlayer, placerPlayer, placerPlayer, "placed a block incorrectly");
            return;
        }
        if (!BasicBlocks.blockCheck(material, localMatchSingles.getCurrentWarp(), localMatchSingles.getNight())) {
            localMatchSingles.end(oppositeTurnPlayer, placerPlayer, placerPlayer, "placed an illegal block");
            return;
        }
        if (!(9 >= x && x >= 0 && 9 >= z && z >= 0)) {
            localMatchSingles.end(oppositeTurnPlayer, placerPlayer, placerPlayer,"placed a block out of bounds");
            return;
        }
        if (localMatchSingles.getTurnJustStarted()) {
            gameScore = BasicBlocks.calculateGameScore(gameScore, material, lastBlock, turn, localMatchSingles.getBlockBoosts());
        }
        localMatchSingles.setGameScore(gameScore);
        if (gameScore > 5) {
            localMatchSingles.end(localMatchSingles.getPlayer(true), localMatchSingles.getPlayer(false), localMatchSingles.getPlayer(true), "got a high score");
        } else if (gameScore < -5) {
            localMatchSingles.end(localMatchSingles.getPlayer(false), localMatchSingles.getPlayer(true), localMatchSingles.getPlayer(false), "got a high score");
        }
    }

}
