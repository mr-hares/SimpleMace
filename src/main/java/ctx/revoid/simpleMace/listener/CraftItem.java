package ctx.revoid.simpleMace.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import static ctx.revoid.simpleMace.SimpleMace.*;

public class CraftItem implements Listener {

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (event.getRecipe().getResult().getType().equals(Material.MACE)) {
            Player player = (Player) event.getView().getPlayer();
            if (getTrackingMace().getMaces().values().stream().mapToInt(Integer::intValue).sum() >= getInstance().getConfig().getInt("max-mace-count", 5)) {
                player.sendMessage(color(getMessages().getString("craft-message.decline")
                        .replace("{current_mace}",
                                String.valueOf(getTrackingMace().getMaces().values().stream().mapToInt(Integer::intValue).sum()))
                        .replace("{max_maces}", String.valueOf(getInstance().getConfig().getInt("max-mace-count", 5)))
                ));
                event.setCancelled(true);
                return;
            }

            player.sendMessage(color(getMessages().getString("craft-message.success")
                    .replace("{current_mace}", String.valueOf(getTrackingMace().getMaces().values().stream().mapToInt(Integer::intValue).sum()))
            ));

            if (getInstance().getConfig().getBoolean("broadcasts.craft", false)) {
                for (Player online: Bukkit.getOnlinePlayers()) {
                    if (online != player) {
                        online.sendMessage(color(getMessages().getString("craft-message.success")
                                .replace("{current_mace}", String.valueOf(getTrackingMace().getMaces().values().stream().mapToInt(Integer::intValue).sum())
                                        .replace("{player}", player.getName())
                                )));
                    }
                }
            }
        }
    }
}
