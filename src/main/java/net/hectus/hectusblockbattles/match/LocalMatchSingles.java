package net.hectus.hectusblockbattles.match;

import net.hectus.hectusblockbattles.InGameShop;
import net.hectus.hectusblockbattles.maps.GameMap;
import net.hectus.hectusblockbattles.playermode.PlayerMode;
import net.hectus.hectusblockbattles.playermode.PlayerModeManager;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.hectusblockbattles.warps.Warp;
import net.hectus.hectusblockbattles.warps.WarpSettings;
import net.hectus.color.McColor;
import net.hectus.time.Time;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
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
import org.jetbrains.annotations.NotNull;


import java.time.Duration;
import java.util.*;

@SuppressWarnings("deprecation")
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

    private BukkitTask main;
    private int turnIndex;
    private int turnTimeLeft;
    private final Location location;

    public LocalMatchSingles(JavaPlugin plugin, @NotNull GameMap gameMap, Player p1, Player p2) {
        this.plugin = plugin;
        this.gameMap = gameMap;
        this.location = new Location(gameMap.getWorld(), 0, 1, 0);

        this.warp = Warp.DEFAULT;

        players = new LinkedList<>();
        players.add(p1);
        players.add(p2);

        playerPlacedBlocks = new HashMap<>();
        playerLuckHashmap = new HashMap<>();

        potionsToRemoveAtWarp = new ArrayList<>();

        RISK = false;
        END_RISK = "NONE";
        RISK_TYPE = "NONE";


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
            StringBuilder causeBuilder = new StringBuilder();
            if (causeSubject == won) {
                causeBuilder.append("you ");
            } else {
                causeBuilder.append("your opponent ");
            }
            causeBuilder.append(cause);
            cause = causeBuilder.toString();
        }
        won.sendMessage(Component.text(McColor.GREEN + "You won the match because " + cause));
        lost.sendMessage(Component.text(McColor.RED + "You lost the match because " + cause));
        stop(false);
    }

    @Override
    public void start() {
        if (isRunning()) {
            return;
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
            InGameShop.displayShop(player);
        }

        final int BUY_TIME = 45;
        new BukkitRunnable() {
            int t = BUY_TIME;
            @Override
            public void run() {
                if (!isRunning() || getGameMap().getWorld() == null) {
                    this.cancel();
                }

                for (Player player : players) {
                    player.sendActionBar(Component.text(McColor.GRAY + Time.format(t)));
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
        }.runTaskTimer(plugin, 100L, 20L);

        // Run main match
        main = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isRunning() || getGameMap().getWorld() == null) {
                    this.cancel();
                }

                String currentPlayer = players.get(turnIndex).getName();

                for (Player player : players) {
                    player.sendActionBar(Component.text(McColor.YELLOW + currentPlayer + "'s turn." + McColor.GRAY + " Time left: " + McColor.YELLOW + Time.format(turnTimeLeft)));
                }

                turnTimeLeft--;

                if (turnTimeLeft <= 0) {
                    for (Player player : players) {
                        player.sendMessage(Component.text(McColor.YELLOW + currentPlayer + McColor.RED + " didn't play in time!"));
                    }
                    nextTurn(true);
                }
            }
        }.runTaskTimer(plugin, 105L + BUY_TIME, 20L);

        isRunning();
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
            int i = 10;
            @Override
            public void run() {
                for (Player player : getGameMap().getWorld().getPlayers()) {
                    player.sendActionBar(Component.text(McColor.YELLOW + "Returning to lobby in" + Time.format(i) + "..."));
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
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void sendPlayerToLobby(Player player) {
        PlayerModeManager.setPlayerMode(PlayerMode.DEFAULT, player);
        player.kick();
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

    public boolean outOfBounds(double x, double z) {
        Structure.Cord m = this.warp.middle;

        Structure.Cord lc = new Structure.Cord(m.x() - 4, m.y(), m.z() - 4);
        Structure.Cord hc = new Structure.Cord(m.x() + 4, m.y(), m.z() + 4);

        return x<lc.x() || x>hc.x() || z<lc.z() || z>hc.z();
    }

    @Override
    public boolean isRunning() {
        return main != null && !main.isCancelled();
    }

    @EventHandler
    public void onQuit(@NotNull PlayerQuitEvent e) {
        if (!players.remove(e.getPlayer())) {
            return;
        }

        if (players.size() <= 1) {
            stop(true);
        }
    }

    @EventHandler
    public void onEntitySpawn(@NotNull EntitySpawnEvent event){

        LivingEntity entity = (LivingEntity) event.getEntity();

        entity.setAI(false);
        entity.setInvulnerable(true);
        entity.setSilent(true);

    }

    @EventHandler
    public void onEntityRename(@NotNull PlayerInteractEntityEvent event) {
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

    private boolean disallowsClass(WarpSettings.Class allow){

        for (WarpSettings.Class aClass : warp.allow) {
            if (aClass.equals(allow)) {
                return false;
            }
        }
        return true;

    }
    
    public void failed() {
        getOppositeTurnPlayer().showTitle(Title.title(Component.empty(), Component.text(McColor.RED + "Failed!"), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
        getCurrentTurnPlayer().showTitle(Title.title(Component.empty(), Component.text(McColor.RED + "Failed!"), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
    }
}