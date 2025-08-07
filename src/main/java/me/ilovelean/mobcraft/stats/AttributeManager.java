package me.ilovelean.mobcraft.stats;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import io.lumine.mythic.lib.api.stat.provider.StatProvider;
import io.lumine.mythic.lib.player.modifier.ModifierSource;
import io.lumine.mythic.lib.player.modifier.ModifierType;
import me.ilovelean.mobcraft.MobCraft;
import me.ilovelean.mobcraft.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AttributeManager {
    private static AttributeManager instance;
    private final AttributesJson attributesJson = MobCraft.getInstance().getAttributesJson();

    private AttributeManager() {
        instance = this;
    }

    public static AttributeManager get() {
        return instance == null ? new AttributeManager() : instance;
    }

    public void refreshStats(Profile profile) {
        StatProvider statProvider = StatProvider.get(profile.getPlayer());
        MMOPlayerData mmoPlayerData = MMOPlayerData.get(profile.getUuid());
        ModifierType modifierType = ModifierType.FLAT;
        mmoPlayerData.getStatMap().getInstances().forEach(statInstance -> statInstance.removeIf(statKey -> statKey.startsWith("mobcraft")));
        HashMap<String, Double> attributes = new HashMap<String, Double>();
        block0:
        for (AttributeType attributeType : AttributeType.values()) {
            int points = profile.getAttribute(attributeType);
            if (points == 0) continue;
            int idx = 0;
            for (Map.Entry<String, Map<String, Double>> entry : this.attributesJson.getAttributes().get(attributeType).entrySet()) {
                if (idx >= points) continue block0;
                entry.getValue().forEach((StatType, value) -> attributes.put(StatType, attributes.getOrDefault(StatType, 0.0) + value));
                ++idx;
            }
        }
        attributes.forEach((statType, value) -> {
            Bukkit.broadcastMessage("adding stat " + statType + " with value " + value);
            new StatModifier("mobcraft", statType, value.doubleValue(), modifierType, EquipmentSlot.OTHER, ModifierSource.OTHER).register(mmoPlayerData);
        });
    }
}
