package de.domii.gifts.manager;

import de.domii.gifts.Gifts;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class ParticleManager {
    Gifts main;
    public ParticleManager(Gifts main) {this.main = main;}
    private BukkitRunnable particles;
    public Map<Location, BukkitRunnable> particleMap = new HashMap<>();
    public void spawnParticles(Location location) {
        particles = new BukkitRunnable() {
            double radius = 0.6;
            double angle = 0.0;

            @Override
            public void run() {
                double x = location.getX() + 0.5 + radius * Math.cos(angle);
                double z = location.getZ() + 0.5 + radius * Math.sin(angle);

                Location particleLoc = new Location(location.getWorld(), x , location.getY() + 0.25, z);

                particleLoc.getWorld().spawnParticle(Particle.COMPOSTER, particleLoc, 1);
                Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                    @Override
                    public void run() {
                        particleLoc.getWorld().spawnParticle(Particle.COMPOSTER, particleLoc, 1);
                    }
                }, 20);

                angle += Math.toRadians(10);
                if (angle >= 2 * Math.PI) {
                    angle = 0.0;
                }
            }
        };
        particles.runTaskTimer(main, 0, 1);
        particleMap.put(location, particles);
    }

    public void stopParticles(Location location) {
        if (particleMap.containsKey(location)) {
            particleMap.get(location).cancel();
            particleMap.remove(location);
        }
    }

}
