package me.ilovelean.mobcraft.items;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.pagination.PaginationManager;
import me.ilovelean.mobcraft.profile.Profile;
import me.ilovelean.mobcraft.profile.ProfileManager;
import me.ilovelean.mobcraft.utils.ItemSerializer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class MailMenu
    extends Gui {
    private final Icon previousPageIcon = new Icon(Material.ARROW).setName("§6Előző Oldal");
    private final Icon nextPageIcon = new Icon(Material.ARROW).setName("§6Következő Oldal");
    private final PaginationManager pagination = new PaginationManager(this);
    private final Player target;

    public MailMenu(Player player, Player target) {
        super(player, "mail", "Talált tárgyak", 6);
        this.target = target;
        this.pagination.registerPageSlotsBetween(0, 44);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        this.fillRow(new Icon(Material.GRAY_STAINED_GLASS_PANE), 5);
        this.calculateItems();
    }

    private void calculateItems() {
        Profile profile = ProfileManager.get().getProfile(this.player.getUniqueId());
        this.pagination.getItems().clear();
        if (!profile.getMailItems().isEmpty()) {
            profile.getMailItems().forEach(item -> {
                ItemStack itemStack = ItemSerializer.decodeItem(item);
                Icon icon = new Icon(itemStack.clone());
                icon.appendLore("", "§eKatt a tárgy lekéréséhez.");
                icon.onClick((InventoryClickEvent e) -> {
                    if (!profile.getMailItems().contains(item)) {
                        this.player.sendMessage(IridiumColorAPI.process("<RAINBOW1>faggot</RAINBOW>"));
                        return;
                    }
                    if (this.player.getInventory().firstEmpty() == -1) {
                        this.player.sendMessage("§cNincs szabad helyed a tárgyhoz!");
                        return;
                    }
                    this.player.getInventory().addItem(itemStack.clone());
                    this.player.sendMessage("§eLekérted §b" + itemStack.getItemMeta().getDisplayName() + "§e tárgyat!");
                    this.player.playSound(this.player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
                    profile.getMailItems().remove(item);
                    this.open();
                });
                this.pagination.addItem(icon);
            });
        }
        if (this.pagination.getCurrentPage() != 0) {
            this.addItem(47, this.previousPageIcon);
            this.previousPageIcon.onClick((InventoryClickEvent e) -> {
                this.player.playSound(this.player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
                this.pagination.goPreviousPage();
                this.calculateItems();
            });
        } else {
            this.addItem(47, new Icon(Material.GRAY_STAINED_GLASS_PANE));
        }
        if (!this.pagination.isLastPage() && this.pagination.getLastPage() != 0 && !this.pagination.getItems().isEmpty()) {
            this.addItem(51, this.nextPageIcon);
            this.nextPageIcon.onClick((InventoryClickEvent e) -> {
                this.player.playSound(this.player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
                this.pagination.goNextPage();
                this.calculateItems();
            });
        } else {
            this.addItem(51, new Icon(Material.GRAY_STAINED_GLASS_PANE));
        }
        this.pagination.update();
    }
}
