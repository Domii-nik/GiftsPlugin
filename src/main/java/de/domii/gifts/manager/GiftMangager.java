package de.domii.gifts.manager;

import de.domii.gifts.Gifts;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GiftMangager {
    private Gifts main;
    public GiftMangager(Gifts main) {
        this.main = main;
    }
    public void createGift(Location location) {
        PlayerProfile playerProfile = Bukkit.getServer().createPlayerProfile(UUID.randomUUID());
        PlayerTextures skullTexture = playerProfile.getTextures();
        List<String> textures;
        textures = main.getConfig().getStringList("Gift-Textures");

        String skullTextureString = getRandomElement(textures);
        try {
            skullTexture.setSkin(new URL("http://textures.minecraft.net/texture/" + skullTextureString));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (!location.getBlock().getType().isSolid() && !location.getBlock().getType().equals(Material.PLAYER_HEAD)) {
            location.getBlock().setType(Material.PLAYER_HEAD);
            Skull skull = (Skull) location.getBlock().getState();
            skull.setOwnerProfile(playerProfile);
            skull.update(true);

            FileConfiguration gifts = YamlConfiguration.loadConfiguration(new File(main.getDataFolder(), "gifts.yml"));
            ConfigurationSection giftLocs = gifts.getConfigurationSection("Gift-Locations");

            if (giftLocs == null) {
                gifts.createSection("Gift-Locations");
                giftLocs = gifts.getConfigurationSection("Gift-Locations");
            }

            ConfigurationSection giftData = giftLocs.createSection("Gift-" + (giftLocs.getKeys(false).size() + 1));
            giftData.set("x", location.getX());
            giftData.set("y", location.getY());
            giftData.set("z", location.getZ());
            giftData.set("world", location.getWorld().getName());
            try {
                gifts.save(new File(main.getDataFolder(), "gifts.yml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (main.getConfig().getBoolean("particles")) {
                main.getParticleManager().spawnParticles(location);
            }
        }

    }

    public void deleteGift(Location location) {
        FileConfiguration gifts = YamlConfiguration.loadConfiguration(new File(main.getDataFolder(), "gifts.yml"));
        ConfigurationSection giftLocs = gifts.getConfigurationSection("Gift-Locations");
        for (String giftID : giftLocs.getKeys(false)) {

            ConfigurationSection giftData = giftLocs.getConfigurationSection(giftID);
            double x = giftData.getDouble("x");
            double y = giftData.getDouble("y");
            double z = giftData.getDouble("z");

            if (location.getX() == x && location.getY() == y && location.getZ() == z) {
                location.getBlock().setType(Material.AIR);
                giftLocs.set(giftID, null);
                try {
                    gifts.save(new File(main.getDataFolder(), "gifts.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                main.getParticleManager().stopParticles(location);
            }
        }
    }

    public void editGift(Location location) {

    }

    private String getRandomElement(List<String> list) {
        if (list != null && !list.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(list.size());

            return list.get(randomIndex);
        } else {
            return null;
        }
    }
}
