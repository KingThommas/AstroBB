package net.hectus.hectusblockbattles;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemExplorer implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.hasItem()) {
            if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                //displayInventory(event.getPlayer(), );
            }
        }
    }
    public void displayInventory(Player player, Material[] blocks, String[] names, boolean[] glowing) {
        Inventory inventory = Bukkit.createInventory(null, 27, "My Inventory");
        for (int i = 0; i < blocks.length; i++) {
            ItemStack itemStack = new ItemStack(blocks[i]);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(names[i]);
            if (glowing[i]) {
                itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
            }
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(i, itemStack);
        }
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Item Explorer")) {
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
