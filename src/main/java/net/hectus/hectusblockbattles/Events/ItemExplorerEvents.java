package net.hectus.hectusblockbattles.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemExplorerEvents implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Block Battles Explorer")) {
            if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT) {
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null) {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    player.sendMessage("You clicked " + clickedItem.getType());
                }
            }
        }
    }
}
