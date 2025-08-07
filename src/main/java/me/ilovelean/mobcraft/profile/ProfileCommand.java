package me.ilovelean.mobcraft.profile;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import me.ilovelean.mobcraft.stats.AttributeManager;
import me.ilovelean.mobcraft.stats.AttributeType;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ProfileCommand {
    MiniMessage miniMessage = MiniMessage.miniMessage();

    @Command(name = "", desc = "Megmutatja egy j\u00e1t\u00e9kos statjait", usage = "<j\u00e1t\u00e9kos>")
    public void playerStatsCommand(@Sender CommandSender sender, Player target) {
        if (sender instanceof ConsoleCommandSender && target == null) {
            sender.sendMessage("\u00a7c\u00cdrd be a j\u00e1t\u00e9kos nev\u00e9t!");
            return;
        }
        Profile profile = ProfileManager.get().getProfile(target.getUniqueId());
        sender.sendMessage(this.miniMessage.deserialize("<gold><target> Statisztik\u00e1i:", Placeholder.unparsed("target", target.getName())));
        sender.sendMessage(this.miniMessage.deserialize(" <dark_gray>- <yellow>Szint: <white><level>", Formatter.number("level", profile.getLevel())));
        sender.sendMessage(this.miniMessage.deserialize(" <dark_gray>- <yellow>XP: <white><xp>", Formatter.number("xp", profile.getLevelHandler().getExperience())));
        sender.sendMessage(this.miniMessage.deserialize(" <dark_gray>- <yellow>Sz\u00fcks\u00e9ges XP: <white><nextxp>", Formatter.number("nextxp", profile.getLevelHandler().getExpToNextLevel())));
    }

    @Command(name = "givexp", desc = "Xp-t ad egy j\u00e1t\u00e9kosnak.", usage = "<j\u00e1t\u00e9kos> <xp>")
    @Require(value = "mobcraft.admin.givexp")
    public void giveExpCommand(@Sender CommandSender sender, Player target, long exp) {
        Profile profile = ProfileManager.get().getProfile(target.getUniqueId());
        profile.addExp(exp);
        sender.sendMessage(this.miniMessage.deserialize("<yellow><amount> <green>xp sikeresen hozz\u00e1adva <yellow><target> <green>j\u00e1t\u00e9kosnak", Placeholder.unparsed("target", target.getName()), Formatter.number("amount", exp)));
    }

    @Command(name = "setlevel", desc = "Be\u00e1ll\u00edtja egy j\u00e1t\u00e9kos szintj\u00e9t.", usage = "<j\u00e1t\u00e9kos> <szint>")
    @Require(value = "mobcraft.admin.setlevel")
    public void setLevelCommand(@Sender CommandSender sender, Player target, int level) {
        Profile profile = ProfileManager.get().getProfile(target.getUniqueId());
        profile.setExp(profile.getLevelHandler().getTotalExpToLevel(level));
        profile.refreshExpBar();
        sender.sendMessage(this.miniMessage.deserialize("<yellow><target> <green>szintje sikeresen be\u00e1ll\u00edtva <yellow><amount> <green>szintre", Placeholder.unparsed("target", target.getName()), Formatter.number("amount", level)));
    }

    @Command(name = "resetxp", desc = "Reseteli a j\u00e1t\u00e9kos xp-j\u00e9t.", usage = "<j\u00e1t\u00e9kos>")
    @Require(value = "mobcraft.admin.resetxp")
    public void resetExpCommand(@Sender CommandSender sender, Player target) {
        Profile profile = ProfileManager.get().getProfile(target.getUniqueId());
        profile.setExp(0.0);
        profile.refreshExpBar();
        sender.sendMessage(this.miniMessage.deserialize("<yellow><target> <green>xp-je sikeresen resetelve.", Placeholder.unparsed("target", target.getName())));
    }

    @Command(name = "setattribute", desc = "Be\u00e1ll\u00edtja a j\u00e1t\u00e9kos attrib\u00fatumot.", usage = "<j\u00e1t\u00e9kos> <attrib\u00fatum> <sz\u00e1m>")
    @Require(value = "mobcraft.level.setattribute")
    public void setAttributeCommand(@Sender CommandSender sender, Player target, String attributeString, int amount) {
        Profile profile = ProfileManager.get().getProfile(target.getUniqueId());
        if (Arrays.stream(AttributeType.values()).noneMatch(attributeType -> attributeType.name().equalsIgnoreCase(attributeString.toUpperCase()))) {
            sender.sendMessage(this.miniMessage.deserialize("<red>Nem l\u00e9tez\u0151 attrib\u00fatum!", Placeholder.unparsed("target", target.getName())));
            return;
        }
        AttributeType attributeType2 = AttributeType.valueOf(attributeString.toUpperCase());
        profile.setAttribute(attributeType2, amount);
        AttributeManager.get().refreshStats(profile);
        sender.sendMessage(this.miniMessage.deserialize("<gold><target> <green>j\u00e1t\u00e9kos attrib\u00fatuma be\u00e1ll\u00edtva <aqua><amount> <green>sz\u00e1mmal!", Placeholder.unparsed("target", target.getName()), Formatter.number("amount", amount)));
    }
}
