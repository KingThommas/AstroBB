package net.hectus.hectusblockbattles.Structures;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
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

    public TestCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players may execute this command.",  NamedTextColor.RED));
            return true;
        }

        if (args.length == 7) {
            Structure toSerialize;
            try {
                toSerialize = new Structure(player.getWorld(), args[6], Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
            } catch (NumberFormatException exception) {
                sender.sendMessage(Component.text("Wrong args!", NamedTextColor.RED));
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

            sender.sendMessage(Component.text("You did it! Check logs for info.", NamedTextColor.GREEN));
            return true;
        }

        if (args.length != 12) {
            sender.sendMessage(Component.text("Wrong args!",  NamedTextColor.RED));
            return false;
        }

        Structure original;
        Structure toCompare;
        try {
            original = new Structure(player.getWorld(), "Original", Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
            toCompare = new Structure(player.getWorld(), "toCompare", Integer.parseInt(args[6]), Integer.parseInt(args[7]), Integer.parseInt(args[8]), Integer.parseInt(args[9]), Integer.parseInt(args[10]), Integer.parseInt(args[11]));
        } catch (NumberFormatException exception) {
            sender.sendMessage(Component.text("Wrong args!", NamedTextColor.RED));
            return false;
        }

        sender.sendMessage(Component.text(original.hasSubset(toCompare), NamedTextColor.YELLOW));

        return true;
    }
}
