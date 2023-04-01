package net.hectus.hectusblockbattles.Events;

import net.hectus.hectusblockbattles.IngameShop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class IngameShopEvents implements Listener {

    @EventHandler
    public static void onShopClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("SHOP")) {
            if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT) {
                if (event.getCurrentItem() != null) {
                    IngameShop.onItemClicked((Player) event.getWhoClicked(), event.getCurrentItem());
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public static void onShopClose(InventoryCloseEvent event) {
        if ((event.getView().getTitle().equals("SHOP"))) {
            IngameShop.onShopClose();
        }
    }
}
