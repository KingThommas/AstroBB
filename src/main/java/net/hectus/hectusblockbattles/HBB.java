package net.hectus.hectusblockbattles;

import net.hectus.hectusblockbattles.commands.StructureCommand;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.structures.v2.StructureManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

@SuppressWarnings("ResultOfMethodCallIgnored") // For "Result of '*' is ignored."
public final class HBB extends JavaPlugin {
    public static Logger LOGGER;
    public static final World WORLD = Bukkit.getWorld("world");
    public static File dataFolder;

    @Override
    public void onEnable() {
        LOGGER = this.getLogger();

        // this.saveDefaultConfig();

        // InGameShop.initialize();

        // // Don't remove this, this is very important to not break anything!
        // try { PlayerDatabase.connect(); }
        // catch (SQLException e) { throw new RuntimeException(e); }
        // //=================================================================

        getServer().getPluginManager().registerEvents(new BlockBattleEvents(), this);
        // getServer().getPluginManager().registerEvents(new InGameShopEvents(), this);

        // LOGGER.info("Hectus BlockBattles started.");

        // I removed the map thingy, since it's not important
        // I will later on have the right setup in every instance anyways

        dataFolder = getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();

        // File mapsFolder = new File(dataFolder, "maps");
        // if (!mapsFolder.exists()) mapsFolder.mkdirs();

        StructureManager.loadAll(true);

        // TODO: REMOVE
        // Objects.requireNonNull(getCommand("test")).setExecutor(new TestCommand(this));
        //=============
        // Objects.requireNonNull(getCommand("match")).setExecutor(new MatchCommand(this));
        Objects.requireNonNull(getCommand("structure")).setExecutor(new StructureCommand());
    }

    @Override
    public void onDisable() {
        // // Don't remove this, this is very important to not break anything!
        // try { PlayerDatabase.disconnect(); }
        // catch (SQLException e) { throw new RuntimeException(e); }
        // //=================================================================

        LOGGER.info("Hectus BlockBattles stopped.");
    }
}
