package net.hectus.hectusblockbattles.events;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import io.papermc.paper.event.entity.EntityCompostItemEvent;
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent;
import io.papermc.paper.event.player.PlayerNameEntityEvent;
import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.Compring;
import net.hectus.hectusblockbattles.Cord;
import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.Translation;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.hectusblockbattles.turn.Turn;
import net.hectus.hectusblockbattles.turn.TurnInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.TNTPrimeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class BaseEvents implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerNameEntity(@NotNull PlayerNameEntityEvent event) {
        if (cantPlace(event)) return;

        String name = Compring.from(event.getName()).toLowerCase();
        if (name.contains("dinnerbone") || name.contains("grumm")) {
            turn(Turn.DINNERBONE, event.getPlayer(), Cord.of(event.getEntity().getLocation()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTNTPrime(@NotNull TNTPrimeEvent event) {
        event.setCancelled(true);
        if (event.getCause() == TNTPrimeEvent.PrimeCause.PLAYER) {
            turn(Turn.TNT, Match.getPlacer().player, Cord.of(event.getBlock().getLocation()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerItemConsume(@NotNull PlayerItemConsumeEvent event) {
        if (cantPlace(event)) return;

        Player player = event.getPlayer();
        if (event.getItem().getType() == Material.ENCHANTED_GOLDEN_APPLE) {
            player.sendMessage(Component.text(Translation.get("super_apple", player.locale())));
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
    public void onEntityTransform(@NotNull EntityTransformEvent event) {
        if (event.getTransformReason() == EntityTransformEvent.TransformReason.PIGLIN_ZOMBIFIED && Match.hasStarted) {
            turn(Turn.PIGLIN_CONVERT, Match.getPlacer().player, Cord.of(event.getTransformedEntity().getLocation()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLaunchProjectile(@NotNull PlayerLaunchProjectileEvent event) {
        if (cantPlace(event)) return;

        if (event.getProjectile().getType() == EntityType.TRIDENT) {
            if (((Trident) event.getProjectile()).hasGlint()) {
                turn(Turn.LIGHTNING_TRIDENT, event.getPlayer(), Cord.of(event.getPlayer().getLocation()));
            }
        } else if (event.getProjectile().getType() == EntityType.SNOWBALL) {
            turn(Turn.SNOWBALL, event.getPlayer(), Cord.of(event.getPlayer().getLocation()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPotionSplash(@NotNull PotionSplashEvent event) {
        Player p = (Player) event.getPotion().getShooter();

        if (cantPlace(p)) {
            event.setCancelled(true);
            return;
        }

        Cord c = Cord.of(event.getPotion().getLocation());
        if (event.getPotion().getEffects().size() == 0) {
            turn(Turn.SPLASH_WATER_BOTTLE, p, c);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        if (!Match.hasStarted) return;

        Player p = event.getPlayer();
        if (cantPlace(p)) {
            event.setCancelled(true);
            return;
        }

        Block b = event.getBlock();
        Cord c = new Cord(b.getX(), b.getY(), b.getZ());

        switch (b.getType()) {
            case PURPLE_WOOL -> turn(Turn.PURPLE_WOOL, p, c);
            case POWDER_SNOW -> turn(Turn.POWDER_SNOW, p, c);
            case SPRUCE_TRAPDOOR -> turn(Turn.SPRUCE_TRAPDOOR, p, c);
            case SEA_PICKLE -> {
                if (b instanceof SeaPickle pickle) {
                    if (pickle.getPickles() >= 4) turn(Turn.SEA_PICKLE_STACK, p, c);
                }
            }
            case STONECUTTER -> turn(Turn.STONECUTTER, p, c);
            case MAGENTA_GLAZED_TERRACOTTA -> turn(Turn.MAGENTA_GLAZED_TERRACOTTA, p, c);
            case SPONGE -> turn(Turn.CYAN_CARPET, p, c);
            case MAGMA_BLOCK -> turn(Turn.MAGMA_BLOCK, p, c);
            case NETHERRACK -> turn(Turn.NETHERRACK, p, c);
            case LAVA -> turn(Turn.LAVA_BUCKET, p, c);
            case FIRE -> turn(Turn.FLINT_N_STEEL, p, c);
            case ORANGE_WOOL -> turn(Turn.ORANGE_WOOL, p, c);
            case CAMPFIRE -> turn(Turn.CAMPFIRE, p, c);
            case RESPAWN_ANCHOR -> turn(Turn.RESPAWN_ANCHOR, p, c);
            case BEE_NEST -> turn(Turn.BEE_NEST, p, c);
            case HONEY_BLOCK -> turn(Turn.HONEY_BLOCK, p, c);
            case GREEN_WOOL -> turn(Turn.GREEN_WOOL, p, c);
            case MANGROVE_ROOTS -> turn(Turn.MANGROVE_ROOTS, p, c);
            case COMPOSTER -> turn(Turn.COMPOSTER, p, c);
            case HAY_BLOCK -> turn(Turn.HAY_BALE, p, c);
            case LEVER -> turn(Turn.LEVER, p, c);
            case OAK_FENCE_GATE -> turn(Turn.FENCE_GATE, p, c);
            case REPEATER -> turn(Turn.REDSTONE_REPEATER, p, c);
            case RED_BED -> turn(Turn.RED_BED, p, c);
            case PINK_BED -> turn(Turn.PINK_BED, p, c);
            case GREEN_BED -> turn(Turn.GREEN_BED, p, c);
            case BLUE_BED -> turn(Turn.BLUE_BED, p, c);
            case DRAGON_HEAD -> turn(Turn.DRAGON_HEAD, p, c);
            case SOUL_SAND -> turn(Turn.SOUL_SAND, p, c);
            case OAK_BUTTON -> turn(Turn.WOODEN_BUTTON, p, c);
            case STONE_BUTTON -> turn(Turn.STONE_BUTTON, p, c);
            case DAYLIGHT_DETECTOR -> turn(Turn.DAYLIGHT_SENSOR, p, c);
            case BRAIN_CORAL_BLOCK -> turn(Turn.BRAIN_CORAL_BLOCK, p, c);
            case HORN_CORAL -> turn(Turn.HORN_CORAL, p, c);
            case FIRE_CORAL -> turn(Turn.FIRE_CORAL, p, c);
            case FIRE_CORAL_FAN -> turn(Turn.FIRE_CORAL_FAN, p, c);
            case SEA_LANTERN -> turn(Turn.SEA_LANTERN, p, c);
            case WATER -> {
                if (c.toLocation().getNearbyEntitiesByType(PufferFish.class, 4).size() != 0) {
                    turn(Turn.PUFFERFISH_BUCKET, p, c);
                } else {
                    turn(Turn.WATER_BUCKET, p, c);
                }
            }
            case DRIED_KELP_BLOCK -> turn(Turn.DRIED_KELP_BLOCK, p, c);
            case VERDANT_FROGLIGHT -> turn(Turn.VERDANT_FROGLIGHT, p, c);
            case CAULDRON -> turn(Turn.CAULDRON, p, c);
            case OAK_STAIRS -> turn(Turn.OAK_STAIRS, p, c);

            default -> {
                Structure.BlockData blockData = new Structure.BlockData(b.getType(), b.getX(), b.getY(), b.getZ(), Structure.blockFace(b), Structure.blockBound(b), Structure.isOpen(b));
                Match.algorithm.addBlock(blockData);
            }
        }
    }



    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!Match.hasStarted || cantPlace(event)) return;

        Player p = event.getPlayer();

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
    public void onEntityCompostItem(@NotNull EntityCompostItemEvent event) {
        if (!(event.getEntity() instanceof Player p)) return;

        if (cantPlace(p)) {
            event.setCancelled(true);
            return;
        }

        switch (event.getItem().getType()) {
            case POPPY, BLUE_ORCHID, ALLIUM, AZURE_BLUET, RED_TULIP,
                    ORANGE_TULIP, WHITE_TULIP, PINK_TULIP, CORNFLOWER,
                    OXEYE_DAISY, WITHER_ROSE, SUNFLOWER, OAK_SAPLING,
                    SPORE_BLOSSOM -> turn(Turn.COMPOSTER_FILL, p, Cord.of(event.getBlock().getLocation()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerFlowerPotManipulate(@NotNull PlayerFlowerPotManipulateEvent event) {
        if (cantPlace(event)) return;

        Player p = event.getPlayer();
        Cord c = Cord.of(event.getFlowerpot().getLocation());

        switch (event.getItem().getType()) {
            case POPPY -> turn(Turn.POPPY, p, c);
            case BLUE_ORCHID -> turn(Turn.BLUE_ORCHID, p, c);
            case ALLIUM -> turn(Turn.ALLIUM, p, c);
            case AZURE_BLUET -> turn(Turn.AZURE_BLUET, p, c);
            case RED_TULIP -> turn(Turn.RED_TULIP, p, c);
            case ORANGE_TULIP -> turn(Turn.ORANGE_TULIP, p, c);
            case WHITE_TULIP -> turn(Turn.WHITE_TULIP, p, c);
            case PINK_TULIP -> turn(Turn.PINK_TULIP, p, c);
            case CORNFLOWER -> turn(Turn.CORNFLOWER, p, c);
            case OXEYE_DAISY -> turn(Turn.OXEYE_DAISY, p, c);
            case WITHER_ROSE -> turn(Turn.WITHER_ROSE, p, c);
            case SUNFLOWER -> turn(Turn.SUNFLOWER, p, c);
            case OAK_SAPLING -> turn(Turn.OAK_SAPLING, p, c);
            case SPORE_BLOSSOM -> turn(Turn.SPORE_BLOSSOM, p, c);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockFertilize(@NotNull BlockFertilizeEvent event) {
        if (cantPlace(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        if (event.getBlock().getType() == Material.OAK_SAPLING || event.getBlock().getType() == Material.OAK_LOG) {
            turn(Turn.OAK_SAPLING, event.getPlayer(), Cord.of(event.getBlock().getLocation()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntitySpawn(@NotNull EntitySpawnEvent event) {
        Player p = Match.getPlacer().player;
        Cord c = Cord.of(event.getLocation());

        switch (event.getEntity().getType()) {
            case BLAZE -> turn(Turn.BLAZE, p, c);
            case PIGLIN -> turn(Turn.PIGLIN, p, c);
            case POLAR_BEAR -> turn(Turn.POLAR_BEAR, p, c);
            case BOAT -> turn(Turn.BOAT, p, c);
            case AXOLOTL -> {
                Axolotl axolotl = (Axolotl) event.getEntity();
                if(axolotl.getVariant().equals(Axolotl.Variant.BLUE)) turn(Turn.BLUE_AXOLOTL, p, c);
                if(axolotl.getVariant().equals(Axolotl.Variant.GOLD)) turn(Turn.GOLD_AXOLOTL, p, c);
                if(axolotl.getVariant().equals(Axolotl.Variant.CYAN)) turn(Turn.BLUE_AXOLOTL, p, c);
                if(axolotl.getVariant().equals(Axolotl.Variant.WILD)) turn(Turn.BROWN_AXOLOTL, p, c);
                if(axolotl.getVariant().equals(Axolotl.Variant.LUCY)) turn(Turn.PINK_AXOLOTL, p, c);
            }
            case SHEEP -> {
                Sheep sheep = (Sheep) event.getEntity();

                DyeColor color = Match.getPlayer(p).randomSheepColor();
                sheep.setColor(color);

                if(color == DyeColor.PINK) turn(Turn.PINK_SHEEP, p, c);
                if(color == DyeColor.WHITE) turn(Turn.WHITE_SHEEP, p, c);
                if(color == DyeColor.LIGHT_GRAY) turn(Turn.LIGHT_GRAY_SHEEP, p, c);
                if(color == DyeColor.GRAY) turn(Turn.GRAY_SHEEP, p, c);
                if(color == DyeColor.BLACK) turn(Turn.BLACK_SHEEP, p, c);
                if(color == DyeColor.BROWN) turn(Turn.BROWN_SHEEP, p, c);
                if(color == DyeColor.BLUE) turn(Turn.BLUE_SHEEP, p, c);
            }
            case PHANTOM -> turn(Turn.PHANTOM, p, c);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDropItem(@NotNull PlayerDropItemEvent event) {
        if (cantPlace(event)) return;

        if (event.getItemDrop().getItemStack().getType() == Material.IRON_SHOVEL) {
            turn(Turn.IRON_SHOVEL, event.getPlayer(), Cord.of(event.getPlayer().getLocation()));
            event.getItemDrop().setHealth(-1);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!Match.hasStarted || player.isOp() || Match.getPlayer(player).canAlwaysMove()) return;

        if (event.hasChangedPosition()) {
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

    private void turn(Turn turn, Player p, Cord cord) {
        BlockBattleEvents.onTurn(new TurnInfo(turn, p, cord));
    }

    private boolean outOfBounds(@NotNull Location location) {
        double xDiff = Math.abs(location.x() - Match.currentWarp.middle.x());
        double zDiff = Math.abs(location.z() - Match.currentWarp.middle.z());
        return xDiff > 4 || zDiff > 4;
    }

    public boolean cantPlace(Player player) {
        if (!Match.algorithm.isPlacer(player) && !Match.netherPortalAwaitIgnite) {
            player.sendMessage(Component.text(McColor.RED + Translation.get("turn.not_placer", player.locale())));
            return true;
        }
        return false;
    }

    public boolean cantPlace(PlayerEvent event) {
        if (!Match.algorithm.isPlacer(event.getPlayer()) && !Match.netherPortalAwaitIgnite) {
            event.getPlayer().sendMessage(Component.text(McColor.RED + Translation.get("turn.not_placer", event.getPlayer().locale())));
            if (event instanceof Cancellable) ((Cancellable) event).setCancelled(true);
            return true;
        }
        return false;
    }
}
