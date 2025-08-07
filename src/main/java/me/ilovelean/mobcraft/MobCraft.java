package me.ilovelean.mobcraft;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import mc.obliviate.inventory.InventoryAPI;
import me.ilovelean.mobcraft.items.MailCommand;
import me.ilovelean.mobcraft.listeners.BossListener;
import me.ilovelean.mobcraft.listeners.PlayerListener;
import me.ilovelean.mobcraft.mobs.MobCommand;
import me.ilovelean.mobcraft.mobs.data.BossManager;
import me.ilovelean.mobcraft.mobs.data.MobsJson;
import me.ilovelean.mobcraft.modules.HologramManager;
import me.ilovelean.mobcraft.profile.ProfileCommand;
import me.ilovelean.mobcraft.profile.ProfileManager;
import me.ilovelean.mobcraft.profile.ProfilePlaceholder;
import me.ilovelean.mobcraft.stats.AttributesJson;
import me.ilovelean.mobcraft.utils.JsonLoader;
import me.ilovelean.mobcraft.utils.MobCraftHook;
import net.Indyuce.mmoitems.MMOItems;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobCraft
    extends JavaPlugin {
    private static MobCraft instance;
    private BossManager bossManager;
    private MobsJson mobsJson;
    private Economy econ;
    private AttributesJson attributesJson;

    public static MobCraft getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        this.loadFiles();
        this.bossManager = new BossManager();
        new InventoryAPI(this).init();
        this.setupEconomy();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new BossListener(), this);
        CommandService drink = Drink.get(this);
        drink.register(new MailCommand(), "mail");
        drink.register(new ProfileCommand(), "profil", "pf");
        drink.register(new MobCommand(), "mobcraft", "mc").setDefaultCommandIsHelp(true).setPermission("mobcraft.admin");
        drink.registerCommands();
        HologramManager.get();
        for (Player player : Bukkit.getOnlinePlayers()) {
            ProfileManager.get().getProfile(player.getUniqueId());
        }
        new ProfilePlaceholder().register();
        MMOItems.plugin.setRPG(new MobCraftHook());
    }

    private void loadFiles() {
        this.mobsJson = JsonLoader.loadOrDefault(MobCraft.getInstance().getDataFolder(), "mobs.json", MobsJson.class);
        this.attributesJson = JsonLoader.loadOrDefault(MobCraft.getInstance().getDataFolder(), "attributes.json", AttributesJson.class);
    }

    public void reloadFiles() {
        this.mobsJson = JsonLoader.loadConfig(this.getDataFolder(), "mobs.json", MobsJson.class);
        this.attributesJson = JsonLoader.loadOrDefault(MobCraft.getInstance().getDataFolder(), "attributes.json", AttributesJson.class);
        this.bossManager.reload();
    }

    public void onDisable() {
        new ProfilePlaceholder().unregister();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (InventoryAPI.getInstance().getPlayersCurrentGui(player) == null) continue;
            player.closeInventory();
        }
        HologramManager.get().cleanup();
        ProfileManager.get().saveAll();
        this.mobsJson.save();
    }

    private void setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        this.econ = (Economy) rsp.getProvider();
    }

    public BossManager getBossManager() {
        return this.bossManager;
    }

    public MobsJson getMobsJson() {
        return this.mobsJson;
    }

    public Economy getEcon() {
        return this.econ;
    }

    public AttributesJson getAttributesJson() {
        return this.attributesJson;
    }
}
