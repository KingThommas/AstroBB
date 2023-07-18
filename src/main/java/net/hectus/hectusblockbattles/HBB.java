package net.hectus.hectusblockbattles;

import net.hectus.color.Ansi;
import net.hectus.hectusblockbattles.commands.DebugCommand;
import net.hectus.hectusblockbattles.commands.GiveUpCommand;
import net.hectus.hectusblockbattles.commands.MatchCommand;
import net.hectus.hectusblockbattles.commands.StructureCommand;
import net.hectus.hectusblockbattles.events.BaseEvents;
import net.hectus.hectusblockbattles.events.ClientEvents;
import net.hectus.hectusblockbattles.events.InGameShopEvents;
import net.hectus.hectusblockbattles.structures.v2.StructureManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public final class HBB extends JavaPlugin {
    public static Logger LOGGER;
    public static World WORLD;
    public static File dataFolder;

    @Override
    public void onEnable() {
        LOGGER = getLogger();

        // // Don't remove this, this is very important to not break anything!
        // try { PlayerDatabase.connect(); }
        // catch (SQLException e) { throw new RuntimeException(e); }
        // //=================================================================

        getServer().getPluginManager().registerEvents(new InGameShopEvents(), this);
        getServer().getPluginManager().registerEvents(new BaseEvents(), this);
        getServer().getPluginManager().registerEvents(new ClientEvents(), this);

        LOGGER.info(Ansi.LIME + "Hectus BlockBattles started." + Ansi.RESET);

        dataFolder = getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();

        // File mapsFolder = new File(dataFolder, "maps");
        // if (!mapsFolder.exists()) mapsFolder.mkdirs();

        StructureManager.loadAll(true);

        Objects.requireNonNull(getCommand("structure")).setExecutor(new StructureCommand());
        Objects.requireNonNull(getCommand("match")).setExecutor(new MatchCommand());
        Objects.requireNonNull(getCommand("giveup")).setExecutor(new GiveUpCommand());
        Objects.requireNonNull(getCommand("debug")).setExecutor(new DebugCommand());

        WORLD = Bukkit.getWorld("world");

        try {
            Translation.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
