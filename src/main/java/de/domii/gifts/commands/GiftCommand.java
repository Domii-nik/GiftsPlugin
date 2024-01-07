package de.domii.gifts.commands;

import de.domii.gifts.Gifts;
import de.domii.gifts.manager.GiftMangager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GiftCommand implements CommandExecutor {
    private Gifts main;
    private GiftMangager giftMangager;
    public GiftCommand(Gifts main) {
        this.main = main;
        this.giftMangager = main.getGiftManager();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(" "); // ONLY FOR PLAYER MSG
            return false;
        }
        Player player = (Player) sender;

        if (giftMangager == null) { giftMangager = main.getGiftManager(); }

        if (args.length >= 1) {
            switch (args[0]) {
                case "create":
                    if (!player.hasPermission("Gifts.createGift")) {
                        player.sendMessage(" "); // NO PERM MSG
                        return false;
                    }

                    if (player.getLocation().getBlock().getType().equals(Material.PLAYER_HEAD)) {
                        player.sendMessage("§cHier gibt es schon ein Geschenk");
                        return false;
                    }

                    giftMangager.createGift(player.getLocation().getBlock().getLocation());
                    player.sendMessage("§aDas Geschenk wurde erstellt");
                    return true;

                case "delete":
                    if (!player.hasPermission("Gifts.deleteGift")) {
                        player.sendMessage(" "); // NO PERM MSG
                        return false;
                    }
                    if (player.getTargetBlock(null, 5).getType().equals(Material.PLAYER_HEAD)) {
                        giftMangager.deleteGift(player.getTargetBlock(null, 5).getLocation());
                        player.sendMessage("§cDas Geschenk wurde gelöscht");
                    } else {
                        player.sendMessage("§cBitte gucke ein Geschenk an und führe diesen Command erneut aus");
                    }
                    return true;


                case "edit":
                    if (!player.hasPermission("Gifts.editGift")) {
                        player.sendMessage(" "); // NO PERM MSG
                        return false;
                    }


                case "wand":
                    if (!player.hasPermission("Gifts.wand")) {
                        player.sendMessage(" "); // NO PERM MSG
                        return false;
                    }

                    ItemStack wand = new ItemStack(Material.STICK);
                    ItemMeta wandMeta = wand.getItemMeta();
                    wandMeta.setDisplayName("§bGiftWand");
                    wandMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                    wandMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    wandMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    wandMeta.setLore(Arrays.asList("§7Mit dem §bGiftWand §7kannst du mit §6Rechtsklick", "§7ein neues §bGeschenk §aerstellen §7oder mit", "§6Linksklick §7ein Geschenk §clöschen"));
                    wand.setItemMeta(wandMeta);

                    player.getInventory().addItem(wand);
                    player.sendMessage("§aDu hast den §bGiftWand §aerhalten");
            }
        } else {
            player.openInventory(Bukkit.createInventory(null, 9*2, "Gifts-GUI"));
        }

        return false;
    }
}
