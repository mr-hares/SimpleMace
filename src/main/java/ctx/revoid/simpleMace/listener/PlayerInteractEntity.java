package ctx.revoid.simpleMace.listener;

import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static ctx.revoid.simpleMace.SimpleMace.*;

public class PlayerInteractEntity implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame && getInstance().getConfig().getBoolean("restrictions.block-item-frames",
                true)) {
            if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.MACE)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(color(getMessages().getString("restrictions.block-item-frames")));
            }
        }
    }
}
