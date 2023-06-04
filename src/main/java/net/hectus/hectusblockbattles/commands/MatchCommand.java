package net.hectus.hectusblockbattles.commands;

import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.maps.GameMap;
import net.hectus.hectusblockbattles.maps.LocalGameMap;
import net.hectus.hectusblockbattles.match.LocalMatchSingles;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.match.MatchManager;
import net.hectus.util.color.McColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class MatchCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public MatchCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) {
            sender.sendMessage(Component.text("Usage: /<command> <player1> <player2>"));
            return true;
        }

        Player p1 = Bukkit.getPlayer(args[0]);
        Player p2 = Bukkit.getPlayer(args[1]);

        HBB.LOGGER.info("p1: " + p1);

        if (!(p1 != null && p1.isOnline() && p2 != null && p2.isOnline())) {
            sender.sendMessage(Component.text(McColor.RED + "This player is not online..."));
            return true;
        }

        if (p1 == p2) {
            sender.sendMessage(Component.text(McColor.RED + "That is the same player!"));
            return true;
        }

        sender.sendMessage(Component.text(McColor.YELLOW + "Matching " + p1.getName() + " and " + p2.getName()));

        // TODO: MAKE THIS WORK
        File mapsFolder = new File(plugin.getDataFolder(), "maps");
        GameMap map = new LocalGameMap(mapsFolder, "defaultmap", true, ((Player)sender).getWorld());
        Match match = new LocalMatchSingles(plugin, map, p1, p2);
        MatchManager.addMatch(match);
        match.start();

        return true;
    }
}