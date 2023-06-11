package net.hectus.hectusblockbattles.warps;

import net.hectus.hectusblockbattles.structures.v2.Structure;

import static net.hectus.hectusblockbattles.warps.WarpSettings.*;
import static net.hectus.hectusblockbattles.warps.WarpSettings.Class;

// TODO: Hell Warp needs to be build and configured.

public enum Warp {
    DEFAULT(
            1,
            true, true,
            new Structure.Cord(-276,59,148),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE,
            Class.NEUTRAL, Class.WATER, Class.REDSTONE, Class.NATURE
    ), NETHER(
            0.8,
            false, true,
            new Structure.Cord(-199,19,146),
            Temperature.WARM, Dimension.NETHER, Layer.UNDERGROUND,
            Class.NEUTRAL, Class.HOT, Class.REDSTONE
    ), ICE(
            0.69,
            false, true,
            new Structure.Cord(-128,20,152),
            Temperature.COLD, Dimension.OVERWORLD, Layer.SURFACE,
            Class.NEUTRAL, Class.COLD, Class.WATER, Class.NATURE
    ), SNOW(
            0.8,
            false, true,
            new Structure.Cord(-354,13,249),
            Temperature.COLD, Dimension.OVERWORLD, Layer.UNDERGROUND,
            Class.NEUTRAL, Class.COLD, Class.WATER, Class.DREAM
    ), CLIFF(
            0.67,
            false, true,
            new Structure.Cord(7,30,180),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE,
            Class.NEUTRAL, Class.COLD, Class.WATER
    ), UNDERWATER(
            0.73,
            false, true,
            new Structure.Cord(-82,22,186),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.NONE,
            Class.NEUTRAL, Class.COLD, Class.WATER, Class.NATURE
    ), VOID(
            0.67,
            true, false,
            new Structure.Cord(-134,22,306),
            Temperature.COLD, Dimension.NONE, Layer.NONE,
            Class.NEUTRAL, Class.COLD, Class.WATER, Class.NATURE
    ), REDSTONE(
            0.65,
            false, true,
            new Structure.Cord(-178,33,349),
            Temperature.MEDIUM, Dimension.NONE, Layer.NONE,
            Class.NEUTRAL, Class.HOT, Class.COLD, Class.REDSTONE
    ), WOOD(
            1,
            false, true,
            new Structure.Cord(-221,22,473),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE,
            Class.NEUTRAL, Class.WATER, Class.NATURE
    ), DESERT(
            0.8,
            false, true,
            new Structure.Cord(-54,8,520),
            Temperature.WARM, Dimension.OVERWORLD, Layer.SURFACE,
            Class.NEUTRAL, Class.HOT, Class.REDSTONE
    ), AETHER(
            0.8,
            false, true,
            new Structure.Cord(-363,-3,606),
            Temperature.MEDIUM, Dimension.NONE, Layer.SKY,
            Class.WATER, Class.DREAM
    ), BOOK(
            0.2,
            false, true,
            new Structure.Cord(-387,22,472),
            Temperature.MEDIUM, Dimension.NONE, Layer.NONE,
            Class.NEUTRAL, Class.DREAM
    ), SUN(
            0.75,
            false, true,
            new Structure.Cord(-345,13,148),
            Temperature.WARM, Dimension.OVERWORLD, Layer.SKY,
            Class.NEUTRAL, Class.HOT
    ), MUSHROOM(
            0.6,
            false, false,
            new Structure.Cord(-306,16,28),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE,
            Class.WATER, Class.NATURE, Class.DREAM
    ), END(
            0.85,
            true, true,
            new Structure.Cord(-17,29,19),
            Temperature.COLD, Dimension.END, Layer.NONE,
            Class.COLD, Class.WATER, Class.REDSTONE, Class.DREAM
    ), AMETHYST(
            0.3,
            false, true,
            new Structure.Cord(-341,62,324),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.UNDERGROUND,
            Class.NEUTRAL, Class.COLD
    ), HEAVEN(
            0.5,
            false, true,
            new Structure.Cord(248,-27,80),
            Temperature.MEDIUM, Dimension.NONE, Layer.SKY,
            Class.NEUTRAL, Class.COLD, Class.NATURE, Class.DREAM
    ), HELL(
            0.5,
            false, true,
            new Structure.Cord(-199,19,146),
            Temperature.WARM, Dimension.NONE, Layer.UNDERGROUND,
            Class.NEUTRAL, Class.HOT, Class.REDSTONE
    ), INDUSTRIAL(
            0.8,
            false, false,
            new Structure.Cord(-147,-59,986),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE,
            Class.NEUTRAL, Class.REDSTONE, Class.HOT
    );

    public final double chance;
    public final boolean night;
    public final boolean mobs;
    public final Structure.Cord middle;
    public final Temperature temperature;
    public final Dimension dimension;
    public final Layer layer;
    public final Class[] allow;

    Warp(double c, boolean n, boolean mo, Structure.Cord mi, Temperature t, Dimension d, Layer l, Class... a) {
        chance = c;
        night = n;mobs = mo;
        middle = mi;
        temperature = t;dimension = d;layer = l;
        allow = a;
    }
}
