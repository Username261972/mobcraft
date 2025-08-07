package me.ilovelean.mobcraft.profile;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.player.PlayerData;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class ProfilePlaceholder
    extends PlaceholderExpansion {
    private final ProfileManager profileManager = ProfileManager.get();

    @Nonnull
    public String getIdentifier() {
        return "profile";
    }

    @Nonnull
    public String getAuthor() {
        return "KamillLays";
    }

    @Nonnull
    public String getVersion() {
        return "1.0";
    }

    public boolean persist() {
        return true;
    }

    public String onPlaceholderRequest(Player player, @Nonnull String identifier) {
        if (player == null) {
            return null;
        }
        Profile profile = this.profileManager.getProfile(player.getUniqueId());
        return switch (identifier) {
            case "level" -> String.valueOf(profile.getLevel());
            case "xp" -> "%d".formatted(Math.round(profile.getExp()));
            case "nextxp" -> "%d".formatted(Math.round(profile.getLevelHandler().getExpToNextLevel()));
            case "mana" -> "%d".formatted(Math.round(profile.getMana()));
            case "manapercent" -> {
                double mana = profile.getMana();
                double maxMana = MMOItems.plugin.getPlayerDataManager().get(player.getUniqueId()).getStats().getStat(ItemStats.MAX_MANA);
                yield "%d".formatted(Math.round(mana / maxMana * 100.0));
            }
            default -> "amongus";
        };
    }
}
