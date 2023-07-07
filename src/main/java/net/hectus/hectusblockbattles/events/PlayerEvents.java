package net.hectus.hectusblockbattles.events;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import io.papermc.paper.event.player.PlayerNameEntityEvent;
import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.Cord;
import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.hectusblockbattles.turn.Turn;
import net.hectus.hectusblockbattles.turn.TurnInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
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
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

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
            case "STONECUTTER" -> turn(Turn.STONECUTTER, p, c);
            case "MAGENTA_GLAZED_TERRACOTTA" -> turn(Turn.MAGENTA_GLAZED_TERRACOTTA, p, c);
            case "MAGMA_BLOCK" -> turn(Turn.MAGMA_BLOCK, p, c);
            case "NETHERRACK" -> turn(Turn.NETHERRACK, p, c);
            case "LAVA" -> turn(Turn.LAVA_BUCKET, p, c);
            case "FIRE" -> turn(Turn.FLINT_N_STEEL, p, c);
            case "ORANGE_WOOL" -> turn(Turn.ORANGE_WOOL, p, c);
            case "CAMPFIRE" -> turn(Turn.CAMPFIRE, p, c);
            case "RESPAWN_ANCHOR" -> turn(Turn.RESPAWN_ANCHOR, p, c);

            default -> {
                Structure.BlockData blockData = new Structure.BlockData(b.getType(), b.getX(), b.getY(), b.getZ(), Structure.blockFace(b), Structure.blockBound(b), Structure.isOpen(b));
                Match.algorithm.addBlock(blockData);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!Match.hasStarted) return;

        Player p = event.getPlayer();
        if (!Match.algorithm.isPlacer(p)) {
            p.sendMessage(Component.text(McColor.RED + "It isn't your turn right now!"));
            event.setCancelled(true);
            return;
        }

        switch (event.getCause()) {
            case ENDER_PEARL -> {
                if (outOfBounds(event.getTo())) {
                    Match.lose();
                } else {
                    turn(Turn.ENDER_PEARL_TP, p, Cord.of(event.getTo()));
                }
            }
            case CHORUS_FRUIT -> {
                if (outOfBounds(event.getTo())) {
                    int mx = Match.currentWarp.middle.x();
                    int mz = Match.currentWarp.middle.z();
                    double x = new Random().nextDouble(mx - 4, mx + 4);
                    double z = new Random().nextDouble(mz - 4, mz + 4);
                    p.teleport(new Location(HBB.WORLD, x, p.getLocation().y(), z));
                } else {
                    turn(Turn.ENDER_PEARL_TP, p, Cord.of(event.getTo()));
                }
                turn(Turn.CHORUS_FRUIT_EAT, p, Cord.of(event.getTo()));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntitySpawn(EntitySpawnEvent event) {
        Player p = Match.getPlacer().player;
        Cord c = Cord.of(event.getLocation());
        switch (event.getEntity().getType()) {
            case BLAZE -> turn(Turn.BLAZE, p, c);
            case PIGLIN -> turn(Turn.PIGLIN, p, c);
            case POLAR_BEAR -> turn(Turn.POLAR_BEAR, p, c);
            case BOAT -> turn(Turn.BOAT, p, c);
            case AXOLOTL -> turn(Turn.AXOLOTL, p, c);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        if (!Match.hasStarted) return;

        Player player = event.getPlayer();

        if (event.hasChangedPosition() && !player.isOp()) {
            Match.getPlayer(player).setDefended(false);

            if (Match.getPlayer(player).isJailed()) {
                Match.getPlayer(player).showTitle("", "You are jailed, so you can't move!", null);
                event.setCancelled(true);
            }

            if (!Match.algorithm.isPlacer(player) || !Match.getPlayer(player).canMove() || outOfBounds(event.getTo())) {
                Match.win(Match.getOpposite(Match.getPlayer(player)));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDropItem(@NotNull PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.IRON_SHOVEL) {
            turn(Turn.IRON_SHOVEL, event.getPlayer(), Cord.of(event.getPlayer().getLocation()));
            event.getItemDrop().setHealth(-1);
        }
    }

    private void turn(Turn turn, Player p, Cord cord) {
        BlockBattleEvents.onTurn(new TurnInfo(turn, p, cord));
    }

    private boolean outOfBounds(@NotNull Location location) {
        double xDiff = Math.abs(location.x() - Match.currentWarp.middle.x());
        double zDiff = Math.abs(location.z() - Match.currentWarp.middle.z());
        return xDiff > 4 || zDiff > 4;
    }
}
