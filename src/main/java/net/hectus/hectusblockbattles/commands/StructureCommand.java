package net.hectus.hectusblockbattles.commands;

import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.hectusblockbattles.structures.v2.StructureManager;
import net.hectus.text.Completer;
import net.hectus.color.McColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * <b>/structure command</b>
 * <p>
 * <b>Usage:</b> <br>
 * /structure save {@link Integer x1} {@link Integer y1} {@link Integer z1} {@link Integer x2} {@link Integer y2} {@link Integer z2} {@link String NAME} <br>
 * /structure load {@link String NAME} <br>
 * /structure remove {@link String NAME} <br>
 * /structure reload
 * </p>
 */
public class StructureCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player player = (Player) sender;

        if (args.length == 9) {
            if (args[0].equals("save")) {
                player.sendMessage(Component.text(McColor.GRAY + "Saving structure named " + args[7] + "..."));
                long start = System.currentTimeMillis();

                // The Structure's corners / boundaries
                Structure.Cord c1 = new Structure.Cord(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                Structure.Cord c2 = new Structure.Cord(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));

                Structure structure = Structure.save(c1, c2, args[8], Boolean.parseBoolean(args[7]), player.getWorld());
                boolean worked = StructureManager.save(structure);
                StructureManager.loadAll(false);

                player.sendMessage(Component.text(
                        (worked ?
                                McColor.GREEN + "Successfully saved the structure!" :
                                McColor.RED + "Something went wrong! Please take a look at the server logs. "
                            ) + "Time elapsed : " + (System.currentTimeMillis() - start) + "ms"));

                return true;
            }
        } else if (args.length == 2) {
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

                    if (structure.useExactBlockData) { // If some more advanced metadata should be used
                        setBlockDirection(block, data.direction());
                        setBlockBound(block, data.blockBound());
                        setOpen(block, data.open());
                    }

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
        player.sendMessage(Component.text(McColor.YELLOW + "- /structure save <x1> <y1> <z1> <x2> <y2> <z2> <use_exact_block_data> <name>"));
        player.sendMessage(Component.text(McColor.YELLOW + "- /structure load <name>"));
        player.sendMessage(Component.text(McColor.YELLOW + "- /structure remove <name>"));
        player.sendMessage(Component.text(McColor.YELLOW + "- /structure reload"));

        return true;
    }

    final String[] tabComplete1 = { "save", "load", "remove", "reload" };
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return Completer.startComplete(args[0], tabComplete1);
        } else if (args.length == 2) {
            if (args[0].equals("load") || args[0].equals("remove")) {
                LinkedList<String> availableStructures = new LinkedList<>();
                StructureManager.loadedStructures.forEach(structure -> availableStructures.add(structure.name));

                return Completer.containComplete(args[1], availableStructures);
            }
        } else if (args.length == 8) {
            if (args[0].equals("save")) return Completer.startComplete(args[7], List.of("true", "false"));

        }

        return Collections.emptyList();
    }

    private void setBlockDirection(@NotNull Block block, BlockFace direction) {
        BlockData blockData = block.getBlockData();

        if (blockData instanceof Directional directionalData) {
            directionalData.setFacing(direction);
            block.setBlockData(directionalData);
        } else if (blockData instanceof Rotatable rotatableData) {
            rotatableData.setRotation(direction);
            block.setBlockData(rotatableData);
        }
    }

    private void setBlockBound(@NotNull Block block, Structure.BlockBound bound) {
        BlockData blockData = block.getBlockData();

        if (blockData instanceof Slab slabData) {
            if (bound == Structure.BlockBound.TOP) slabData.setType(Slab.Type.TOP);
            else if (bound == Structure.BlockBound.BOTTOM) slabData.setType(Slab.Type.BOTTOM);
            else slabData.setType(Slab.Type.DOUBLE);

            block.setBlockData(slabData);

        } else if (blockData instanceof PointedDripstone dripstoneData) {
            if (bound == Structure.BlockBound.STALAGMITE) dripstoneData.setVerticalDirection(BlockFace.UP);
            else dripstoneData.setVerticalDirection(BlockFace.DOWN);

            block.setBlockData(dripstoneData);

        } else if (blockData instanceof TrapDoor trapDoorData) {
            if (bound == Structure.BlockBound.TOP) trapDoorData.setHalf(Bisected.Half.TOP);
            else trapDoorData.setHalf(Bisected.Half.BOTTOM);

            block.setBlockData(trapDoorData);
        }
    }

    private void setOpen(@NotNull Block block, boolean open) {
        BlockData blockData = block.getBlockData();

        if (blockData instanceof Openable data) {
            data.setOpen(open);
            block.setBlockData(data);
        }
    }
}
