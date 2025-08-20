package me.ilovelean.mobcraft.modules;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;
import io.lumine.mythic.bukkit.events.MythicPostReloadedEvent;
import io.lumine.mythic.bukkit.utils.Schedulers;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.ilovelean.mobcraft.MobCraft;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HologramManager
    implements Listener {
    private final Map<UUID, Hologram> mobHolograms = new HashMap<UUID, Hologram>();
    private final Map<MythicMob, Double> hologramOffsets = new HashMap<MythicMob, Double>();
    private final Map<MythicMob, String> displayNames = new HashMap<MythicMob, String>();

    private HologramManager() {
        this.refreshOffsets();
        MobCraft.getInstance().getServer().getPluginManager().registerEvents(this, MobCraft.getInstance());
    }

    public static HologramManager get() {
        return Holder.INSTANCE;
    }

    private void refreshOffsets() {
        for (MythicMob mob : MythicBukkit.inst().getMobManager().getMobTypes()) {
            if (!mob.getConfig().isSet("hologram-offset") || !mob.getConfig().isSet("display-name")) continue;
            this.hologramOffsets.put(mob, mob.getConfig().getDouble("hologram-offset"));
            this.displayNames.put(mob, mob.getConfig().getString("display-name"));
        }
    }

    public void cleanup() {
        this.mobHolograms.forEach((uuid, hologram) -> {
            if (DHAPI.getHologram(uuid.toString()) == null) {
                return;
            }
            hologram.delete();
        });
    }

    public Hologram getHologram(UUID uuid) {
        return this.mobHolograms.get(uuid);
    }

    public Hologram getHologram(LivingEntity livingEntity) {
        return this.getHologram(livingEntity.getUniqueId());
    }

    public void addHologram(ActiveMob activeMob) {
        if (this.hologramOffsets.get(activeMob.getType()) == null) {
            return;
        }
        if (this.displayNames.get(activeMob.getType()) == null) {
            return;
        }
        UUID uuid = activeMob.getEntity().getUniqueId();
        Location createLocation = BukkitAdapter.adapt(activeMob.getEntity().getEyeLocation());
        double offset = this.hologramOffsets.get(activeMob.getType());
        createLocation.add(0.0, offset, 0.0);
        String displayName = IridiumColorAPI.process(this.displayNames.get(activeMob.getType()));
        Hologram hologram = DHAPI.createHologram(uuid.toString(), createLocation);
        DHAPI.addHologramLine(hologram, 0, this.getHP(activeMob.getEntity()));
        DHAPI.addHologramLine(hologram, 0, displayName);
        this.mobHolograms.put(uuid, hologram);
        Schedulers.async().runRepeating(task -> {
            if (activeMob.isDead() || !activeMob.getEntity().isValid()) {
                hologram.delete();
                this.mobHolograms.remove(uuid);
                task.terminate();
            }
        }, 1L, 10L);
        Schedulers.async().runRepeating(task -> {
            AbstractLocation abstractLocation = activeMob.getEntity().getEyeLocation();
            if (abstractLocation == null) {
                task.terminate();
                return;
            }
            Location location = BukkitAdapter.adapt(abstractLocation);
            location.add(0.0, offset, 0.0);
            DHAPI.setHologramLine(hologram, 0, this.getHP(activeMob.getEntity()));
            DHAPI.setHologramLine(hologram, 1, displayName);
            DHAPI.moveHologram(uuid.toString(), location);
        }, 1L, 1L);
    }

    private String getHP(AbstractEntity abstractEntity) {
        int maxHealth;
        int health = (int) abstractEntity.getHealth();
        double percentage = (double) health / (double) (maxHealth = (int) abstractEntity.getMaxHealth());
        String string = percentage < 0.1 ? "§4" : (percentage < 0.25 ? "§c" : (percentage < 0.5 ? "§6" : (percentage < 0.75 ? "§e" : "§a")));
        return string + health + "§f/§a" + maxHealth + " §c❤";
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawn(MythicMobSpawnEvent e) {
        ActiveMob mob = e.getMob();
        if (mob == null) {
            return;
        }
        this.addHologram(mob);
    }

    @EventHandler
    public void onMythicReload(MythicPostReloadedEvent event) {
        this.refreshOffsets();
    }

    private static class Holder {
        private static final HologramManager INSTANCE = new HologramManager();

        private Holder() {
        }
    }
}
