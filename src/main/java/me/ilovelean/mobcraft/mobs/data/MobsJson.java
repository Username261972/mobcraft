package me.ilovelean.mobcraft.mobs.data;

import me.ilovelean.mobcraft.MobCraft;
import me.ilovelean.mobcraft.items.DropItem;
import me.ilovelean.mobcraft.utils.JsonLoader;

import java.util.HashMap;
import java.util.Map;

public class MobsJson {
    private final Map<String, MobData> mobs = this.generateDefault();

    public void save() {
        JsonLoader.saveConfig(MobCraft.getInstance().getDataFolder(), "mobs.json", this);
    }

    private Map<String, MobData> generateDefault() {
        HashMap<String, MobData> defaults = new HashMap<String, MobData>();
        MobData placeholderBoss = new MobData();
        placeholderBoss.setMinExp(25L);
        placeholderBoss.setMaxExp(200L);
        placeholderBoss.setMinMoney(10.0);
        placeholderBoss.setMaxMoney(75.0);
        placeholderBoss.setMoneyChance(0.0);
        placeholderBoss.getDrops().add(new DropItem("DIAMOND_BLOCK", 10, 10, 1.0, 100.0));
        placeholderBoss.getDrops().add(new DropItem("custom:AXE-BUTCHER_AXE", 10, 10, 1.0, 100.0));
        defaults.put("placeholder_boss", placeholderBoss);
        return defaults;
    }

    public Map<String, MobData> getMobs() {
        return this.mobs;
    }
}
