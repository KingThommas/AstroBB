package net.hectus.hectusblockbattles.structures.v1;

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

public final class OutdatedStructures {

    private static final HashSet<OutdatedStructure> OUTDATED_STRUCTURES = new HashSet<>();

    public static void loadStructure(File file, Gson gson) {
        OutdatedStructure deserialized;
        try (FileReader fileReader = new FileReader(file)) {
            deserialized = gson.fromJson(fileReader, OutdatedStructure.class);
        } catch (IOException | JsonIOException | JsonSyntaxException e) {
            Bukkit.getLogger().log(Level.WARNING, "Encountered an exception.");
            e.printStackTrace();
            return;
        }

        if (deserialized == null) return;

        OUTDATED_STRUCTURES.add(deserialized);
    }

    public static void loadAllStructures(File structuresFolder) {
        if (structuresFolder.exists()) {
            Gson gson = new Gson();
            for (File file : Objects.requireNonNull(structuresFolder.listFiles())) {
                if (file.isFile()) loadStructure(file, gson);
            }
        }
    }

    public static HashSet<OutdatedStructure> getAllStructures() {
        return OUTDATED_STRUCTURES;
    }
}
