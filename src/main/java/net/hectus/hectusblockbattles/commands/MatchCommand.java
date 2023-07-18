package net.hectus.hectusblockbattles.commands;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.text.Completer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MatchCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 2) {
            if (args[0].equals("challenge")) {
                Player challenged = Bukkit.getPlayer(args[1]);

                if (challenged == null) {
                    sender.sendMessage(Component.text(McColor.RED + "The player wasn't found!"));
                    return true;
                }

                challenged.sendMessage(Component.text(McColor.LIME + sender.getName() + " challenged you to a 1v1! "));
                challenged.sendMessage(Component.text(McColor.LIME + "------- ").append(Component.text(McColor.LIME + "[ACCEPT]"))
                        .clickEvent(ClickEvent.runCommand("/match startmatchbutitssuperlongsonobodystartsamatchandcanforceplayerstoplaywiththem " + sender.getName() + " " + args[1]))
                        .hoverEvent(HoverEvent.showText(Component.text("Click here to accept!")))
                        .append(Component.text(McColor.LIME + " -------"))
                );
                sender.sendMessage(Component.text(McColor.GREEN + "Successfully challenged " + args[1] + " to a 1v1!"));
            }
        } else if (args.length == 3) {
            if (args[0].equals("startmatchbutitssuperlongsonobodystartsamatchandcanforceplayerstoplaywiththem")) {
                Player p1 = Objects.requireNonNullElse(Bukkit.getPlayer(args[1]), (Player) sender);
                Player p2 = Objects.requireNonNullElse(Bukkit.getPlayer(args[2]), (Player) sender);

                p1.sendMessage(Component.text(McColor.GREEN + "Started a match between you and " + p2.getName()));
                p2.sendMessage(Component.text(McColor.GREEN + "Started a match between you and " + p1.getName()));

                Match.start(p1, p2);
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) players.add(player.getName());
        players.remove(sender.getName());

        if (args.length == 1) return Completer.startComplete(args[0], List.of("challenge"));
        if (args.length == 2) {
            if (args[0].equals("challenge")) return players;
        }

        return List.of();
    }
}
