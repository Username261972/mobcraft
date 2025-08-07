package me.ilovelean.mobcraft.stats;

import org.bukkit.Material;

public enum AttributeType {
    STRENGTH(Material.GOLDEN_SWORD, "\u00a7cEr\u0151"),
    DEXTERITY(Material.BOW, "\u00a7a\u00dcgyess\u00e9g"),
    INTELLIGENCE(Material.PRISMARINE_SHARD, "\u00a7bIntelligencia"),
    TOUGHNESS(Material.IRON_CHESTPLATE, "\u00a77Ellen\u00e1ll\u00e1s");

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
