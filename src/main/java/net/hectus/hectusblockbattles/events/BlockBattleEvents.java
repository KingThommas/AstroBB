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
import org.bukkit.event.block.BlockBreakEvent;
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

        LocalMatchSingles localMatchSingles = (LocalMatchSingles) MatchManager.getMatch(player.getWorld());
        if (localMatchSingles == null) {
            return;
        }

        if (PlayerModeManager.getPlayerMode(player) != PlayerMode.BLOCK_BATTLES) {
            return;
        }

        if (localMatchSingles.getCurrentTurnPlayer() != player && event.hasChangedBlock()) {
            localMatchSingles.end(localMatchSingles.getCurrentTurnPlayer(), player, player, "moved out of turn");
        }

        if (!localMatchSingles.checkBounds(x, z)) {
            localMatchSingles.end(localMatchSingles.getOppositeTurnPlayer(), player,  player, "stepped out of bounds");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (PlayerModeManager.getPlayerMode(event.getPlayer()) != PlayerMode.BLOCK_BATTLES) {
            return;
        }

        event.getPlayer().sendMessage("You can't break blocks in Block Battles!");
        event.setCancelled(true);
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

        LocalMatchSingles localMatchSingles = (LocalMatchSingles) MatchManager.getMatch(placerPlayer.getWorld());
        if (localMatchSingles == null) {
            localMatchSingles = new LocalMatchSingles();
            MatchManager.addMatch(localMatchSingles);
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
        if (!localMatchSingles.checkBounds(x, z)) {
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
