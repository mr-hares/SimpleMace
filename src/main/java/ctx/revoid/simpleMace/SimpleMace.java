package ctx.revoid.simpleMace;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ctx.revoid.simpleMace.command.simplemace;
import ctx.revoid.simpleMace.listener.*;
import ctx.revoid.simpleMace.utils.TrackingMace;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SimpleMace extends JavaPlugin {
    private static SimpleMace instance;
    private static TrackingMace trackingMace;
    private static YamlConfiguration messages;

    @Override
    public void onEnable() {
        instance = this;

        sendConsole("&f&r\n  &#ffff99SimpleMace &8- &7v" + getDescription().getVersion() + "\n  &7Enable plugin&r\n&f&r");
        load();

        trackingMace = new TrackingMace();
        registerAllEvents();

        new simplemace();
    }

    private void registerAllEvents() {
        try {
            Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
            Bukkit.getPluginManager().registerEvents(new CraftItem(), this);
            Bukkit.getPluginManager().registerEvents(new CrafterCraft(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerArmorStandManipulate(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerInteractEntity(), this);
            sendConsole("(SimpleMace) Minecraft events are initialized");
        } catch (Exception e) {
            sendConsole("(SimpleMace) &cMinecraft Event Initialization Error");
        }
    }

    private void load() {
        saveDefaultConfig();

        File ru_ru = new File(getDataFolder(), "messages/ru_ru.yml");
        File en_us = new File(getDataFolder(), "messages/en_us.yml");

        if (!ru_ru.exists()) saveResource("messages/ru_ru.yml", false);
        if (!en_us.exists()) saveResource("messages/en_us.yml", false);

        messages = YamlConfiguration.loadConfiguration(Objects.equals(getConfig().getString("lang", "en"),
                "ru") ? ru_ru : en_us);
    }

    public void reload() {
        reloadConfig();

        File ru_ru = new File(getInstance().getDataFolder(), "messages/ru_ru.yml");
        File en_us = new File(getInstance().getDataFolder(), "messages/en_us.yml");

        if (!ru_ru.exists()) getInstance().saveResource("messages/ru_ru.yml", false);
        if (!en_us.exists()) getInstance().saveResource("messages/en_us.yml", false);

        messages = YamlConfiguration.loadConfiguration(Objects.equals(getInstance().getConfig().getString("lang",
                        "en"),
                "ru") ? ru_ru : en_us);
    }

    @Override
    public void onDisable() {
        sendConsole("&f&r\n  &#ffff99SimpleMace &8- &7v" + getDescription().getVersion() + "\n  &7Disable " +
                "plugin&r\n&f&r");
    }

    public static SimpleMace getInstance() { return instance; }
    public static TrackingMace getTrackingMace() { return trackingMace; }
    public static YamlConfiguration getMessages() { return messages; }

    public static String color(String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }

        message = message.replace("{prefix}", getInstance().getConfig().getString("messages.prefix", "&#ffff99" +
                "(SimpleMace) &r"));

        Matcher matcher = Pattern.compile("&#([A-Fa-f0-9]{6})").matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(buffer, ChatColor.of("#" + hex).toString());
        }
        matcher.appendTail(buffer);

        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }

    public static void sendConsole(String text) {
        getInstance().getServer().getConsoleSender().sendMessage(color(text));
    }

    public static String updateCheck() {
        try {
            URL url = new URL("https://api.github.com/repos/mr-hares/SimpleMace/releases/latest");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            String latestVersion = json.get("tag_name").getAsString();
            String currentVersion = SimpleMace.getInstance().getDescription().getVersion();

            if (!latestVersion.equalsIgnoreCase(currentVersion)) {
                return latestVersion;
            }
        } catch (Exception e) {
            sendConsole("(SimpleMace) Error checking for a new update");
        }

        return null;
    }
}
