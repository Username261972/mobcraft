package me.ilovelean.mobcraft.mobs;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import me.ilovelean.mobcraft.MobCraft;
import org.bukkit.command.CommandSender;

public class MobCommand {
    @Command(name = "reload", desc = "\u00dajrat\u00f6lti a fileokat.")
    @Require(value = "mobcraft.admin.reload")
    public void reloadCommand(@Sender CommandSender sender) {
        MobCraft.getInstance().reloadFiles();
        sender.sendMessage(IridiumColorAPI.process("\u00a7aA fileok sikeresen bet\u00f6ltve."));
    }
}
