package net.hectus.hectusblockbattles.events;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import io.papermc.paper.event.player.PlayerNameEntityEvent;
import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.hectusblockbattles.turn.Turn;
import net.hectus.hectusblockbattles.turn.TurnInfo;
import net.hectus.hectusblockbattles.Cord;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.TNTPrimeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlayerEvents implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerNameEntity(@NotNull PlayerNameEntityEvent event) {
        String name = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(event.getName())).toLowerCase();
        if (name.contains("dinnerbone") || name.contains("grumm")) {
            turn(Turn.DINNERBONE, event.getPlayer(), Cord.of(event.getEntity().getLocation()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTNTPrime(@NotNull TNTPrimeEvent event) {
        event.setCancelled(true);
        if (event.getCause() == TNTPrimeEvent.PrimeCause.PLAYER) {
            turn(Turn.TNT, (Player) event.getPrimingEntity(), Cord.of(event.getBlock().getLocation()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerItemConsume(@NotNull PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (event.getItem().getType() == Material.ENCHANTED_GOLDEN_APPLE) {
            player.sendMessage(Component.text("Super Apple."));
            turn(Turn.OP_GAP, player, Cord.of(player.getLocation()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPortalCreate(@NotNull PortalCreateEvent event) {
        if (event.getReason() == PortalCreateEvent.CreateReason.FIRE) {
            turn(Turn.NETHER_PORTAL_IGNITE, (Player) event.getEntity(), Cord.of(Objects.requireNonNull(event.getEntity()).getLocation()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLaunchProjectile(@NotNull PlayerLaunchProjectileEvent event) {
        if (event.getProjectile().getType() == EntityType.TRIDENT) {
            if (((Trident) event.getProjectile()).hasGlint()) {
                turn(Turn.LIGHTNING_TRIDENT, event.getPlayer(), Cord.of(event.getPlayer().getLocation()));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        if (!Match.hasStarted) return;

        Player p = event.getPlayer();

        if (!Match.algorithm.isPlacer(p) && !Match.netherPortalAwaitIgnite) {
            p.sendMessage(Component.text(McColor.RED + "It isn't your turn right now!"));
            event.setCancelled(true);
            return;
        }

        Block b = event.getBlock();
        Cord c = new Cord(b.getX(), b.getY(), b.getZ());

        switch (b.getType().name()) {
            case "PURPLE_WOOL" -> turn(Turn.PURPLE_WOOL, p, c);
            case "POWDER_SNOW" -> turn(Turn.POWDER_SNOW, p, c);
            case "SPRUCE_TRAPDOOR" -> turn(Turn.SPRUCE_TRAPDOOR, p, c);
            case "SEA_PICKLE" -> {
                if (b instanceof SeaPickle pickle) {
                    if (pickle.getPickles() >= 4) turn(Turn.SEA_PICKLE_STACK, p, c);
                }
            }

            default -> {
                Structure.BlockData blockData = new Structure.BlockData(b.getType(), b.getX(), b.getY(), b.getZ(), Structure.blockFace(b), Structure.blockBound(b), Structure.isOpen(b));
                Match.algorithm.addBlock(blockData);
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!Match.hasStarted) return;

        Player p = event.getPlayer();
        if (!Match.algorithm.isPlacer(p)) {
            p.sendMessage(Component.text(McColor.RED + "It isn't your turn right now!"));
            event.setCancelled(true);
            return;
        }

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            turn(Turn.ENDER_PEARL_TP, p, Cord.of(event.getTo()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        if (!Match.hasStarted) return;

        Player player = event.getPlayer();

        if (event.hasChangedPosition() && !player.isOp()) {
            Match.getPlayer(player).setDefended(false);

            if (!Match.algorithm.isPlacer(player) || !Match.getPlayer(player).canMove()) {
                Match.win(Match.getOpposite(Match.getPlayer(player)));
            }

            double xDiff = Math.abs(event.getTo().x() - Match.currentWarp.middle.x());
            double zDiff = Math.abs(event.getTo().z() - Match.currentWarp.middle.z());
            if (xDiff > 4 || zDiff > 4) {
                Match.win(Match.getOpposite(Match.getPlayer(player)));
            }
        }
    }

    private void turn(Turn turn, Player p, Cord cord) {
        BlockBattleEvents.onTurn(new TurnInfo(turn, p, cord));
    }
}
