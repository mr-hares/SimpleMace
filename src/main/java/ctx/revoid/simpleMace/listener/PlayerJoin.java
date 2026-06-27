package ctx.revoid.simpleMace.listener;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static ctx.revoid.simpleMace.SimpleMace.*;
import static ctx.revoid.simpleMace.SimpleMace.color;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isOp()) {
            if (updateCheck() != null) {
                if (getInstance().getConfig().getString("lang", "en_us").equals("ru_ru")) {
                    TextComponent button = new TextComponent(color("&#ffff99&nСсылке"));
                    button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("https://modrinth.com/plugin/simlemace/versions")));
                    button.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth" +
                            ".com/plugin/simlemace/versions"));
                    TextComponent message =
                            new TextComponent(color("&#ffff99(SimpleMace) &fВаша версия &#ffff99" + getInstance().getDescription().getVersion() + " &fустарела. Установите новую версию &#ffff99" + updateCheck() + "&fпо "));
                    message.addExtra(button);
                    message.addExtra(" &r&f, дабы разблокировать новые возможности");
                    event.getPlayer().spigot().sendMessage(button);
                } else {
                    TextComponent button = new TextComponent(color("&#ffff99&nLink"));
                    button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("https://modrinth.com/plugin/simlemace/versions")));
                    button.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth" +
                            ".com/plugin/simlemace/versions"));
                    TextComponent message =
                            new TextComponent(color("&#ffff99(SimpleMace) &fYour version &#ffff99" + getInstance().getDescription().getVersion() + " &fis outdated. Install the new version &#ffff99" + updateCheck() + "&fat "));
                    message.addExtra(button);
                    message.addExtra(" &r&f to unlock new features");
                    event.getPlayer().spigot().sendMessage(button);
                }
            }
        }
    }
}
