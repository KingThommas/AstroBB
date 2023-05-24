package net.hectus.hectusblockbattles.match;

import net.hectus.hectusblockbattles.IngameShop;
import net.hectus.hectusblockbattles.maps.GameMap;
import net.hectus.hectusblockbattles.playermode.PlayerMode;
import net.hectus.hectusblockbattles.playermode.PlayerModeManager;
import net.hectus.hectusblockbattles.structures.Structure;
import net.hectus.hectusblockbattles.structures.Structures;
import net.hectus.hectusblockbattles.warps.Warp;
import net.hectus.hectusblockbattles.warps.WarpSettings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.bukkit.Bukkit;
import org.bukkit.World;


import java.time.Duration;
import java.util.*;
import java.util.logging.Level;

public class LocalMatchSingles implements Match, Listener {
    private final JavaPlugin plugin;
    private final GameMap gameMap;

    private Warp warp;

    private final List<Player> players;
    private final HashMap<Player, Integer> playerLuckHashmap;
    private final HashMap<Player, Set<Block>> playerPlacedBlocks;
    private final List<PotionEffectType> potionsToRemoveAtWarp;
    //todo: on warp -> remove potion effects.

    private boolean RISK;
    private String END_RISK;
    private String RISK_TYPE;
    private String RISK_COUNTER;
    private String RISK_COUNTER_TYPE;

    private BukkitTask main;
    private int turnIndex;
    private int turnTimeLeft;
    private final Location location;

    private Block lastBlock;

    public LocalMatchSingles(JavaPlugin plugin, GameMap gameMap, Player p1, Player p2) {
        this.plugin = plugin;
        this.gameMap = gameMap;
        this.location = new Location(gameMap.getWorld(), 0, 1, 0);

        this.warp = Warp.DEFAULT;

        players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        playerPlacedBlocks = new HashMap<>();
        playerLuckHashmap = new HashMap<>();

        potionsToRemoveAtWarp = new ArrayList<>();

        RISK = false;
        END_RISK = "NONE";
        RISK_COUNTER = "NONE";
        RISK_TYPE = "NONE";
        RISK_COUNTER_TYPE = "NONE";



        turnIndex = -1;
        turnTimeLeft = -1;
    }

    @Override
    public void nextTurn(boolean wasSkipped) {
        turnIndex++;
        if (turnIndex >= players.size()) {
            turnIndex = 0;
        }

        // 15 seconds, multiplied by 20 for ticks
        turnTimeLeft = 15 * 20;
    }

    @Override
    public GameMap getGameMap() {
        return gameMap;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    public void end(Player won, Player lost, Player causeSubject, String cause) {
        PlayerModeManager.initializePlayerMode(won);
        PlayerModeManager.initializePlayerMode(lost);
        for (PotionEffect effect : won.getActivePotionEffects()) {
            won.removePotionEffect(effect.getType());
        }
        for (PotionEffect effect : lost.getActivePotionEffects()) {
            lost.removePotionEffect(effect.getType());
        }
        if (causeSubject != null) {
            if (causeSubject == won) {
                cause = "you " + cause;
            } else {
                cause = "your opponent " + cause;
            }
        }
        won.sendMessage("You won the match because " + cause);
        lost.sendMessage("You lost the match because " + cause);
        stop(false);
    }

    @Override
    public boolean start() {
        if (isRunning()) {
            return true;
        }

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        // Teleport players
        for (Player player : players) {
            player.teleport(location);
            player.setFlying(false);
            player.setVelocity(new Vector(0, 0, 0));
        }

        // Enter shop phase (45-second buy time)
        for (Player player : players) {
            PlayerModeManager.setPlayerMode(PlayerMode.SHOP_PHASE, player);
            // TODO: OPEN SHOP INVENTORY
            IngameShop.displayShop(player);
        }

        final int BUY_TIME = 45 * 20;
        new BukkitRunnable() {
            int t = BUY_TIME;
            @Override
            public void run() {
                if (!isRunning() || getGameMap().getWorld() == null) {
                    this.cancel();
                }

                if (t % 20 == 0) {
                    for (Player player : players) {
                        player.sendActionBar(Component.text(t, NamedTextColor.GRAY));
                    }
                }

                if (t <= 0) {
                    for (Player player : players) {
                        PlayerModeManager.setPlayerMode(PlayerMode.BLOCK_BATTLES, player);
                        player.closeInventory();
                    }
                    nextTurn(false);
                    this.cancel();
                }

                t--;
            }
        }.runTaskTimer(plugin, 100, 1);

        // Run main match
        main = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isRunning() || getGameMap().getWorld() == null) {
                    this.cancel();
                }

                for (Player player : players) {
                    player.sendActionBar(players.get(turnIndex).displayName().color(NamedTextColor.YELLOW)
                            .append(Component.text("'s turn. Time left: ", NamedTextColor.GRAY))
                            .append(Component.text(turnTimeLeft / 20d, NamedTextColor.YELLOW)));
                }

                turnTimeLeft--;

                if (turnTimeLeft <= 0) {
                    for (Player player : players) {
                        player.sendMessage(players.get(turnIndex).displayName().color(NamedTextColor.YELLOW)
                                .append(Component.text(" did not play in time!", NamedTextColor.RED)));
                    }
                    nextTurn(true);
                }
            }
        }.runTaskTimer(plugin, 101 + BUY_TIME, 1);

        return isRunning();
    }

    @Override
    public void stop(boolean isAbrupt) {
        if (main != null) {
            main.cancel();
        }
        main = null;

        HandlerList.unregisterAll(this);

        for (Player player : getGameMap().getWorld().getPlayers()) {
            player.setVelocity(new Vector(0, 2, 0));
            player.setFlying(true);
            if (isAbrupt) {
                sendPlayerToLobby(player);
            }
        }

        if (isAbrupt) {
            getGameMap().unload();
            return;
        }

        new BukkitRunnable() {
            int i = 10 * 20;
            @Override
            public void run() {
                for (Player player : getGameMap().getWorld().getPlayers()) {
                    player.sendActionBar(Component.text("Returning to lobby in ", NamedTextColor.YELLOW)
                            .append(Component.text(i / 20, NamedTextColor.RED))
                            .append(Component.text("...", NamedTextColor.YELLOW)));
                }

                if (getGameMap().getWorld().getPlayers().size() < 1) {
                    this.cancel();
                }

                if (i <= 0) {
                    for (Player player : getGameMap().getWorld().getPlayers()) {
                        sendPlayerToLobby(player);
                    }
                    getGameMap().unload();
                    this.cancel();
                }

                i--;
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void sendPlayerToLobby(Player player) {
        PlayerModeManager.setPlayerMode(PlayerMode.DEFAULT, player);
        // TODO: SEND TO LOBBY
        player.teleport(getGameMap().getSourceWorld().getSpawnLocation());
    }

    @Override
    public Player getCurrentTurnPlayer() {
        return players.get(turnIndex);
    }

    @Override
    public double getGameScore() {
        return 0;
    }

    @Override
    public void setLuck(Player player, int amount) {
        playerLuckHashmap.put(player, amount);
    }

    @Override
    public void addLuck(Player player, int amount) {
        playerLuckHashmap.put(player, playerLuckHashmap.get(player) + amount);
    }

    @Override
    public void removeLuck(Player player, int amount) {
        playerLuckHashmap.put(player, playerLuckHashmap.get(player) - amount);
    }

    @Override
    public int getLuck(Player player) {
        return playerLuckHashmap.get(player);
    }

    @Override
    public boolean luckCheck(Player player, double chance) {
        int random = (int) Math.round(Math.random()) * 100;
        double reverseChange = 100 - chance;
        int luck = playerLuckHashmap.get(player);
        return random + luck >= reverseChange;
    }

    public Player getPlayer(boolean turn) {
        return turn ? players.get(1) : players.get(0);
    }

    public boolean getTurn() {
        return turnIndex == 1;
    }

    public Player getOppositeTurnPlayer() {
        return getPlayer(!getTurn());
    }

    public Block getLastBlock() {
        return lastBlock;
    }

    public Location getLocation() {
        return location;
    }

    public boolean checkBounds(int x, int z) {

        Location point1 = this.warp.getCorner1();
        Location point2 = this.warp.getCorner2();

        double minX = Math.min(point1.getX(), point2.getX());
        double minZ = Math.min(point1.getZ(), point2.getZ());
        double maxX = Math.max(point1.getX(), point2.getX());
        double maxZ = Math.max(point1.getZ(), point2.getZ());

        return (double) x >= minX && (double) x <= maxX && (double) z >= minZ && (double) z <= maxZ;
    }

    @Override
    public boolean isRunning() {
        return main != null && !main.isCancelled();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!players.remove(e.getPlayer())) {
            return;
        }

        if (players.size() <= 1) {
            stop(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();
        int index = players.indexOf(player);
        if (index < 0) {
            return;
        }

        if (turnIndex < 0 || player != (players.get(turnIndex))) {
            e.setCancelled(true);
            return;
        }

        playerPlacedBlocks.putIfAbsent(player, new HashSet<>());
        Set<Block> blocks = playerPlacedBlocks.get(player);
        blocks.add(e.getBlockPlaced());
        lastBlock = e.getBlockPlaced();

        Structure build = new Structure("Build", playerPlacedBlocks.get(e.getPlayer()));
        for (Structure structure : Structures.getAllStructures()) {
            Bukkit.getLogger().log(Level.INFO, "TESTING: " + structure.getName());
            if (structure.hasSubset(build)) {
                if (structure.getPlacedBlocks().size() == blocks.size()) {
                    // Perfect match
                    Bukkit.broadcast(player.displayName()
                            .append(Component.text(" played ", NamedTextColor.YELLOW))
                            .append(Component.text(structure.getName())));
                    blocks.clear();
                    switch (structure.getName()){
                        case "PURPLE_WOOL":
                            if(!allowsClass(WarpSettings.Classes.NEUTRAL)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            RISK = true;
                            END_RISK = "PURPLE_WOOL";
                            RISK_TYPE = "NEUTRAL";
                            nextTurn(false);
                            return;
                        case "SPRUCE_TRAPDOOR":
                            if(!allowsClass(WarpSettings.Classes.NEUTRAL)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            if(RISK){
                                if(Arrays.asList("NEUTRAL", "WATER", "NATURE").contains(RISK_TYPE)){
                                    RISK = false;
                                    END_RISK = "NONE";
                                    RISK_COUNTER = "NONE";
                                    RISK_COUNTER_TYPE = "NONE";
                                    RISK_TYPE = "NONE";
                                    return;
                                }
                            }else{
                                //should the game stop here for wrong usage?
                            }
                            break;
                        case "IRON_TRAPDOOR":
                            if(!allowsClass(WarpSettings.Classes.NEUTRAL)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            if(RISK){
                                if(Arrays.asList("NEUTRAL","WATER", "REDSTONE").contains(RISK_TYPE)){
                                    RISK = true;
                                    END_RISK = "IRON_TRAPDOOR";
                                    RISK_COUNTER = "NONE";
                                    RISK_COUNTER_TYPE = "NONE";
                                    RISK_TYPE = "NEUTRAL";
                                    nextTurn(false);
                                    return;
                                }
                            }else{
                                //should the game stop here for wrong usage?
                            }
                            break;
                        case "GOLD_BLOCK":
                            if(!allowsClass(WarpSettings.Classes.NEUTRAL)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            if(RISK){
                                if(Arrays.asList("NEUTRAL","HOT", "NATURE").contains(RISK_TYPE)){
                                    RISK = true;
                                    END_RISK = "GOLD_BLOCK";
                                    RISK_COUNTER = "NONE";
                                    RISK_COUNTER_TYPE = "NONE";
                                    RISK_TYPE = "NEUTRAL";
                                    nextTurn(false);
                                    return;
                                }
                            }else{
                                //should the game stop here for wrong usage?
                            }
                            break;
                        case "BLACK_WOOL":
                            if(!allowsClass(WarpSettings.Classes.NEUTRAL)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            //todo: stop any dream blocks;
                            players.forEach(player1 -> player1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1)));
                            potionsToRemoveAtWarp.add(PotionEffectType.BLINDNESS);
                            break;
                        case "SCULK_BLOCK":
                            if(!allowsClass(WarpSettings.Classes.NEUTRAL)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            //todo: stop neutral or dream blocks;
                            getOppositeTurnPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, Integer.MAX_VALUE, 1));
                            removeLuck(getOppositeTurnPlayer(), 10);
                            potionsToRemoveAtWarp.add(PotionEffectType.DARKNESS);
                            break;
                        case "GREEN_CARPET":
                            if(!allowsClass(WarpSettings.Classes.NEUTRAL)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            if(RISK){
                                if(END_RISK.endsWith("_WOOL")){
                                    END_RISK = "GREEN_CARPET";
                                    RISK_COUNTER = "NONE";
                                    RISK_COUNTER_TYPE = "NONE";
                                    RISK_TYPE = "NEUTRAL";
                                    nextTurn(false);
                                    return;
                                }
                            }else{
                                //should the game stop here for wrong usage?
                            }
                            break;
                        case "MAGMA_BLOCK":
                            if(!allowsClass(WarpSettings.Classes.HOT)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            RISK = true;
                            END_RISK = "MAGMA_BLOCK";
                            RISK_COUNTER = "NONE";
                            RISK_COUNTER_TYPE = "NONE";
                            RISK_TYPE = "HOT";
                            nextTurn(false);
                            return;
                        case "NETHERRACK":
                            if(!allowsClass(WarpSettings.Classes.HOT)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            if(RISK){
                                if(Arrays.asList("NEUTRAL","HOT", "NATURE").contains(RISK_TYPE)){
                                    END_RISK = "NETHERRACK";
                                    RISK_COUNTER = "NONE";
                                    RISK_COUNTER_TYPE = "NONE";
                                    RISK_TYPE = "HOT";
                                    nextTurn(false);
                                    return;
                                }
                            }else{
                                //should the game stop here for wrong usage?
                            }
                        case "ORANGE_WOOL":
                            if(!allowsClass(WarpSettings.Classes.HOT)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            if(RISK){
                                if(Arrays.asList("REDSTONE", "DREAM").contains(RISK_TYPE)){
                                    END_RISK = "ORANGE_WOOL";
                                    RISK_COUNTER = "NONE";
                                    RISK_COUNTER_TYPE = "NONE";
                                    RISK_TYPE = "HOT";
                                    nextTurn(false);
                                    return;
                                }
                            }else{
                                //should the game stop here for wrong usage?
                            }
                        case "CAMPFIRE":
                            if(!allowsClass(WarpSettings.Classes.HOT)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            if(RISK){
                                if(Arrays.asList("NEUTRAL", "HOT", "COLD").contains(RISK_TYPE) || END_RISK == "BEEHIVE"){
                                    END_RISK = "ORANGE_WOOL";
                                    RISK_COUNTER = "NONE";
                                    RISK_COUNTER_TYPE = "NONE";
                                    RISK_TYPE = "HOT";
                                    nextTurn(false);
                                    return;
                                }
                            }else{
                                //should the game stop here for wrong usage?
                            }
                        case "PACKED_ICE":
                            if(!allowsClass(WarpSettings.Classes.COLD)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            RISK = true;
                            END_RISK = "PACKED_ICE";
                            RISK_TYPE = "COLD";
                            nextTurn(false);
                            return;
                        case "BLUE_ICE":
                            if(!allowsClass(WarpSettings.Classes.COLD)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            if(RISK){
                                if(Arrays.asList("COLD", "WATER", "DREAM").contains(RISK_TYPE)){
                                    RISK = false;
                                    END_RISK = "NONE";
                                    RISK_COUNTER = "NONE";
                                    RISK_COUNTER_TYPE = "NONE";
                                    RISK_TYPE = "NONE";
                                }
                            }else{
                                //should the game stop here for wrong usage?
                            }
                        case "SPRUCE_LEAVES":
                            if(!allowsClass(WarpSettings.Classes.COLD)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            if(RISK){
                                if(Arrays.asList("NEUTRAL", "NATURE").contains(RISK_TYPE)){
                                    END_RISK = "SPRUCE_LEAVES";
                                    RISK_COUNTER = "NONE";
                                    RISK_COUNTER_TYPE = "NONE";
                                    RISK_TYPE = "COLD";
                                    nextTurn(false);
                                    return;
                                }
                            }else{
                                RISK = true;
                                END_RISK = "SPRUCE_LEAVES";
                                RISK_COUNTER = "NONE";
                                RISK_COUNTER_TYPE = "NONE";
                                RISK_TYPE = "COLD";
                                nextTurn(false);
                                return;
                            }
                        case "LIGHT_BLUE_WOOL":
                            if(!allowsClass(WarpSettings.Classes.COLD)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            RISK = true;
                            END_RISK = "LIGHT_BLUE_WOOL";
                            RISK_TYPE = "COLD";
                            nextTurn(false);
                            return;
                        case "WHITE_WOOL":
                            if(!allowsClass(WarpSettings.Classes.COLD)){
                                this.end(getCurrentTurnPlayer(), getOppositeTurnPlayer(), getCurrentTurnPlayer(), "Used a denied block.");
                                return;
                            }
                            if(RISK){
                                if(Arrays.asList("NEUTRAL", "WATER", "REDSTONE", "DREAM").contains(RISK_TYPE)){
                                    RISK = false;
                                    END_RISK = "NONE";
                                    RISK_TYPE = "NONE";
                                    RISK_COUNTER_TYPE = "NONE";
                                    RISK_COUNTER = "NONE";
                                }
                            }
                            players.forEach(player1 -> player1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 225)));
                            potionsToRemoveAtWarp.add(PotionEffectType.SLOW);
                        case "WARP_NETHER":
                            if(warp.getDimension().equals(WarpSettings.Dimension.END) || warp.getDimension().equals(WarpSettings.Dimension.OVERWORLD) || warp.getTemperature().equals(WarpSettings.Temperature.WARM) || warp.getTemperature().equals(WarpSettings.Temperature.MEDIUM)){
                                if(luckCheck(getCurrentTurnPlayer(), Warp.NETHER.getChance() * 100)){
                                    warp = Warp.NETHER;
                                    //todo: warp;
                                }else{
                                    getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                    getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                }
                            }else{
                                end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Has played a warp, which cannot be used here.");
                            }
                            break;
                        case "WARP_ICE":
                            if(warp.getTemperature().equals(WarpSettings.Temperature.WARM)){
                                end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Has played a warp, which cannot be used here.");
                            }else{
                                if(luckCheck(player, Warp.ICE.getChance() * 100)){
                                    addLuck(player, 5);
                                    warp = Warp.ICE;
                                    //todo: warp;
                                }else{
                                    getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                    getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                }
                            }
                            break;
                        case "WARP_SNOW":
                            if(warp.getTemperature().equals(WarpSettings.Temperature.WARM)){
                                end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Has played a warp, which cannot be used here.");
                            }else{
                                if(luckCheck(player, Warp.SNOW.getChance() * 100)){
                                    addLuck(player, 5);
                                    warp = Warp.SNOW;
                                    //todo: warp;
                                }else{
                                    getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                    getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                }
                            }
                            break;
                        case "WARP_CLIFF":
                            if(warp.getTemperature().equals(WarpSettings.Temperature.MEDIUM) || warp.getTemperature().equals(WarpSettings.Temperature.COLD)){
                                if(luckCheck(player, Warp.CLIFF.getChance() * 100)){
                                    addLuck(player, 15);
                                    warp = Warp.CLIFF;
                                    //todo: warp;
                                }else{
                                    getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                    getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                }
                            }else{
                                end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Has played a warp, which cannot be used here.");
                            }
                            break;
                        case "WARP_UNDERWATER":
                            if(warp.getTemperature().equals(WarpSettings.Temperature.WARM) || warp.getDimension().equals(WarpSettings.Dimension.NETHER)){
                                end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Has played a warp, which cannot be used here.");
                            }else{
                                if(luckCheck(player, Warp.UNDERWATER.getChance() * 100)){
                                    addLuck(player, 8);
                                    warp = Warp.UNDERWATER;
                                    //todo: warp;
                                }else{
                                    getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                    getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                }
                            }
                            break;
                        case "WARP_VOID":
                            if(warp.getLayer().equals(WarpSettings.Layer.SURFACE)){
                                end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Has played a warp, which cannot be used here.");
                            }else{
                                if(luckCheck(player, Warp.VOID.getChance() * 100)){
                                    addLuck(player, 15);
                                    warp = Warp.VOID;
                                    //todo: warp;
                                    getOppositeTurnPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 225));
                                    getOppositeTurnPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 225));
                                    potionsToRemoveAtWarp.add(PotionEffectType.BLINDNESS);
                                    potionsToRemoveAtWarp.add(PotionEffectType.JUMP);
                                }else{
                                    getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                    getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                }
                            }
                            break;
                        case "WARP_REDSTONE":
                            if(!(warp.getTemperature().equals(WarpSettings.Temperature.MEDIUM) || warp.getTemperature().equals(WarpSettings.Temperature.WARM))){
                                end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Has played a warp, which cannot be used here.");
                            }else{
                                if(luckCheck(player, Warp.REDSTONE.getChance() * 100)){
                                    addLuck(player, 5);
                                    warp = Warp.REDSTONE;
                                    //todo: warp;
                                    //todo: All redstone items played in this warp give +5 Luck to the user;
                                }else{
                                    getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                    getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                }
                            }
                            break;
                        case "WARP_FOREST":
                            if(!(warp.getLayer().equals(WarpSettings.Layer.SURFACE) && warp.getDimension().equals(WarpSettings.Dimension.OVERWORLD))){
                                end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Has played a warp, which cannot be used here.");
                            }else{
                                if(luckCheck(player, Warp.FOREST.getChance() * 100)){
                                    warp = Warp.FOREST;
                                    //todo: warp;
                                }else{
                                    getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                    getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                }
                            }
                            break;
                        case "WARP_DESERT":
                            if(!(warp.getLayer().equals(WarpSettings.Layer.SURFACE) && warp.getDimension().equals(WarpSettings.Dimension.OVERWORLD))){
                                end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Has played a warp, which cannot be used here.");
                            }else{
                                if(luckCheck(player, Warp.DESERT.getChance() * 100)){
                                    warp = Warp.DESERT;
                                    getOppositeTurnPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1));
                                    potionsToRemoveAtWarp.add(PotionEffectType.SLOW);
                                    addLuck(player, 5);
                                    //todo: warp;
                                }else{
                                    getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                    getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                }
                            }
                            break;
                        case "WARP_AETHER":
                            if(!warp.getDimension().equals(WarpSettings.Dimension.OVERWORLD)){
                                end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Has played a warp, which cannot be used here.");
                            }else{
                                if(luckCheck(player, Warp.AETHER.getChance() * 100)){
                                    warp = Warp.AETHER;
                                    addLuck(player, 10);
                                    //todo: warp;
                                }else{
                                    getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                    getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                }
                            }
                            break;
                        case "WARP_BOOK":
                            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                            if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
                                end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Has played a warp, which cannot be used today (based on UTC/GMT).");
                            }else{
                                if(luckCheck(player, Warp.BOOK.getChance() * 100)){
                                    warp = Warp.BOOK;
                                    addLuck(player, 35);
                                    //todo: warp;
                                }else{
                                    getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                    getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                }
                            }
                            break;
                        case "WARP_AMETHYST":
                            if(luckCheck(player, Warp.AMETHYST.getChance() * 100)){
                                warp = Warp.AMETHYST;
                                addLuck(player, 20);
                                //todo: warp;
                            }else{
                                getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                            }
                            break;
                        case "WARP_SUN":
                            Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
                            if(Calendar.HOUR_OF_DAY >= 6 && Calendar.HOUR_OF_DAY < 18){
                                end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Has played a warp, which cannot be used at this moment of the day (based on UTC/GMT).");
                            }else{
                                if(luckCheck(player, Warp.SUN.getChance() * 100)){
                                    warp = Warp.SUN;
                                    //todo: warp;
                                    potionsToRemoveAtWarp.add(PotionEffectType.BLINDNESS);
                                    getOppositeTurnPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1));
                                    for(Player player1 : players){
                                        for(ItemStack itemStack : player1.getInventory().getContents()){
                                            List<Material> waterItems = new ArrayList<>(Arrays.asList(
                                                    Material.TRIDENT,
                                                    Material.BRAIN_CORAL_BLOCK,
                                                    Material.HORN_CORAL,
                                                    Material.SEA_LANTERN,
                                                    Material.WATER_BUCKET,
                                                    Material.DRIED_KELP_BLOCK,
                                                    Material.OAK_BOAT,
                                                    Material.AXOLOTL_SPAWN_EGG,
                                                    Material.VERDANT_FROGLIGHT,
                                                    Material.PUFFERFISH_BUCKET,
                                                    Material.BLUE_STAINED_GLASS
                                            ));
                                            if(waterItems.contains(itemStack.getType())){
                                                player1.getInventory().remove(itemStack);
                                            }
                                        }
                                    }
                                }else{
                                    getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                    getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                }
                            }
                            break;
                        case "WARP_MUSHROOM":
                            if(warp != Warp.FOREST){
                                end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Has played a warp, which cannot be used here.");
                            }
                            if(luckCheck(player, Warp.MUSHROOM.getChance() * 100)){
                                warp = Warp.MUSHROOM;
                                //todo: warp;
                                //todo: +1 random item;
                            }else{
                                getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                            }
                            break;
                        case "WARP_END":
                            if(warp.getDimension() != WarpSettings.Dimension.OVERWORLD){
                                end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Has played a warp, which cannot be used here.");
                            }
                            if(luckCheck(player, Warp.END.getChance() * 100)){
                                warp = Warp.END;
                                //todo: warp;
                                addLuck(player, 10);
                                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
                            }else{
                                getOppositeTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                                getCurrentTurnPlayer().showTitle(Title.title(Component.text(""), Component.text("Failed!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
                            }
                            break;
                        case "WARP_GOD":
                            if(Math.random() < 0.5){
                                warp = Warp.HEAVEN;
                                addLuck(getCurrentTurnPlayer(), 20);
                                addLuck(getOppositeTurnPlayer(), 10);
                                //todo: warp;
                            }else{
                                warp = Warp.HELL;
                                removeLuck(getCurrentTurnPlayer(), 10);
                                removeLuck(getOppositeTurnPlayer(), 20);
                                //todo: warp;
                            }
                        default:
                            for(Player Lplayer : players){
                                Lplayer.sendMessage(Component.text("This structure is", NamedTextColor.GRAY)
                                        .append(Component.text(" known ", NamedTextColor.GREEN))
                                        .append(Component.text("but has", NamedTextColor.GRAY))
                                        .append(Component.text(" no actions linked", NamedTextColor.RED))
                                        .append(Component.text(".", NamedTextColor.GRAY)));
                            }
                            nextTurn(true);
                            return;
                    }
                }

                return;
            }
        }

        // No such structure exists: Misplace!
        player.showTitle(Title.title(Component.text(""), Component.text("Misplace!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));

        for (Block block : blocks) {
            player.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5d, 0.5d, 0.5d), 10, 0.5d, 0.5d, 0.5d, block.getBlockData());
            block.setType(Material.AIR);
        }

        blocks.clear();

        if(RISK){
            this.end(getOppositeTurnPlayer(), getCurrentTurnPlayer(), getCurrentTurnPlayer(), "Can't counter " + END_RISK);
        }

        nextTurn(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event){

        LivingEntity entity = (LivingEntity) event.getEntity();

        entity.setAI(false);
        entity.setInvulnerable(true);
        entity.setSilent(true);

    }

    @EventHandler
    public void onEntityRename(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType().isItem() && item.getItemMeta() != null && item.getItemMeta().hasDisplayName()) {
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("dinnerbone") || item.getItemMeta().getDisplayName().equalsIgnoreCase("grumm")) {
                entity.remove();
                //todo: remove effect from mob.
            }
        }
    }

    private boolean allowsClass(WarpSettings.Classes allow){

        for (WarpSettings.Classes classes : warp.getAllow()) {
            if (classes.equals(allow)) {
                return true;
            }
        }
        return false;

    }
    public void moveRegion(World world, Location start, Location end, Location newLocation) {
        int minX = Math.min(start.getBlockX(), end.getBlockX());
        int minY = Math.min(start.getBlockY(), end.getBlockY());
        int minZ = Math.min(start.getBlockZ(), end.getBlockZ());

        int maxX = Math.max(start.getBlockX(), end.getBlockX());
        int maxY = Math.max(start.getBlockY(), end.getBlockY());
        int maxZ = Math.max(start.getBlockZ(), end.getBlockZ());

        int dx = newLocation.getBlockX() - minX;
        int dy = newLocation.getBlockY() - minY;
        int dz = newLocation.getBlockZ() - minZ;

        for (int y = maxY; y >= minY; y--) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    Block newBlock = world.getBlockAt(x + dx, y + dy, z + dz);
                    newBlock.setType(block.getType());
                    newBlock.setBlockData(block.getBlockData());
                    block.setType(Material.AIR);
                }
            }
        }
    }
}