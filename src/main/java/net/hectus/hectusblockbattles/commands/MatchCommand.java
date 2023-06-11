package net.hectus.hectusblockbattles.commands;

import net.hectus.hectusblockbattles.match.NormalMatch;
import net.hectus.text.Completer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 3) return false;
        if (!args[0].equals("start")) return false;

        NormalMatch.start(Bukkit.getPlayer(args[1]), Bukkit.getPlayer(args[2]));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) players.add(player.getName());

        if (args.length == 1)
            return Completer.startComplete(args[0], List.of("start"));
        else if (args.length == 2)
            return Completer.containComplete(args[1], players);
        else if (args.length == 3)
            return Completer.containComplete(args[2], players);

        return Collections.emptyList();
    }
}