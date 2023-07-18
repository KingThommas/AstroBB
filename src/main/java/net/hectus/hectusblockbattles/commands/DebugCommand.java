package net.hectus.hectusblockbattles.commands;

import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.player.BBPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            BBPlayer player = new BBPlayer((Player) sender);
            if (args[0].equals("players")) {
                player.sendMessage("p1 = " + Match.p1.player.getName());
                player.sendMessage("p2 = " + Match.p2.player.getName());
                player.sendMessage("placer = " + Match.algorithm.placer.player.getName());
                player.sendMessage("getPlacer() = " + Match.getPlacer().player.getName());
                player.sendMessage("getOpponent() = " + Match.getOpponent().player.getName());
            }
        }
        return true;
    }
}
