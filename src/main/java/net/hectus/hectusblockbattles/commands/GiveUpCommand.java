package net.hectus.hectusblockbattles.commands;

import net.hectus.hectusblockbattles.match.GameFlow;
import net.hectus.hectusblockbattles.match.Match;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveUpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        GameFlow.lose(Match.getPlayer((Player) sender), GameFlow.LoseReason.GAVE_UP);
        return true;
    }
}
