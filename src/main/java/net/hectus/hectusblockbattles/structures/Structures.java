package net.hectus.hectusblockbattles.structures;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.logging.Level;

public final class Structures {

    private static final HashSet<Structure> structures = new HashSet<>();

    public static boolean loadStructure(File file, Gson gson) {
        Structure deserialized;
        try (FileReader fileReader = new FileReader(file)) {
            deserialized = gson.fromJson(fileReader, Structure.class);
        } catch (IOException | JsonIOException | JsonSyntaxException e) {
            Bukkit.getLogger().log(Level.WARNING, "Encountered an exception.");
            e.printStackTrace();
            return false;
        }
        if (deserialized == null) {
            return false;
        }
        structures.add(deserialized);
        return true;
    }

    public static void loadAllStructures(File structuresFolder) {
        if (structuresFolder.exists()) {
            Gson gson = new Gson();
            for (File file : Objects.requireNonNull(structuresFolder.listFiles())) {
                if (file.isFile()) {
                    loadStructure(file, gson);
                }
            }
        }
    }

    public static HashSet<Structure> getAllStructures() {
        return structures;
    }
}
