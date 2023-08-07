package net.hectus.hectusblockbattles.warps;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.Cord;
import net.hectus.hectusblockbattles.Trace;
import net.hectus.hectusblockbattles.Translation;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.match.GameFlow;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.turn.Turn;
import net.hectus.util.Randomizer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

public class WarpManager {
    public static void warp(@NotNull Warp warp, Player activator, Player otherPlayer) {
        System.out.println("WarpManager.warp(" + "warp = " + warp + ", activator = " + activator + ", otherPlayer = " + otherPlayer + ") by: " + Trace.last());

        boolean doWarp = Randomizer.boolByChance(warp.chance * 100);

        if (Match.nextWarp100P) {
            doWarp = true;
            Match.nextWarp100P = false;
        }

        if (!doWarp) {
            if (warp == Warp.HELL) {
                doWarp = true;
                warp = Warp.HEAVEN;
            } else if (warp == Warp.HEAVEN) {
                doWarp = true;
                warp = Warp.HELL;
            }
        }

        if (doWarp) {
            Cord cord = warp.middle;

            switch(warp) {
                case ICE, REDSTONE -> Match.getPlacer().addLuck(5);
                case CLIFF -> Match.getPlacer().addLuck(15);
                case UNDERWATER -> Match.getPlacer().addLuck(8);
                case VOID -> {
                    Match.getPlacer().addLuck(15);
                    otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, -1, 1));
                    otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, -1, 250));
                }
                case DESERT -> {
                    Match.getPlacer().addLuck(5);
                    otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, -1, 2));
                }
                case AETHER -> Match.getPlacer().addLuck(10);
                case BOOK -> {
                    DayOfWeek day = LocalDateTime.now().getDayOfWeek();
                    if (day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY) {
                        Match.getPlacer().showTitle(McColor.RED + Translation.get("turn.wrong_usage", activator.locale()), McColor.RED + Translation.get("turn.wrong_usage.book_warp_only_in_week", activator.locale()), null);
                        otherPlayer.showTitle(BlockBattleEvents.subtitle(McColor.YELLOW + Translation.get("turn.wrong_usage.book_warp_only_in_week.opponent", otherPlayer.locale(), activator.getName())));

                        return;
                    }
                    Match.getPlacer().addLuck(35);
                }
                case AMETHYST -> Match.getPlacer().addLuck(20);
                case SUN -> {
                    int hour = LocalTime.now(Clock.systemUTC()).getHour();
                    if (hour < 6 || hour >= 18) {
                        Match.getPlacer().showTitle(McColor.RED + Translation.get("turn.wrong_usage", activator.locale()), McColor.RED + Translation.get("turn.wrong_usage.sun_warp_only_during_day", activator.locale()), null);
                        otherPlayer.showTitle(BlockBattleEvents.subtitle(McColor.YELLOW + Translation.get("turn.wrong_usage.sun_warp_only_during_day.opponent", otherPlayer.locale(), activator.getName())));
                        return;
                    }

                    otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, -1, 1));

                    for (Turn turn : Turn.values()) {
                        if (turn.clazz == WarpSettings.Class.WATER) {
                            activator.getInventory().remove(turn.material);
                            otherPlayer.getInventory().remove(turn.material);
                        }
                    }
                }
                case MUSHROOM -> Match.getPlacer().giveRandomItem();
                case END -> {
                    activator.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                    Match.getPlacer().addLuck(10);
                }
                case HEAVEN -> {
                    Match.getPlacer().addLuck(20);
                    Match.getOpponent().addLuck(10);
                }
                case HELL -> {
                    Match.getPlacer().removeLuck(10);
                    Match.getOpponent().removeLuck(20);
                }
            }

            Match.p1.player.teleport(new Location(activator.getWorld(), cord.x() - 2, cord.y(), cord.z()));
            Match.p2.player.teleport(new Location(activator.getWorld(), cord.x() + 2, cord.y(), cord.z()));
            activator.showTitle(BlockBattleEvents.subtitle(McColor.GREEN + Translation.get("turn.warp.success", activator.locale())));
            otherPlayer.showTitle(BlockBattleEvents.subtitle(McColor.YELLOW + Translation.get("turn.warp.success.opponent", otherPlayer.locale(), activator.getName())));

            Match.getPlacer().startJailCounter(-3);
            Match.getPlacer().startBurnCounter(-3);

            Match.getOpponent().startJailCounter(-3);
            Match.getOpponent().startBurnCounter(-3);

            Match.allowed.clear();
            Collections.addAll(Match.allowed, warp.allow);

            Match.disallowed.clear();
            Collections.addAll(Match.disallowed, warp.deny);

            GameFlow.next();
        } else {
            activator.showTitle(BlockBattleEvents.subtitle(McColor.RED + Translation.get("turn.warp.fail", activator.locale())));
            otherPlayer.showTitle(BlockBattleEvents.subtitle(McColor.YELLOW + Translation.get("turn.warp.fail.opponent", otherPlayer.locale(), activator.getName())));

            GameFlow.next();
        }
    }
}
