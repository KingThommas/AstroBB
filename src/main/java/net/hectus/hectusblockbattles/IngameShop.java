package net.hectus.hectusblockbattles;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IngameShop implements Listener {
    static ItemStack glass_wall = new ItemStack(Material.BRAIN_CORAL_BLOCK, 1);
    static int[] backgroundSlots = {0,1,2,3,5,6,7,8,9,10,16,17,18,19,25,26,27,28,34,35,36,37,43,44,45,46,47,48,49,50,51,52,53};
    static int money = 40;
    static boolean shopState = true;

    public static void createItems() {
        ItemMeta meta = glass_wall.getItemMeta();
        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        glass_wall.setItemMeta(meta);
    }

    public static void displayShop(Player player) {
        Inventory shop = Bukkit.createInventory(player, 9*6, "SHOP");
        for (int slot : backgroundSlots) {
            shop.setItem(slot, new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
        }
        shop.setItem(4, new ItemStack(Material.GOLD_INGOT, money));
        shop.addItem(glass_wall);
        player.openInventory(shop);
    }

    @EventHandler
    public static void onShopClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("SHOP")) {
            if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT) {
                if (event.getCurrentItem() != null) {
                    if ((event.getCurrentItem()).equals(glass_wall) && (money >= 2)) {
                        money -= 2;
                        event.getWhoClicked().getInventory().addItem(new ItemStack(Material.GLASS, 6));
                        shopState = false;
                        displayShop((Player) event.getWhoClicked());
                        shopState = true;
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public static void onShopClose(InventoryCloseEvent event) {
        if ((event.getView().getTitle().equals("SHOP")) && (shopState)) {
            money = 40;
        }
    }
}
