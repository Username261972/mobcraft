package me.ilovelean.mobcraft.mobs.data;

import me.ilovelean.mobcraft.MobCraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BossManager {
    private Map<String, MobData> customMobs = new HashMap<String, MobData>(MobCraft.getInstance().getMobsJson().getMobs());

    public void reload() {
        this.customMobs.clear();
        this.customMobs = new HashMap<String, MobData>(MobCraft.getInstance().getMobsJson().getMobs());
    }

    public MobData getBoss(String id) {
        return this.customMobs.get(id);
    }

    public boolean containsId(String id) {
        return this.customMobs.containsKey(id);
    }

    public List<String> getKeys() {
        return new ArrayList<String>(this.customMobs.keySet());
    }
}
