package net.hectus.hectusblockbattles;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.player.BBPlayer;
import net.kyori.adventure.text.Component;
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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

import static net.hectus.hectusblockbattles.InGameShop.HotBar.BOTH;
import static net.hectus.hectusblockbattles.InGameShop.HotBar.OVERTIME;

public final class InGameShop implements Listener {
    public static final HashSet<ShopItem> SHOP_ITEMS_NEUTRAL = new HashSet<>(Set.of(
            i(Material.OAK_DOOR, 2, BOTH, McColor.GOLD),
            i(Material.IRON_BARS, 1, BOTH, McColor.GRAY),
            i(Material.PURPLE_WOOL, 25, OVERTIME, McColor.PURPLE),
            i(Material.NAME_TAG, 7, BOTH, McColor.GOLD),
            i(Material.SPRUCE_TRAPDOOR, 4, BOTH, McColor.GOLD),
            i(Material.IRON_TRAPDOOR, 5, BOTH, McColor.GRAY),
            i(Material.CAULDRON, 9, BOTH, McColor.DARK_GRAY),
            i(Material.GOLD_BLOCK, 8, BOTH, McColor.GOLD),
            i(Material.BLACK_WOOL, 7, BOTH, McColor.BLACK),
            i(Material.SCULK, 7, BOTH, McColor.DARK_AQUA),
            i(Material.GREEN_CARPET, 7, BOTH, McColor.GREEN),
            i(Material.CYAN_CARPET, 7, BOTH, McColor.AQUA),
            i(Material.ENDER_PEARL, 10, BOTH, McColor.DARK_AQUA),
            i(Material.CHORUS_FRUIT, 8, BOTH, McColor.PURPLE),
            i(Material.IRON_SHOVEL, 6, BOTH, McColor.WHITE),
            i(Material.STONECUTTER, 6, BOTH, McColor.GRAY),
            i(Material.MAGENTA_GLAZED_TERRACOTTA, 7, BOTH, McColor.PURPLE)
    ));

    public static final HashSet<ShopItem> SHOP_ITEMS_HOT = new HashSet<>(Set.of(
            i(Material.OBSIDIAN, 1, BOTH, McColor.BLACK),
            i(Material.FLINT_AND_STEEL, 3, BOTH, McColor.RED),
            i(Material.MAGMA_BLOCK, 6, BOTH, McColor.GOLD),
            i(Material.NETHERRACK, 5, BOTH, McColor.RED),
            i(Material.LAVA_BUCKET, 15, BOTH, McColor.GOLD),
            i(Material.ORANGE_WOOL, 5, BOTH, McColor.GOLD),
            i(Material.CAMPFIRE, 4, BOTH, McColor.RED),
            i(Material.BLAZE_SPAWN_EGG, 8, BOTH, McColor.YELLOW),
            i(Material.PIGLIN_SPAWN_EGG, 10, BOTH, McColor.PINK),
            i(Material.RESPAWN_ANCHOR, 9, BOTH, McColor.PURPLE)
    ));

    public static final HashSet<ShopItem> SHOP_ITEMS_COLD = new HashSet<>(Set.of(
            i(Material.POWDER_SNOW_BUCKET, 7, BOTH, McColor.WHITE),
            i(Material.PACKED_ICE, 6, BOTH, McColor.BLUE),
            i(Material.BLUE_ICE, 5, BOTH, McColor.BLUE),
            i(Material.SPRUCE_LEAVES, 6, BOTH, McColor.GREEN),
            i(Material.LIGHT_BLUE_WOOL, 6, BOTH, McColor.BLUE),
            i(Material.WHITE_WOOL, 4, BOTH, McColor.WHITE),
            i(Material.SNOWBALL, 9, BOTH, McColor.WHITE),
            i(Material.POLAR_BEAR_SPAWN_EGG, 10, BOTH, McColor.WHITE)
    ));

    public static final HashSet<ShopItem> SHOP_ITEMS_WATER = new HashSet<>(Set.of(
            i(Material.SEA_PICKLE, 2, BOTH, McColor.GREEN),
            i(Material.BRAIN_CORAL_BLOCK, 6, BOTH, McColor.PINK),
            i(Material.HORN_CORAL, 6, BOTH, McColor.YELLOW),
            i(Material.FIRE_CORAL, 6, BOTH, McColor.RED),
            i(Material.FIRE_CORAL_FAN, 8, BOTH, McColor.RED),
            i(Material.SEA_LANTERN, 12, BOTH, McColor.AQUA),
            i(Material.WATER_BUCKET, 5, BOTH, McColor.DARK_BLUE),
            i(Material.DRIED_KELP_BLOCK, 10, BOTH, McColor.GREEN),
            i(Material.OAK_BOAT, 5, BOTH, McColor.DARK_RED),
            i(Material.AXOLOTL_SPAWN_EGG, 13, BOTH, McColor.WHITE),
            i(Material.VERDANT_FROGLIGHT, 6, BOTH, McColor.LIME),
            i(Material.PUFFERFISH_BUCKET, 16, BOTH, McColor.GOLD),
            i(Material.SPLASH_POTION, 8, BOTH, McColor.DARK_BLUE)
    ));

    public static final HashSet<ShopItem> SHOP_ITEMS_NATURE = new HashSet<>(Set.of(
            i(Material.SEA_PICKLE, 2, BOTH, McColor.GREEN),
            i(Material.BEE_NEST, 5, BOTH, McColor.LIME),
            i(Material.HONEY_BLOCK, 5, BOTH, McColor.YELLOW),
            i(Material.CARVED_PUMPKIN, 1, BOTH, McColor.GOLD),
            i(Material.GREEN_WOOL, 6, BOTH, McColor.GREEN),
            i(Material.MANGROVE_ROOTS, 5, BOTH, McColor.GREEN),
            i(Material.COMPOSTER, 5, BOTH, McColor.LIME),
            i(Material.OAK_STAIRS, 7, BOTH, McColor.WHITE),
            i(Material.OAK_SAPLING, 4, BOTH, McColor.GREEN),
            i(Material.HAY_BLOCK, 6, BOTH, McColor.YELLOW),
            i(Material.DIRT, 2, BOTH, McColor.GRAY),
            i(Material.BONE_MEAL, 1, BOTH, McColor.WHITE)
    ));

    public static final HashSet<ShopItem> SHOP_ITEMS_REDSTONE = new HashSet<>(Set.of(
            i(Material.TNT, 4, OVERTIME, McColor.RED),
            i(Material.DAYLIGHT_DETECTOR, 1, BOTH, McColor.YELLOW),
            i(Material.LEVER, 5, BOTH, McColor.RED),
            i(Material.OAK_FENCE_GATE, 6, BOTH, McColor.GOLD),
            i(Material.REPEATER, 6, BOTH, McColor.RED),
            i(Material.OAK_BUTTON, 6, BOTH, McColor.GOLD),
            i(Material.LIGHTNING_ROD, 5, BOTH, McColor.DARK_RED),
            i(Material.STONE_BUTTON, 6, BOTH, McColor.GRAY)
    ));

    public static final HashSet<ShopItem> SHOP_ITEMS_DREAM = new HashSet<>(Set.of(
            i(Material.RED_BED, 6, BOTH, McColor.RED),
            i(Material.PINK_BED, 14, BOTH, McColor.PINK),
            i(Material.GREEN_BED, 6, BOTH, McColor.GREEN),
            i(Material.BLUE_BED, 6, BOTH, McColor.BLUE),
            i(Material.SHEEP_SPAWN_EGG, 12, BOTH, McColor.WHITE),
            i(Material.PHANTOM_SPAWN_EGG, 9, BOTH, McColor.GRAY),
            i(Material.DRAGON_HEAD, 16, BOTH, McColor.PURPLE),
            i(Material.SOUL_SAND, 5, BOTH, McColor.DARK_GRAY),
            i(Material.ENCHANTED_GOLDEN_APPLE, 6, BOTH, McColor.GOLD)
    ));

    public static final HashSet<ShopItem> SHOP_ITEMS_GARDEN = new HashSet<>(Set.of(
            i(Material.COMPOSTER, 5, BOTH, McColor.LIME),
            i(Material.FLOWER_POT, 1, BOTH, McColor.DARK_RED),
            i(Material.DIRT, 2, BOTH, McColor.GRAY),
            i(Material.POPPY, 12, BOTH, McColor.RED),
            i(Material.BLUE_ORCHID, 7, BOTH, McColor.BLUE),
            i(Material.ALLIUM, 11, BOTH, McColor.PURPLE),
            i(Material.AZURE_BLUET, 13, BOTH, McColor.WHITE),
            i(Material.RED_TULIP, 4, BOTH, McColor.RED),
            i(Material.ORANGE_TULIP, 4, BOTH, McColor.GOLD),
            i(Material.WHITE_TULIP, 4, BOTH, McColor.WHITE),
            i(Material.PINK_TULIP, 4, BOTH, McColor.PINK),
            i(Material.CORNFLOWER, 8, BOTH, McColor.BLUE),
            i(Material.OXEYE_DAISY, 5, BOTH, McColor.WHITE),
            i(Material.WITHER_ROSE, 7, BOTH, McColor.BLACK),
            i(Material.SUNFLOWER, 8, BOTH, McColor.YELLOW),
            i(Material.SPORE_BLOSSOM, 15, BOTH, McColor.PINK),
            i(Material.BONE_MEAL, 1, BOTH, McColor.WHITE)
    ));


    public static final HashSet<ShopItem> SHOP_ITEMS_WARPS = new HashSet<>(Set.of(
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
    private static final int[] BACKGROUND_SLOTS = { 9, 10, 11, 12, 14, 15, 16, 17 };
    private static final ItemStack BACKGROUND_FRAME = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
    private static final ItemStack NOT_IN_CURRENT_HOTBAR_FRAME = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    private static final ItemStack
            PAGE_NEUTRAL = new ItemStack(Material.GRAY_CONCRETE),
            PAGE_HOT = new ItemStack(Material.ORANGE_CONCRETE),
            PAGE_COLD = new ItemStack(Material.LIGHT_BLUE_CONCRETE),
            PAGE_WATER = new ItemStack(Material.BLUE_CONCRETE),
            PAGE_NATURE = new ItemStack(Material.GREEN_CONCRETE),
            PAGE_REDSTONE = new ItemStack(Material.RED_CONCRETE),
            PAGE_DREAM = new ItemStack(Material.WHITE_CONCRETE),
            PAGE_GARDEN = new ItemStack(Material.LIME_CONCRETE),
            PAGE_WARPS = new ItemStack(Material.PURPLE_CONCRETE);

    public static void displayShop(Player player, @NotNull HotBar hotBar) {
        displayShop(player, hotBar, 64, 1);
    }

    public static void displayShop(@NotNull Player player, @NotNull HotBar hotBar, int coins, int page) {
        player.closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);

        if (!NOT_IN_CURRENT_HOTBAR_FRAME.displayName().toString().contains("Not available!")){
            ItemMeta meta = NOT_IN_CURRENT_HOTBAR_FRAME.getItemMeta();
            meta.displayName(Component.text(McColor.RED + "Not available!"));
            meta.displayName(Component.text(McColor.GRAY + "You can't buy this item in the current shop phase!"));
            NOT_IN_CURRENT_HOTBAR_FRAME.setItemMeta(meta);
        }

        PAGE_NEUTRAL.editMeta(meta -> meta.displayName(Component.text(McColor.GRAY + "Neutral Items")));
        PAGE_NEUTRAL.removeEnchantment(Enchantment.MENDING);
        PAGE_HOT.editMeta(meta -> meta.displayName(Component.text(McColor.GOLD + "Hot Items")));
        PAGE_HOT.removeEnchantment(Enchantment.MENDING);
        PAGE_COLD.editMeta(meta -> meta.displayName(Component.text(McColor.BLUE + "Cold Items")));
        PAGE_COLD.removeEnchantment(Enchantment.MENDING);
        PAGE_WATER.editMeta(meta -> meta.displayName(Component.text("§1Water Items")));
        PAGE_WATER.removeEnchantment(Enchantment.MENDING);
        PAGE_NATURE.editMeta(meta -> meta.displayName(Component.text(McColor.GREEN + "Nature Items")));
        PAGE_NATURE.removeEnchantment(Enchantment.MENDING);
        PAGE_REDSTONE.editMeta(meta -> meta.displayName(Component.text(McColor.RED + "Redstone Items")));
        PAGE_REDSTONE.removeEnchantment(Enchantment.MENDING);
        PAGE_DREAM.editMeta(meta -> meta.displayName(Component.text(McColor.WHITE + "Dream Items")));
        PAGE_DREAM.removeEnchantment(Enchantment.MENDING);
        PAGE_GARDEN.editMeta(meta -> meta.displayName(Component.text(McColor.LIME + "Garden Items")));
        PAGE_GARDEN.removeEnchantment(Enchantment.MENDING);
        PAGE_WARPS.editMeta(meta -> meta.displayName(Component.text(McColor.PURPLE + "Warp Items")));
        PAGE_WARPS.removeEnchantment(Enchantment.MENDING);

        if (hotBar == OVERTIME) {
            Match.getPlayer(player).setState(Match.PlayerState.SHOP_OVERTIME);
        } else {
            Match.getPlayer(player).setState(Match.PlayerState.SHOP_NORMAL);
        }

        Inventory shop = Bukkit.createInventory(player, 9*6, Component.text("Pre-Round Shop, " + hotBar.name().toLowerCase() + " phase"));

        for (int i : BACKGROUND_SLOTS) shop.setItem(i, BACKGROUND_FRAME);

        HashSet<ShopItem> items = new HashSet<>();
        switch (page) {
            case 2 -> {
                PAGE_HOT.editMeta(meta -> meta.addEnchant(Enchantment.MENDING, 1, true));
                items.addAll(SHOP_ITEMS_HOT);
            }
            case 3 -> {
                PAGE_COLD.editMeta(meta -> meta.addEnchant(Enchantment.MENDING, 1, true));
                items.addAll(SHOP_ITEMS_COLD);
            }
            case 4 -> {
                PAGE_WATER.editMeta(meta -> meta.addEnchant(Enchantment.MENDING, 1, true));
                items.addAll(SHOP_ITEMS_WATER);
            }
            case 5 -> {
                PAGE_REDSTONE.editMeta(meta -> meta.addEnchant(Enchantment.MENDING, 1, true));
                items.addAll(SHOP_ITEMS_REDSTONE);
            }
            case 6 -> {
                PAGE_DREAM.editMeta(meta -> meta.addEnchant(Enchantment.MENDING, 1, true));
                items.addAll(SHOP_ITEMS_DREAM);
            }
            case 7 -> {
                PAGE_NATURE.editMeta(meta -> meta.addEnchant(Enchantment.MENDING, 1, true));
                items.addAll(SHOP_ITEMS_NATURE);
            }
            case 8 -> {
                PAGE_GARDEN.editMeta(meta -> meta.addEnchant(Enchantment.MENDING, 1, true));
                items.addAll(SHOP_ITEMS_GARDEN);
            }
            case 9 -> {
                PAGE_WARPS.editMeta(meta -> meta.addEnchant(Enchantment.MENDING, 1, true));
                items.addAll(SHOP_ITEMS_WARPS);
            }
            default -> {
                PAGE_NEUTRAL.editMeta(meta -> meta.addEnchant(Enchantment.MENDING, 1, true));
                items.addAll(SHOP_ITEMS_NEUTRAL);
            }
        }

        shop.addItem(PAGE_NEUTRAL);
        shop.addItem(PAGE_HOT);
        shop.addItem(PAGE_COLD);
        shop.addItem(PAGE_WATER);
        shop.addItem(PAGE_REDSTONE);
        shop.addItem(PAGE_DREAM);
        shop.addItem(PAGE_NATURE);
        shop.addItem(PAGE_GARDEN);
        shop.addItem(PAGE_WARPS);
        shop.addItem(new ItemStack(Material.GOLD_INGOT, coins));

        for (ShopItem shopItem : items) {
            if (shopItem.hotBar == BOTH || shopItem.hotBar == hotBar) {
                ItemStack item = new ItemStack(shopItem.item, shopItem.price);

                ItemMeta meta = item.getItemMeta();
                meta.displayName(Component.text(shopItem.color + Compring.from(item.displayName())));
                item.setItemMeta(meta);

                shop.addItem(item);
            } else {
                shop.addItem(NOT_IN_CURRENT_HOTBAR_FRAME);
            }
        }

        player.openInventory(shop);
    }

    public static void onItemClicked(Player player, @NotNull ItemStack item, @NotNull Inventory shopInv) {
        ShopItem shopItem = Objects.requireNonNull(getItem(item.getType()));

        ItemStack coinItem = shopInv.getItem(13);
        if (coinItem != null && coinItem.getType() == Material.GOLD_INGOT) {
            int money = coinItem.getAmount();

            if (money >= shopItem.price) {
                String title = Compring.from(player.getOpenInventory().title()).toLowerCase();

                player.getInventory().addItem(new ItemStack(shopItem.item));

                shopInv.remove(Material.GOLD_INGOT);
                if (money - shopItem.price <= 0) {
                    player.closeInventory(InventoryCloseEvent.Reason.PLAYER);

                    finish(player, title);
                    return;
                } else {
                    shopInv.addItem(new ItemStack(Material.GOLD_INGOT, money - shopItem.price));
                }

                if (IntStream.range(0, 9)
                        .mapToObj(s -> player.getInventory().getItem(s))
                        .noneMatch(i -> i == null || i.getType().isAir())
                ) {
                    finish(player, title);
                }
            }
        }
    }

    public static void finish(Player player, @NotNull String title) {
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

    @Contract(pure = true)
    public static @Nullable ShopItem getItem(Material material) {
        for (ShopItem item : SHOP_ITEMS_NEUTRAL) if (item.item == material) return item;
        for (ShopItem item : SHOP_ITEMS_HOT) if (item.item == material) return item;
        for (ShopItem item : SHOP_ITEMS_COLD) if (item.item == material) return item;
        for (ShopItem item : SHOP_ITEMS_WATER) if (item.item == material) return item;
        for (ShopItem item : SHOP_ITEMS_NATURE) if (item.item == material) return item;
        for (ShopItem item : SHOP_ITEMS_REDSTONE) if (item.item == material) return item;
        for (ShopItem item : SHOP_ITEMS_DREAM) if (item.item == material) return item;
        for (ShopItem item : SHOP_ITEMS_GARDEN) if (item.item == material) return item;
        for (ShopItem item : SHOP_ITEMS_WARPS) if (item.item == material) return item;
        return null;
    }

    @Contract("_, _, _, _ -> new")
    private static @NotNull ShopItem i(Material m, Integer p, HotBar h, McColor c) {
        return new ShopItem(m, p, h, c);
    }

    public record ShopItem(Material item, Integer price, HotBar hotBar, McColor color) {}
    public enum HotBar { BOTH, NORMAL, OVERTIME }
}
