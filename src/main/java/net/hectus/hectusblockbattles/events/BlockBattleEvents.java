package net.hectus.hectusblockbattles.events;

import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.hectusblockbattles.turn.Turn;
import net.hectus.hectusblockbattles.turn.TurnInfo;
import net.hectus.hectusblockbattles.player.BBPlayer;
import net.hectus.hectusblockbattles.Cord;
import net.hectus.hectusblockbattles.warps.Warp;
import net.hectus.hectusblockbattles.warps.WarpManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Random;

import static net.hectus.hectusblockbattles.warps.WarpSettings.Class.*;

public class BlockBattleEvents {
    public static void onStructurePlace(@NotNull StructurePlaceEvent event) {
        Structure structure = event.structure();

        Turn turn = switch (structure.name) {
            case "NETHER_PORTAL"   -> Turn.NETHER_PORTAL_FRAME;
            case "OAK_DOOR_TURTLE" -> Turn.OAK_DOORS;
            default                -> Turn.NONE;
        };

        if (structure.name.contains("WARP")) {
            String warp = structure.name.replace("_WARP", "");
            WarpManager.warp(Warp.valueOf(warp), event.placer(), event.opponent());
        }

        if (turn != Turn.NONE) {
            onTurn(new TurnInfo(turn, event.placer(), event.relative()));
        }
    }
    public static void onTurn(TurnInfo turn) {
        Match.addTurn(turn);

        if (turn.turn().type == Turn.Type.ATTACK || turn.turn().type == Turn.Type.WARP) {
            Match.lose();
            Match.getPlacer().showTitle("", "You can't use warps or attacks as your first move!", null);
            Match.getOpponent().showTitle("", "Your opponent used a warp or attack as his first move💀", null);
            return;
        }

        BBPlayer player = Match.getPlayer(turn.player());
        BBPlayer opponent = Match.getOpponent();
        Cord cord = turn.cord();

        boolean initialAttacked = player.isAttacked();

        switch (turn.turn()) {
            case PURPLE_WOOL -> Match.win();
            case DINNERBONE -> {
                if (player.isAttacked()) {
                    if (Match.getLatestTurn().turn().mob) {
                        player.setAttacked(false);
                    } else {
                        Match.lose();
                    }
                }
            }
            case NETHER_PORTAL_FRAME -> Match.netherPortalAwaitIgnite = true;
            case NETHER_PORTAL_IGNITE -> {
                if (Match.netherPortalAwaitIgnite) {
                    opponent.setAttacked(true);
                    Match.netherPortalAwaitIgnite = false;
                }
            }
            case POWDER_SNOW -> {
                opponent.setMovement(false);
                opponent.startDieCounter(2);
            }
            case LIGHTNING_TRIDENT -> {
                if (Match.getLatestTurn().turn() == Turn.LIGHTNING_ROD) {
                    turn.player().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, -1, 1));
                    opponent.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, -1, 1));
                }
            }
            case SEA_PICKLE_STACK -> {
                opponent.setAttacked(true);
                opponent.setDoubleCounterAttack(true);
            }
            case TNT -> {
                if (player.isDefended() && !opponent.isDefended()) Match.win();
                else if (!player.isDefended() && opponent.isDefended()) Match.lose();

                Match.draw();
            }
            case OP_GAP -> {
                if (new Random().nextBoolean()) player.addLuck(100);
                for (PotionEffect effect : player.player.getActivePotionEffects()) {
                    player.player.removePotionEffect(effect.getType());
                }
            }
            case SPRUCE_TRAPDOOR -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, WATER, NATURE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                }
            }
            case IRON_TRAPDOOR -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, WATER, REDSTONE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                }
            }
            case CAULDRON -> opponent.setAttacked(true);
            case GOLD_BLOCK -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, HOT, NATURE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case BLACK_WOOL -> {
                player.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, -1, 1));
                opponent.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, -1, 1));
            }
            case SCULK_BLOCK -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, -1, 1));
                    opponent.removeLuck(10);
                }
            }
            case GREEN_CARPET -> {
                Material latestMaterial = Match.getLatestTurn().turn().material;
                if (player.isAttacked() && latestMaterial.name().contains("WOOL") && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case CYAN_CARPET -> {
                if (player.isAttacked() && Match.latestTurnIsClass(REDSTONE, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                } else if (!player.isAttacked()) {
                    player.addExtraTurns(1);
                }
            }
            case LIGHTNING_ROD -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, HOT, REDSTONE, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case OAK_DOORS -> {
                player.setDefended(true);
                opponent.setAttacked(true);
            }
            case ENDER_PEARL_TP -> {
                // TODO: Make it counter only traps or levitation
                if (player.isAttacked()) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
        }

        if (initialAttacked) {
            Turn lt = Match.getLatestTurn().turn();
            if (lt == Turn.OAK_DOORS) {
                if (turn.turn().clazz != REDSTONE && turn.turn().clazz != HOT) {
                    player.setAttacked(true);
                }
            }
        }

        if (Match.currentWarp == Warp.REDSTONE) {
            if (turn.turn().clazz == REDSTONE) Match.getPlacer().addLuck(5);
        }

        if (player.isAttacked()) Match.lose();

        Match.p1 = Match.p1.player == player.player ? player : opponent;
        Match.p2 = Match.p1.player == player.player ? opponent : player;

        Match.next();
    }

    @Contract("_ -> new")
    public static @NotNull Title subtitle(String content) {
        return Title.title(
                Component.empty(),
                Component.text(content),
                Title.Times.times(
                        Duration.ZERO,
                        Duration.ofMillis(500L),
                        Duration.ofMillis(200L)
                )
        );
    }
}
