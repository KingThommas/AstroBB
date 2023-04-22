package net.hectus.hectusblockbattles;

import net.hectus.hectusblockbattles.commands.MatchCommand;
import net.hectus.hectusblockbattles.commands.StructureCommand;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.events.IngameShopEvents;
import net.hectus.hectusblockbattles.events.ItemExplorerEvents;
import net.hectus.hectusblockbattles.commands.TestCommand;
import net.hectus.hectusblockbattles.maps.MapWorldGenerator;
import net.hectus.hectusblockbattles.structures.Structures;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public final class HectusBlockBattles extends JavaPlugin {

    public static Logger LOGGER;

    @Override
    public void onEnable() {
        LOGGER = this.getLogger();

        this.saveDefaultConfig();

//        HectusDatabase database = new HectusDatabase();
//        database.initialize(this.getConfig());

        IngameShop.initialize();

        getServer().getPluginManager().registerEvents(new BlockBattleEvents(), this);
        getServer().getPluginManager().registerEvents(new IngameShopEvents(), this);
        getServer().getPluginManager().registerEvents(new ItemExplorerEvents(), this);

        LOGGER.info("Hectus plugin started.");

        // TODO: REMOVE
        getCommand("test").setExecutor(new TestCommand(this));
        // ----

        // Creates default map for BlockBattles to be then copied to the maps folder as the base map to edit.
        // It is not neccessary. You can create your own map and put it in the "maps" folder.
        if (!new File(Bukkit.getWorldContainer(), "defaultmap").exists()) {
            World defaultMap = Bukkit.createWorld(new WorldCreator("defaultmap").generator(new MapWorldGenerator()));
            if (defaultMap != null) {
                Bukkit.unloadWorld(defaultMap, true);
            }
        }

        File mapsFolder = new File(this.getDataFolder(), "maps");
        if (!mapsFolder.exists()) mapsFolder.mkdirs();

        File structuresFolder = new File(this.getDataFolder(), "structures");
        if (!structuresFolder.exists()) structuresFolder.mkdirs();
        Structures.loadAllStructures(structuresFolder);

        getCommand("match").setExecutor(new MatchCommand(this));
        getCommand("structure").setExecutor(new StructureCommand(this));
    }

    @Override
    public void onDisable() {
        LOGGER.info("Hectus plugin stopped.");
    }


    public static void disablePlugin() {
        LOGGER.info("Disabling plugin...");
        Bukkit.getPluginManager().disablePlugin(getPlugin(HectusBlockBattles.class));
    }
}
