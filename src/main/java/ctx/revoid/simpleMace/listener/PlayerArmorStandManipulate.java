package ctx.revoid.simpleMace.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import static ctx.revoid.simpleMace.SimpleMace.*;

public class PlayerArmorStandManipulate implements Listener {

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        if (event.getPlayerItem().getType().equals(Material.MACE) && getInstance().getConfig().getBoolean(
                "restrictions.block-armor-stands", true)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(color(getMessages().getString("restrictions.block-armor-stands")));
        }
    }
}
