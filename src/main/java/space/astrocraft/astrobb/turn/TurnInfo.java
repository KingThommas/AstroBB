package space.astrocraft.astrobb.turn;

import org.bukkit.entity.Player;
import space.astrocraft.astrobb.Cord;

public record TurnInfo(Turn turn, Player player, Cord cord) {}
