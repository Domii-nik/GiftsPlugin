package de.domii.gifts.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListener implements Listener {

    @EventHandler
    public void onGiftBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (e.getBlock().getType().equals(Material.PLAYER_HEAD)) {
            if (!player.getInventory().getItemInMainHand().getType().equals(Material.STICK) || !player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§bGiftWand")) {
                e.setCancelled(true);
                player.sendMessage("§cDu kannst Geschenke nur mit dem §bGiftWand §coder dem Command §b/gift delete §clöschen.");
            }
        }
    }

}
