package net.hectus.hectusblockbattles.events;

import net.hectus.hectusblockbattles.structures.v2.Structure;
import org.bukkit.entity.Player;

import java.util.HashMap;

public record StructurePlaceEvent (
        Structure structure,
        Structure.Cord relative,
        Player placer, Player opponent,
        HashMap<Structure, Double> otherPossibleStructures
) {}
