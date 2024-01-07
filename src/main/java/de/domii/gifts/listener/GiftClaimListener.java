package de.domii.gifts.listener;

import de.domii.gifts.Gifts;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;

public class GiftClaimListener implements Listener {
    private Gifts main;
    public GiftClaimListener(Gifts main) { this.main = main; }
    @EventHandler
    public void onGiftClaim(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();

        if (block.getType() != Material.PLAYER_HEAD) {
            return;
        }
        Location location = block.getLocation();
        Player player = e.getPlayer();

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            FileConfiguration gifts = YamlConfiguration.loadConfiguration(new File(main.getDataFolder(), "gifts.yml"));
            ConfigurationSection giftLocs = gifts.getConfigurationSection("Gift-Locations");
            for (String giftID : giftLocs.getKeys(false)) {
                ConfigurationSection giftData = giftLocs.getConfigurationSection(giftID);
                double x = giftData.getDouble("x");
                double y = giftData.getDouble("y");
                double z = giftData.getDouble("z");

                if (location.getX() == x && location.getY() == y && location.getZ() == z) {
                    FileConfiguration playerData = YamlConfiguration.loadConfiguration(new File(main.getDataFolder(), "playerdata.yml"));
                    ConfigurationSection playersSection = playerData.getConfigurationSection("Players");
                    ConfigurationSection playerSection = playersSection.getConfigurationSection(player.getUniqueId().toString());
                    if (playerSection == null) {
                        playerData.createSection(player.getUniqueId().toString());
                        playerSection = playerData.getConfigurationSection(player.getUniqueId().toString());
                        playerSection.set("Name", player.getName());
                    }
                    playerSection.getStringList("claimed-Gifts").add(giftID);

                    if (main.getConfig().getBoolean("delete-Gift-after-collect")) {
                        // DELETE
                    } else {
                        player.sendMessage("§aDu hast ein Geschenk eingesammelt");
                    }
                }
            }
        }
    }
}
