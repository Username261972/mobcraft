package me.ilovelean.mobcraft.listeners;

import me.ilovelean.mobcraft.profile.ProfileManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener
    implements Listener {
    private final ProfileManager profileManager = ProfileManager.get();

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPlayedBefore()) {
            return;
        }
        player.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));
        player.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.displayName(Component.text(player.getName()).color(NamedTextColor.GREEN).append(Component.text(" kardja").color(NamedTextColor.GRAY)));
        sword.setItemMeta(swordMeta);
        player.getInventory().addItem(sword);
    }

    @EventHandler
    public void durability(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setKeepLevel(true);
        event.setKeepInventory(true);
        event.setDroppedExp(0);
        event.getDrops().clear();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.profileManager.getProfile(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.profileManager.saveProfileThenRemove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        int oldLevel = event.getOldLevel();
        int newLevel = event.getNewLevel();
        if (newLevel > oldLevel) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_TOTEM_USE, 0.025f, 0.5f);
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.05f, 0.1f);
            event.getPlayer().sendMessage("");
            event.getPlayer().sendMessage("§5§lSZINTLÉPÉS §a↑");
            event.getPlayer().sendMessage("§8➥ §7Elérted a §e" + newLevel + ". §7szintet.");
            event.getPlayer().sendMessage("§6Jutalmak:");
            event.getPlayer().sendMessage("§a[+] 3 Képesség Pont");
            event.getPlayer().sendMessage("");
        }
    }
}
