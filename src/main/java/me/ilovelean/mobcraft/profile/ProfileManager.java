package me.ilovelean.mobcraft.profile;

import io.lumine.mythic.lib.api.stat.provider.StatProvider;
import me.ilovelean.mobcraft.MobCraft;
import me.ilovelean.mobcraft.utils.JsonLoader;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager {
    private static ProfileManager instance;
    private final Map<UUID, Profile> profiles = new HashMap<UUID, Profile>();
    private final Map<UUID, BukkitTask> manaTasks = new HashMap<UUID, BukkitTask>();

    private ProfileManager() {
        File profilesFolder = new File(MobCraft.getInstance().getDataFolder(), "profile");
        if (!profilesFolder.exists()) {
            profilesFolder.mkdir();
        }
        instance = this;
    }

    public static ProfileManager get() {
        return instance == null ? new ProfileManager() : instance;
    }

    public Profile getProfile(UUID uuid) {
        if (this.profiles.containsKey(uuid)) {
            return this.profiles.get(uuid);
        }
        Profile profile = JsonLoader.loadOrDefault(MobCraft.getInstance().getDataFolder(), "profile/" + uuid + ".json", Profile.class);
        if (profile == null) {
            profile = new Profile();
            profile.setUuid(uuid);
        } else if (profile.getUuid() == null && Bukkit.getPlayer(uuid) != null) {
            profile.setUuid(uuid);
        }
        this.startManaTask(profile);
        this.profiles.put(uuid, profile);
        return this.profiles.get(uuid);
    }

    public void saveProfile(UUID uuid) {
        if (this.profiles.containsKey(uuid)) {
            JsonLoader.saveConfig(MobCraft.getInstance().getDataFolder(), "profile/" + uuid + ".json", this.profiles.get(uuid));
        }
    }

    public void saveAll() {
        this.profiles.forEach((uuid, profile) -> {
            JsonLoader.saveConfig(MobCraft.getInstance().getDataFolder(), "profile/" + uuid + ".json", profile);
            this.stopManaTask(uuid);
        });
    }

    public void saveProfileThenRemove(UUID uuid) {
        this.saveProfile(uuid);
        this.stopManaTask(uuid);
        this.profiles.remove(uuid);
    }

    public Map<UUID, Profile> getProfiles() {
        return this.profiles;
    }

    public void startManaTask(Profile profile) {
        UUID uuid = profile.getUuid();
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(MobCraft.getInstance(), () -> {
            if (Bukkit.getPlayer(uuid) == null) {
                this.manaTasks.remove(uuid);
                return;
            }
            Player player = profile.getPlayer();
            StatProvider statProvider = StatProvider.get(player);
            double manaRegeneration = statProvider.getStat("CUSTOM_MANA_REGENERATION");
            double manaRegenerationBonus = statProvider.getStat("CUSTOM_MANA_REGENERATION_BONUS");
            double percentManaRegeneration = statProvider.getStat("CUSTOM_PERCENT_MANA_REGENERATION");
            double maxMana = MMOItems.plugin.getPlayerDataManager().get(player.getUniqueId()).getStats().getStat(ItemStats.MAX_MANA);
            manaRegeneration += manaRegeneration * manaRegenerationBonus;
            double totalManaRegenerated = manaRegeneration + maxMana * percentManaRegeneration / 100.0;
            profile.setMana(Math.min(maxMana, profile.getMana() + totalManaRegenerated));
        }, 20L, 30L);
        this.manaTasks.put(profile.getUuid(), task);
    }

    public void stopManaTask(Profile profile) {
        BukkitTask task = this.manaTasks.get(profile.getPlayer().getUniqueId());
        if (task != null) {
            task.cancel();
            this.manaTasks.remove(profile.getPlayer().getUniqueId());
        }
    }

    public void stopManaTask(UUID uuid) {
        this.stopManaTask(this.profiles.get(uuid));
    }
}
