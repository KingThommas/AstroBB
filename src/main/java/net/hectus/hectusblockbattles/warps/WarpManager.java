package net.hectus.hectusblockbattles.warps;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.InGameShop;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.turn.Turn;
import net.hectus.hectusblockbattles.util.Cord;
import net.hectus.util.Randomizer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class WarpManager {
    public static void warp(Warp warp, Player activator, Player otherPlayer) {
        boolean doWarp = Randomizer.boolByChance(warp.chance * 100);

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
                        Match.getPlacer().showTitle(McColor.RED + "Wrong Usage!", McColor.RED + "You can only use the book warp in the week!", null);
                        otherPlayer.showTitle(BlockBattleEvents.subtitle(McColor.YELLOW + activator.getName() + " forgot that the days of the week exist💀"));
                        return;
                    }
                    Match.getPlacer().addLuck(35);
                }
                case AMETHYST -> Match.getPlacer().addLuck(20);
                case SUN -> {
                    int hour = LocalTime.now(Clock.systemUTC()).getHour();
                    if (hour < 6 || hour >= 18) {
                        Match.getPlacer().showTitle(McColor.RED + "Wrong Usage!", McColor.RED + "You can only use the sun warp during the day!", null);
                        otherPlayer.showTitle(BlockBattleEvents.subtitle(McColor.YELLOW + activator.getName() + " forgot that the time exist💀"));
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
                case MUSHROOM -> {
                    ArrayList<InGameShop.ShopItem> shopItems = new ArrayList<>(InGameShop.SHOP_ITEMS_1);
                    shopItems.addAll(InGameShop.SHOP_ITEMS_2);

                    Material mat = ((InGameShop.ShopItem) Randomizer.fromCollection(shopItems)).item();

                    activator.getInventory().addItem(new ItemStack(mat));
                }
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

            activator.teleport(new Location(activator.getWorld(), cord.x() - 2, cord.y(), cord.z()));
            otherPlayer.teleport(new Location(activator.getWorld(), cord.x() + 2, cord.y(), cord.z()));

            activator.showTitle(BlockBattleEvents.subtitle(McColor.GREEN + "Success!"));
            otherPlayer.showTitle(BlockBattleEvents.subtitle(McColor.YELLOW + activator.getName() + "'s Warp succeeded!"));

            Match.next();
        } else {
            activator.showTitle(BlockBattleEvents.subtitle(McColor.RED + "Fail!"));
            otherPlayer.showTitle(BlockBattleEvents.subtitle(McColor.YELLOW + activator.getName() + "'s Warp failed!"));
        }
    }
}
