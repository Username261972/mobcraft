package me.ilovelean.mobcraft.mobs.data;


import me.ilovelean.mobcraft.items.DropItem;

import java.util.ArrayList;
import java.util.List;

public class MobData {
    long minExp;
    long maxExp;
    double minMoney;
    double maxMoney;
    double moneyChance;
    List<DropItem> drops = new ArrayList<DropItem>();

    public MobData(long minExp, long maxExp, double minMoney, double maxMoney, double moneyChance, List<DropItem> drops) {
        this.minExp = minExp;
        this.maxExp = maxExp;
        this.minMoney = minMoney;
        this.maxMoney = maxMoney;
        this.moneyChance = moneyChance;
        this.drops = drops;
    }

    public MobData() {
    }

    public long getExp() {
        return (long) (Math.random() * (double) (this.maxExp - this.minExp + 1L) + (double) this.minExp);
    }

    public double getMoney() {
        if (this.minMoney == this.maxMoney) {
            return this.minMoney;
        }
        return Math.random() * (this.maxMoney - this.minMoney + 1.0) + this.minMoney;
    }

    public long getMinExp() {
        return this.minExp;
    }

    public void setMinExp(long minExp) {
        this.minExp = minExp;
    }

    public long getMaxExp() {
        return this.maxExp;
    }

    public void setMaxExp(long maxExp) {
        this.maxExp = maxExp;
    }

    public double getMinMoney() {
        return this.minMoney;
    }

    public void setMinMoney(double minMoney) {
        this.minMoney = minMoney;
    }

    public double getMaxMoney() {
        return this.maxMoney;
    }

    public void setMaxMoney(double maxMoney) {
        this.maxMoney = maxMoney;
    }

    public double getMoneyChance() {
        return this.moneyChance;
    }

    public void setMoneyChance(double moneyChance) {
        this.moneyChance = moneyChance;
    }

    public List<DropItem> getDrops() {
        return this.drops;
    }

    public void setDrops(List<DropItem> drops) {
        this.drops = drops;
    }
}
