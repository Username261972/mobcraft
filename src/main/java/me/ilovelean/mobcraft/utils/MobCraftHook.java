package me.ilovelean.mobcraft.utils;

import me.ilovelean.mobcraft.profile.Profile;
import me.ilovelean.mobcraft.profile.ProfileManager;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.Indyuce.mmoitems.comp.rpg.RPGHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class MobCraftHook
    implements RPGHandler,
    Listener {
    public void refreshStats(PlayerData data) {
        Profile profile = ProfileManager.get().getProfile(data.getPlayer().getUniqueId());
        double maxMana = data.getStats().getStat(ItemStats.MAX_MANA);
        if (profile.getMana() > maxMana) {
            profile.setMana(maxMana);
        }
    }

    @EventHandler
    public void a(PlayerLevelChangeEvent event) {
        PlayerData.get(event.getPlayer()).getInventory().scheduleUpdate();
    }

    public RPGPlayer getInfo(PlayerData data) {
        return new MobCraftPlayer(data);
    }

    public static class MobCraftPlayer
        extends RPGPlayer {
        private final ProfileManager profileManager = ProfileManager.get();

        public MobCraftPlayer(PlayerData playerData) {
            super(playerData);
        }

        public int getLevel() {
            return this.profileManager.getProfile(this.getPlayer().getUniqueId()).getLevel();
        }

        public String getClassName() {
            return "";
        }

        public double getMana() {
            return this.profileManager.getProfile(this.getPlayer().getUniqueId()).getMana();
        }

        public void setMana(double value) {
            double maxMana = this.getPlayerData().getStats().getStat(ItemStats.MAX_MANA);
            if (value > maxMana) {
                value = maxMana;
            }
            this.profileManager.getProfile(this.getPlayer().getUniqueId()).setMana(value);
        }

        public double getStamina() {
            return 0.0;
        }

        public void setStamina(double value) {
        }
    }
}
