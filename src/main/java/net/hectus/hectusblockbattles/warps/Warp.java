package net.hectus.hectusblockbattles.warps;

import net.hectus.hectusblockbattles.util.Cord;
import org.jetbrains.annotations.Contract;

import static net.hectus.hectusblockbattles.warps.WarpSettings.Class;
import static net.hectus.hectusblockbattles.warps.WarpSettings.*;

public enum Warp {
    DEFAULT(
            1.00,
            false, true,
            new Cord(-276,59,148),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE,
            new Class[] { Class.NEUTRAL, Class.WATER, Class.NATURE, Class.REDSTONE },
            new Class[] { Class.HOT, Class.COLD, Class.DREAM }
    ), NETHER(
            0.80,
            true, true,
            new Cord(-199,19,146),
            Temperature.WARM, Dimension.NETHER, Layer.UNDERGROUND,
            new Class[] { Class.NEUTRAL, Class.HOT, Class.REDSTONE },
            new Class[] { Class.COLD, Class.WATER, Class.NATURE, Class.DREAM }
    ), ICE(
            0.69,
            false, true,
            new Cord(-128,20,152),
            Temperature.COLD, Dimension.OVERWORLD, Layer.SURFACE,
            new Class[] { Class.NEUTRAL, Class.COLD, Class.WATER, Class.NATURE },
            new Class[] { Class.HOT, Class.REDSTONE, Class.DREAM }
    ), SNOW(
            0.80,
            false, true,
            new Cord(-354,13,249),
            Temperature.COLD, Dimension.OVERWORLD, Layer.UNDERGROUND,
            new Class[] { Class.NEUTRAL, Class.COLD, Class.WATER, Class.DREAM },
            new Class[] { Class.HOT, Class.NATURE, Class.REDSTONE }
    ), CLIFF(
            0.67,
            false, true,
            new Cord(7,30,180),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE,
            new Class[] { Class.NEUTRAL, Class.COLD, Class.WATER },
            new Class[] { Class.HOT, Class.NATURE, Class.REDSTONE, Class.DREAM }
    ), UNDERWATER(
            0.73,
            true, true,
            new Cord(-82,22,186),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.NONE,
            new Class[] { Class.NEUTRAL, Class.COLD, Class.WATER, Class.NATURE },
            new Class[] { Class.HOT, Class.REDSTONE, Class.DREAM }
    ), VOID(
            0.30,
            true, false,
            new Cord(-134,22,306),
            Temperature.COLD, Dimension.NONE, Layer.NONE,
            new Class[] { Class.NEUTRAL, Class.COLD, Class.WATER, Class.NATURE },
            new Class[] { Class.HOT, Class.WATER, Class.NATURE, Class.DREAM }
    ), REDSTONE(
            0.65,
            false, false,
            new Cord(-178,33,349),
            Temperature.MEDIUM, Dimension.NONE, Layer.NONE,
            new Class[] { Class.NEUTRAL, Class.HOT, Class.COLD, Class.REDSTONE },
            new Class[] { Class.WATER, Class.NATURE, Class.DREAM }
    ), WOOD(
            1.00,
            false, true,
            new Cord(-221,22,473),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE,
            new Class[] { Class.NEUTRAL, Class.WATER, Class.NATURE },
            new Class[] { Class.HOT, Class.COLD, Class.REDSTONE, Class.DREAM }
    ), DESERT(
            0.80,
            false, false,
            new Cord(-54,8,520),
            Temperature.WARM, Dimension.OVERWORLD, Layer.SURFACE,
            new Class[] { Class.NEUTRAL, Class.HOT, Class.REDSTONE },
            new Class[] { Class.COLD, Class.WATER, Class.NATURE, Class.DREAM }
    ), AETHER(
            0.50,
            false, true,
            new Cord(-363,-3,606),
            Temperature.MEDIUM, Dimension.NONE, Layer.SKY,
            new Class[] { Class.WATER, Class.NATURE, Class.DREAM },
            new Class[] { Class.NEUTRAL, Class.HOT, Class.COLD, Class.REDSTONE }
    ), BOOK(
            0.20,
            false, false,
            new Cord(-387,22,472),
            Temperature.MEDIUM, Dimension.NONE, Layer.NONE,
            new Class[] { Class.NEUTRAL, Class.DREAM },
            new Class[] { Class.HOT, Class.COLD, Class.WATER, Class.NATURE, Class.REDSTONE }
    ), AMETHYST(
            0.30,
            false, false,
            new Cord(-341,62,324),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.UNDERGROUND,
            new Class[] { Class.NEUTRAL, Class.COLD },
            new Class[] { Class.HOT, Class.COLD, Class.WATER, Class.NATURE, Class.REDSTONE, Class.DREAM }
    ), SUN(
            0.30,
            false, false,
            new Cord(-345,13,148),
            Temperature.WARM, Dimension.OVERWORLD, Layer.SKY,
            new Class[] { Class.NEUTRAL, Class.HOT },
            new Class[] { Class.COLD, Class.WATER, Class.NATURE, Class.REDSTONE, Class.DREAM }
    ), MUSHROOM(
            0.60,
            false, true,
            new Cord(-306,16,28),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE,
            new Class[] { Class.WATER, Class.NATURE, Class.DREAM },
            new Class[] { Class.NEUTRAL, Class.HOT, Class.COLD }
    ), END(
            0.60,
            true, false,
            new Cord(-17,29,19),
            Temperature.COLD, Dimension.END, Layer.NONE,
            new Class[] { Class.COLD, Class.WATER, Class.REDSTONE, Class.DREAM },
            new Class[] { Class.NEUTRAL, Class.HOT, Class.NATURE }
    ), HEAVEN(
            0.55,
            false, true,
            new Cord(248,-27,80),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SKY,
            new Class[] { Class.NEUTRAL, Class.COLD, Class.NATURE, Class.DREAM },
            new Class[] { Class.HOT, Class.WATER, Class.REDSTONE }
    ), HELL(
            0.55,
            true, false,
            new Cord(-199,19,146),
            Temperature.WARM, Dimension.NONE, Layer.UNDERGROUND,
            new Class[] { Class.NEUTRAL, Class.HOT, Class.REDSTONE },
            new Class[] { Class.COLD, Class.WATER, Class.NATURE, Class.DREAM }
    ), INDUSTRIAL(
            0.50,
            true, false,
            new Cord(-147,-59,986),
            Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE,
            new Class[] { Class.REDSTONE, Class.HOT, Class.WATER },
            new Class[] { Class.NEUTRAL, Class.NATURE, Class.COLD, Class.DREAM }
    );

    public final double chance;
    public final boolean night, dark;
    public final Cord middle;
    public final Temperature temperature;
    public final Dimension dimension;
    public final Layer layer;
    public final Class[] allow, deny;

    @Contract(pure = true)
    Warp(double c, boolean n, boolean da, Cord mi, Temperature t, Dimension d, Layer l, Class[] ac, Class[] dc) {
        chance = c;
        night = n; dark = da;
        middle = mi;
        temperature = t; dimension = d; layer = l;
        allow = ac; deny = dc;
    }
}
