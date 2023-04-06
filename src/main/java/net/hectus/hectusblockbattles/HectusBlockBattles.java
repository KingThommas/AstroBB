package net.hectus.hectusblockbattles;

import net.hectus.hectusblockbattles.Database.HectusDatabase;
import net.hectus.hectusblockbattles.Events.BlockBattleEvents;
import net.hectus.hectusblockbattles.Events.IngameShopEvents;
import net.hectus.hectusblockbattles.Events.ItemExplorerEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class HectusBlockBattles extends JavaPlugin {

    public static Logger LOGGER;

    @Override
    public void onEnable() {
        LOGGER = this.getLogger();

        this.saveDefaultConfig();

        HectusDatabase database = new HectusDatabase();
        database.initialize(this.getConfig());

        IngameShop.initialize();

        getServer().getPluginManager().registerEvents(new BlockBattleEvents(), this);
        getServer().getPluginManager().registerEvents(new IngameShopEvents(), this);
        getServer().getPluginManager().registerEvents(new ItemExplorerEvents(), this);

        LOGGER.info("Hectus plugin started.");
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
