package net.hectus.hectusblockbattles;

import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.events.IngameShopEvents;
import net.hectus.hectusblockbattles.events.ItemExplorerEvents;
import net.hectus.hectusblockbattles.commands.TestCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
        getDataFolder().mkdirs();
        getCommand("test").setExecutor(new TestCommand(this));
        // ----
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
