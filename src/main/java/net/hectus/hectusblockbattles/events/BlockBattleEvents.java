package net.hectus.hectusblockbattles.events;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.BBPlayer;
import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.match.NormalMatch;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.hectusblockbattles.warps.Warp;
import net.hectus.hectusblockbattles.warps.WarpManager;
import net.hectus.util.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class BlockBattleEvents implements Listener {
    @EventHandler
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!NormalMatch.algorithm.isPlacer(player)) {
            player.sendMessage(Component.text(McColor.RED + "It isn't your turn right now!"));
            event.setCancelled(true);
            return;
        }

        BBPlayer opponent = NormalMatch.getOpponent();
        Block b = event.getBlock();

        switch (b.getType().name()) {
            case "PURPLE_WOOL" -> NormalMatch.win();

            default -> {
                Structure.BlockData blockData = new Structure.BlockData(b.getType(), b.getX(), b.getY(), b.getZ(), Structure.blockFace(b), Structure.blockBound(b), Structure.isOpen(b));
                NormalMatch.algorithm.addBlock(blockData);
            }
        }
    }

    public static void onStructurePlace(@NotNull StructurePlaceEvent event) {
        Structure structure = event.structure();
        String name = structure.name;
        World world = HBB.WORLD;

        if (name.contains("WARP")) {
            String warp = name.replace("_WARP", "");

            if (WarpManager.warp(Warp.valueOf(warp), event.placer(), event.opponent()))
                world.showTitle(subtitle(McColor.GREEN + Formatter.toPascalCase(warp)));
            else world.showTitle(subtitle(McColor.RED + Formatter.toPascalCase(warp) + " failed!"));
        }
    }

    @Contract("_ -> new")
    public static @NotNull Title subtitle(String content) {
        return Title.title(
                Component.empty(),
                Component.text(content),
                Title.Times.times(
                        Duration.ZERO,
                        Duration.ofMillis(500L),
                        Duration.ofMillis(200L)
                )
        );
    }
}
