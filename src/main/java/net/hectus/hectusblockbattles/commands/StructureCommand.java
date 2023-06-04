package net.hectus.hectusblockbattles.commands;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import net.hectus.hectusblockbattles.structures.Structure;
import net.hectus.util.color.McColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class StructureCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public StructureCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String structureName = args[6];

        if (args.length == 7) {
            Structure toSerialize;
            try {
                toSerialize = new Structure(((Player) sender).getWorld(), structureName, Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
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
            File structuresFolder = new File(plugin.getDataFolder(), "structures");
            if (!structuresFolder.exists()) {
                if (!structuresFolder.mkdir()) {
                    Bukkit.getLogger().log(Level.WARNING, "Failed to create structures folder.");
                    return true;
                }
            }
            File structureFile = new File(structuresFolder, structureName + ".json");
            try (FileWriter fileWriter = new FileWriter(structureFile)) {
                gson.toJson(toSerialize, fileWriter);
            } catch (IOException | JsonIOException e) {
                Bukkit.getLogger().log(Level.WARNING, "Encountered an exception.");
                e.printStackTrace();
                return true;
            }

            Bukkit.getLogger().log(Level.INFO, "DEBUG: Serialized.");

            sender.sendMessage(Component.text(McColor.GREEN + "You did it! Check logs for info."));
            return true;
        }

        return true;
    }
}
