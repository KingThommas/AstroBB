package net.hectus.hectusblockbattles.events;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.Translation;
import net.hectus.hectusblockbattles.player.BBPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class ClientEvents implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLocaleChange(@NotNull PlayerLocaleChangeEvent event) {
        System.out.println("ClientEvents.onPlayerLocaleChange(player = " + event.getPlayer().getName() + ", locale = " + event.locale() + ")");

        Locale l = event.locale();
        BBPlayer p = new BBPlayer(event.getPlayer());
        p.sendMessage(McColor.PURPLE + "You just changed your language to " + l.getDisplayLanguage());
        p.sendMessage(McColor.PURPLE + "Switching your translations to " + Translation.get("lang", l));
        p.sendMessage(McColor.PURPLE + "Translation By: " + Translation.get("credits", l));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        System.out.println("ClientEvents.onPlayerJoin(player = " + event.getPlayer().getName() + ")");

        Locale l = event.getPlayer().locale();
        BBPlayer p = new BBPlayer(event.getPlayer());
        p.sendMessage(McColor.PURPLE + "Showing text in " + Translation.get("lang", l));
        p.sendMessage(McColor.PURPLE + "Translation By: " + Translation.get("credits", l));
    }
}
