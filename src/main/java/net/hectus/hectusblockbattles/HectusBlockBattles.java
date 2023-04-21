package net.hectus.hectusblockbattles;

import com.google.gson.Gson;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.events.IngameShopEvents;
import net.hectus.hectusblockbattles.events.ItemExplorerEvents;
import net.hectus.hectusblockbattles.commands.TestCommand;
import net.hectus.hectusblockbattles.structures.Structure;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public final class HectusBlockBattles extends JavaPlugin {

    public static Logger LOGGER;
    public static List<Structure> structures;

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
        getDataFolder().mkdirs();
        getCommand("test").setExecutor(new TestCommand(this));
        // ----

        // Load structures (structures.json file)
        Gson gson = new Gson();
        try (FileReader fileReader = new FileReader(new File(getDataFolder(), "structures.json"))) {
            structures = Arrays.asList(gson.fromJson(fileReader, Structure[].class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
