package net.hectus.hectusblockbattles;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.warps.Warp;
import org.bukkit.Material;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public enum Turn {
    PURPLE_WOOL(Type.ATTACK, McColor.PURPLE, Material.PURPLE_WOOL, null),
    NETHER_WARP(Type.WARP, McColor.DARK_RED, Material.NETHERRACK, Warp.NETHER),
    ICE_WARP(Type.WARP, McColor.BLUE, Material.ICE, Warp.ICE),
    SNOW_WARP(Type.WARP, McColor.WHITE, Material.SNOW, Warp.SNOW),
    CLIFF_WARP(Type.WARP, McColor.GRAY, Material.STONE, Warp.CLIFF),
    UNDERWATER_WARP(Type.WARP, McColor.DARK_AQUA, Material.PRISMARINE, Warp.UNDERWATER),
    VOID_WARP(Type.WARP, McColor.BLACK, Material.BLACK_CONCRETE, Warp.VOID),
    REDSTONE_WARP(Type.WARP, McColor.DARK_RED, Material.REDSTONE_BLOCK, Warp.REDSTONE),
    WOOD_WARP(Type.WARP, McColor.GREEN, Material.OAK_LOG, Warp.WOOD),
    DESERT_WARP(Type.WARP, McColor.YELLOW, Material.SAND, Warp.DESERT),
    AETHER_WARP(Type.WARP, McColor.RESET, Material.WHITE_CONCRETE, Warp.AETHER),
    BOOK_WARP(Type.WARP, McColor.GOLD, Material.BOOKSHELF, Warp.BOOK),
    SUN_WARP(Type.WARP, McColor.YELLOW, Material.SHROOMLIGHT, Warp.SUN),
    MUSHROOM_WARP(Type.WARP, McColor.WHITE, Material.BROWN_MUSHROOM_BLOCK, Warp.MUSHROOM),
    END_WARP(Type.WARP, McColor.YELLOW, Material.END_STONE, Warp.END),
    AMETHYST_WARP(Type.WARP, McColor.PINK, Material.AMETHYST_BLOCK, Warp.AMETHYST),
    HEAVEN_WARP(Type.WARP, McColor.WHITE, Material.WHITE_WOOL, Warp.HEAVEN),
    HELL_WARP(Type.WARP, McColor.DARK_RED, Material.NETHER_BRICKS, Warp.HELL),
    INDUSTRIAL_WARP(Type.WARP, McColor.DARK_GRAY, Material.IRON_BLOCK, Warp.INDUSTRIAL);

    public final Type type;
    public final McColor color;
    public final Material material;

    @Contract(pure = true)
    Turn(Type t, McColor c, Material m, @Nullable Warp warp) {
        type = t;
        color = c;
        material = m;
    }

    public enum Type { ATTACK, COUNTER, COUNTERATTACK, WARP, BUFF, DEFENSE }
}
