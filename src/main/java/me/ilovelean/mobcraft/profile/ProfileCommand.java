package me.ilovelean.mobcraft.profile;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import me.ilovelean.mobcraft.stats.AttributeManager;
import me.ilovelean.mobcraft.stats.AttributeType;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ProfileCommand {
    MiniMessage miniMessage = MiniMessage.miniMessage();

    @Command(name = "", desc = "Megmutatja egy játékos statjait", usage = "<játékos>")
    public void playerStatsCommand(@Sender CommandSender sender, Player target) {
        if (sender instanceof ConsoleCommandSender && target == null) {
            sender.sendMessage("§cÍrd be a játékos nevét!");
            return;
        }
        Profile profile = ProfileManager.get().getProfile(target.getUniqueId());
        sender.sendMessage(this.miniMessage.deserialize("<gold><target> Statisztikái:", Placeholder.unparsed("target", target.getName())));
        sender.sendMessage(this.miniMessage.deserialize(" <dark_gray>- <yellow>Szint: <white><level>", Formatter.number("level", profile.getLevel())));
        sender.sendMessage(this.miniMessage.deserialize(" <dark_gray>- <yellow>XP: <white><xp>", Formatter.number("xp", profile.getLevelHandler().getExperience())));
        sender.sendMessage(this.miniMessage.deserialize(" <dark_gray>- <yellow>Szükséges XP: <white><nextxp>", Formatter.number("nextxp", profile.getLevelHandler().getExpToNextLevel())));
    }

    @Command(name = "givexp", desc = "Xp-t ad egy játékosnak.", usage = "<játékos> <xp>")
    @Require(value = "mobcraft.admin.givexp")
    public void giveExpCommand(@Sender CommandSender sender, Player target, long exp) {
        Profile profile = ProfileManager.get().getProfile(target.getUniqueId());
        profile.addExp(exp);
        sender.sendMessage(this.miniMessage.deserialize("<yellow><amount> <green>xp sikeresen hozzáadva <yellow><target> <green>játékosnak", Placeholder.unparsed("target", target.getName()), Formatter.number("amount", exp)));
    }

    @Command(name = "setlevel", desc = "Beállítja egy játékos szintjét.", usage = "<játékos> <szint>")
    @Require(value = "mobcraft.admin.setlevel")
    public void setLevelCommand(@Sender CommandSender sender, Player target, int level) {
        Profile profile = ProfileManager.get().getProfile(target.getUniqueId());
        profile.setExp(profile.getLevelHandler().getTotalExpToLevel(level));
        profile.refreshExpBar();
        sender.sendMessage(this.miniMessage.deserialize("<yellow><target> <green>szintje sikeresen beállítva <yellow><amount> <green>szintre", Placeholder.unparsed("target", target.getName()), Formatter.number("amount", level)));
    }

    @Command(name = "resetxp", desc = "Reseteli a játékos xp-jét.", usage = "<játékos>")
    @Require(value = "mobcraft.admin.resetxp")
    public void resetExpCommand(@Sender CommandSender sender, Player target) {
        Profile profile = ProfileManager.get().getProfile(target.getUniqueId());
        profile.setExp(0.0);
        profile.refreshExpBar();
        sender.sendMessage(this.miniMessage.deserialize("<yellow><target> <green>xp-je sikeresen resetelve.", Placeholder.unparsed("target", target.getName())));
    }

    @Command(name = "setattribute", desc = "Beállítja a játékos attribútumot.", usage = "<játékos> <attribútum> <szám>")
    @Require(value = "mobcraft.level.setattribute")
    public void setAttributeCommand(@Sender CommandSender sender, Player target, String attributeString, int amount) {
        Profile profile = ProfileManager.get().getProfile(target.getUniqueId());
        if (Arrays.stream(AttributeType.values()).noneMatch(attributeType -> attributeType.name().equalsIgnoreCase(attributeString.toUpperCase()))) {
            sender.sendMessage(this.miniMessage.deserialize("<red>Nem létező attribútum!", Placeholder.unparsed("target", target.getName())));
            return;
        }
        AttributeType attributeType2 = AttributeType.valueOf(attributeString.toUpperCase());
        profile.setAttribute(attributeType2, amount);
        AttributeManager.get().refreshStats(profile);
        sender.sendMessage(this.miniMessage.deserialize("<gold><target> <green>játékos attribútuma beállítva <aqua><amount> <green>számmal!", Placeholder.unparsed("target", target.getName()), Formatter.number("amount", amount)));
    }
}
