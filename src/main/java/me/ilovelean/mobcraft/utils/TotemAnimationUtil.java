package me.ilovelean.mobcraft.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import me.ilovelean.mobcraft.MobCraft;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TotemAnimationUtil {
    public static void showTotemAnimation(Player player, int customModelData) {
        try {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            if (customModelData != -1) {
                ItemStack originalItem = player.getInventory().getItemInMainHand();
                ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
                ItemMeta meta = totem.getItemMeta();
                meta.setCustomModelData(Integer.valueOf(customModelData));
                totem.setItemMeta(meta);
                PacketContainer equipPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
                equipPacket.getIntegers().write(0, player.getEntityId());
                equipPacket.getSlotStackPairLists().write(0, List.of(new Pair(EnumWrappers.ItemSlot.MAINHAND, totem)));
                protocolManager.sendServerPacket(player, equipPacket);
                PacketContainer statusPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_STATUS);
                statusPacket.getIntegers().write(0, player.getEntityId());
                statusPacket.getBytes().write(0, (byte) 35);
                protocolManager.sendServerPacket(player, statusPacket);
                Bukkit.getServer().getScheduler().runTaskLater(MobCraft.getInstance(), () -> {
                    PacketContainer restorePacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
                    restorePacket.getIntegers().write(0, player.getEntityId());
                    restorePacket.getSlotStackPairLists().write(0, List.of(new Pair(EnumWrappers.ItemSlot.MAINHAND, originalItem)));
                    protocolManager.sendServerPacket(player, restorePacket);
                }, 2L);
            } else {
                PacketContainer statusPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_STATUS);
                statusPacket.getIntegers().write(0, player.getEntityId());
                statusPacket.getBytes().write(0, (byte) 35);
                protocolManager.sendServerPacket(player, statusPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showTotemAnimation(Player player) {
        TotemAnimationUtil.showTotemAnimation(player, -1);
    }

    public static void showTotemAnimationToPlayers(int customModelData, Player... players) {
        for (Player player : players) {
            TotemAnimationUtil.showTotemAnimation(player, customModelData);
        }
    }

    public static void showTotemAnimationInRadius(Player centerPlayer, double radius, int customModelData) {
        centerPlayer.getWorld().getPlayers().stream().filter(p -> p.getLocation().distance(centerPlayer.getLocation()) <= radius).forEach(p -> TotemAnimationUtil.showTotemAnimation(p, customModelData));
    }
}
