package net.hectus.hectusblockbattles.events;

import net.hectus.hectusblockbattles.InGameShop;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InGameShopEvents implements Listener {
    @EventHandler
    public static void onInventoryClick(@NotNull InventoryClickEvent event) {
        if (PlainTextComponentSerializer.plainText().serialize(event.getView().title()).startsWith("Pre-Round Shop")) {
            event.setCancelled(true);

            if (event.getCurrentItem() != null) {
                InGameShop.onItemClicked((Player) event.getWhoClicked(), event.getCurrentItem(), Objects.requireNonNull(event.getClickedInventory()));
            }
        }
    }
}
