package net.hectus.hectusblockbattles.turn;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.warps.Warp;
import org.bukkit.Material;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;

import static net.hectus.hectusblockbattles.warps.WarpSettings.Class;

public enum Turn {
    NONE(Type.CUSTOM, McColor.RESET, "Nothing, this is an error!", Material.BARRIER, null, Class.NEUTRAL, false),
    PURPLE_WOOL(Type.ATTACK, McColor.PURPLE, "Purple Wool", Material.PURPLE_WOOL, null, Class.NEUTRAL, false),
    DINNERBONE(Type.COUNTER, McColor.GREEN, "Dinnerbone/Grumm Name Tag", Material.NAME_TAG, null, Class.NEUTRAL, false),
    NETHER_PORTAL_FRAME(Type.AWAIT, McColor.BLACK, "Nether Portal Frame", Material.OBSIDIAN, null, Class.HOT, false),
    NETHER_PORTAL_IGNITE(Type.ATTACK, McColor.PURPLE, "Ignite Nether Portal", Material.NETHER_PORTAL, null, Class.HOT, false),
    POWDER_SNOW(Type.BUFF, McColor.WHITE, "Powder Snow", Material.POWDER_SNOW, null, Class.COLD, false),
    LIGHTNING_TRIDENT(Type.COUNTER, McColor.YELLOW, "Lightning Trident", Material.TRIDENT, null, Class.WATER, false),
    SEA_PICKLE_STACK(Type.ATTACK, McColor.DARK_AQUA, "4 Sea Pickles in a Stack", Material.SEA_PICKLE, null, Class.NATURE, false),
    TNT(Type.ATTACK, McColor.RED, "Using TNT", Material.TNT, null, Class.REDSTONE, false),
    OP_GAP(Type.CUSTOM, McColor.GOLD, "Enchanted Golden Apple", Material.ENCHANTED_GOLDEN_APPLE, null, Class.DREAM, false),
    SPRUCE_TRAPDOOR(Type.COUNTER, McColor.DARK_GRAY, "Spruce Trapdoor", Material.SPRUCE_TRAPDOOR, null, Class.NEUTRAL, false),
    OAK_DOORS(Type.DEFENSE, McColor.DARK_GRAY, "Oak Door Turtling", Material.OAK_DOOR, null, Class.NEUTRAL, false),
    IRON_TRAPDOOR(Type.COUNTERATTACK, McColor.GRAY, "Iron Trapdoor", Material.IRON_TRAPDOOR, null, Class.NEUTRAL, false),
    CAULDRON(Type.ATTACK, McColor.DARK_GRAY, "Cauldron", Material.CAULDRON, null, Class.NEUTRAL, false),
    GOLD_BLOCK(Type.COUNTERATTACK, McColor.GOLD, "Gold Block", Material.GOLD_BLOCK, null, Class.NEUTRAL, false),
    JAIL_METHOD(Type.BUFF, McColor.GRAY, "Jail Method (Iron Bars)", Material.IRON_BARS, null, Class.NEUTRAL, false),
    BLACK_WOOL(Type.CUSTOM, McColor.BLACK, "Black Wool", Material.BLACK_WOOL, null, Class.NEUTRAL, false),
    SCULK_BLOCK(Type.COUNTER, McColor.DARK_AQUA, "Sculk Block", Material.SCULK, null, Class.NEUTRAL, false),
    GREEN_CARPET(Type.COUNTERATTACK, McColor.GREEN, "Green Carpet", Material.GREEN_CARPET, null, Class.NEUTRAL, false),
    ENDER_PEARL_TP(Type.COUNTER, McColor.DARK_AQUA, "Throw Ender Pearl", Material.ENDER_PEARL, null, Class.NEUTRAL, false),
    CHORUS_FRUIT_EAT(Type.COUNTER, McColor.PURPLE, "Eat Chorus Fruit to RTP", Material.CHORUS_FRUIT, null, Class.NEUTRAL, false),
    IRON_SHOVEL(Type.COUNTER, McColor.GRAY, "Throw Iron Shovel", Material.IRON_SHOVEL, null, Class.NEUTRAL, false),
    STONECUTTER(Type.COUNTER, McColor.DARK_GRAY, "Stonecutter", Material.STONECUTTER, null, Class.NEUTRAL, false),
    MAGENTA_GLAZED_TERRACOTTA(Type.COUNTERATTACK, McColor.PURPLE, "Magenta glazed Terracotta", Material.MAGENTA_GLAZED_TERRACOTTA, null, Class.NEUTRAL, false),
    CYAN_CARPET(Type.CUSTOM, McColor.AQUA, "Cyan Carpet", Material.CYAN_CARPET, null, Class.NEUTRAL, false),
    SPONGE(Type.COUNTERATTACK, McColor.YELLOW, "Sponge", Material.SPONGE, null, Class.NEUTRAL, false),
    NETHER_WARP(Type.WARP, McColor.DARK_RED, "Nether Warp", Material.NETHERRACK, Warp.NETHER, null, false),
    ICE_WARP(Type.WARP, McColor.BLUE, "Ice Warp", Material.ICE, Warp.ICE, null, false),
    SNOW_WARP(Type.WARP, McColor.WHITE, "Snow Warp", Material.SNOW, Warp.SNOW, null, false),
    CLIFF_WARP(Type.WARP, McColor.GRAY, "Ice Warp", Material.STONE, Warp.CLIFF, null, false),
    UNDERWATER_WARP(Type.WARP, McColor.DARK_AQUA, "(Under)water Warp", Material.PRISMARINE, Warp.UNDERWATER, null, false),
    VOID_WARP(Type.WARP, McColor.BLACK, "Void Warp", Material.BLACK_CONCRETE, Warp.VOID, null, false),
    REDSTONE_WARP(Type.WARP, McColor.DARK_RED, "Redstone Warp", Material.REDSTONE_BLOCK, Warp.REDSTONE, null, false),
    WOOD_WARP(Type.WARP, McColor.GREEN, "Wood Warp", Material.OAK_LOG, Warp.WOOD, null, false),
    DESERT_WARP(Type.WARP, McColor.YELLOW, "Desert Warp", Material.SAND, Warp.DESERT, null, false),
    AETHER_WARP(Type.WARP, McColor.RESET, "Aether Warp", Material.WHITE_CONCRETE, Warp.AETHER, null, false),
    BOOK_WARP(Type.WARP, McColor.GOLD, "Book Warp", Material.BOOKSHELF, Warp.BOOK, null, false),
    SUN_WARP(Type.WARP, McColor.YELLOW, "Sun Warp", Material.SHROOMLIGHT, Warp.SUN, null, false),
    MUSHROOM_WARP(Type.WARP, McColor.WHITE, "Mushroom Warp", Material.BROWN_MUSHROOM_BLOCK, Warp.MUSHROOM, null, false),
    END_WARP(Type.WARP, McColor.YELLOW, "End Warp", Material.END_STONE, Warp.END, null, false),
    AMETHYST_WARP(Type.WARP, McColor.PINK, "Amethyst Warp", Material.AMETHYST_BLOCK, Warp.AMETHYST, null, false),
    HEAVEN_WARP(Type.WARP, McColor.WHITE, "Heaven Warp", Material.WHITE_WOOL, Warp.HEAVEN, null, false),
    HELL_WARP(Type.WARP, McColor.DARK_RED, "Hell Warp", Material.NETHER_BRICKS, Warp.HELL, null, false),
    INDUSTRIAL_WARP(Type.WARP, McColor.DARK_GRAY, "Industrial Warp", Material.IRON_BLOCK, Warp.INDUSTRIAL, null, false),
    LIGHTNING_ROD(Type.COUNTERATTACK, McColor.GOLD, "Lightning Rod", Material.LIGHTNING_ROD, null, Class.REDSTONE, false),
    MAGMA_BLOCK(Type.ATTACK, McColor.DARK_RED, "Magma Block", Material.MAGMA_BLOCK, null, Class.HOT, false),
    NETHERRACK(Type.COUNTERATTACK, McColor.RED, "Netherrack", Material.NETHERRACK, null, Class.HOT, false),
    LAVA_BUCKET(Type.CUSTOM, McColor.GOLD, "Lava Bucket", Material.LAVA_BUCKET, null, Class.HOT, false),
    FLINT_N_STEEL(Type.COUNTER, McColor.GOLD, "Flint and Steel", Material.FLINT_AND_STEEL, null, Class.HOT, false),
    ORANGE_WOOL(Type.COUNTERATTACK, McColor.GOLD, "Orange Wool", Material.ORANGE_WOOL, null, Class.HOT, false),
    CAMPFIRE(Type.COUNTERATTACK, McColor.GOLD, "Campfire", Material.CAMPFIRE, null, Class.HOT, false),
    BLAZE(Type.ATTACK, McColor.YELLOW, "Blaze Spawn", Material.BLAZE_SPAWN_EGG, null, Class.HOT, true),
    PIGLIN(Type.ATTACK, McColor.PINK, "Piglin Spawn", Material.PIGLIN_SPAWN_EGG, null, Class.HOT, true),
    RESPAWN_ANCHOR(Type.BUFF, McColor.BLACK, "Respawn Anchor", Material.RESPAWN_ANCHOR, null, Class.HOT, false),
    PACKED_ICE(Type.ATTACK, McColor.BLUE, "Packed Ice", Material.PACKED_ICE, null, Class.COLD, false),
    BLUE_ICE(Type.COUNTER, McColor.BLUE, "Blue Ice", Material.BLUE_ICE, null, Class.COLD, false),
    SPRUCE_LEAVES(Type.COUNTERATTACK, McColor.GREEN, "Spruce Leaves", Material.SPRUCE_LEAVES, null, Class.COLD, false),
    LIGHT_BLUE_WOOL(Type.ATTACK, McColor.BLUE, "Light Blue Wool", Material.LIGHT_BLUE_WOOL, null, Class.COLD, false),
    WHITE_WOOL(Type.CUSTOM, McColor.WHITE, "White Wool", Material.WHITE_WOOL, null, Class.COLD, false),
    SNOWBALL(Type.CUSTOM, McColor.WHITE, "Snowball", Material.SNOWBALL, null, Class.COLD, false),
    POLAR_BEAR(Type.BUFF, McColor.WHITE, "Polar Bear", Material.POLAR_BEAR_SPAWN_EGG, null, Class.COLD, true),
    BRAIN_CORAL_BLOCK(Type.COUNTERATTACK, McColor.PINK, "Brain Coral Block", Material.BRAIN_CORAL_BLOCK, null, Class.WATER, false),
    HORN_CORAL(Type.COUNTERATTACK, McColor.YELLOW, "Horn Coral", Material.HORN_CORAL, null, Class.WATER, false),
    FIRE_CORAL(Type.COUNTERATTACK, McColor.RED, "Fire Coral", Material.FIRE_CORAL, null, Class.WATER, false),
    FIRE_CORAL_FAN(Type.COUNTER, McColor.RED, "Fire Coral Fan", Material.FIRE_CORAL_FAN, null, Class.WATER, false),
    SEA_LANTERN(Type.BUFF, McColor.AQUA, "Sea Lantern", Material.SEA_LANTERN, null, Class.WATER, false),
    WATER_BUCKET(Type.COUNTER, McColor.DARK_BLUE, "Water Bucket", Material.WATER_BUCKET, null, Class.WATER, false),
    DRIED_KELP_BLOCK(Type.ATTACK, McColor.GREEN, "Dried Kelp Block", Material.DRIED_KELP_BLOCK, null, Class.WATER, false),
    BOAT(Type.COUNTER, McColor.DARK_RED, "Boat", Material.OAK_BOAT, null, Class.WATER, false),
    BLUE_AXOLOTL(Type.CUSTOM, McColor.BLUE, "Axolotl Spawn", Material.AXOLOTL_BUCKET, null, Class.WATER, true),
    PINK_AXOLOTL(Type.CUSTOM, McColor.PINK, "Axolotl Spawn", Material.AXOLOTL_BUCKET, null, Class.WATER, true),
    BROWN_AXOLOTL(Type.CUSTOM, McColor.GOLD, "Axolotl Spawn", Material.AXOLOTL_BUCKET, null, Class.WATER, true),
    GOLD_AXOLOTL(Type.CUSTOM, McColor.GOLD, "Axolotl Spawn", Material.AXOLOTL_BUCKET, null, Class.WATER, true),
    CYAN_AXOLOTL(Type.CUSTOM, McColor.DARK_AQUA, "Axolotl Spawn", Material.AXOLOTL_BUCKET, null, Class.WATER, true),
    VERDANT_FROGLIGHT(Type.COUNTERATTACK, McColor.LIME, "Verdant Froglight", Material.VERDANT_FROGLIGHT, null, Class.WATER, false),
    PUFFERFISH_BUCKET(Type.BUFF, McColor.GOLD, "Pufferfish in a Bucket", Material.PUFFERFISH_BUCKET, null, Class.WATER, false),
    SPLASH_WATER_BOTTLE(Type.CUSTOM, McColor.DARK_BLUE, "Splash Water Bottle", Material.SPLASH_POTION, null, Class.WATER, false),
    BEE_NEST(Type.ATTACK, McColor.LIME, "Bee Nest", Material.BEE_NEST, null, Class.NATURE, false),
    HONEY_BLOCK(Type.COUNTER, McColor.YELLOW, "Honey Block", Material.HONEY_BLOCK, null, Class.NATURE, false),
    PUMPKIN_WALL(Type.BUFF, McColor.GOLD, "Pumpkin Wall", Material.PUMPKIN, null, Class.NATURE, false),
    GREEN_WOOL(Type.COUNTERATTACK, McColor.GREEN, "Green Wool", Material.GREEN_WOOL, null, Class.NATURE, false),
    MANGROVE_ROOTS(Type.COUNTER, McColor.GREEN, "Mangrove Roots", Material.MANGROVE_ROOTS, null, Class.NATURE, false),
    COMPOSTER(Type.ATTACK, McColor.LIME, "Composter", Material.COMPOSTER, null, Class.NATURE, false),
    HAY_BALE(Type.COUNTERATTACK, McColor.YELLOW, "Hay Bale", Material.HAY_BLOCK, null, Class.NATURE, false),
    LEVER(Type.COUNTER, McColor.RED, "Lever", Material.LEVER, null, Class.REDSTONE, false),
    FENCE_GATE(Type.COUNTERATTACK, McColor.RED, "Fence Gate", Material.OAK_FENCE_GATE, null, Class.REDSTONE, false),
    REDSTONE_REPEATER(Type.COUNTERATTACK, McColor.RED, "Redstone Repeater", Material.REPEATER, null, Class.REDSTONE, false),
    WOODEN_BUTTON(Type.COUNTER, McColor.RED, "Wooden Button", Material.OAK_BUTTON, null, Class.REDSTONE, false),
    STONE_BUTTON(Type.COUNTER, McColor.RED, "Stone Button", Material.STONE_BUTTON, null, Class.REDSTONE, false),
    DAYLIGHT_SENSOR(Type.BUFF, McColor.RED, "Daylight Sensor", Material.DAYLIGHT_DETECTOR, null, Class.REDSTONE, false),
    RED_BED(Type.COUNTERATTACK, McColor.DARK_RED, "Red Bed", Material.RED_BED, null, Class.DREAM, false),
    PINK_BED(Type.BUFF, McColor.PINK, "Pink", Material.PINK_BED, null, Class.DREAM, false),
    GREEN_BED(Type.COUNTERATTACK, McColor.GREEN, "Green Bed", Material.GREEN_BED, null, Class.DREAM, false),
    BLUE_BED(Type.COUNTER, McColor.BLUE, "Blue Bed", Material.BLUE_BED, null, Class.DREAM, false),
    PINK_SHEEP(Type.CUSTOM, McColor.WHITE, "Pink Sheep", Material.SHEEP_SPAWN_EGG, null, Class.DREAM, true),
    WHITE_SHEEP(Type.CUSTOM, McColor.WHITE, "White Sheep", Material.SHEEP_SPAWN_EGG, null, Class.DREAM, true),
    LIGHT_GRAY_SHEEP(Type.CUSTOM, McColor.WHITE, "Light Gray Sheep", Material.SHEEP_SPAWN_EGG, null, Class.DREAM, true),
    GRAY_SHEEP(Type.CUSTOM, McColor.WHITE, "Gray Sheep", Material.SHEEP_SPAWN_EGG, null, Class.DREAM, true),
    BLACK_SHEEP(Type.CUSTOM, McColor.WHITE, "Black Sheep", Material.SHEEP_SPAWN_EGG, null, Class.DREAM, true),
    BROWN_SHEEP(Type.CUSTOM, McColor.WHITE, "Brown Sheep", Material.SHEEP_SPAWN_EGG, null, Class.DREAM, true),
    BLUE_SHEEP(Type.CUSTOM, McColor.WHITE, "Blue Sheep", Material.SHEEP_SPAWN_EGG, null, Class.DREAM, true),
    PHANTOM(Type.ATTACK, McColor.GRAY, "Phantom", Material.PHANTOM_SPAWN_EGG, null, Class.DREAM, true),
    DRAGON_HEAD(Type.BUFF, McColor.BLACK, "Dragon Head", Material.DRAGON_HEAD, null, Class.DREAM, false),
    SOUL_SAND(Type.ATTACK, McColor.DARK_GRAY, "Soul Sand", Material.SOUL_SAND, null, Class.DREAM, false),
    FLOWER_POT(Type.AWAIT, McColor.DARK_RED, "Flower Pot", Material.FLOWER_POT, null, Class.NATURE, false),
    DIRT(Type.AWAIT, McColor.GRAY, "Dirt", Material.DIRT, null, Class.NATURE, false),
    COMPOSTER_FILL(Type.COUNTER, McColor.GREEN, "Filling a Composter", Material.BONE_MEAL, null, Class.NATURE, false),
    POPPY(Type.CUSTOM, McColor.RED, "Poppy Flower", Material.POPPY, null, Class.NATURE, false),
    BLUE_ORCHID(Type.BUFF, McColor.BLUE, "Blue Orchid", Material.BLUE_ORCHID, null, Class.NATURE, false),
    ALLIUM(Type.BUFF, McColor.PURPLE, "Allium Flower", Material.ALLIUM, null, Class.NATURE, false),
    AZURE_BLUET(Type.BUFF, McColor.WHITE, "Azure Bluet", Material.AZURE_BLUET, null, Class.NATURE, false),
    RED_TULIP(Type.BUFF, McColor.RED, "Red Tulip", Material.RED_TULIP, null, Class.NATURE, false),
    ORANGE_TULIP(Type.BUFF, McColor.GOLD, "Orange Tulip", Material.ORANGE_TULIP, null, Class.NATURE, false),
    WHITE_TULIP(Type.BUFF, McColor.WHITE, "White Tulip", Material.WHITE_TULIP, null, Class.NATURE, false),
    PINK_TULIP(Type.BUFF, McColor.PINK, "Pink Tulip", Material.PINK_TULIP, null, Class.NATURE, false),
    CORNFLOWER(Type.BUFF, McColor.BLUE, "Cornflower", Material.CORNFLOWER, null, Class.NATURE, false),
    OXEYE_DAISY(Type.BUFF, McColor.WHITE, "Oxeye Daisy", Material.OXEYE_DAISY, null, Class.NATURE, false),
    WITHER_ROSE(Type.BUFF, McColor.BLACK, "Wither Rose", Material.WITHER_ROSE, null, Class.NATURE, false),
    SUNFLOWER(Type.BUFF, McColor.YELLOW, "Sunflower", Material.SUNFLOWER, null, Class.NATURE, false),
    OAK_SAPLING(Type.BUFF, McColor.GREEN, "Oak Sapling", Material.OAK_SAPLING, null, Class.NATURE, false),
    SPORE_BLOSSOM(Type.BUFF, McColor.PINK, "Spore Blossom", Material.SPORE_BLOSSOM, null, Class.NATURE, false),
    TREE(Type.ATTACK, McColor.GREEN, "Grow a Tree", Material.OAK_WOOD, null, Class.NATURE, false);

    public final Type type;
    public final McColor color;
    public final String name;
    public final Material material;
    public final Warp warp;
    public final Class clazz;
    public final boolean mob;

    @Contract(pure = true)
    Turn(Type t, McColor co, String n, Material ma, @Nullable Warp w, Class cl, boolean mo) {
        type = t;
        color = co;
        name = n;
        material = ma;
        warp = w;
        clazz = cl;
        mob = mo;
    }

    public enum Type { ATTACK, COUNTER, COUNTERATTACK, WARP, BUFF, DEFENSE, AWAIT, CUSTOM }
}
