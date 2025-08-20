package me.ilovelean.mobcraft.items;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import me.ilovelean.mobcraft.profile.ProfileManager;
import org.bukkit.entity.Player;

public class MailCommand {
    @Command(name = "", desc = "Megnyitja a talált tárgyak menüt.")
    public void mailCommand(@Sender Player player) {
        new MailMenu(player, player).open();
    }

    @Command(name = "forceopen", desc = "Megnyitja egy játékos talált tárgyak menüjét.", usage = "<játékos>")
    @Require(value = "mobcraft.mail.forceopen")
    public void forceOpenCommand(@Sender Player sender, Player target) {
        new MailMenu(sender, target).open();
    }

    @Command(name = "clear", desc = "Törli az összes tárgyat a talált tárgyak menüben.")
    @Require(value = "mobcraft.mail.clear")
    public void clearCommand(@Sender Player player) {
        ProfileManager.get().getProfile(player.getUniqueId()).getMailItems().clear();
        player.sendMessage("§aMail sikeresen ürítve.");
    }
}
