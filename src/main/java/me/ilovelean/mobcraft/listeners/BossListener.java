package me.ilovelean.mobcraft.listeners;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.ilovelean.mobcraft.MobCraft;
import me.ilovelean.mobcraft.mobs.data.BossManager;
import me.ilovelean.mobcraft.mobs.data.MobData;
import me.ilovelean.mobcraft.profile.ProfileManager;
import me.ilovelean.mobcraft.utils.ComponentBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossListener
    implements Listener {
    private final BossManager bossManager = MobCraft.getInstance().getBossManager();
    private final Map<UUID, Map<UUID, Double>> mobDamages = new HashMap<UUID, Map<UUID, Double>>();

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        ActiveMob mob = MythicBukkit.inst().getMobManager().getActiveMob(event.getEntity().getUniqueId()).orElse(null);
        if (mob == null) {
            return;
        }
        if (!this.bossManager.containsId(mob.getType().getInternalName())) {
            return;
        }
        MobData mobData = this.bossManager.getBoss(mob.getType().getInternalName());
        UUID mobUUID = mob.getEntity().getUniqueId();
        Map<UUID, Double> playerDamages = this.mobDamages.get(mobUUID);
        double maxHealth = mob.getEntity().getMaxHealth();
        if (!mobData.getDrops().isEmpty()) {
            playerDamages.forEach((uuid, damage) -> {
                if (Bukkit.getPlayer(uuid) == null) {
                    return;
                }
                Player player = Bukkit.getPlayer(uuid);
                mobData.getDrops().forEach(dropItem -> dropItem.rollItem(player, Math.min(maxHealth, damage), maxHealth));
            });
        }
        playerDamages.forEach((uuid, damage) -> {
            if (Bukkit.getPlayer(uuid) == null) {
                return;
            }
            Player player = Bukkit.getPlayer(uuid);
            long exp = (long) ((double) mobData.getExp() * Math.min(1.0, damage / maxHealth));
            ProfileManager.get().getProfile(player.getUniqueId()).addExp(exp);
            double moneyRoll = Math.random() * 100.0;
            double money = mobData.getMoneyChance() < moneyRoll ? 0.0 : mobData.getMoney() * Math.min(1.0, damage / maxHealth);
            MobCraft.getInstance().getEcon().depositPlayer(player, money);
            Component component = ComponentBuilder.of().append(money > 0.0 ? ComponentBuilder.of().text("%.1f".formatted(money) + " ⛃").color(NamedTextColor.GOLD).build() : Component.empty()).append(exp > 0L && money > 0.0 ? ComponentBuilder.of().text(" | ").color(NamedTextColor.DARK_GRAY).build() : Component.empty()).append(exp > 0L ? ComponentBuilder.of().text(exp + " XP").color(NamedTextColor.LIGHT_PURPLE).build() : Component.empty()).build();
            player.sendActionBar(component);
        });
        this.mobDamages.remove(mobUUID);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        ActiveMob mob = MythicBukkit.inst().getMobManager().getActiveMob(event.getEntity().getUniqueId()).orElse(null);
        if (mob == null) {
            return;
        }
        this.addDamage((Player) event.getDamager(), mob, event.getFinalDamage());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getDamager() instanceof Projectile projectile)) {
            return;
        }
        if (!(projectile.getShooter() instanceof Player player)) {
            return;
        }
        ActiveMob mob = MythicBukkit.inst().getMobManager().getActiveMob(event.getEntity().getUniqueId()).orElse(null);
        if (mob == null) {
            return;
        }
        this.addDamage(player, mob, event.getFinalDamage());
    }

    private void addDamage(Player player, ActiveMob mob, double damage) {
        UUID mobUUID = mob.getUniqueId();
        UUID playerUUID = player.getUniqueId();
        var playerDamages = this.mobDamages.getOrDefault(mobUUID, new HashMap<>());
        int maxHealth = (int) mob.getEntity().getMaxHealth();
        double damageDealt = 0.0;
        for (Double value : playerDamages.values()) {
            damageDealt += value.doubleValue();
        }
        playerDamages.put(playerUUID, playerDamages.getOrDefault(playerUUID, 0.0) + Math.min(damage, (double) maxHealth - damageDealt));
        this.mobDamages.put(mobUUID, playerDamages);
        Bukkit.getScheduler().runTaskLater(MobCraft.getInstance(), () -> {
            if (mob.isDead() || !mob.getEntity().isValid()) {
                return;
            }
            int health = (int) mob.getEntity().getHealth();
            double damagePercentage = this.mobDamages.get(mobUUID).get(playerUUID) / (double) maxHealth * 100.0;
            double percentage = (double) health / (double) maxHealth;
            String string = percentage < 0.1 ? "§4" : (percentage < 0.25 ? "§c" : (percentage < 0.5 ? "§6" : (percentage < 0.75 ? "§e" : "§a")));
            String mobName = mob.getType().getConfig().getString("display-name");
            player.sendActionBar(IridiumColorAPI.process(mobName) + " §8- " + string + health + " §c❤ §8(§c%.2f".formatted(damagePercentage) + "% ⚔§8)");
        }, 1L);
    }
}
