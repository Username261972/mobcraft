package me.ilovelean.mobcraft.items;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import io.lumine.mythic.lib.api.item.NBTItem;
import me.ilovelean.mobcraft.MobCraft;
import me.ilovelean.mobcraft.profile.ProfileManager;
import me.ilovelean.mobcraft.utils.TotemAnimationUtil;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ItemTier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DropItem {
    String item;
    int minAmount;
    int maxAmount;
    double threshold;
    double chance;

    public DropItem(String item, int minAmount, int maxAmount, double threshold, double chance) {
        this.item = item;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.threshold = threshold;
        this.chance = chance;
    }

    public int getAmount() {
        return (int) (Math.random() * (double) (this.maxAmount - this.minAmount + 1) + (double) this.minAmount);
    }

    public void rollItem(Player player, double damage, double mobHealth) {
        double damagePercentage = Math.min(damage / mobHealth * 100.0, 100.0);
        if (damagePercentage >= this.threshold) {
            ItemStack itemStack;
            double roll = Math.random() * 100.0;
            if (roll > this.chance) {
                return;
            }
            int amount = this.getAmount();
            if (amount == 0) {
                return;
            }
            if (this.item.startsWith("custom:")) {
                String id;
                String[] split = this.item.replace("custom:", "").split("-");
                String type = split[0].toUpperCase();
                itemStack = MMOItems.plugin.getItem(type, id = split[1].toUpperCase());
                if (itemStack == null) {
                    return;
                }
            } else {
                itemStack = new ItemStack(Material.valueOf(this.item.toUpperCase()));
            }
            itemStack.setAmount(amount);
            ProfileManager.get().getProfile(player.getUniqueId()).giveOrMailItem(itemStack);
            player.sendMessage("\u00a78 - \u00a77" + this.getDisplayName(itemStack) + " \u00a7ex" + amount);
            NBTItem nbtItem = NBTItem.get(itemStack);
            ItemTier itemTier = ItemTier.ofItem(nbtItem);
            if (itemTier != null) {
                String tierId = itemTier.getId();
                String mythicalId = "MYTHICAL";
                String ancientRelicId = "ANCIENT_RELIC";
                String legendaryId = "LEGENDARY";
                if (tierId.equalsIgnoreCase(ancientRelicId)) {
                    String itemName = this.getDisplayName(itemStack);
                    String playerName = player.getName();
                    final String broadcastMessageAncientRelic = IridiumColorAPI.process("<SOLID:CC704E>&l" + playerName + "&r<SOLID:CC704E> megszerzett egy &lAncient Relic&r<SOLID:CC704E> t\u00e1rgyat: " + itemName + ", " + "%.2f".formatted(damagePercentage) + "% sebz\u00e9ssel.");
                    TotemAnimationUtil.showTotemAnimation(player, 10001);
                    new BukkitRunnable() {

                        public void run() {
                            Bukkit.broadcastMessage(broadcastMessageAncientRelic);
                        }
                    }.runTaskLater(MobCraft.getInstance(), 10L);
                } else if (tierId.equalsIgnoreCase(mythicalId)) {
                    String itemName = this.getDisplayName(itemStack);
                    String playerName = player.getName();
                    final String broadcastMessageMythical = IridiumColorAPI.process("<SOLID:B22121>&l" + playerName + "&r<SOLID:B22121> megszerzett egy &lMythical&r<SOLID:B22121> t\u00e1rgyat: " + itemName + ", " + "%.2f".formatted(damagePercentage) + "% sebz\u00e9ssel.");
                    TotemAnimationUtil.showTotemAnimation(player, 10002);
                    new BukkitRunnable() {

                        public void run() {
                            Bukkit.broadcastMessage(broadcastMessageMythical);
                        }
                    }.runTaskLater(MobCraft.getInstance(), 10L);
                } else if (tierId.equalsIgnoreCase(legendaryId)) {
                    TotemAnimationUtil.showTotemAnimation(player, 10003);
                }
            }
        }
    }

    public String getDisplayName(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null && itemMeta.hasDisplayName()) {
            NBTItem nbtItem = NBTItem.get(itemStack);
            ItemTier itemTier = ItemTier.ofItem(nbtItem);
            String displayName = itemTier == null ? itemMeta.getDisplayName() : IridiumColorAPI.process(itemTier.getUnidentificationInfo().getPrefix() + itemMeta.getDisplayName());
            return displayName;
        }
        String itemName = itemStack.getType().name().toLowerCase();
        CharSequence[] words = itemName.split("_");
        for (int i = 0; i < words.length; ++i) {
            words[i] = ((String) words[i]).substring(0, 1).toUpperCase() + ((String) words[i]).substring(1);
        }
        return String.join(" ", words);
    }

    public String getItem() {
        return this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getMinAmount() {
        return this.minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public int getMaxAmount() {
        return this.maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public double getThreshold() {
        return this.threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getChance() {
        return this.chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }
}
