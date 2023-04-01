package net.hectus.hectusblockbattles.Events;

import net.hectus.hectusblockbattles.BasicBlocks;
import net.hectus.hectusblockbattles.Match.Match;
import net.hectus.hectusblockbattles.Match.MatchManager;
import net.hectus.hectusblockbattles.PlayerMode.PlayerMode;
import net.hectus.hectusblockbattles.PlayerMode.PlayerModeManager;
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

        Match match = MatchManager.getMatch(player.getWorld());
        if (match == null) {
            return;
        }

        if (PlayerModeManager.getPlayerMode(player) != PlayerMode.BLOCKBATTLES) {
            return;
        }

        if (x < 0 || x >= 9 || z >= 9 || z < 0) {
            match.end(match.getOppositeTurnPlayer(), player,  player, "stepped out of bounds");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player placerPlayer = event.getPlayer();

        if (PlayerModeManager.getPlayerMode(placerPlayer) != PlayerMode.BLOCKBATTLES) {
            return;
        }

        Block block = event.getBlock();
        Material material = block.getType();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        Match match = MatchManager.getMatch(placerPlayer.getWorld());
        if (match == null) {
            match = new Match();
            MatchManager.addMatch(match, placerPlayer.getWorld());
        }

        Player currentTurnPlayer = match.getCurrentTurnPlayer();
        Player oppositeTurnPlayer = match.getOppositeTurnPlayer();

        Material lastBlock = match.getLastBlock();
        boolean turn = match.getTurn();
        double gameScore = match.getGameScore();
        if (currentTurnPlayer != placerPlayer) {
            match.end(oppositeTurnPlayer, placerPlayer, placerPlayer,"placed a block not in their turn");
            return;
        }
        if (!match.check(x, y, z, material, placerPlayer)) {
            match.end(oppositeTurnPlayer, placerPlayer, placerPlayer, "placed a block incorrectly");
            return;
        }
        if (!BasicBlocks.blockCheck(material, match.getCurrentWarp(), match.getNight())) {
            match.end(oppositeTurnPlayer, placerPlayer, placerPlayer, "placed an illegal block");
            return;
        }
        if (!(9 >= x && x >= 0 && 9 >= z && z >= 0)) {
            match.end(oppositeTurnPlayer, placerPlayer, placerPlayer,"placed a block out of bounds");
            return;
        }
        if (match.getTurnJustStarted()) {
            gameScore = BasicBlocks.calculateGameScore(gameScore, material, lastBlock, turn, match.getBlockBoosts());
        }
        match.setGameScore(gameScore);
        if (gameScore > 5) {
            match.end(match.getPlayer(true), match.getPlayer(false), match.getPlayer(true), "got a high score");
        } else if (gameScore < -5) {
            match.end(match.getPlayer(false), match.getPlayer(true), match.getPlayer(false), "got a high score");
        }
    }

}
