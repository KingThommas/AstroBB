package net.hectus.hectusblockbattles.events;

import net.hectus.hectusblockbattles.InGameShop;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

public class InGameShopEvents implements Listener {

    @EventHandler
    public static void onShopClick(@NotNull InventoryClickEvent event) {
        if (event.getView().title().equals(Component.text("SHOP"))) {
            if (event.getCurrentItem() != null) {
                InGameShop.onItemClicked((Player) event.getWhoClicked(), event.getCurrentItem());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void onShopClose(@NotNull InventoryCloseEvent event) {
        if ((event.getView().title().equals(Component.text("SHOP")))) {
            InGameShop.onShopClose();
        }
    }
}
