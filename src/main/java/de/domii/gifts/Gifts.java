package de.domii.gifts;

import de.domii.gifts.commands.GiftCommand;
import de.domii.gifts.commands.GiftCommandTabComplete;
import de.domii.gifts.listener.BlockListener;
import de.domii.gifts.listener.GiftClaimListener;
import de.domii.gifts.listener.WandListener;
import de.domii.gifts.manager.GiftMangager;
import de.domii.gifts.manager.ParticleManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Gifts extends JavaPlugin{
    private GiftMangager giftMangager;
    private ParticleManager particleManager;
    @Override
    public void onEnable() {
        // CONSOLE LOG

        // COMMANDS
        getCommand("gift").setExecutor(new GiftCommand(this));
        getCommand("gift").setTabCompleter(new GiftCommandTabComplete());

        // LISTENER
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new GiftClaimListener(this), this);
        pluginManager.registerEvents(new WandListener(this), this);
        pluginManager.registerEvents(new BlockListener(), this);

        // CONFIG
        saveDefaultConfig();

        // MANAGER
        giftMangager = new GiftMangager(this);
        particleManager = new ParticleManager(this);

        // RELOAD PARTICLES
        if (getConfig().getBoolean("particles")) {
            try {
                FileConfiguration gifts = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "gifts.yml"));
                ConfigurationSection giftLocs = gifts.getConfigurationSection("Gift-Locations");
                if (giftLocs != null ) {
                    for (String giftID : giftLocs.getKeys(false)) {
                        ConfigurationSection giftData = giftLocs.getConfigurationSection(giftID);
                        double x = giftData.getDouble("x");
                        double y = giftData.getDouble("y");
                        double z = giftData.getDouble("z");
                        World world = Bukkit.getWorld(giftData.getString("world"));

                        Location pLocation = new Location(world, x, y, z);
                        particleManager.spawnParticles(pLocation);
                    }
                }
            } catch (Exception e) {
                getLogger().severe("Fehler beim Laden der Datei");
            }
        }
    }

    public GiftMangager getGiftManager() { return giftMangager; }
    public ParticleManager getParticleManager() { return particleManager; }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
