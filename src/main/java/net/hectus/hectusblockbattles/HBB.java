package net.hectus.hectusblockbattles;

import net.hectus.hectusblockbattles.commands.MatchCommand;
import net.hectus.hectusblockbattles.commands.StructureCommand;
import net.hectus.hectusblockbattles.commands.TestCommand;
import net.hectus.hectusblockbattles.database.PlayerDatabase;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.events.InGameShopEvents;
import net.hectus.hectusblockbattles.structures.Structures;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Logger;

@SuppressWarnings("ResultOfMethodCallIgnored") // For "Result of '*' is ignored."
public final class HBB extends JavaPlugin {
    public static Logger LOGGER;
    public static final World WORLD = Bukkit.getWorld("world");

    @Override
    public void onEnable() {
        LOGGER = this.getLogger();

        this.saveDefaultConfig();

        InGameShop.initialize();

        // Don't remove this, this is very important to not break anything!
        try { PlayerDatabase.connect(); }
        catch (SQLException e) { throw new RuntimeException(e); }
        //=================================================================

        getServer().getPluginManager().registerEvents(new BlockBattleEvents(), this);
        getServer().getPluginManager().registerEvents(new InGameShopEvents(), this);

        LOGGER.info("Hectus BlockBattles started.");

        // I removed the map thingy, since it's not important
        // I will later on have the right setup in every instance anyways

        File mapsFolder = new File(this.getDataFolder(), "maps");
        if (!mapsFolder.exists()) mapsFolder.mkdirs();

        File structuresFolder = new File(this.getDataFolder(), "structures");
        if (!structuresFolder.exists()) structuresFolder.mkdirs();
        Structures.loadAllStructures(structuresFolder);

        // TODO: REMOVE
        Objects.requireNonNull(getCommand("test")).setExecutor(new TestCommand(this));
        //=============
        Objects.requireNonNull(getCommand("match")).setExecutor(new MatchCommand(this));
        Objects.requireNonNull(getCommand("structure")).setExecutor(new StructureCommand(this));

    }

    @Override
    public void onDisable() {
        // Don't remove this, this is very important to not break anything!
        try { PlayerDatabase.disconnect(); }
        catch (SQLException e) { throw new RuntimeException(e); }
        //=================================================================

        LOGGER.info("Hectus BlockBattles stopped.");
    }
}
