package space.astrocraft.astrobb.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.astrocraft.astrobb.match.Match;
import space.astrocraft.astrobb.player.BBPlayer;

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
