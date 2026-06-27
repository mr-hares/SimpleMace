package ctx.revoid.simpleMace.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import static ctx.revoid.simpleMace.SimpleMace.*;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() != Material.MACE) return;

        if (event.getInventory().getType() == InventoryType.CHEST) {
            if (getInstance().getConfig().getBoolean("restrictions.disable-chest", true)) {
                event.setCancelled(true);
                player.sendMessage(color(getMessages().getString("restrictions.disable-chest")));
            }
        } else if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
            if (getInstance().getConfig().getBoolean("restrictions.disable-ender-chest", true)) {
                event.setCancelled(true);
                player.sendMessage(color(getMessages().getString("restrictions.disable-ender-chest")));
            }
        } else if (event.getInventory().getType() == InventoryType.SHULKER_BOX) {
            if (getInstance().getConfig().getBoolean("restrictions.disable-shulker-boxes", true)) {
                event.setCancelled(true);
                player.sendMessage(color(getMessages().getString("restrictions.disable-shulker-boxes")));
            }
        }
    }
}
