package ctx.revoid.simpleMace.utils;

import ctx.revoid.simpleMace.SimpleMace;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import static ctx.revoid.simpleMace.SimpleMace.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.zip.GZIPInputStream;

public class TrackingMace {

    public TrackingMace() {
        sendConsole("(SimpleMace) Initializing the Pin tracking system");
        sendConsole("(SimpleMace) Found " + getMaces().values().stream().mapToInt(Integer::intValue).sum() + " mace on the server");
    }

    public CompletableFuture<Map<String, Integer>> countMaceForOffline() {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Integer> uuids = new HashMap<>();

            File playerDataFolder = new File(Bukkit.getWorlds().get(0).getWorldFolder(), "playerdata");

            if (!playerDataFolder.exists() || !playerDataFolder.isDirectory()) {
                return uuids;
            }

            File[] playerFiles = playerDataFolder.listFiles((dir, name) -> name.endsWith(".dat"));

            if (playerFiles == null) {
                return uuids;
            }

            for (File playerFile : playerFiles) {
                try {
                    String uuidString = playerFile.getName().replace(".dat", "");
                    UUID uuid = UUID.fromString(uuidString);

                    if (Bukkit.getPlayer(uuid) != null) {
                        continue;
                    }

                    if (hasMaceInFile(playerFile) != 0) {
                        uuids.put(uuid.toString(), hasMaceInFile(playerFile));
                    }

                } catch (Exception ignored) {}
            }

            return uuids;
        });
    }

    public CompletableFuture<Map<String, Integer>> countMaceForOnline() {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Integer> uuids = new HashMap<>();

            for (Player online: Bukkit.getOnlinePlayers()) {
                if (online.getInventory().contains(Material.MACE)) {
                    uuids.put(online.getUniqueId().toString(), online.getInventory().all(Material.MACE).size());
                }
            }

            return uuids;
        });
    }

    public Map<String, Integer> countMaceForWorld() {
        Map<String, Integer> uuids = new HashMap<>();
        Integer maces = 0;

        for (World world: Bukkit.getWorlds()) {
            for (Entity e : world.getEntities()) {
                if (e.getType().equals(EntityType.ITEM)) {
                    ItemStack itemStack = ((Item) e).getItemStack();

                    if (itemStack.getType().equals(Material.MACE)) {
                        maces++;
                    }
                }
            }
        }

        if (maces > 0) uuids.put("world", maces);

        return uuids;
    }

    public Map<String, Integer> getMaces() {
        Map<String, Integer> uuid = new HashMap<>();

        Map<String, Integer> online = countMaceForOnline().join();
        uuid.putAll(online);

        Map<String, Integer> offline = countMaceForOffline().join();
        uuid.putAll(offline);

        uuid.putAll(countMaceForWorld());

        return uuid;
    }

    private int hasMaceInFile(File playerFile) {
        try {
            byte[] data = readGzipFile(playerFile);
            if (data == null) return 0;

            String content = new String(data, java.nio.charset.StandardCharsets.ISO_8859_1);

            if (content.contains("minecraft:mace") ||
                    content.contains("\"mace\"") ||
                    content.contains("mace") && content.contains("id")) {
                return StringUtils.countMatches(content, "mace");
            }

            return 0;

        } catch (Exception e) {
            return 0;
        }
    }

    private byte[] readGzipFile(File file) {
        try (GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(file));
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = gzip.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            return baos.toByteArray();

        } catch (IOException e) {
            return null;
        }
    }
}
