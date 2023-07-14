package net.hectus.hectusblockbattles;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.player.BBPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.IntStream;

import static net.hectus.hectusblockbattles.InGameShop.HotBar.BOTH;
import static net.hectus.hectusblockbattles.InGameShop.HotBar.OVERTIME;

public final class InGameShop implements Listener {
    public static final HashSet<ShopItem> SHOP_ITEMS_1 = new HashSet<>(Set.of(
            i(Material.PURPLE_WOOL, 25, OVERTIME, McColor.PURPLE),
            i(Material.SPRUCE_TRAPDOOR, 4, BOTH, McColor.GOLD),
            i(Material.IRON_TRAPDOOR, 5, BOTH, McColor.GRAY),
            i(Material.CAULDRON, 9, BOTH, McColor.DARK_GRAY),
            i(Material.GOLD_BLOCK, 8, BOTH, McColor.GOLD),
            i(Material.BLACK_WOOL, 7, BOTH, McColor.BLACK),
            i(Material.SCULK, 7, BOTH, McColor.DARK_AQUA),
            i(Material.GREEN_CARPET, 7, BOTH, McColor.GREEN),
            i(Material.GLASS, 1, BOTH, McColor.GRAY)
    ));
    public static final HashSet<ShopItem> SHOP_ITEMS_2 = new HashSet<>(Set.of(
            i(Material.NETHERRACK, 2, BOTH, McColor.DARK_RED),
            i(Material.MAGMA_BLOCK, 4, BOTH, McColor.RED),
            i(Material.OAK_LOG, 3, BOTH, McColor.GOLD),
            i(Material.OAK_LEAVES, 1, BOTH, McColor.GREEN),
            i(Material.IRON_BLOCK, 2, BOTH, McColor.WHITE),
            i(Material.CAMPFIRE, 4, BOTH, McColor.RED),
            i(Material.END_STONE_BRICKS, 4, BOTH, McColor.YELLOW),
            i(Material.NETHER_BRICKS, 4, BOTH, McColor.DARK_RED),
            i(Material.REDSTONE_BLOCK, 4, BOTH, McColor.DARK_RED),
            i(Material.REDSTONE_TORCH, 3, BOTH, McColor.DARK_RED),
            i(Material.POWDER_SNOW_BUCKET, 7, BOTH, McColor.WHITE),
            i(Material.ICE, 1, BOTH, McColor.BLUE),
            i(Material.SNOW_BLOCK, 1, BOTH, McColor.WHITE),
            i(Material.BLACK_STAINED_GLASS, 1, BOTH, McColor.BLACK),
            i(Material.BLACK_CONCRETE, 4, BOTH, McColor.BLACK),
            i(Material.YELLOW_STAINED_GLASS, 1, BOTH, McColor.YELLOW),
            i(Material.SHROOMLIGHT, 4, BOTH, McColor.GOLD),
            i(Material.PRISMARINE, 2, BOTH, McColor.DARK_AQUA),
            i(Material.SEA_PICKLE, 2, BOTH, McColor.GREEN),
            i(Material.AMETHYST_BLOCK, 2, BOTH, McColor.PINK),
            i(Material.AMETHYST_CLUSTER, 4, BOTH, McColor.PINK),
            i(Material.MYCELIUM, 2, BOTH, McColor.GOLD),
            i(Material.BROWN_MUSHROOM, 3, BOTH, McColor.WHITE),
            i(Material.SAND, 1, BOTH, McColor.YELLOW),
            i(Material.CACTUS, 2, BOTH, McColor.GREEN),
            i(Material.MOSS_BLOCK, 3, BOTH, McColor.GREEN),
            i(Material.STONE, 1, BOTH, McColor.DARK_GRAY),
            i(Material.CHORUS_PLANT, 3, BOTH, McColor.PURPLE),
            i(Material.END_STONE, 1, BOTH, McColor.YELLOW)
    ));
    private static final int[] BACKGROUND_SLOTS = { 1, 2, 3, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
    private static final ItemStack BACKGROUND_FRAME = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
    private static final ItemStack NOT_IN_CURRENT_HOTBAR_FRAME = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    private static final ItemStack LAST_PAGE_ITEM = new ItemStack(Material.WHITE_CONCRETE);
    private static final ItemStack NEXT_PAGE_ITEM = new ItemStack(Material.WHITE_CONCRETE);

    public static void displayShop(Player player, @NotNull HotBar hotBar) {
        displayShop(player, hotBar, 64, 1);
    }

    public static void displayShop(Player player, @NotNull HotBar hotBar, int coins, int page) {
        player.closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);

        if (!NOT_IN_CURRENT_HOTBAR_FRAME.displayName().toString().contains("Not available!")){
            ItemMeta meta = NOT_IN_CURRENT_HOTBAR_FRAME.getItemMeta();
            meta.displayName(Component.text(McColor.RED + "Not available!"));
            meta.displayName(Component.text(McColor.GRAY + "You can't buy this item in the current shop phase!"));
            NOT_IN_CURRENT_HOTBAR_FRAME.setItemMeta(meta);

            meta = LAST_PAGE_ITEM.getItemMeta();
            meta.displayName(Component.text("Last Page"));
            LAST_PAGE_ITEM.setItemMeta(meta);

            meta = NEXT_PAGE_ITEM.getItemMeta();
            meta.displayName(Component.text("Next Page"));
            NEXT_PAGE_ITEM.setItemMeta(meta);
        }

        if (hotBar == OVERTIME) {
            Match.getPlayer(player).setState(Match.PlayerState.SHOP_OVERTIME);
        } else {
            Match.getPlayer(player).setState(Match.PlayerState.SHOP_NORMAL);
        }

        Inventory shop = Bukkit.createInventory(player, 9*6, Component.text("Pre-Round Shop, " + hotBar.name().toLowerCase() + " phase"));

        for (int i : BACKGROUND_SLOTS) shop.setItem(i, BACKGROUND_FRAME);

        shop.addItem(LAST_PAGE_ITEM);
        shop.addItem(new ItemStack(Material.GOLD_INGOT, coins));
        shop.addItem(NEXT_PAGE_ITEM);

        for (ShopItem shopItem : (page == 1 ? SHOP_ITEMS_1 : SHOP_ITEMS_2)) {
            if (shopItem.hotBar == BOTH || shopItem.hotBar == hotBar) {
                ItemStack item = new ItemStack(shopItem.item, shopItem.price);
                shop.addItem(item);
            } else {
                shop.addItem(NOT_IN_CURRENT_HOTBAR_FRAME);
            }
        }

        player.openInventory(shop);
    }

    public static void onItemClicked(Player player, @NotNull ItemStack item, @NotNull Inventory shopInv) {
        ShopItem shopItem = Objects.requireNonNull(getItem(item.getType()));

        ItemStack coinItem = shopInv.getItem(4);
        if (coinItem != null && coinItem.getType() == Material.GOLD_INGOT){
            int money = coinItem.getAmount();

            if (money >= shopItem.price) {
                player.getInventory().addItem(new ItemStack(shopItem.item));

                shopInv.remove(Material.GOLD_INGOT);
                if (money - shopItem.price <= 0) {
                    player.closeInventory(InventoryCloseEvent.Reason.PLAYER);

                    Match.algorithm.start(player);

                    Match.p2.sendActionBar(McColor.GREEN + Translation.get("match.start.message", Match.p2.locale()));
                    Match.p2.sendActionBar(McColor.GREEN + Translation.get("match.start.message", Match.p2.locale()));
                } else {
                    shopInv.addItem(new ItemStack(Material.GOLD_INGOT, money - shopItem.price));
                }

                shopInv.remove(item.getType());

                ItemMeta meta = item.getItemMeta();
                meta.addEnchant(Enchantment.MENDING, 1, false);
                item.setItemMeta(meta);

                shopInv.addItem(item);

                if (IntStream.range(0, 9)
                        .mapToObj(s -> player.getInventory().getItem(s))
                        .anyMatch(i -> i == null || i.getType().isAir())
                ) return;

                String title = PlainTextComponentSerializer.plainText().serialize(player.getOpenInventory().title()).toLowerCase();

                if (title.contains("normal")) {
                    Match.getPlayer(player).swapHotbars();
                    displayShop(player, OVERTIME, 64, 1);
                } else if (title.contains("overtime")) {
                    player.closeInventory(InventoryCloseEvent.Reason.PLAYER);

                    BBPlayer bbPlayer = Match.getPlayer(player);

                    bbPlayer.setState(Match.PlayerState.SHOP_OVERTIME_FINISHED);

                    if (bbPlayer.getState() == Match.PlayerState.SHOP_OVERTIME_FINISHED && Match.getOpposite(bbPlayer).getState() == Match.PlayerState.SHOP_OVERTIME_FINISHED) {
                        Match.p1.sendActionBar(McColor.RED + Translation.get("match.start.message", Match.p1.locale()));
                        Match.p2.sendActionBar(McColor.RED + Translation.get("match.start.message", Match.p2.locale()));

                        Match.algorithm.start(player);
                    } else {
                        bbPlayer.sendMessage(McColor.GOLD + Translation.get("shop.finish_early", bbPlayer.locale()));
                    }
                }
            }
        }
    }

    @Contract(pure = true)
    public static @Nullable ShopItem getItem(Material material) {
        for (ShopItem item : SHOP_ITEMS_1) {
            if (item.item == material) return item;
        }
        for (ShopItem item : SHOP_ITEMS_2) {
            if (item.item == material) return item;
        }
        return null;
    }

    private static @NotNull ShopItem i(Material m, Integer p, HotBar h, McColor c) {
        return new ShopItem(m, p, h, c);
    }

    public record ShopItem(Material item, Integer price, HotBar hotBar, McColor color) {}
    public enum HotBar { BOTH, NORMAL, OVERTIME }
}
