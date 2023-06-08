package net.hectus.hectusblockbattles.events;

import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.structures.v2.Algorithm;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.util.color.McColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class BlockBattleEvents implements Listener {
    @EventHandler
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        Block block = event.getBlock();
        Structure.BlockData blockData = new Structure.BlockData(block.getType(), block.getX(), block.getY(), block.getZ());

        if (!Algorithm.running) Algorithm.start(event.getPlayer());

        Algorithm.addBlock(blockData);
    }

    public static void onStructurePlace(StructurePlaceEvent event) {
        String structure = event.structure().name;

        String title = McColor.YELLOW + structure;
        String subtitle = McColor.GREEN + "was placed by " + event.placer().getName() + "!";
        HBB.WORLD.showTitle(Title.title(Component.text(title), Component.text(subtitle), Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofMillis(250))));
    }
}
