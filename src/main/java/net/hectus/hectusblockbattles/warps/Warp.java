package net.hectus.hectusblockbattles.warps;

import net.hectus.hectusblockbattles.HBB;
import org.bukkit.Location;

import static net.hectus.hectusblockbattles.warps.WarpSettings.*;

// TODO: Hell Warp needs to be build and configured.

public enum Warp {
    DEFAULT(1,true, true, loc(-166, 25, 4), loc(-158, 29, 12), Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE, WarpSettings.Class.NEUTRAL, WarpSettings.Class.WATER, WarpSettings.Class.REDSTONE, WarpSettings.Class.NATURE),
    NETHER(0.8, false, true, loc(-203,19,142), loc(-195,23,150), Temperature.WARM, Dimension.NETHER, Layer.UNDERGROUND, WarpSettings.Class.NEUTRAL, WarpSettings.Class.HOT, WarpSettings.Class.REDSTONE),
    ICE(0.69, false, true, loc(-132,20,148), loc(-124,24,156),  Temperature.COLD, Dimension.OVERWORLD, Layer.SURFACE, WarpSettings.Class.NEUTRAL, WarpSettings.Class.COLD, WarpSettings.Class.WATER, WarpSettings.Class.NATURE),
    SNOW(0.8, false, true, loc(-358, 13, 245), loc(-350, 17, 253), Temperature.COLD, Dimension.OVERWORLD, Layer.UNDERGROUND, WarpSettings.Class.NEUTRAL, WarpSettings.Class.COLD, WarpSettings.Class.WATER, WarpSettings.Class.DREAM),
    CLIFF(0.67, false, true, loc(3,30,176), loc(11,34,184), Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE, WarpSettings.Class.NEUTRAL, WarpSettings.Class.COLD, WarpSettings.Class.WATER),
    UNDERWATER(0.73, false, true, loc(-83, 33, 400), loc(-75, 37, 408), Temperature.MEDIUM, Dimension.OVERWORLD, Layer.NONE, WarpSettings.Class.NEUTRAL, WarpSettings.Class.COLD, WarpSettings.Class.WATER, WarpSettings.Class.NATURE),
    VOID(0.67, true, false, loc(-138, 22, 302), loc(-130, 26, 310), Temperature.COLD, Dimension.NONE, Layer.NONE, WarpSettings.Class.NEUTRAL, WarpSettings.Class.COLD, WarpSettings.Class.WATER, WarpSettings.Class.NATURE),
    REDSTONE(0.65, false, true, loc(-182, 33, 345), loc(-174, 37, 353), Temperature.MEDIUM, Dimension.NONE, Layer.NONE, WarpSettings.Class.NEUTRAL, WarpSettings.Class.HOT, WarpSettings.Class.COLD, WarpSettings.Class.REDSTONE),
    FOREST(1, false, true, loc(-225, 22, 469), loc(-217, 26, 477), Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE, WarpSettings.Class.NEUTRAL, WarpSettings.Class.WATER, WarpSettings.Class.NATURE),
    DESERT(0.8, false, true, loc(37, 47, 431), loc(45, 51, 439), Temperature.WARM, Dimension.OVERWORLD, Layer.SURFACE, WarpSettings.Class.NEUTRAL, WarpSettings.Class.HOT, WarpSettings.Class.REDSTONE),
    AETHER(0.8, false, true, loc(-367, -3, 602), loc(-359, 1, 610), Temperature.MEDIUM, Dimension.NONE, Layer.SKY, WarpSettings.Class.WATER, WarpSettings.Class.DREAM),
    BOOK(0.2, false, true, loc(-391, 22, 468), loc(-383, 26, 476), Temperature.MEDIUM, Dimension.NONE, Layer.NONE, WarpSettings.Class.NEUTRAL, WarpSettings.Class.DREAM),
    SUN(0.75, false, true, loc(-349, 13, 144), loc(-341, 17, 152), Temperature.WARM, Dimension.OVERWORLD, Layer.SKY, WarpSettings.Class.NEUTRAL, WarpSettings.Class.HOT),
    MUSHROOM(0.6, false, false, loc(-310, 16, 24), loc(-302, 20, 32), Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE, WarpSettings.Class.WATER, WarpSettings.Class.NATURE, WarpSettings.Class.DREAM),
    END(0.85, true, true, loc(-21, 29, 15), loc(-13, 33, 23), Temperature.COLD, Dimension.END, Layer.NONE, WarpSettings.Class.COLD, WarpSettings.Class.WATER, WarpSettings.Class.REDSTONE, WarpSettings.Class.DREAM),
    AMETHYST(0.3, false, true, loc(-345, 62, 320), loc(-337, 66, 328), Temperature.MEDIUM, Dimension.OVERWORLD, Layer.UNDERGROUND, WarpSettings.Class.NEUTRAL, WarpSettings.Class.COLD),
    HEAVEN(0.5, false, true, loc(0,0,0), loc(0,0,0), Temperature.MEDIUM, Dimension.NONE, Layer.SKY, WarpSettings.Class.NEUTRAL, WarpSettings.Class.COLD, WarpSettings.Class.NATURE, WarpSettings.Class.DREAM),
    HELL(0.5, false, true, loc(0,0,0), loc(0,0,0), Temperature.WARM, Dimension.NONE, Layer.UNDERGROUND, WarpSettings.Class.NEUTRAL, WarpSettings.Class.HOT, WarpSettings.Class.REDSTONE),
    
    // TODO: Add industrial as structure & his things
    INDUSTRIAL(0.8, false, false, loc(-147,-59,986), loc(-139,-55,994), Temperature.MEDIUM, Dimension.OVERWORLD, Layer.SURFACE, WarpSettings.Class.NEUTRAL, WarpSettings.Class.REDSTONE, WarpSettings.Class.HOT);

    private final double chance;
    private final boolean night;
    private final boolean mobs;
    private final Location corner1; //north-west bottom corner
    private final Location corner2; //south-east top corner
    private final Temperature temperature;
    private final Dimension dimension;
    private final Layer layer;
    private final WarpSettings.Class[] allow;

    public boolean isNight() {
        return night;
    }

    public double getChance() {
        return chance;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Layer getLayer() {
        return layer;
    }

    public WarpSettings.Class[] getAllow() {
        return allow;
    }

    public boolean isMobs() {
        return mobs;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    Warp(double chance, boolean night, boolean mobs, Location corner1, Location corner2, Temperature temperature, Dimension dimension, Layer layer, WarpSettings.Class... allow) {
        this.chance = chance;
        this.night = night;
        this.mobs = mobs;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.temperature = temperature;
        this.dimension = dimension;
        this.layer = layer;
        this.allow = allow;
    }
    
    private static Location loc(int x, int y, int z) {
        return new Location(HBB.WORLD, x, y, z);
    }
}
