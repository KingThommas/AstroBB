package net.hectus.hectusblockbattles.commands;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.hectusblockbattles.structures.v2.StructureManager;
import net.hectus.hectusblockbattles.Cord;
import net.hectus.text.Completer;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StructureCommand implements CommandExecutor, TabExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player player = (Player) sender;

        switch (args.length) {
            case 9 -> {
                if (args[0].equals("save")) {
                    player.sendMessage(Component.text(McColor.GRAY + "Saving structure named " + args[7] + "..."));
                    long start = System.currentTimeMillis();

                    // The Structure's corners / boundaries
                    Cord c1 = new Cord(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                    Cord c2 = new Cord(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));

                    Structure structure = Structure.save(c1, c2, args[8], Boolean.parseBoolean(args[7]), player.getWorld());
                    boolean worked = StructureManager.save(structure);
                    StructureManager.loadAll(false);

                    player.sendMessage(Component.text(
                            (worked ?
                                    McColor.GREEN + "Successfully saved the structure!" :
                                    McColor.RED + "Something went wrong! Please take a look at the server logs. "
                            ) + "Time elapsed : " + (System.currentTimeMillis() - start) + "ms"));
                }
            }
            case 2 -> {
                if (args[0].equals("load")) {
                    Structure structure = StructureManager.get(args[1]);

                    if (structure == null) {
                        player.sendMessage(Component.text(McColor.RED + "Found no structure named " + args[1] + "!"));
                        return false;
                    }

                    player.sendMessage(Component.text(McColor.GRAY + "Loading structure named " + structure.name + "..."));
                    long start = System.currentTimeMillis();

                    // The base location on which all relatives are based on
                    Location loc = player.getLocation();
                    World world = loc.getWorld();

                    for (Structure.BlockData data : structure.blockData) { // Looping through all the structure's data

                        Location relativeLocation = new Location(world, loc.getBlockX() + data.x(), loc.getBlockY() + data.y(), loc.getBlockZ() + data.z());
                        Block block = relativeLocation.getBlock();

                        block.setType(data.material());
                    }

                    player.sendMessage(Component.text(McColor.GREEN + "Successfully loaded the structure! Time elapsed : " + (System.currentTimeMillis() - start) + "ms"));
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
                }
            }
            case 1 -> {
                if (args[0].equals("reload")) {
                    player.sendMessage(Component.text(McColor.GRAY + "Reloading the structure data..."));
                    long start = System.currentTimeMillis();

                    StructureManager.loadAll(true);

                    player.sendMessage(Component.text(McColor.GREEN + "Successfully reloaded the structure data! Time elapsed : " + (System.currentTimeMillis() - start) + "ms"));
                }
            }
            default -> {
                player.sendMessage(Component.text(McColor.RED + "Wrong usage! Possible usages:"));
                player.sendMessage(Component.text("/structure save x1 y1 z1 x2 y2 z2 advancedBlockData NAME"));
                player.sendMessage(Component.text("/structure load NAME"));
                player.sendMessage(Component.text("/structure remove NAME"));
                player.sendMessage(Component.text("/structure reload"));
            }
        }
        return true;
    }

    private static final String[] MAIN_ARGS = { "save", "load", "remove", "reload" };

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return Completer.startComplete(args[0], MAIN_ARGS);
        } else if (args.length == 2) {
            if (args[0].equals("load") || args[0].equals("remove")) {
                ArrayList<String> structures = new ArrayList<>();
                for (Structure struct : StructureManager.loadedStructures) structures.add(struct.name);
                return Completer.containComplete(args[1], structures);
            }
        } else if (args.length == 8) {
            return Completer.startComplete(args[7], new String[]{"true", "false"});
        }

        return List.of();
    }
}
