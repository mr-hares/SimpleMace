package ctx.revoid.simpleMace.command;

import ctx.revoid.simpleMace.utils.CommandTemplate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import static ctx.revoid.simpleMace.SimpleMace.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class simplemace extends CommandTemplate {
    public simplemace() {
        super("simplemace", getInstance());
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("simplemace.use")) {
            sender.sendMessage(color(getMessages().getString("not-permissions")));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(color(String.join("\n", getMessages().getStringList("help.command-list"))));
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("simplemace.reload")) {
                sender.sendMessage(color(getMessages().getString("not-permissions")));
                return;
            }
            getInstance().reload();
            sender.sendMessage(color(getMessages().getString("reload")));
        } else if (args[0].equalsIgnoreCase("list")) {
            Map<String, Integer> maces = getTrackingMace().getMaces();

            if (maces.isEmpty()) {
                sender.sendMessage(color(getMessages().getString("list.is-empty")));
                return;
            }

            String online = getMessages().getString("list.status.online");
            String offline = getMessages().getString("list.status.offline");

            sender.sendMessage(color(getMessages().getString("list.header")));
            for (String uuid: maces.keySet()) {
                if (Objects.equals(uuid, "world")) {
                    sender.sendMessage(color(getMessages().getString("list.not-player")
                            .replace("{count}", String.valueOf(maces.get(uuid)))
                    ));
                    continue;
                }
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));

                sender.sendMessage(color(getMessages().getString("list.item")
                        .replace("{player}", player.getName())
                        .replace("{count}", String.valueOf(maces.get(uuid)))
                        .replace("{status}", player.isOnline() ? online : offline)
                ));
            }
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return List.of("reload", "list");
        }
        return List.of();
    }
}
