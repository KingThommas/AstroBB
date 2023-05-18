package net.hectus.hectusblockbattles.commands;

import net.hectus.hectusblockbattles.match.LocalMatchSingles;
import net.hectus.hectusblockbattles.match.MatchManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players may execute this command.",  NamedTextColor.RED));
            return true;
        }

        LocalMatchSingles match = (LocalMatchSingles) MatchManager.getMatch(player.getWorld());

        if(match == null){
            player.sendMessage(Component.text("You can only use this command in a match.", NamedTextColor.RED));
            return true;
        }

        player.sendMessage(Component.text("You have successfully given up.", NamedTextColor.GREEN));

        if(player == match.getOppositeTurnPlayer()){
            match.end(match.getCurrentTurnPlayer(), player, player, "Gived up");
        }else{
            match.end(match.getOppositeTurnPlayer(), player, player, "Gived up");
        }

        return true;
    }
}
