package me.ilovelean.mobcraft.utils;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public final class ItemSerializer {

    private ItemSerializer() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @SneakyThrows
    public static String encodeItem(ItemStack item) {
        ReadWriteNBT nbt = NBT.itemStackToNBT(item);
        String nbtString = nbt.toString();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
        dataOutput.writeObject(nbtString);
        dataOutput.close();
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    @SneakyThrows
    public static ItemStack decodeItem(String data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
        String itemString = (String) dataInput.readObject();
        dataInput.close();
        ReadWriteNBT nbt = NBT.parseNBT(itemString);
        return NBT.itemStackFromNBT(nbt);
    }
}
