package net.hectus.hectusblockbattles.events;

import net.hectus.hectusblockbattles.match.LocalMatchSingles;
import net.hectus.hectusblockbattles.match.MatchManager;
import net.hectus.hectusblockbattles.playermode.PlayerMode;
import net.hectus.hectusblockbattles.playermode.PlayerModeManager;
import net.hectus.util.color.McColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class BlockBattleEvents  implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerModeManager.initializePlayerMode(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLoc = player.getLocation();

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

        if (localMatchSingles.outOfBounds(playerLoc.getX(), playerLoc.getZ())) {
            localMatchSingles.end(localMatchSingles.getOppositeTurnPlayer(), player,  player, "stepped out of bounds");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (PlayerModeManager.getPlayerMode(event.getPlayer()) != PlayerMode.BLOCK_BATTLES) {
            return;
        }

        event.getPlayer().sendMessage(Component.text(McColor.RED + "You can't break blocks in BlockBattles!"));
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if (event.getAction().isLeftClick()) {
            return;
        }

        LocalMatchSingles localMatchSingles = (LocalMatchSingles) MatchManager.getMatch(player.getWorld());

        if (localMatchSingles == null ||
                PlayerModeManager.getPlayerMode(player) != PlayerMode.BLOCK_BATTLES ||
                player.getInventory().getItemInMainHand().getType().equals(Material.AIR)
        ) {
            return;
        }

        if (localMatchSingles.getCurrentTurnPlayer() != player) {
            localMatchSingles.end(localMatchSingles.getCurrentTurnPlayer(), player, player, "used a item not in their turn");
        }

        if (localMatchSingles.outOfBounds(Objects.requireNonNull(event.getInteractionPoint()).getX(), event.getInteractionPoint().getZ())) {
            localMatchSingles.end(localMatchSingles.getOppositeTurnPlayer(), player,  player, " used an item out of bounds");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // TODO: Replace all this code with proper game play code
        /*
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

        Player currentTurnPlayer = localMatchSingles.getCurrentTurnPlayer();
        Player oppositeTurnPlayer = localMatchSingles.getOppositeTurnPlayer();

        Material lastBlock = localMatchSingles.getLastBlock().getType();
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

        if (!BasicBlocks.blockCheck(material, localMatchSingles.getWarp(), localMatchSingles.getNight())) {
            localMatchSingles.end(oppositeTurnPlayer, placerPlayer, placerPlayer, "placed an illegal block");
            return;
        }

        if (!localMatchSingles.outOfBounds(x, z)) {
            localMatchSingles.end(oppositeTurnPlayer, placerPlayer, placerPlayer,"placed a block out of bounds");
            return;
        }

        gameScore = BasicBlocks.calculateGameScore(gameScore, material, lastBlock, turn, localMatchSingles.getBlockBoosts());

        localMatchSingles.setGameScore(gameScore);

        if (gameScore > 5) {
            localMatchSingles.end(localMatchSingles.getPlayer(true), localMatchSingles.getPlayer(false), localMatchSingles.getPlayer(true), "got a high score");
        } else if (gameScore < -5) {
            localMatchSingles.end(localMatchSingles.getPlayer(false), localMatchSingles.getPlayer(true), localMatchSingles.getPlayer(false), "got a high score");
        }
        */
    }
}
