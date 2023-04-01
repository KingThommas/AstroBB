package net.hectus.hectusblockbattles;

import net.hectus.hectusblockbattles.Events.BlockBattleEvents;
import net.hectus.hectusblockbattles.Events.IngameShopEvents;
import net.hectus.hectusblockbattles.Events.ItemExplorerEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class HectusBlockBattles extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Hectus plugin started.");

        IngameShop.initialize();

        getServer().getPluginManager().registerEvents(new BlockBattleEvents(), this);
        getServer().getPluginManager().registerEvents(new IngameShopEvents(), this);
        getServer().getPluginManager().registerEvents(new ItemExplorerEvents(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
