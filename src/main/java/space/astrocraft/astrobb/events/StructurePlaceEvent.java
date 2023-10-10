package space.astrocraft.astrobb.events;

import space.astrocraft.astrobb.Cord;
import space.astrocraft.astrobb.structures.Structure;
import org.bukkit.entity.Player;

import java.util.HashMap;

public record StructurePlaceEvent (
        Structure structure,
        Cord relative,
        Player placer, Player opponent,
        HashMap<Structure, Double> otherPossibleStructures
) {}
