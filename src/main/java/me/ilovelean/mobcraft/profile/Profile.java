package me.ilovelean.mobcraft.profile;

import me.ilovelean.mobcraft.stats.AttributeType;
import me.ilovelean.mobcraft.utils.ItemSerializer;
import me.ilovelean.mobcraft.utils.LevelHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Profile {
    private UUID uuid;
    private double exp = 0.0;
    private int skillpoints = 0;
    private Map<AttributeType, Integer> attributes = new HashMap<AttributeType, Integer>();
    private double mana = 100.0;
    private List<String> mailItems = new ArrayList<String>();

    public Profile(UUID uuid, double exp, int skillpoints, Map<AttributeType, Integer> attributes, double mana, List<String> mailItems) {
        this.uuid = uuid;
        this.exp = exp;
        this.skillpoints = skillpoints;
        this.attributes = attributes;
        this.mana = mana;
        this.mailItems = mailItems;
    }

    public Profile() {
    }

    public int getAttribute(AttributeType attributeType) {
        return this.attributes.getOrDefault(attributeType, 0);
    }

    public void addAttribute(AttributeType attributeType, int amount) {
        this.attributes.put(attributeType, this.attributes.getOrDefault(attributeType, 0) + amount);
    }

    public void setAttribute(AttributeType attributeType, int amount) {
        this.attributes.put(attributeType, amount);
    }

    public void giveOrMailItem(ItemStack item) {
        Player player = this.getPlayer();
        if (player == null) {
            return;
        }
        var remainingItems = player.getInventory().addItem(item);
        if (!remainingItems.isEmpty()) {
            for (Map.Entry<Integer, ItemStack> entry : remainingItems.entrySet()) {
                this.mailItems.add(ItemSerializer.encodeItem(entry.getValue()));
            }
            Component component = Component.text("Nincs el\u00e9g helyed hogy megkapd ezt a t\u00e1rgyat!").color(NamedTextColor.RED).appendNewline().append(Component.text(" [Men\u00fc megnyit\u00e1sa]").color(NamedTextColor.AQUA).clickEvent(ClickEvent.runCommand("/mail")));
            player.sendMessage(component);
        }
    }

    public void addExp(double exp) {
        this.exp += exp;
        this.refreshExpBar();
    }

    public int getLevel() {
        return this.getLevelHandler().getLevel();
    }

    public boolean hasUUID() {
        return this.uuid != null;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public boolean equals(Profile other) {
        return this.hashCode() == other.hashCode();
    }

    public void refreshExpBar() {
        if (this.hasUUID()) {
            LevelHandler levelHandler = this.getLevelHandler();
            this.getPlayer().setLevel(levelHandler.getLevel());
            this.getPlayer().setExp((float) levelHandler.getPercentageToNextLevel());
        }
    }

    public LevelHandler getLevelHandler() {
        return new LevelHandler(this.exp);
    }

    public int hashCode() {
        return this.uuid.hashCode();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public double getExp() {
        return this.exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public int getSkillpoints() {
        return this.skillpoints;
    }

    public void setSkillpoints(int skillpoints) {
        this.skillpoints = skillpoints;
    }

    public Map<AttributeType, Integer> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<AttributeType, Integer> attributes) {
        this.attributes = attributes;
    }

    public double getMana() {
        return this.mana;
    }

    public void setMana(double mana) {
        this.mana = mana;
    }

    public List<String> getMailItems() {
        return this.mailItems;
    }

    public void setMailItems(List<String> mailItems) {
        this.mailItems = mailItems;
    }
}
