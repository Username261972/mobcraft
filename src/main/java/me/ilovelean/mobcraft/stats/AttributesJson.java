package me.ilovelean.mobcraft.stats;

import java.lang.invoke.CallSite;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AttributesJson {
    private final Map<AttributeType, Map<String, Map<String, Double>>> attributes = this.getDefault();

    public Map<AttributeType, Map<String, Map<String, Double>>> getDefault() {
        LinkedHashMap<AttributeType, Map<String, Map<String, Double>>> defaultAttributes = new LinkedHashMap<AttributeType, Map<String, Map<String, Double>>>();
        HashMap strength = new HashMap();
        for (int i = 1; i < 50; ++i) {
            HashMap<String, Double> stats = new HashMap<String, Double>();
            stats.put("PVE_DAMAGE", (double) Math.round(Math.random() * 100.0) / 100.0);
            strength.put((Object) ("" + i), stats);
        }
        defaultAttributes.put(AttributeType.STRENGTH, strength);
        return defaultAttributes;
    }

    public Map<AttributeType, Map<String, Map<String, Double>>> getAttributes() {
        return this.attributes;
    }
}
