package net.hectus.hectusblockbattles.events;

import net.hectus.color.Ansi;
import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.Cord;
import net.hectus.hectusblockbattles.Trace;
import net.hectus.hectusblockbattles.Translation;
import net.hectus.hectusblockbattles.match.GameFlow;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.player.BBPlayer;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.hectusblockbattles.turn.Turn;
import net.hectus.hectusblockbattles.turn.TurnInfo;
import net.hectus.hectusblockbattles.warps.Warp;
import net.hectus.hectusblockbattles.warps.WarpManager;
import net.hectus.hectusblockbattles.warps.WarpSettings;
import net.hectus.util.Randomizer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Random;

import static net.hectus.hectusblockbattles.warps.WarpSettings.Class.*;

public class BlockBattleEvents {
    public static void onStructurePlace(@NotNull StructurePlaceEvent event) {
        System.out.println("BlockBattleEvents.onStructurePlace(event = " + event + ")");

        Structure structure = event.structure();

        Turn turn = switch (structure.name) {
            case "NETHER_PORTAL"   -> Turn.NETHER_PORTAL_FRAME;
            case "OAK_DOOR_TURTLE" -> Turn.OAK_DOORS;
            case "JAIL_METHOD"     -> Turn.JAIL_METHOD;
            case "PUMPKIN_WALL"    -> Turn.PUMPKIN_WALL;
            case "DAYLIGHT_WALL"   -> Turn.DAYLIGHT_ROW;
            default                -> Turn.NONE;
        };

        if (structure.name.contains("WARP")) {
            String warp = structure.name.replace("_WARP", "");
            WarpManager.warp(Warp.valueOf(warp), event.placer(), event.opponent());
            return;
        }

        if (turn != Turn.NONE) {
            onTurn(new TurnInfo(turn, event.placer(), event.relative()));
        }
    }

    public static void onTurn(TurnInfo turn) {
        System.out.println("BlockBattleEvents.onTurn(turn = " + turn + ") by: " + Trace.last() + Ansi.YELLOW);
        System.out.println("++++++++++++++++++++++++++ TURN ++++++++++++++++++++++++++");

        Match.addTurn(turn);
        BBPlayer player = Match.getPlayer(turn.player());

        if(!Match.allowed.contains(turn.turn().clazz) || Match.disallowed.contains(turn.turn().clazz)) {
            GameFlow.lose(GameFlow.LoseReason.DISALLOWED_TURN);
        }

        if (Match.blazeDebuff && (turn.turn().clazz == COLD || turn.turn().clazz == WATER)) {
            GameFlow.next();
            return;
        }
        if (player.isJailed() && turn.turn().type == Turn.Type.ATTACK) {
            player.showTitle("", Translation.get("attack.jailed", turn.player().locale()), null);
            GameFlow.next();
            return;
        }

        if (turn.turn().type == Turn.Type.WARP) {
            GameFlow.lose(GameFlow.LoseReason.WARP_ON_FIRST_TURN);
            Match.getPlacer().showTitle("", Translation.get("warp.first_move", player.locale()), null);
            Match.getOpponent().showTitle("", Translation.get("warp.first_move.opponent", Match.getOpponent().locale()), null);
            return;
        }

        BBPlayer opponent = Match.getOpponent();
        Cord cord = turn.cord();

        boolean initialAttacked = player.isAttacked();
        boolean doNext = true;
        boolean ignoreOpponentDefense = false;

        Turn last = Match.getLatestTurn().turn();

        switch (turn.turn()) {
            case CAULDRON, MAGMA_BLOCK, PACKED_ICE, LIGHT_BLUE_WOOL, SOUL_SAND, COMPOSTER, OAK_SAPLING, TREE, BEE_NEST -> opponent.setAttacked(true);
            case PURPLE_WOOL -> {
                GameFlow.win(GameFlow.WinReason.PURPLE_WOOL);
                return;
            }
            case DINNERBONE -> {
                if (player.isAttacked() && last.mob) {
                    player.setAttacked(false);
                }
            }
            case JAIL_METHOD -> {
                int sx = cord.x();
                int sz = cord.z();
                double px = opponent.player.getLocation().x();
                double pz = opponent.player.getLocation().x();

                if ((px == sx || px == sx + 1) && (pz == sz || pz == sz + 1)) {
                    opponent.startJailCounter(3);
                }
            }
            case NETHER_PORTAL_FRAME -> Match.netherPortalAwaitIgnite = true;
            case NETHER_PORTAL_IGNITE -> {
                if (Match.netherPortalAwaitIgnite) {
                    opponent.setAttacked(true);
                    Match.netherPortalAwaitIgnite = false;
                }
            }
            case POWDER_SNOW -> {
                opponent.setMovement(false);
                opponent.startDieCounter(2);
            }
            case LIGHTNING_TRIDENT -> {
                if (last == Turn.LIGHTNING_ROD) {
                    player.addPotionEffect(PotionEffectType.GLOWING, -1, 1);
                    opponent.addPotionEffect(PotionEffectType.BLINDNESS, -1, 1);
                }
            }
            case SEA_PICKLE_STACK -> {
                if(Match.allowed.contains(WATER)){
                    opponent.setAttacked(true);
                    opponent.setDoubleCounterAttack(true);
                }
            }
            case TNT -> {
                if (player.isDefended() && !opponent.isDefended()) GameFlow.win(GameFlow.WinReason.TNT);
                else if (!player.isDefended() && opponent.isDefended()) GameFlow.lose(GameFlow.LoseReason.TNT);

                GameFlow.draw(GameFlow.DrawReason.TNT);
            }
            case OP_GAP -> {
                if (new Random().nextBoolean()) player.addLuck(100);
                for (PotionEffect effect : player.player.getActivePotionEffects()) {
                    player.player.removePotionEffect(effect.getType());
                }
            }
            case SPRUCE_TRAPDOOR, MANGROVE_ROOTS -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, WATER, NATURE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                }
            }
            case IRON_TRAPDOOR -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, WATER, REDSTONE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                }
            }
            case GOLD_BLOCK -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, HOT, NATURE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case BLACK_WOOL -> {
                player.addPotionEffect(PotionEffectType.BLINDNESS, -1, 1);
                opponent.addPotionEffect(PotionEffectType.BLINDNESS, -1, 1);
            }
            case SCULK_BLOCK -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.addPotionEffect(PotionEffectType.DARKNESS, -1, 1);
                    opponent.removeLuck(10);
                }
            }
            case GREEN_CARPET -> {
                Material latestMaterial = Match.getLatestTurn().turn().material;
                if (player.isAttacked() && latestMaterial.name().contains("WOOL") && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case CYAN_CARPET -> {
                if (player.isAttacked() && Match.latestTurnIsClass(REDSTONE, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                } else if (!player.isAttacked()) {
                    doNext = false;
                }
            }
            case SPONGE -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, WATER, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    if(last.equals(Turn.WATER_BUCKET)){
                        Match.setRain(true);
                        doNext = false;
                        player.addLuck(15);
                        player.addPotionEffect(PotionEffectType.JUMP, -1, 255);
                    }else if(last.clazz.equals(WATER)){
                        Match.setRain(true);
                        doNext = false;
                        player.addLuck(5);
                    }
                }
            }
            case LIGHTNING_ROD -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, HOT, REDSTONE, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case OAK_DOORS -> {
                player.setDefended(true);
                opponent.setAttacked(true);
            }
            case ENDER_PEARL_TP -> {
                // TODO: Make it counter only traps or levitation
                if (player.isAttacked()) {
                    player.setAttacked(false);
                    doNext = false;
                }
            }
            case CHORUS_FRUIT_EAT -> {
                if (player.isAttacked()) {
                    player.setAttacked(false);
                }
            }
            case IRON_SHOVEL -> {
                if (player.isAttacked() && (last == Turn.CAULDRON || last.clazz == HOT || last == Turn.DIRT)) {
                    player.setAttacked(false);
                    doNext = false;
                }
            }
            case STONECUTTER -> {
                if (player.isAttacked() && (last.name().contains("TRAP") || last.name().contains("WALL"))) {
                    player.setAttacked(false);
                    doNext = false;
                }
            }
            case MAGENTA_GLAZED_TERRACOTTA -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, HOT, COLD, WATER, NATURE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case NETHERRACK -> {
                if (player.isAttacked() && Match.latestTurnIsClass(HOT, COLD, WATER) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case LAVA_BUCKET -> {
                if (Match.currentWarp.temperature == WarpSettings.Temperature.WARM) {
                    opponent.startBurnCounter(3);
                }
            }
            case FLINT_N_STEEL -> {
                if (player.isAttacked() && Match.latestTurnIsClass(COLD, REDSTONE, NATURE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);

                    player.addPotionEffect(PotionEffectType.BLINDNESS, -1, 1);
                    player.addPotionEffect(PotionEffectType.GLOWING, -1, 1);
                    opponent.addPotionEffect(PotionEffectType.BLINDNESS, -1, 1);
                    opponent.addPotionEffect(PotionEffectType.GLOWING, -1, 1);
                }
            }
            case ORANGE_WOOL -> {
                if (player.isAttacked() && Match.latestTurnIsClass(REDSTONE, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case CAMPFIRE -> {
                boolean clazzAllow = Match.latestTurnIsClass(NEUTRAL, HOT, COLD) || last.equals(Turn.BEE_NEST);
                if (player.isAttacked() && clazzAllow && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                    if(last.equals(Turn.BEE_NEST)){
                        doNext = false;
                        player.addLuck(10);
                    }
                }
            }
            case BLAZE -> {
                Match.blazeDebuff = true;
                opponent.setAttacked(true);
            }
            case RESPAWN_ANCHOR -> {
                player.player.teleport(cord.toLocation());
                doNext = new Random().nextBoolean();
            }
            case BLUE_ICE -> {
                if (player.isAttacked() && Match.latestTurnIsClass(COLD, WATER, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                }
            }
            case SPRUCE_LEAVES, GREEN_BED -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, NATURE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case WHITE_WOOL -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, WATER, REDSTONE, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    player.addPotionEffect(PotionEffectType.SLOW, -1, 125);
                    opponent.addPotionEffect(PotionEffectType.SLOW, -1, 125);
                }
            }
            case HONEY_BLOCK -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NATURE, WATER, REDSTONE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                }
            }
            case PUMPKIN_WALL -> {
                Match.setIsNight(true);
                player.addLuck(10);
            }
            case GREEN_WOOL -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NATURE, HOT, COLD) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case HAY_BALE -> {
                if (player.isAttacked() && Match.latestTurnIsClass(COLD, WATER) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case LEVER -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, REDSTONE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                }
            }
            case FENCE_GATE -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, HOT) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case REDSTONE_REPEATER -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, COLD, NATURE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case WOODEN_BUTTON -> {
                if (player.isAttacked() && Match.latestTurnIsClass(HOT, COLD, WATER) && Match.latestTurnIsUnder(cord)) {
                    if(Match.getLatestTurn().turn().type.equals(Turn.Type.COUNTERATTACK)){
                        player.setAttacked(false);
                    }
                }
            }
            case STONE_BUTTON -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NATURE, REDSTONE, DREAM) && Match.latestTurnIsUnder(cord)) {
                    if(Match.getLatestTurn().turn().type.equals(Turn.Type.COUNTERATTACK)){
                        player.setAttacked(false);
                    }
                }
            }
            case DAYLIGHT_ROW -> {
                if (player.isAttacked() && Match.latestTurnIsClass(HOT, NATURE, WATER, REDSTONE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                }
                Match.setIsNight(false);
            }
            case RED_BED -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, COLD, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case BLUE_BED -> {
                if (player.isAttacked() && Match.latestTurnIsClass(COLD, WATER, NATURE, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    player.addLuck(5);
                }
            }
            case PINK_SHEEP -> GameFlow.win(GameFlow.WinReason.PINK_SHEEP);
            case WHITE_SHEEP -> {
                if(Match.allowed.contains(COLD)){
                    doNext = false;
                }
            }
            case LIGHT_GRAY_SHEEP, DIRT, FLOWER_POT -> doNext = false;
            case GRAY_SHEEP -> player.addLuck(25);
            case BLACK_SHEEP -> {
                opponent.addPotionEffect(PotionEffectType.BLINDNESS, -1, 1);
                opponent.removeLuck(10);
            }
            case BROWN_SHEEP -> player.giveRandomItem();
            case BLUE_SHEEP -> player.setAttacked(false);
            case PHANTOM -> {
                opponent.setAttacked(true);
                ignoreOpponentDefense = true;
            }
            case DRAGON_HEAD -> {
                opponent.addPotionEffect(PotionEffectType.BLINDNESS, -1, 1);
                opponent.addPotionEffect(PotionEffectType.SLOW, -1, 1);
                opponent.removeLuck(20);
            }
            case SNOWBALL -> {
                if (player.isAttacked() && Match.latestTurnIsClass(HOT)) {
                    player.setAttacked(false);
                } else {
                    doNext = false;
                    player.addLuck(15);
                    opponent.addPotionEffect(PotionEffectType.GLOWING, -1, 1);
                }
            }
            case POLAR_BEAR -> {
                opponent.addPotionEffect(PotionEffectType.SPEED, -1, 3);
                opponent.addPotionEffect(PotionEffectType.JUMP, -1, 128);
            }
            case BRAIN_CORAL_BLOCK -> {
                if (player.isAttacked() && Match.latestTurnIsClass(REDSTONE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case HORN_CORAL -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, NATURE, WATER) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case FIRE_CORAL -> {
                if (player.isAttacked() && Match.latestTurnIsClass(HOT, REDSTONE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case FIRE_CORAL_FAN -> {
                if (player.isAttacked() && Match.latestTurnIsClass(HOT, REDSTONE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);

                    doNext = Randomizer.boolByChance(20);
                }
            }
            case SEA_LANTERN -> {
                player.addRevive();
                return;
            }
            case WATER_BUCKET -> {
                if (player.isAttacked() && (Match.getLatestTurn().cord() == cord || last == Turn.LAVA_BUCKET)) {
                    player.setAttacked(false);
                }
            }
            case DRIED_KELP_BLOCK -> {
                if (last.clazz == NATURE) {
                    opponent.setAttacked(true);
                    player.addPotionEffect(PotionEffectType.JUMP, -1, 1);
                }
            }
            case BLUE_AXOLOTL -> {
                doNext = false;
                player.addLuck(10);
                opponent.removeLuck(10);
                GameFlow.win(GameFlow.WinReason.BLUE_AXOLOTL);
            }
            case PINK_AXOLOTL -> {
                doNext = false;
                player.addLuck(10);
                opponent.removeLuck(10);

                Warp newWarp;
                do { newWarp = (Warp) Randomizer.fromArray(Warp.values()); }
                while (newWarp.temperature == WarpSettings.Temperature.WARM);

                WarpManager.warp(newWarp, player.player, opponent.player);
            }
            case BROWN_AXOLOTL -> {
                doNext = false;
                player.addLuck(10);
                opponent.removeLuck(10);

                Warp newWarp;
                do { newWarp = (Warp) Randomizer.fromArray(Warp.values()); }
                while (newWarp.temperature == WarpSettings.Temperature.MEDIUM);

                WarpManager.warp(newWarp, player.player, opponent.player);
            }
            case GOLD_AXOLOTL -> {
                doNext = false;
                player.addLuck(5);
                opponent.removeLuck(5);

                Warp newWarp;
                do { newWarp = (Warp) Randomizer.fromArray(Warp.values()); }
                while (newWarp.temperature == WarpSettings.Temperature.COLD);

                WarpManager.warp(newWarp, player.player, opponent.player);
            }
            case CYAN_AXOLOTL -> {
                doNext = false;
                player.addLuck(10);
                opponent.removeLuck(10);

                Warp newWarp;
                do { newWarp = (Warp) Randomizer.fromArray(Warp.values()); }
                while (newWarp.temperature == WarpSettings.Temperature.COLD);

                WarpManager.warp(newWarp, player.player, opponent.player);
            }
            case VERDANT_FROGLIGHT -> {
                if (player.isAttacked() && Match.latestTurnIsClass(DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case PUFFERFISH_BUCKET -> {
                opponent.addPotionEffect(PotionEffectType.POISON, -1, 1);
                opponent.startDieCounter(2);
            }
            case SPLASH_WATER_BOTTLE -> {
                if (player.isAttacked() && Match.latestTurnIsClass(HOT, REDSTONE)) {
                    player.addLuck(20);
                } else {
                    opponent.addPotionEffect(PotionEffectType.SLOW, -1, 3);
                    opponent.addPotionEffect(PotionEffectType.BLINDNESS, -1, 3);
                }
            }
            case COMPOSTER_FILL -> {
                if (Match.getLatestTurn().turn() == Turn.COMPOSTER) {
                    player.setAttacked(false);
                }
            }
            case POPPY -> Match.swapHotbars();
            case BLUE_ORCHID -> Match.nextWarp100P = true;
            case ALLIUM -> {
                int index;
                do { index = new Random().nextInt(0, 9); }
                while (opponent.player.getInventory().getItem(index) == null);

                opponent.player.getInventory().clear(index);
            }
            case AZURE_BLUET -> {
                if (!Match.azureWasUsed) {
                    player.addLuck(20);
                    Match.azureWasUsed = true;
                }
            }
            case RED_TULIP -> {
                Match.allowed.add(REDSTONE);
                doNext = false;
            }
            case ORANGE_TULIP -> {
                Match.allowed.add(HOT);
                doNext = false;
            }
            case WHITE_TULIP -> {
                Match.allowed.add(COLD);
                doNext = false;
            }
            case PINK_TULIP -> {
                Match.allowed.add(DREAM);
                doNext = false;
            }
            case CORNFLOWER -> {
                Match.allowed.add(WATER);
                Match.setRain(true);
                player.addPotionEffect(PotionEffectType.DARKNESS, -1, 1);
            }
            case OXEYE_DAISY -> {
                if (Randomizer.boolByChance(25)) doNext = false;
            }
            case WITHER_ROSE -> {
                opponent.removeLuck(15);
                Match.disallowed.add(DREAM);
            }
            case SUNFLOWER -> {
                Match.setRain(false);
                player.addLuck(10);
                player.addPotionEffect(PotionEffectType.SPEED, -1, 2);
                opponent.addPotionEffect(PotionEffectType.SPEED, -1, 2);
            }
            case SPORE_BLOSSOM -> player.makeAlwaysMove();
            case PIGLIN -> {
                player.sendMessage(McColor.RED + Translation.get("turn.piglin.msg", player.player.locale()));
                opponent.sendMessage(McColor.RED + Translation.get("turn.piglin.msg", opponent.player.locale()));
            }
            case PIGLIN_CONVERT -> {
                player.addPotionEffect(PotionEffectType.BLINDNESS, -1, 1);
                player.addPotionEffect(PotionEffectType.SLOW, -1, 2);
                player.removeLuck(-50);
                doNext = false;
            }
            case OAK_STAIRS -> {
                if (player.isAttacked() && Match.latestTurnIsClass(HOT, NATURE, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                } else {
                    doNext = false;
                }
            }
            case PINK_BED -> {
                if (Randomizer.boolByChance(70)) {
                    for (PotionEffect effect : player.player.getActivePotionEffects()) {
                        player.player.removePotionEffect(effect.getType());
                    }
                    player.startBurnCounter(-3);
                    player.startJailCounter(-3);
                    player.startDieCounter(-3);
                    player.setMovement(true);
                }
            }

            default -> {
                player.sendActionBar(McColor.RED + Translation.get("turn.waste", player.player.locale()));
                opponent.sendActionBar(McColor.YELLOW + Translation.get("turn.waste.opponent", opponent.player.locale(), player.player.getName()));
            }
        }

        if (initialAttacked) {
            Turn lt = Match.getLatestTurn().turn();
            if (lt == Turn.OAK_DOORS) {
                if (turn.turn().clazz != REDSTONE && turn.turn().clazz != HOT) {
                    player.setAttacked(true);
                }
            }
        }

        if (player.isAttacked()) GameFlow.lose(GameFlow.LoseReason.NO_COUNTER);
        if (opponent.isDefended() && !ignoreOpponentDefense) opponent.setAttacked(false);
        if (Match.currentWarp == Warp.REDSTONE && turn.turn().clazz == REDSTONE) player.addLuck(5);

        if (turn.turn().type == Turn.Type.COUNTER) {
            player.startJailCounter(-3);
            player.startBurnCounter(-3);
        }

        // Updating the players with the new temporary players from here
        Match.p1 = Match.p1.player == player.player ? player : opponent;
        Match.p2 = Match.p1.player == player.player ? opponent : player;

        System.out.println("-------------------------- TURN --------------------------" + Ansi.RESET);

        if (doNext) GameFlow.next();
        else Match.getPlacer().sendActionBar(Translation.get("turn.extra", player.locale()));
    }


    @Contract("_ -> new")
    public static @NotNull Title subtitle(String content) {
        System.out.println("BlockBattleEvents.subtitle(" + "content = " + content + ") by: " + Trace.last());

        return Title.title(
                Component.empty(),
                Component.text(content),
                Title.Times.times(
                        Duration.ZERO,
                        Duration.ofMillis(500L),
                        Duration.ofMillis(200L)
                )
        );
    }
}
