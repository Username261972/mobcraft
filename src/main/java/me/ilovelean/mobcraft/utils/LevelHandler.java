package me.ilovelean.mobcraft.utils;

public class LevelHandler {
    private final double base = 1000.0;
    private double xpBoosterMultiplier = 1.0;
    private double experience;

    public LevelHandler(double experience) {
        this.experience = experience;
    }

    public boolean addExperience(double amount) {
        int currentLevel = this.getLevel();
        this.experience += (amount *= this.xpBoosterMultiplier);
        return currentLevel < this.getLevel();
    }

    public String getProgressBar(int length) {
        int forNextLevel = (int) Math.round(this.getPercentageToNextLevel() * (double) length);
        StringBuilder builder = new StringBuilder();
        String barSymbol = "\u2503";
        String unlockedColor = "\u00a7a";
        String lockedColor = "\u00a7c";
        for (int i = 0; i < length; ++i) {
            builder.append(i < forNextLevel ? unlockedColor + barSymbol : lockedColor + barSymbol);
        }
        return builder.toString();
    }

    public int getLevel() {
        if (this.experience < 1000.0) {
            return 0;
        }
        double totalXp = this.experience;
        int level = 0;
        while (totalXp >= this.getExpFromLevelToNext(level)) {
            totalXp -= this.getExpFromLevelToNext(level);
            ++level;
        }
        return level;
    }

    public double getExpToNextLevel() {
        return this.getExpFromLevelToNext(this.getLevel()) * (1.0 - this.getPercentageToNextLevel());
    }

    public double getExactLevel() {
        return (double) this.getLevel() + this.getPercentageToNextLevel();
    }

    public double getExpFromLevelToNext(int level) {
        if (level < 0) {
            return 0.0;
        }
        if (level == 0) {
            return 1000.0;
        }
        return 1000.0 * (double) (level + 2) * (double) (level + 1) / 2.0;
    }

    public double getTotalExpToLevel(double level) {
        int wholeLvl = (int) Math.floor(level);
        double totalXp = 0.0;
        for (int i = 0; i < wholeLvl; ++i) {
            totalXp += this.getExpFromLevelToNext(i);
        }
        if (level > (double) wholeLvl) {
            totalXp += this.getExpFromLevelToNext(wholeLvl) * (level - (double) wholeLvl);
        }
        return totalXp;
    }

    public double getTotalExpToFullLevel(double level) {
        return this.getTotalExpToLevel(level);
    }

    public double getPercentageToNextLevel() {
        if (this.getLevel() == 0) {
            return this.experience / 1000.0;
        }
        double totalExpToCurrentLevel = this.getTotalExpToLevel(this.getLevel());
        double expInCurrentLevel = this.experience - totalExpToCurrentLevel;
        double expNeededForNextLevel = this.getExpFromLevelToNext(this.getLevel());
        return expInCurrentLevel / expNeededForNextLevel;
    }

    public double getXpBoosterMultiplier() {
        return this.xpBoosterMultiplier;
    }

    public void setXpBoosterMultiplier(double xpBoosterMultiplier) {
        this.xpBoosterMultiplier = xpBoosterMultiplier;
    }

    public double getBase() {
        return this.base;
    }

    public double getExperience() {
        return this.experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public String toString() {
        return "LevelHandler(xpBoosterMultiplier=" + this.getXpBoosterMultiplier() + ", base=" + this.getBase() + ", experience=" + this.getExperience() + ")";
    }
}
