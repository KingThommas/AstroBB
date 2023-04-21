package net.hectus.hectusblockbattles.maps;

import net.hectus.hectusblockbattles.warps.Warp;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;

public class LocalGameMap implements GameMap {
    private final File sourceWorldFolder;
    private File activeWorldFolder;

    private World world;
    private Warp currentWarp;
    private boolean isNight;

    public LocalGameMap(File mapsFolder, String mapName, boolean loadOnInit) {
        this.sourceWorldFolder = new File(mapsFolder, mapName);
        if (loadOnInit) load();
    }

    @Override
    public boolean load() {
        if (isLoaded()) return true;

        this.activeWorldFolder = new File(
                Bukkit.getWorldContainer().getParentFile(),
                sourceWorldFolder.getName() + "_" + System.currentTimeMillis()
        );

        try {
            FileUtils.copyDirectory(sourceWorldFolder.toPath().toString(), activeWorldFolder.toPath().toString());
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to copy source to world folder " + activeWorldFolder.getName());
            e.printStackTrace();
            return false;
        }

        world = Bukkit.createWorld(new WorldCreator(activeWorldFolder.getName()));
        if (world != null) world.setAutoSave(false);
        return isLoaded();
    }

    @Override
    public void unload() {
        if (world != null) Bukkit.unloadWorld(world, false);
        if (activeWorldFolder != null) {
            try {
                FileUtils.deleteDirectory(activeWorldFolder.toPath());
            } catch (IOException e) {
                Bukkit.getLogger().severe("Failed to delete " + activeWorldFolder.getName());
                e.printStackTrace();
            }
        }

        world = null;
        activeWorldFolder = null;
    }

    @Override
    public boolean restoreFromSource() {
        unload();
        return load();
    }

    @Override
    public boolean isLoaded() {
        return getWorld() != null;
    }

    @Override
    public World getWorld() {
        return world;
    }

    private static class FileUtils {
        public static void copyDirectory(String sourceDirectory, String destinationDirectory) throws IOException {
            try (Stream<Path> walk = Files.walk(Paths.get(sourceDirectory))) {
                walk.forEach(source -> {
                    Path destination = Paths.get(destinationDirectory, source.toString()
                            .substring(sourceDirectory.length()));

                    try {
                        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        public static void deleteDirectory(Path root) throws IOException {
            try (Stream<Path> walk = Files.walk(root)) {
                walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }

    }
    @Override
    public Warp currentWarp() {
        return currentWarp;
    }

    @Override
    public boolean setWarp(Warp warp) {
        if (currentWarp == warp) {
            return false;
        }
        currentWarp = warp;
        return true;
    }

    @Override
    public boolean isNight() {
        return world.isDayTime();
    }

    @Override
    public void setNight(boolean isNight) {
        world.setTime(isNight? 18000 : 6000); // 18000 is midnight, 6000 is noon
    }
}
