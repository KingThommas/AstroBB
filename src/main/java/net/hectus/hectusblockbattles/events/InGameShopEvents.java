package net.hectus.hectusblockbattles.events;

import net.hectus.hectusblockbattles.Compring;
import net.hectus.hectusblockbattles.InGameShop;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.player.BBPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InGameShopEvents implements Listener {
    @EventHandler
    public static void onInventoryClick(@NotNull InventoryClickEvent event) {
        System.out.println("InGameShopEvents.onInventoryClick(" + "event = " + event + ")");

        Inventory inv = event.getClickedInventory();
        if (inv == null) return;

        if (Compring.from(event.getView().title()).toLowerCase().contains("shop")) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if (item != null) {
                Player player = (Player) event.getWhoClicked();

                if (event.getSlot() > 17) {
                    InGameShop.onItemClicked(player, item, Objects.requireNonNull(event.getClickedInventory()));
                    return;
                }
                InGameShop.HotBar hotBar = Match.getPlayer(player).getState() == Match.PlayerState.SHOP_NORMAL ? InGameShop.HotBar.NORMAL : InGameShop.HotBar.OVERTIME;
                int coins = Objects.requireNonNull(event.getClickedInventory().getItem(13)).getAmount();

                if (event.getSlot() <= 8) {
                    InGameShop.displayShop(player, hotBar, coins, event.getSlot() + 1);
                }
            }
        } else if (inv instanceof PlayerInventory) {
            if (Match.hasStarted) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(@NotNull InventoryCloseEvent event) {
        System.out.println("InGameShopEvents.onInventoryClose(player = " + event.getPlayer() + ")");

        if (!Match.shopPhase || event.getReason() == InventoryCloseEvent.Reason.OPEN_NEW) return;

        BBPlayer player = Match.getPlayer((Player) event.getPlayer());
        BBPlayer opponent = Match.getOpposite(player);
        if (opponent == null) return;

        if (player.getState() == Match.PlayerState.SHOP_NORMAL) {
            if (opponent.getState() == Match.PlayerState.SHOP_NORMAL_FINISHED) {
                InGameShop.displayShop(player.player, InGameShop.HotBar.OVERTIME);
                InGameShop.displayShop(opponent.player, InGameShop.HotBar.OVERTIME);
            } else {
                player.setState(Match.PlayerState.SHOP_NORMAL_FINISHED);
            }
        } else if (player.getState() == Match.PlayerState.SHOP_OVERTIME) {
            if (opponent.getState() == Match.PlayerState.SHOP_OVERTIME_FINISHED) {
                Match.shopDone();
            } else {
                player.setState(Match.PlayerState.SHOP_OVERTIME_FINISHED);
            }
        }
    }
}
