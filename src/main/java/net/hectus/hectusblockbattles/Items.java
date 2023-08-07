package net.hectus.hectusblockbattles;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.warps.WarpSettings;
import net.hectus.util.Formatter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.BreakIterator;
import java.util.*;

import static net.hectus.color.McColor.*;
import static net.hectus.hectusblockbattles.InGameShop.HotBar.*;
import static net.hectus.hectusblockbattles.Items.Quality.*;
import static net.hectus.hectusblockbattles.Items.Rarity.*;
import static net.hectus.hectusblockbattles.Items.Type.*;
import static net.hectus.hectusblockbattles.warps.WarpSettings.Class.*;

public enum Items {
    // ========== NEUTRAL ITEMS ==========
    OAK_DOOR(ATTACK, UNCOMMON, BAD, true, 2, null),
    IRON_BARS(SPECIAL, RARE, GOOD, true, 1, null, new Buff("jail", "3", true)),
    PURPLE_WOOL(OVERTIME, SPECIAL, LEGENDARY, PERFECT, false, 25, null),
    NAME_TAG(COUNTER, UNCOMMON, BAD, true, 7, null),
    SPRUCE_TRAPDOOR(NORMAL, COUNTER, COMMON, DECENT, false, 4, List.of(NEUTRAL, WATER, NATURE)),
    IRON_TRAPDOOR(OVERTIME, COUNTER, UNCOMMON, DECENT, false, 5, List.of(NEUTRAL, WATER, REDSTONE)),
    CAULDRON(ATTACK, UNCOMMON, DECENT, false, 9, null),
    GOLD_BLOCK(COUNTERATTACK, RARE, DECENT, false, 8, null),
    BLACK_WOOL(BUFF, RARE, BAD, false, 6, null, new Buff("blindness", "0", false), new Buff("blindness", "0", true)),
    SCULK(COUNTER, RARE, GOOD, false, 7, null, new Buff("darkness", "0", true), Buff.luck(-10, true)),
    GREEN_CARPET(COUNTERATTACK, EPIC, DECENT, false, 7, null),
    CYAN_CARPET(COUNTER, RARE, DECENT, false, 7, List.of(REDSTONE, DREAM), Buff.extraTurn()),
    ENDER_PEARL(NORMAL, COUNTER, UNCOMMON, DECENT, true, 10, null, Buff.extraTurn()),
    CHORUS_FRUIT(OVERTIME, COUNTER, RARE, BAD, true, 8, null),
    IRON_SHOVEL(COUNTER, RARE, BAD, true, 5, List.of(HOT), Buff.extraTurn()),
    STONECUTTER(COUNTER, EPIC, DECENT, false, 7, null, Buff.extraTurn()),
    MAGENTA_GLAZED_TERRACOTTA(COUNTERATTACK, RARE, BAD, false, 6, List.of(NEUTRAL, HOT, COLD, WATER, NATURE)),
    SPONGE(COUNTERATTACK, RARE, GOOD, false, 8, List.of(NEUTRAL, WATER, DREAM), new Buff("rain", "0", false), Buff.extraTurn(), Buff.luck(5, false), new Buff("sponge.bucket_jump_boost", "10", false)),
    // ============ HOT ITEMS ============
    MAGMA_BLOCK(ATTACK, UNCOMMON, DECENT, false, 5, null),
    NETHERRACK(COUNTERATTACK, UNCOMMON, BAD, false, 4, List.of(HOT, COLD, WATER)),
    LAVA_BUCKET(BUFF, RARE, GOOD, true, 10, null, new Buff("burns", "0", true)),
    FLINT_AND_STEEL(COUNTER, EPIC, BAD, false, 4, List.of(COLD, REDSTONE, NATURE), new Buff("blindness", "0", false), new Buff("blindness", "0", true), new Buff("glowing", "0", false), new Buff("glowing", "0", true)),
    ORANGE_WOOL(COUNTER, UNCOMMON, BAD, false, 5, List.of(REDSTONE, DREAM)),
    CAMPFIRE(COUNTERATTACK, COMMON, DECENT, false, 6, List.of(NEUTRAL, HOT, COLD), Buff.extraTurn(), Buff.luck(10, false)),
    BLAZE_SPAWN_EGG(ATTACK, EPIC, GOOD, false, 9, null, new Buff("blaze_buff", "0", false)),
    PIGLIN_SPAWN_EGG(SPECIAL, RARE, POOR, true, 6, null, Buff.luck(-50, false), new Buff("blindness", "0", false), new Buff("slowness", "0", false)),
    RESPAWN_ANCHOR(BUFF, RARE, GOOD, false, 8, null, new Buff("extra_turn", "50% Chance", false)),
    // =========== COLD ITEMS ===========
    PACKED_ICE(ATTACK, COMMON, BAD, false, 4, null),
    BLUE_ICE(COUNTER, UNCOMMON, BAD, false, 4, List.of(COLD, WATER, DREAM)),
    SPRUCE_LEAVES(COUNTERATTACK, UNCOMMON, BAD, false, 3, List.of(NEUTRAL, NATURE)),
    LIGHT_BLUE_WOOL(ATTACK, COMMON, BAD, false, 4, null),
    WHITE_WOOL(COUNTER, COMMON, DECENT, false, 7, List.of(NEUTRAL, WATER, REDSTONE, DREAM), new Buff("slowness", "255", false), new Buff("slowness", "255", true)),
    POWDER_SNOW(SPECIAL, RARE, GOOD, false, 8, null, new Buff("freezes", "0", true)),
    SNOWBALL(SPECIAL, EPIC, GOOD, false, 7, List.of(HOT), Buff.extraTurn(), Buff.luck(15, false), new Buff("glowing", "0", true)),
    POLAR_BEAR_SPAWN_EGG(BUFF, EPIC, GOOD, false, 8, null, new Buff("speed", "3", true), new Buff("no_jump", "0", true)),
    // =========== WATER ITEMS ===========
    BRAIN_CORAL_BLOCK(COUNTERATTACK, RARE, POOR, false, 4, List.of(REDSTONE)),
    HORN_CORAL(COUNTERATTACK, COMMON, BAD, false, 5, List.of(NEUTRAL, NATURE, WATER)),
    FIRE_CORAL(COUNTERATTACK, UNCOMMON, BAD, false, 5, List.of(HOT, REDSTONE)),
    FIRE_CORAL_FAN(COUNTER, UNCOMMON, DECENT, false, 6, List.of(HOT, REDSTONE), new Buff("extra_turn", "80% Chance", false)),
    SEA_LANTERN(BUFF, UNCOMMON, PERFECT, false, 9, null, new Buff("revive", "1", false)),
    WATER_BUCKET(COUNTER, RARE, DECENT, true, 6, null),
    DRIED_KELP_BLOCK(ATTACK, UNCOMMON, DECENT, false, 8, null, new Buff("jump_boost", "1", false)),
    OAK_BOAT(COUNTER, RARE, GOOD, false, 8, null, Buff.extraTurn()),
    AXOLOTL_SPAWN_EGG(LUCK, UNCOMMON, DECENT, false, 10, null),
    VERDANT_FROGLIGHT(COUNTERATTACK, UNCOMMON, POOR, false, 5, List.of(DREAM)),
    PUFFERFISH_BUCKET(BUFF, RARE, GOOD, false, 16, null, new Buff("death_counter", "2", true)),
    SPLASH_POTION(SPECIAL);

    public final InGameShop.HotBar hotBar;
    public final Type type;
    public final Rarity rarity;
    public final Quality quality;
    public final boolean use;
    public final int cost;
    public final List<WarpSettings.Class> counters;
    public final Buff[] buffs;

    Items(InGameShop.HotBar hotBar, Type type, Rarity rarity, Quality quality, boolean use, int cost, List<WarpSettings.Class> counters, Buff... buffs) {
        this.hotBar = hotBar;
        this.type = type;
        this.rarity = rarity;
        this.quality = quality;
        this.use = use;
        this.cost = cost;
        this.counters = counters;
        this.buffs = buffs;
    }

    Items(Type type, Rarity rarity, Quality quality, boolean use, int cost, List<WarpSettings.Class> counters, Buff... buffs) {
        this.hotBar = BOTH;
        this.type = type;
        this.rarity = rarity;
        this.quality = quality;
        this.use = use;
        this.cost = cost;
        this.counters = counters;
        this.buffs = buffs;
    }

    public @NotNull ItemStack getItemStack(@NotNull Items bbItem, Locale l) {
        ItemStack item = new ItemStack(Material.valueOf(bbItem.name()));
        ItemMeta meta = item.getItemMeta();

        List<Component> lore = new ArrayList<>();

        lore.add(Compring.format("§e❖ %s: %s", t("item.type", l), type.get(l)));
        lore.add(Compring.format("§e❖ %s: %s", t("item.hotbar", l), t("hotbar." + hotBar.name(), l)));
        lore.add(Compring.format("§e❖ %s: %s", t("item.rarity", l), rarity.get(l)));

        lore.add(Compring.format("§e❖ %s:", t("item.description", l)));
        for (String line : splitStringIntoLines(t("item." + name() + ".description", l))) {
            lore.add(Component.text("   §9| " + line));
        }

        if (!counters.isEmpty()) {
            String countersString = Formatter.toPascalCase(counters.toString().replace("[", "").replace("]", ""));
            lore.add(Component.text(String.format("§e⚔ %s:", t("item.counters", l)) + countersString)); // Choices: ⛨⚔ (I need to test what looks better)
        }

        if (use) {
            lore.add(Component.text("§8——————————"));
            lore.add(Compring.format("§e➽ %s:", t("item.use", l)));
            for (String line : splitStringIntoLines(t("item." + name() + ".use", l))) {
                lore.add(Component.text("   §9| " + line));
            }
        }

        if (buffs.length != 0) {
            lore.add(Component.text("§8——————————"));
            lore.add(Compring.format("§e↑ %s:", t("item.buffs", l)));
            for (Buff buff : buffs) {
                lore.add(Component.text("   §9| " + buff.get(l)));
            }
        }

        lore.add(Component.text("§8——————————"));
        lore.add(Compring.format("§e✪ %s: %s", t("item.quality", l), quality.get(l)));
        lore.add(Component.text("§e₪ " + Translation.get("item.cost", l, cost)));

        meta.lore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public enum Type {
        SUMMON(GREEN), COUNTER(YELLOW), COUNTERATTACK(GOLD), ATTACK(RED), SPECIAL(PINK), COMBO(PURPLE), LUCK(LIME), ALIEN_CUM(WHITE), WARP(DARK_AQUA), BUFF(AQUA);

        final McColor color;
        Type(McColor color) { this.color = color; }

        public @NotNull String get(Locale locale) {
            return color + t("item.types." + name().toLowerCase(), locale);
        }
    }

    public enum Rarity {
        COMMON("§7✫ "), UNCOMMON("§2✫✫ "), RARE("§9✫✫✫ "), EPIC("§5✫✫✫✫ "), LEGENDARY("§6✫✫✫✫✫ "), MYTHIC("§b✪ "), SEASONAL("§e❇ ");

        final String base;
        Rarity(String base) { this.base = base; }

        public @NotNull String get(Locale locale) {
            return base + t("item.rarity." + name().toLowerCase(), locale);
        }
    }

    public enum Quality {
        POOR("§c➀ "), BAD("§6➁ "), DECENT("§e➂ "), GOOD("§2➃ "), PERFECT("§b➄ ");

        final String base;
        Quality(String base) { this.base = base; }

        public @NotNull String get(Locale locale) {
            return base + t("item.quality." + name().toLowerCase(), locale);
        }
    }

    public record Buff(String key, String value, boolean opponent) {
        public @NotNull String get(Locale locale) {
            int v = Integer.parseUnsignedInt(value.replace("%", ""));
            return (v == 0 ? YELLOW : (v > 0 ? GREEN : RED)) + "   | " + t("item.buff."+key, locale) + (opponent ? t("item.buff.opponent", locale) : "") + (v == 0 ? (": " + v) : "");
        }

        @Contract(value = "_, _ -> new", pure = true)
        public static @NotNull Buff luck(int luck, boolean opponent) {
            return new Buff("luck", String.valueOf(luck), opponent);
        }

        @Contract(" -> new")
        public static @NotNull Buff extraTurn() {
            return new Buff("extra_turn", "0", false);
        }
    }

    /**
     * 100% Not Generated By ChatGPT (NO CLICKBAIT!!!) This just splits a very long String into multiple lines. It
     * prefers to split when a sentence ends for better readability, if possible.
     *
     * @param text The long text that should be formatted.
     *
     * @return A list with the text split into lines, each list entry is one line.
     */
    private static @NotNull List<String> splitStringIntoLines(String text) {
        List<String> lines = new ArrayList<>();
        StringJoiner currentLine = new StringJoiner(" ");
        BreakIterator iterator = BreakIterator.getSentenceInstance();
        iterator.setText(text);

        int start = iterator.first();
        int end = iterator.next();

        while (end != BreakIterator.DONE) {
            String sentence = text.substring(start, end);
            int sentenceLength = sentence.trim().length();

            if (currentLine.length() + sentenceLength > 28) {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringJoiner(" ");
                }

                if (sentenceLength <= 28) {
                    lines.add(sentence);
                } else {
                    int lastSpaceIndex = sentence.substring(0, 28).lastIndexOf(" ");
                    lines.add(sentence.substring(0, lastSpaceIndex).trim());
                }
            } else {
                currentLine.add(sentence.trim());
            }

            start = end;
            end = iterator.next();
        }

        if (currentLine.length() > 0) lines.add(currentLine.toString());

        return lines;
    }

    private static @NotNull String t(String k, Locale l) {
        return Translation.get(k, l);
    }
}
