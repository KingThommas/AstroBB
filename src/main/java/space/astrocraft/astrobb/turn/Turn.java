package space.astrocraft.astrobb.turn;

import net.hectus.color.McColor;
import org.bukkit.Material;
import org.jetbrains.annotations.Contract;
import space.astrocraft.astrobb.warps.WarpSettings;

public enum Turn {
    NONE(Type.CUSTOM, McColor.RESET, "Nothing, this is an error!", Material.BARRIER, WarpSettings.Class.NEUTRAL, false),
    PURPLE_WOOL(Type.ATTACK, McColor.PURPLE, "Purple Wool", Material.PURPLE_WOOL, WarpSettings.Class.NEUTRAL, false),
    DINNERBONE(Type.COUNTER, McColor.GREEN, "Dinnerbone/Grumm Name Tag", Material.NAME_TAG, WarpSettings.Class.NEUTRAL, false),
    NETHER_PORTAL_FRAME(Type.AWAIT, McColor.BLACK, "Nether Portal Frame", Material.OBSIDIAN, WarpSettings.Class.HOT, false),
    NETHER_PORTAL_IGNITE(Type.ATTACK, McColor.PURPLE, "Ignite Nether Portal", Material.NETHER_PORTAL, WarpSettings.Class.HOT, false),
    POWDER_SNOW(Type.BUFF, McColor.WHITE, "Powder Snow", Material.POWDER_SNOW, WarpSettings.Class.COLD, false),
    LIGHTNING_TRIDENT(Type.COUNTER, McColor.YELLOW, "Lightning Trident", Material.TRIDENT, WarpSettings.Class.WATER, false),
    SEA_PICKLE_STACK(Type.ATTACK, McColor.DARK_AQUA, "4 Sea Pickles in a Stack", Material.SEA_PICKLE, WarpSettings.Class.NATURE, false),
    TNT(Type.ATTACK, McColor.RED, "Using TNT", Material.TNT, WarpSettings.Class.REDSTONE, false),
    OP_GAP(Type.CUSTOM, McColor.GOLD, "Enchanted Golden Apple", Material.ENCHANTED_GOLDEN_APPLE, WarpSettings.Class.DREAM, false),
    SPRUCE_TRAPDOOR(Type.COUNTER, McColor.DARK_GRAY, "Spruce Trapdoor", Material.SPRUCE_TRAPDOOR, WarpSettings.Class.NEUTRAL, false),
    OAK_DOORS(Type.DEFENSE, McColor.DARK_GRAY, "Oak Door Turtling", Material.OAK_DOOR, WarpSettings.Class.NEUTRAL, false),
    IRON_TRAPDOOR(Type.COUNTERATTACK, McColor.GRAY, "Iron Trapdoor", Material.IRON_TRAPDOOR, WarpSettings.Class.NEUTRAL, false),
    CAULDRON(Type.ATTACK, McColor.DARK_GRAY, "Cauldron", Material.CAULDRON, WarpSettings.Class.NEUTRAL, false),
    GOLD_BLOCK(Type.COUNTERATTACK, McColor.GOLD, "Gold Block", Material.GOLD_BLOCK, WarpSettings.Class.NEUTRAL, false),
    JAIL_METHOD(Type.BUFF, McColor.GRAY, "Jail Method (Iron Bars)", Material.IRON_BARS, WarpSettings.Class.NEUTRAL, false),
    BLACK_WOOL(Type.CUSTOM, McColor.BLACK, "Black Wool", Material.BLACK_WOOL, WarpSettings.Class.NEUTRAL, false),
    SCULK_BLOCK(Type.COUNTER, McColor.DARK_AQUA, "Sculk Block", Material.SCULK, WarpSettings.Class.NEUTRAL, false),
    GREEN_CARPET(Type.COUNTERATTACK, McColor.GREEN, "Green Carpet", Material.GREEN_CARPET, WarpSettings.Class.NEUTRAL, false),
    ENDER_PEARL_TP(Type.COUNTER, McColor.DARK_AQUA, "Throw Ender Pearl", Material.ENDER_PEARL, WarpSettings.Class.NEUTRAL, false),
    CHORUS_FRUIT_EAT(Type.COUNTER, McColor.PURPLE, "Eat Chorus Fruit to RTP", Material.CHORUS_FRUIT, WarpSettings.Class.NEUTRAL, false),
    IRON_SHOVEL(Type.COUNTER, McColor.GRAY, "Throw Iron Shovel", Material.IRON_SHOVEL, WarpSettings.Class.NEUTRAL, false),
    STONECUTTER(Type.COUNTER, McColor.DARK_GRAY, "Stonecutter", Material.STONECUTTER, WarpSettings.Class.NEUTRAL, false),
    MAGENTA_GLAZED_TERRACOTTA(Type.COUNTERATTACK, McColor.PURPLE, "Magenta glazed Terracotta", Material.MAGENTA_GLAZED_TERRACOTTA, WarpSettings.Class.NEUTRAL, false),
    CYAN_CARPET(Type.CUSTOM, McColor.AQUA, "Cyan Carpet", Material.CYAN_CARPET, WarpSettings.Class.NEUTRAL, false),
    SPONGE(Type.COUNTERATTACK, McColor.YELLOW, "Sponge", Material.SPONGE, WarpSettings.Class.NEUTRAL, false),
    LIGHTNING_ROD(Type.COUNTERATTACK, McColor.GOLD, "Lightning Rod", Material.LIGHTNING_ROD, WarpSettings.Class.REDSTONE, false),
    MAGMA_BLOCK(Type.ATTACK, McColor.DARK_RED, "Magma Block", Material.MAGMA_BLOCK, WarpSettings.Class.HOT, false),
    NETHERRACK(Type.COUNTERATTACK, McColor.RED, "Netherrack", Material.NETHERRACK, WarpSettings.Class.HOT, false),
    LAVA_BUCKET(Type.CUSTOM, McColor.GOLD, "Lava Bucket", Material.LAVA_BUCKET, WarpSettings.Class.HOT, false),
    FLINT_N_STEEL(Type.COUNTER, McColor.GOLD, "Flint and Steel", Material.FLINT_AND_STEEL, WarpSettings.Class.HOT, false),
    ORANGE_WOOL(Type.COUNTERATTACK, McColor.GOLD, "Orange Wool", Material.ORANGE_WOOL, WarpSettings.Class.HOT, false),
    CAMPFIRE(Type.COUNTERATTACK, McColor.GOLD, "Campfire", Material.CAMPFIRE, WarpSettings.Class.HOT, false),
    BLAZE(Type.ATTACK, McColor.YELLOW, "Blaze Spawn", Material.BLAZE_SPAWN_EGG, WarpSettings.Class.HOT, true),
    PIGLIN(Type.ATTACK, McColor.PINK, "Piglin Spawn", Material.PIGLIN_SPAWN_EGG, WarpSettings.Class.HOT, true),
    PIGLIN_CONVERT(Type.CUSTOM, McColor.GREEN, "Piglin Zombified", Material.ZOMBIFIED_PIGLIN_SPAWN_EGG, WarpSettings.Class.HOT, true),
    RESPAWN_ANCHOR(Type.BUFF, McColor.BLACK, "Respawn Anchor", Material.RESPAWN_ANCHOR, WarpSettings.Class.HOT, false),
    PACKED_ICE(Type.ATTACK, McColor.BLUE, "Packed Ice", Material.PACKED_ICE, WarpSettings.Class.COLD, false),
    BLUE_ICE(Type.COUNTER, McColor.BLUE, "Blue Ice", Material.BLUE_ICE, WarpSettings.Class.COLD, false),
    SPRUCE_LEAVES(Type.COUNTERATTACK, McColor.GREEN, "Spruce Leaves", Material.SPRUCE_LEAVES, WarpSettings.Class.COLD, false),
    LIGHT_BLUE_WOOL(Type.ATTACK, McColor.BLUE, "Light Blue Wool", Material.LIGHT_BLUE_WOOL, WarpSettings.Class.COLD, false),
    WHITE_WOOL(Type.CUSTOM, McColor.WHITE, "White Wool", Material.WHITE_WOOL, WarpSettings.Class.COLD, false),
    SNOWBALL(Type.CUSTOM, McColor.WHITE, "Snowball", Material.SNOWBALL, WarpSettings.Class.COLD, false),
    POLAR_BEAR(Type.BUFF, McColor.WHITE, "Polar Bear", Material.POLAR_BEAR_SPAWN_EGG, WarpSettings.Class.COLD, true),
    BRAIN_CORAL_BLOCK(Type.COUNTERATTACK, McColor.PINK, "Brain Coral Block", Material.BRAIN_CORAL_BLOCK, WarpSettings.Class.WATER, false),
    HORN_CORAL(Type.COUNTERATTACK, McColor.YELLOW, "Horn Coral", Material.HORN_CORAL, WarpSettings.Class.WATER, false),
    FIRE_CORAL(Type.COUNTERATTACK, McColor.RED, "Fire Coral", Material.FIRE_CORAL, WarpSettings.Class.WATER, false),
    FIRE_CORAL_FAN(Type.COUNTER, McColor.RED, "Fire Coral Fan", Material.FIRE_CORAL_FAN, WarpSettings.Class.WATER, false),
    SEA_LANTERN(Type.BUFF, McColor.AQUA, "Sea Lantern", Material.SEA_LANTERN, WarpSettings.Class.WATER, false),
    WATER_BUCKET(Type.COUNTER, McColor.DARK_BLUE, "Water Bucket", Material.WATER_BUCKET, WarpSettings.Class.WATER, false),
    DRIED_KELP_BLOCK(Type.ATTACK, McColor.GREEN, "Dried Kelp Block", Material.DRIED_KELP_BLOCK, WarpSettings.Class.WATER, false),
    BOAT(Type.COUNTER, McColor.DARK_RED, "Boat", Material.OAK_BOAT, WarpSettings.Class.WATER, false),
    BLUE_AXOLOTL(Type.CUSTOM, McColor.BLUE, "Axolotl Spawn", Material.AXOLOTL_BUCKET, WarpSettings.Class.WATER, true),
    PINK_AXOLOTL(Type.CUSTOM, McColor.PINK, "Axolotl Spawn", Material.AXOLOTL_BUCKET, WarpSettings.Class.WATER, true),
    BROWN_AXOLOTL(Type.CUSTOM, McColor.GOLD, "Axolotl Spawn", Material.AXOLOTL_BUCKET, WarpSettings.Class.WATER, true),
    GOLD_AXOLOTL(Type.CUSTOM, McColor.GOLD, "Axolotl Spawn", Material.AXOLOTL_BUCKET, WarpSettings.Class.WATER, true),
    CYAN_AXOLOTL(Type.CUSTOM, McColor.DARK_AQUA, "Axolotl Spawn", Material.AXOLOTL_BUCKET, WarpSettings.Class.WATER, true),
    VERDANT_FROGLIGHT(Type.COUNTERATTACK, McColor.LIME, "Verdant Froglight", Material.VERDANT_FROGLIGHT, WarpSettings.Class.WATER, false),
    PUFFERFISH_BUCKET(Type.BUFF, McColor.GOLD, "Pufferfish in a Bucket", Material.PUFFERFISH_BUCKET, WarpSettings.Class.WATER, false),
    SPLASH_WATER_BOTTLE(Type.CUSTOM, McColor.DARK_BLUE, "Splash Water Bottle", Material.SPLASH_POTION, WarpSettings.Class.WATER, false),
    BEE_NEST(Type.ATTACK, McColor.LIME, "Bee Nest", Material.BEE_NEST, WarpSettings.Class.NATURE, false),
    HONEY_BLOCK(Type.COUNTER, McColor.YELLOW, "Honey Block", Material.HONEY_BLOCK, WarpSettings.Class.NATURE, false),
    PUMPKIN_WALL(Type.BUFF, McColor.GOLD, "Pumpkin Wall", Material.PUMPKIN, WarpSettings.Class.NATURE, false),
    GREEN_WOOL(Type.COUNTERATTACK, McColor.GREEN, "Green Wool", Material.GREEN_WOOL, WarpSettings.Class.NATURE, false),
    MANGROVE_ROOTS(Type.COUNTER, McColor.GREEN, "Mangrove Roots", Material.MANGROVE_ROOTS, WarpSettings.Class.NATURE, false),
    COMPOSTER(Type.ATTACK, McColor.LIME, "Composter", Material.COMPOSTER, WarpSettings.Class.NATURE, false),
    HAY_BALE(Type.COUNTERATTACK, McColor.YELLOW, "Hay Bale", Material.HAY_BLOCK, WarpSettings.Class.NATURE, false),
    LEVER(Type.COUNTER, McColor.RED, "Lever", Material.LEVER, WarpSettings.Class.REDSTONE, false),
    FENCE_GATE(Type.COUNTERATTACK, McColor.RED, "Fence Gate", Material.OAK_FENCE_GATE, WarpSettings.Class.REDSTONE, false),
    REDSTONE_REPEATER(Type.COUNTERATTACK, McColor.RED, "Redstone Repeater", Material.REPEATER, WarpSettings.Class.REDSTONE, false),
    WOODEN_BUTTON(Type.COUNTER, McColor.RED, "Wooden Button", Material.OAK_BUTTON, WarpSettings.Class.REDSTONE, false),
    STONE_BUTTON(Type.COUNTER, McColor.RED, "Stone Button", Material.STONE_BUTTON, WarpSettings.Class.REDSTONE, false),
    DAYLIGHT_ROW(Type.BUFF, McColor.RED, "Daylight Sensor \"Wall\"", Material.DAYLIGHT_DETECTOR, WarpSettings.Class.REDSTONE, false),
    RED_BED(Type.COUNTERATTACK, McColor.DARK_RED, "Red Bed", Material.RED_BED, WarpSettings.Class.DREAM, false),
    PINK_BED(Type.BUFF, McColor.PINK, "Pink", Material.PINK_BED, WarpSettings.Class.DREAM, false),
    GREEN_BED(Type.COUNTERATTACK, McColor.GREEN, "Green Bed", Material.GREEN_BED, WarpSettings.Class.DREAM, false),
    BLUE_BED(Type.COUNTER, McColor.BLUE, "Blue Bed", Material.BLUE_BED, WarpSettings.Class.DREAM, false),
    PINK_SHEEP(Type.CUSTOM, McColor.WHITE, "Pink Sheep", Material.SHEEP_SPAWN_EGG, WarpSettings.Class.DREAM, true),
    WHITE_SHEEP(Type.CUSTOM, McColor.WHITE, "White Sheep", Material.SHEEP_SPAWN_EGG, WarpSettings.Class.DREAM, true),
    LIGHT_GRAY_SHEEP(Type.CUSTOM, McColor.WHITE, "Light Gray Sheep", Material.SHEEP_SPAWN_EGG, WarpSettings.Class.DREAM, true),
    GRAY_SHEEP(Type.CUSTOM, McColor.WHITE, "Gray Sheep", Material.SHEEP_SPAWN_EGG, WarpSettings.Class.DREAM, true),
    BLACK_SHEEP(Type.CUSTOM, McColor.WHITE, "Black Sheep", Material.SHEEP_SPAWN_EGG, WarpSettings.Class.DREAM, true),
    BROWN_SHEEP(Type.CUSTOM, McColor.WHITE, "Brown Sheep", Material.SHEEP_SPAWN_EGG, WarpSettings.Class.DREAM, true),
    BLUE_SHEEP(Type.CUSTOM, McColor.WHITE, "Blue Sheep", Material.SHEEP_SPAWN_EGG, WarpSettings.Class.DREAM, true),
    PHANTOM(Type.ATTACK, McColor.GRAY, "Phantom", Material.PHANTOM_SPAWN_EGG, WarpSettings.Class.DREAM, true),
    DRAGON_HEAD(Type.BUFF, McColor.PURPLE, "Dragon Head", Material.DRAGON_HEAD, WarpSettings.Class.DREAM, false),
    SOUL_SAND(Type.ATTACK, McColor.DARK_GRAY, "Soul Sand", Material.SOUL_SAND, WarpSettings.Class.DREAM, false),
    FLOWER_POT(Type.AWAIT, McColor.DARK_RED, "Flower Pot", Material.FLOWER_POT, WarpSettings.Class.NATURE, false),
    DIRT(Type.AWAIT, McColor.GRAY, "Dirt", Material.DIRT, WarpSettings.Class.NATURE, false),
    COMPOSTER_FILL(Type.COUNTER, McColor.GREEN, "Filling a Composter", Material.BONE_MEAL, WarpSettings.Class.NATURE, false),
    POPPY(Type.CUSTOM, McColor.RED, "Poppy Flower", Material.POPPY, WarpSettings.Class.NATURE, false),
    BLUE_ORCHID(Type.BUFF, McColor.BLUE, "Blue Orchid", Material.BLUE_ORCHID, WarpSettings.Class.NATURE, false),
    ALLIUM(Type.BUFF, McColor.PURPLE, "Allium Flower", Material.ALLIUM, WarpSettings.Class.NATURE, false),
    AZURE_BLUET(Type.BUFF, McColor.WHITE, "Azure Bluet", Material.AZURE_BLUET, WarpSettings.Class.NATURE, false),
    RED_TULIP(Type.BUFF, McColor.RED, "Red Tulip", Material.RED_TULIP, WarpSettings.Class.NATURE, false),
    ORANGE_TULIP(Type.BUFF, McColor.GOLD, "Orange Tulip", Material.ORANGE_TULIP, WarpSettings.Class.NATURE, false),
    WHITE_TULIP(Type.BUFF, McColor.WHITE, "White Tulip", Material.WHITE_TULIP, WarpSettings.Class.NATURE, false),
    PINK_TULIP(Type.BUFF, McColor.PINK, "Pink Tulip", Material.PINK_TULIP, WarpSettings.Class.NATURE, false),
    CORNFLOWER(Type.BUFF, McColor.BLUE, "Cornflower", Material.CORNFLOWER, WarpSettings.Class.NATURE, false),
    OXEYE_DAISY(Type.BUFF, McColor.WHITE, "Oxeye Daisy", Material.OXEYE_DAISY, WarpSettings.Class.NATURE, false),
    WITHER_ROSE(Type.BUFF, McColor.BLACK, "Wither Rose", Material.WITHER_ROSE, WarpSettings.Class.NATURE, false),
    SUNFLOWER(Type.BUFF, McColor.YELLOW, "Sunflower", Material.SUNFLOWER, WarpSettings.Class.NATURE, false),
    OAK_SAPLING(Type.BUFF, McColor.GREEN, "Oak Sapling", Material.OAK_SAPLING, WarpSettings.Class.NATURE, false),
    SPORE_BLOSSOM(Type.BUFF, McColor.PINK, "Spore Blossom", Material.SPORE_BLOSSOM, WarpSettings.Class.NATURE, false),
    TREE(Type.ATTACK, McColor.GREEN, "Grow a Tree", Material.OAK_WOOD, WarpSettings.Class.NATURE, false),
    OAK_STAIRS(Type.CUSTOM, McColor.GOLD, "Oak Stairs", Material.OAK_STAIRS, WarpSettings.Class.NATURE, false);

    public final Type type;
    public final McColor color;
    public final String name;
    public final Material material;
    public final WarpSettings.Class clazz;
    public final boolean mob;

    @Contract(pure = true)
    Turn(Type t, McColor co, String n, Material ma, WarpSettings.Class cl, boolean mo) {
        type = t;
        color = co;
        name = n;
        material = ma;
        clazz = cl;
        mob = mo;
    }

    public enum Type { ATTACK, COUNTER, COUNTERATTACK, WARP, BUFF, DEFENSE, AWAIT, CUSTOM }
}
