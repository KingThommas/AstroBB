package net.hectus.hectusblockbattles.events;

import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.structures.v2.Algorithm;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockBattleEvents implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Structure.BlockData blockData = new Structure.BlockData(block.getType(), block.getX(), block.getY(), block.getZ());

        if (Algorithm.running) {
            Algorithm.addBlock(blockData);
        } else {
            Algorithm.start(blockData, event.getPlayer().getName());
        }
    }
}
