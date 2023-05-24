package net.hectus.hectusblockbattles.warps;

import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;

//todo: hell need to be build & configured.

public enum Warp {
    DEFAULT(1,true, true, new Location(Bukkit.getWorld("world"), -166, 25, 4), new Location(Bukkit.getWorld("world"), -158, 29, 12), WarpSettings.Temperature.MEDIUM, WarpSettings.Dimension.OVERWORLD, WarpSettings.Layer.SURFACE, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.WATER, WarpSettings.Classes.REDSTONE, WarpSettings.Classes.NATURE),
    NETHER(0.8, false, true, new Location(Bukkit.getWorld("world"), -203,19,142), new Location(Bukkit.getWorld("world"), -195,23,150), WarpSettings.Temperature.WARM, WarpSettings.Dimension.NETHER, WarpSettings.Layer.UNDERGROUND, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.HOT, WarpSettings.Classes.REDSTONE),
    ICE(0.69, false, true, new Location(Bukkit.getWorld("world"), -132,20,148), new Location(Bukkit.getWorld("world"), -124,24,156),  WarpSettings.Temperature.COLD, WarpSettings.Dimension.OVERWORLD, WarpSettings.Layer.SURFACE, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.COLD, WarpSettings.Classes.WATER, WarpSettings.Classes.NATURE),
    SNOW(0.8, false, true, new Location(Bukkit.getWorld("world"), -358, 13, 245), new Location(Bukkit.getWorld("world"), -350, 17, 253), WarpSettings.Temperature.COLD, WarpSettings.Dimension.OVERWORLD, WarpSettings.Layer.UNDERGROUND, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.COLD, WarpSettings.Classes.WATER, WarpSettings.Classes.DREAM),
    CLIFF(0.67, false, true, new Location(Bukkit.getWorld("world"), 3,30,176), new Location(Bukkit.getWorld("world"), 11,34,184), WarpSettings.Temperature.MEDIUM, WarpSettings.Dimension.OVERWORLD, WarpSettings.Layer.SURFACE, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.COLD, WarpSettings.Classes.WATER),
    UNDERWATER(0.73, false, true, new Location(Bukkit.getWorld("world"), -83, 33, 400), new Location(Bukkit.getWorld("world"), -75, 37, 408), WarpSettings.Temperature.MEDIUM, WarpSettings.Dimension.OVERWORLD, WarpSettings.Layer.NONE, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.COLD, WarpSettings.Classes.WATER, WarpSettings.Classes.NATURE),
    VOID(0.67, true, false, new Location(Bukkit.getWorld("world"), -138, 22, 302), new Location(Bukkit.getWorld("world"), -130, 26, 310), WarpSettings.Temperature.COLD, WarpSettings.Dimension.NONE, WarpSettings.Layer.NONE, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.COLD, WarpSettings.Classes.WATER, WarpSettings.Classes.NATURE),
    REDSTONE(0.65, false, true, new Location(Bukkit.getWorld("world"), -182, 33, 345), new Location(Bukkit.getWorld("world"), -174, 37, 353), WarpSettings.Temperature.MEDIUM, WarpSettings.Dimension.NONE, WarpSettings.Layer.NONE, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.HOT, WarpSettings.Classes.COLD, WarpSettings.Classes.REDSTONE),
    FOREST(1, false, true, new Location(Bukkit.getWorld("world"), -225, 22, 469), new Location(Bukkit.getWorld("world"), -217, 26, 477), WarpSettings.Temperature.MEDIUM, WarpSettings.Dimension.OVERWORLD, WarpSettings.Layer.SURFACE, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.WATER, WarpSettings.Classes.NATURE),
    DESERT(0.8, false, true, new Location(Bukkit.getWorld("world"), 37, 47, 431), new Location(Bukkit.getWorld("world"), 45, 51, 439), WarpSettings.Temperature.WARM, WarpSettings.Dimension.OVERWORLD, WarpSettings.Layer.SURFACE, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.HOT, WarpSettings.Classes.REDSTONE),
    AETHER(0.8, false, true, new Location(Bukkit.getWorld("world"), -367, -3, 602), new Location(Bukkit.getWorld("world"), -359, 1, 610), WarpSettings.Temperature.MEDIUM, WarpSettings.Dimension.NONE, WarpSettings.Layer.SKY, WarpSettings.Classes.WATER, WarpSettings.Classes.DREAM),
    BOOK(0.2, false, true, new Location(Bukkit.getWorld("world"), -391, 22, 468), new Location(Bukkit.getWorld("world"), -383, 26, 476), WarpSettings.Temperature.MEDIUM, WarpSettings.Dimension.NONE, WarpSettings.Layer.NONE, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.DREAM),
    SUN(0.75, false, true, new Location(Bukkit.getWorld("world"), -349, 13, 144), new Location(Bukkit.getWorld("world"), -341, 17, 152), WarpSettings.Temperature.WARM, WarpSettings.Dimension.OVERWORLD, WarpSettings.Layer.SKY, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.HOT),
    MUSHROOM(0.6, false, false, new Location(Bukkit.getWorld("world"), -310, 16, 24), new Location(Bukkit.getWorld("world"), -302, 20, 32), WarpSettings.Temperature.MEDIUM, WarpSettings.Dimension.OVERWORLD, WarpSettings.Layer.SURFACE, WarpSettings.Classes.WATER, WarpSettings.Classes.NATURE, WarpSettings.Classes.DREAM),
    END(0.85, true, true, new Location(Bukkit.getWorld("world"), -21, 29, 15), new Location(Bukkit.getWorld("world"), -13, 33, 23), WarpSettings.Temperature.COLD, WarpSettings.Dimension.END, WarpSettings.Layer.NONE, WarpSettings.Classes.COLD, WarpSettings.Classes.WATER, WarpSettings.Classes.REDSTONE, WarpSettings.Classes.DREAM),
    AMETHYST(0.3, false, true, new Location(Bukkit.getWorld("world"), -345, 62, 320), new Location(Bukkit.getWorld("world"), -337, 66, 328), WarpSettings.Temperature.MEDIUM, WarpSettings.Dimension.OVERWORLD, WarpSettings.Layer.UNDERGROUND, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.COLD),
    HEAVEN(0.5, false, true, new Location(Bukkit.getWorld("world"), 0,0,0), new Location(Bukkit.getWorld("world"), 0,0,0), WarpSettings.Temperature.MEDIUM, WarpSettings.Dimension.NONE, WarpSettings.Layer.SKY, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.COLD, WarpSettings.Classes.NATURE, WarpSettings.Classes.DREAM),
    HELL(0.5, false, true, new Location(Bukkit.getWorld("world"), 0,0,0), new Location(Bukkit.getWorld("world"), 0,0,0), WarpSettings.Temperature.WARM, WarpSettings.Dimension.NONE, WarpSettings.Layer.UNDERGROUND, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.HOT, WarpSettings.Classes.REDSTONE),
    INDUSTRIAL(0.8, false, false, new Location(Bukkit.getWorld("world"),-147,-59,986), new Location(Bukkit.getWorld("world"), -139,-55,994), WarpSettings.Temperature.MEDIUM, WarpSettings.Dimension.OVERWORLD, WarpSettings.Layer.SURFACE, WarpSettings.Classes.NEUTRAL, WarpSettings.Classes.REDSTONE, WarpSettings.Classes.HOT);

    //todo: add industrial as structure & his things

    private final double chance;
    private final boolean isNight;
    private final boolean allowMob;
    private final Location corner1; //north-west bottom corner
    private final Location corner2; //south-east top corner
    private final WarpSettings.Temperature temperature;
    private final WarpSettings.Dimension dimension;
    private final WarpSettings.Layer layer;
    private final WarpSettings.Classes[] allow;

    public boolean isNight() {
        return isNight;
    }

    public double getChance() {
        return chance;
    }

    public WarpSettings.Temperature getTemperature() {
        return temperature;
    }

    public WarpSettings.Dimension getDimension() {
        return dimension;
    }

    public WarpSettings.Layer getLayer() {
        return layer;
    }

    public WarpSettings.Classes[] getAllow() {
        return allow;
    }

    public boolean isAllowMob() {
        return allowMob;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    Warp(double chance, boolean isNight, boolean allowMob, Location corner1, Location corner2, WarpSettings.Temperature temperature, WarpSettings.Dimension dimension, WarpSettings.Layer layer, WarpSettings.Classes... allow) {
        this.chance = chance;
        this.isNight = isNight;
        this.allowMob = allowMob;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.temperature = temperature;
        this.dimension = dimension;
        this.layer = layer;
        this.allow = allow;
    }
}
