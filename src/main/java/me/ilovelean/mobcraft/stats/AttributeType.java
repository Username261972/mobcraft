package me.ilovelean.mobcraft.stats;

import org.bukkit.Material;

public enum AttributeType {
    STRENGTH(Material.GOLDEN_SWORD, "§cErő"),
    DEXTERITY(Material.BOW, "§aÜgyesség"),
    INTELLIGENCE(Material.PRISMARINE_SHARD, "§bIntelligencia"),
    TOUGHNESS(Material.IRON_CHESTPLATE, "§7Ellenállás");

    private final Material material;
    private final String name;

    AttributeType(Material material, String name) {
        this.material = material;
        this.name = name;
    }

    public Material getMaterial() {
        return this.material;
    }

    public String getName() {
        return this.name;
    }
}
