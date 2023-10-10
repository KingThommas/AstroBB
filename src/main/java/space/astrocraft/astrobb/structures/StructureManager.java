package space.astrocraft.astrobb.structures;

import com.google.gson.Gson;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import space.astrocraft.astrobb.AstroBB;
import space.astrocraft.astrobb.Trace;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class StructureManager {
    public static final HashSet<Structure> loadedStructures = new HashSet<>();
    private static final Gson GSON = new Gson();
    private static final File STRUCTURE_FILE = new File(AstroBB.dataFolder, "structures.json");

    /**
     * Save a structure in the structure file
     * @param structure The structure that should get saved
     * @return true = successful
     *         false = failed
     */
    public static boolean save(Structure structure) {
        System.out.println("StructureManager.save(" + "structure = " + structure + ") by: " + Trace.last());

        loadAll(false);
        try {
            if (loadedStructures.contains(structure)) return false;
            for (Structure struct : loadedStructures) {
                if (struct.name.equals(structure.name)) return false;
            }

            loadedStructures.add(structure);

            Structure[] existingStructures;
            try (FileReader reader = new FileReader(STRUCTURE_FILE)) {
                existingStructures = GSON.fromJson(reader, Structure[].class);
            } catch (IOException e) {
                existingStructures = new Structure[0];
            }

            Structure[] combinedStructures = Arrays.copyOf(existingStructures, existingStructures.length + 1);
            combinedStructures[existingStructures.length] = structure;

            String json = GSON.toJson(combinedStructures);
            try (FileWriter writer = new FileWriter(STRUCTURE_FILE)) {
                writer.write(json);
            }

            return true;
        } catch (IOException e) {
            AstroBB.LOGGER.info("Something went wrong while saving a Structure:\n" + structure);
            return false;
        }
    }

    /**
     * Load all structures
     * @param force true = reloads everything, safer
     *              false = only loads new structures that weren't there before
     */
    public static void loadAll(boolean force) {
        System.out.println("StructureManager.loadAll(" + "force = " + force + ") by: " + Trace.last());

        try (FileReader reader = new FileReader(STRUCTURE_FILE)) {
            if (force) loadedStructures.clear();

            Structure[] structures = GSON.fromJson(reader, Structure[].class);
            if (structures != null) {
                for (Structure structure : structures) {
                    loadedStructures.add(structure);
                    loadedStructures.add(structure.rotated());
                }
            }
        } catch (IOException e) {
            AstroBB.LOGGER.warning("Couldn't load all structures! Cause: IOException in reading the structure file!");
        }
    }

    @Contract(pure = true)
    public static @Nullable Structure get(String name) {
        System.out.println("StructureManager.get(" + "name = " + name + ") by: " + Trace.last());

        for (Structure structure : loadedStructures) {
            if (structure.name.equalsIgnoreCase(name)) {
                return structure;
            }
        }
        return null;
    }

    /**
     * Save a structure in the structure file
     * @param structure The structure that should get saved
     * @return true = successful
     *         false = failed
     */
    public static boolean remove(String structure) {
        System.out.println("StructureManager.remove(" + "structure = " + structure + ") by: " + Trace.last());

        try {
            loadAll(true);

            for (Structure struct : loadedStructures) {
                if (struct.name.equals(structure)) {
                    loadedStructures.remove(struct);

                    Structure[] structures = loadedStructures.toArray(new Structure[0]);

                    String json = GSON.toJson(structures);
                    try (FileWriter writer = new FileWriter(STRUCTURE_FILE)) {
                        writer.write(json);
                    }

                    return true;
                }
            }
        } catch (IOException e) {
            AstroBB.LOGGER.info("Something went wrong while saving a Structure:\n" + structure);
        }
        return false;
    }
}
