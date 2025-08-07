package me.ilovelean.mobcraft.items;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import me.ilovelean.mobcraft.profile.ProfileManager;
import org.bukkit.entity.Player;

public class MailCommand {
    @Command(name = "", desc = "Megnyitja a tal\u00e1lt t\u00e1rgyak men\u00fct.")
    public void mailCommand(@Sender Player player) {
        new MailMenu(player, player).open();
    }

    @Command(name = "forceopen", desc = "Megnyitja egy j\u00e1t\u00e9kos tal\u00e1lt t\u00e1rgyak men\u00fcj\u00e9t.", usage = "<j\u00e1t\u00e9kos>")
    @Require(value = "mobcraft.mail.forceopen")
    public void forceOpenCommand(@Sender Player sender, Player target) {
        new MailMenu(sender, target).open();
    }

    @Command(name = "clear", desc = "T\u00f6rli az \u00f6sszes t\u00e1rgyat a tal\u00e1lt t\u00e1rgyak men\u00fcben.")
    @Require(value = "mobcraft.mail.clear")
    public void clearCommand(@Sender Player player) {
        ProfileManager.get().getProfile(player.getUniqueId()).getMailItems().clear();
        player.sendMessage("\u00a7aMail sikeresen \u00fcr\u00edtve.");
    }
}
