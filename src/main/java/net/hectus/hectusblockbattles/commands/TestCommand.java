package net.hectus.hectusblockbattles.commands;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.hectus.hectusblockbattles.structures.Structure;
import net.hectus.util.color.McColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class TestCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    /* This is a temporary command used to generate the JSON for the structures.
     * 1. Create a file named 'data.json'
     * 2. Build the structure in game, then do "/test x1 y1 z1 x2 y2 z2 name" where the coordinates are the two corners of the structure
     * 3. The name should be a continuous string i.e. WALL_PUMPKIN
     * 4. Check the logs for info after you do the command.
     */

    public TestCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        World world = ((Player) sender).getWorld();

        if (args.length == 7) {
            Structure toSerialize;
            try {
                toSerialize = new Structure(world, args[6], Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
            } catch (NumberFormatException exception) {
                sender.sendMessage(Component.text(McColor.RED + "Wrong args!"));
                return false;
            }

            Bukkit.getLogger().log(Level.INFO, "DEBUG: Received structure '" + toSerialize.getName() + "', details:");
            Bukkit.getLogger().log(Level.INFO, toSerialize.getName());
            Bukkit.getLogger().log(Level.INFO, toSerialize.getBlocks().toString());
            Bukkit.getLogger().log(Level.INFO, toSerialize.getMaterials().toString());
            Bukkit.getLogger().log(Level.INFO, toSerialize.getPlacedBlocks().toString());
            Bukkit.getLogger().log(Level.INFO, toSerialize.getBoundary().toString());
            Bukkit.getLogger().log(Level.INFO, "DEBUG: Attempting to serialize...");

            Gson gson = new Gson();
            File data = new File(plugin.getDataFolder(), "data.json");
            try (FileWriter fileWriter = new FileWriter(data)) {
                gson.toJson(toSerialize, fileWriter);
            } catch (IOException | JsonIOException e) {
                Bukkit.getLogger().log(Level.WARNING, "Encountered an exception.");
                e.printStackTrace();
                return true;
            }

            Bukkit.getLogger().log(Level.INFO, "DEBUG: Serialized. Now deserializing...");

            Structure deserialized;
            try (FileReader fileReader = new FileReader(data)) {
                deserialized = gson.fromJson(fileReader, Structure.class);
            } catch (IOException | JsonIOException | JsonSyntaxException e) {
                Bukkit.getLogger().log(Level.WARNING, "Encountered an exception.");
                e.printStackTrace();
                return true;
            }

            Bukkit.getLogger().log(Level.INFO, "DEBUG: Deserialized structure '" + deserialized.getName() + "', details:");
            Bukkit.getLogger().log(Level.INFO, deserialized.getName());
            Bukkit.getLogger().log(Level.INFO, deserialized.getMaterials().toString());
            Bukkit.getLogger().log(Level.INFO, deserialized.getPlacedBlocks().toString());
            Bukkit.getLogger().log(Level.INFO, deserialized.getBoundary().toString());

            sender.sendMessage(Component.text(McColor.GREEN + "You did it! Check logs for info."));
            return true;
        }

        if (args.length != 12) {
            sender.sendMessage(Component.text(McColor.RED + "Wrong args!"));
            return false;
        }

        Structure original;
        Structure toCompare;
        try {
            original = new Structure(world, "Original", Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
            toCompare = new Structure(world, "toCompare", Integer.parseInt(args[6]), Integer.parseInt(args[7]), Integer.parseInt(args[8]), Integer.parseInt(args[9]), Integer.parseInt(args[10]), Integer.parseInt(args[11]));
        } catch (NumberFormatException exception) {
            sender.sendMessage(Component.text(McColor.RED + "Wrong args!"));
            return false;
        }

        sender.sendMessage(Component.text(McColor.YELLOW + (original.hasSubset(toCompare) ? "true" : "false")));

        return true;
    }
}
