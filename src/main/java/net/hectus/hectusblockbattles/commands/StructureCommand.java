package net.hectus.hectusblockbattles.commands;

import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.hectusblockbattles.structures.v2.StructureManager;
import net.hectus.util.color.McColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StructureCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (args.length == 8) {
            if (args[0].equals("save")) { //===== Save a OutdatedStructure =====

                player.sendMessage(Component.text(McColor.GRAY + "Saving structure named " + args[7] + "..."));
                long start = System.currentTimeMillis();

                // The structure's corners / boundaries
                Structure.Cord c1 = new Structure.Cord(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                Structure.Cord c2 = new Structure.Cord(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));

                // Save / Load the OutdatedStructure
                Structure structure = Structure.save(c1, c2, args[7], player.getWorld());

                // Try to save the OutdatedStructure into the files and get a response if it worked
                boolean worked = StructureManager.save(structure);

                StructureManager.loadAll(false);

                player.sendMessage(Component.text(
                        (worked ?
                                McColor.GREEN + "Successfully saved the structure!" :
                                McColor.RED + "Something went wrong! Please take a look at the server logs. "
                            ) + "Time elapsed : " + (System.currentTimeMillis() - start) + "ms"));

                return true;
            }
        } else if (args.length == 2) { //====== Load a OutdatedStructure ======
            if (args[0].equals("load")) {
                Structure structure = StructureManager.get(args[1]);

                if (structure == null) { // If the structure wasn't found / doesn't exist
                    player.sendMessage(Component.text(McColor.RED + "Found no structure named " + args[1] + "!"));
                    return false;
                }

                player.sendMessage(Component.text(McColor.GRAY + "Loading structure named " + structure.name + "..."));
                long start = System.currentTimeMillis();

                // The base location on which the relatives are based on
                Location loc = player.getLocation();
                World world = loc.getWorld();

                for (Structure.BlockData data : structure.blockData) { // Loop through all the block data
                    // Set the block at the relative position to the corresponding material
                    new Location(world, loc.getBlockX() + data.x(), loc.getBlockY() + data.y(), loc.getBlockZ() + data.z()).getBlock().setType(data.material());
                }

                player.sendMessage(Component.text(McColor.GREEN + "Successfully loaded the structure! Time elapsed : " + (System.currentTimeMillis() - start) + "ms"));

                return true;
            } else if (args[0].equals("remove")) {
                player.sendMessage(Component.text(McColor.GRAY + "Removing structure named " + args[1] + "..."));
                long start = System.currentTimeMillis();

                boolean worked = StructureManager.remove(args[1]);

                StructureManager.loadAll(true);

                player.sendMessage(Component.text(
                        (worked ?
                                McColor.GREEN + "Successfully removed the structure!" :
                                McColor.RED + "Something went wrong! Please take a look at the server logs. "
                        ) + "Time elapsed : " + (System.currentTimeMillis() - start) + "ms"));

                return true;
            }
        } else if (args.length == 1) {
            if (args[0].equals("reload")) {
                player.sendMessage(Component.text(McColor.GRAY + "Reloading the structure data..."));
                long start = System.currentTimeMillis();

                StructureManager.loadAll(true);

                player.sendMessage(Component.text(McColor.GREEN + "Successfully reloaded the structure data! Time elapsed : " + (System.currentTimeMillis() - start) + "ms"));

                return true;
            }
        }

        player.sendMessage(Component.text(McColor.RED + "Wrong usage! Possible usages:"));
        player.sendMessage(Component.text(McColor.YELLOW + "- /structure save <x1> <y1> <z1> <x2> <y2> <z2> <name>"));
        player.sendMessage(Component.text(McColor.YELLOW + "- /structure load <name>"));
        player.sendMessage(Component.text(McColor.YELLOW + "- /structure remove <name>"));
        player.sendMessage(Component.text(McColor.YELLOW + "- /structure reload"));

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("save", "load", "remove", "reload");
        } else if (args.length == 2) {
            if (args[0].equals("load") || args[0].equals("remove")) {
                List<String> availableStructures = new ArrayList<>();
                StructureManager.loadedStructures.forEach(structure -> availableStructures.add(structure.name));
                return availableStructures;
            }
        }

        return Collections.emptyList();
    }
}
