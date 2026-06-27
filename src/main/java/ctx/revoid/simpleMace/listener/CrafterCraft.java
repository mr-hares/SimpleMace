package ctx.revoid.simpleMace.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;

public class CrafterCraft implements Listener {

    @EventHandler
    public void onCrafterCraft(CrafterCraftEvent event) {
        if (event.getResult().getType().equals(Material.MACE)) {
            event.setCancelled(true);
        }
    }
}
