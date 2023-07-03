package net.hectus.hectusblockbattles.events;

import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.hectusblockbattles.turn.TurnInfo;
import net.hectus.hectusblockbattles.util.BBPlayer;
import net.hectus.hectusblockbattles.util.Cord;
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
        String name = structure.name;

        if (name.contains("WARP")) {
            String warp = name.replace("_WARP", "");
            WarpManager.warp(Warp.valueOf(warp), event.placer(), event.opponent());
        }
    }
    public static void onTurn(TurnInfo turn) {
        Match.addTurn(turn);

        BBPlayer player = Match.getPlayer(turn.player());
        BBPlayer opponent = Match.getOpponent();
        Cord cord = turn.cord();

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
                Match.next();
            }
            case NETHER_PORTAL_FRAME -> Match.netherPortalAwaitIgnite = true;
            case NETHER_PORTAL_IGNITE -> {
                if (Match.netherPortalAwaitIgnite) {
                    Match.getOpponent().setAttacked(true);
                    Match.netherPortalAwaitIgnite = false;
                }
                Match.next();
            }
            case POWDER_SNOW -> {
                Match.getOpponent().setMovement(false);
                Match.getOpponent().startDieCounter(2);
            }
            case LIGHTNING_TRIDENT -> {
                // Requires the LIGHTNING_ROD turn first
                // TODO: Add if the last move was a lighting rod
                turn.player().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, -1, 1));
                Match.getOpponent().player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, -1, 1));
            }
            case SEA_PICKLE_STACK -> Match.getOpponent().addExtraTurns(2);
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
        }

        if (Match.currentWarp == Warp.REDSTONE) {
            if (turn.turn().clazz == REDSTONE) Match.getPlacer().addLuck(5);
        }

        if (player.isAttacked()) Match.lose();

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
