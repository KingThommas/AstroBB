package net.hectus.hectusblockbattles;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemExplorer {
    public static void displayInventory(Player player, Material[] blocks, String[] names, boolean[] glowing) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Block Battles Explorer");
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
}
