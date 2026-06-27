package ctx.revoid.simpleMace.command;

import ctx.revoid.simpleMace.utils.CommandTemplate;
import ctx.revoid.simpleMace.utils.MaceDate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import static ctx.revoid.simpleMace.SimpleMace.*;

import java.util.List;

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
            reload();
            sender.sendMessage(color(getMessages().getString("reload")));
        } else if (args[0].equalsIgnoreCase("list")) {
            List<MaceDate> maces = getDataBase().getMaces();
            if (maces.isEmpty()) {
                sender.sendMessage(color(getMessages().getString("list.is-empty")));
                return;
            }

            String online = getMessages().getString("list.status.online");
            String offline = getMessages().getString("list.status.offline");

            sender.sendMessage(color(getMessages().getString("list.header")));
            for (MaceDate mace: maces) {
                sender.sendMessage(color(getMessages().getString("list.item")
                        .replace("{player}", mace.getName())
                        .replace("{date}", mace.getDate())
                        .replace("{status}", Bukkit.getPlayer(mace.getUUID()) == null ? offline : online)
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
