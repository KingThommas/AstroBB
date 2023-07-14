package net.hectus.hectusblockbattles.events;

import net.hectus.hectusblockbattles.match.Match;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

public class ClientEvents implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLocaleChange(PlayerLocaleChangeEvent event) {
        if (Match.hasStarted) {
            Match.getPlayer(event.getPlayer()).setLocale(event.locale());
        }
    }
}
