package net.hectus.hectusblockbattles.commands;

import net.hectus.hectusblockbattles.match.LocalMatchSingles;
import net.hectus.hectusblockbattles.match.MatchManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MatchCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public MatchCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /* Currently, this is the only entry point for an actual
     * Block Battle™ match. Simply put two distinct usernames
     * and it *should* start a match. - love, chimkenu.
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp() || sender instanceof BlockCommandSender) {
            sender.sendMessage(Component.text("Insufficient permissions.", NamedTextColor.RED));
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(Component.text("Wrong args! ", NamedTextColor.RED)
                    .append(Component.text("Usage: /match <player> <player>", NamedTextColor.GRAY)));
            return true;
        }

        Player p1 = Bukkit.getPlayer(args[0]);
        Player p2 = Bukkit.getPlayer(args[1]);

        if (!(p1 != null && p1.isOnline() && p2 != null && p2.isOnline())) {
            sender.sendMessage(Component.text("This player is not online...", NamedTextColor.RED));
            return true;
        }

        if (p1 == p2) {
            sender.sendMessage(Component.text("That is the same player!", NamedTextColor.RED));
            return true;
        }

        sender.sendMessage(Component.text("Matching ", NamedTextColor.YELLOW)
                .append(p1.displayName())
                .append(Component.text(" and ", NamedTextColor.YELLOW))
                .append(p2.displayName()));

        // TODO: MAKE THIS WORK
        // MatchManager.addMatch(new LocalMatchSingles(plugin, new LocalGameMap(), p1, p2));

        return true;
    }
}