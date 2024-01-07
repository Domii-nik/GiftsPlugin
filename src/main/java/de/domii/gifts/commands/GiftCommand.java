package de.domii.gifts.commands;

import de.domii.gifts.manager.HeadManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class createGiftCommand implements CommandExecutor {
    HeadManager headManager = new HeadManager();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(" "); // ONLY FOR PLAYER MSG
            return false;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("Gifts.createGift")) {
            player.sendMessage(" "); // NO PERM MSG
            return false;
        }

        Location location = player.getLocation();
        location.getBlock().setType(Material.PLAYER_HEAD);
        
        return false;
    }
}
