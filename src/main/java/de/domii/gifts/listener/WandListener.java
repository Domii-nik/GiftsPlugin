package de.domii.gifts.listener;

import de.domii.gifts.Gifts;
import de.domii.gifts.manager.GiftMangager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WandListener implements Listener {
    private Gifts main;
    private GiftMangager giftMangager;
    public WandListener(Gifts main) {
        this.main = main;
        this.giftMangager = main.getGiftManager();
    }
    @EventHandler
    public void onWandInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        if (!player.hasPermission("Gifts.wand")) {
            player.sendMessage(" "); // NO PERM MSG
            return;
        }

        if (giftMangager == null) { giftMangager = main.getGiftManager(); }

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (player.hasCooldown(Material.STICK)) return;
            if (item != null && item.getType().equals(Material.STICK) && item.hasItemMeta()) {
                String itemName = item.getItemMeta().getDisplayName();
                if (itemName.equals("§bGiftWand")) {
                    Block Clickedblock = e.getClickedBlock();
                    Location loc;
                    if (Clickedblock.getType().isSolid()) {
                        loc = Clickedblock.getLocation().add(0, 1, 0);
                    } else {
                        loc = Clickedblock.getLocation();
                    }


                    if (!loc.getBlock().getType().isSolid() && !loc.getBlock().getType().equals(Material.PLAYER_HEAD)) {
                        giftMangager.createGift(loc);
                        player.sendMessage("§aDas Geschenk wurde erstellt");
                    } else {
                        player.sendMessage("§cÜber dem geklickten Block ist kein Platz zum Platzieren eines Geschenkes");
                    }
                    player.setCooldown(Material.STICK, 10);
                }
            }
        } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (item != null && item.getType().equals(Material.STICK) && item.hasItemMeta()) {
                String itemName = item.getItemMeta().getDisplayName();
                if (itemName.equals("§bGiftWand")) {
                    if (e.getClickedBlock().getType().equals(Material.PLAYER_HEAD)) {
                        if (player.hasCooldown(Material.STICK)) {
                            e.setCancelled(true);
                            return;
                        } else {
                            giftMangager.deleteGift(e.getClickedBlock().getLocation());
                            player.sendMessage("§cDas Geschenk wurde gelöscht");
                        }
                    } else {
                        e.setCancelled(true);
                        player.sendMessage("§cDu kannst mit dem GiftWand keine Blöcke abbauen");
                    }
                }
            }
        }
    }
}
