package me.ilovelean.mobcraft.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ilovelean.mobcraft.MobCraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public final class JsonLoader {
    private static final Logger logger = MobCraft.getInstance().getLogger();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private JsonLoader() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static <T> T loadOrDefault(File folder, String name, Class<T> configType) {
        T config;
        File configFile = new File(folder, name);
        if (configFile.exists()) {
            return JsonLoader.loadConfig(folder, name, configType);
        }
        try {
            config = configType.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            logger.severe("Failed to do something");
            e.printStackTrace();
            return null;
        }
        try {
            if (!folder.exists()) {
                folder.mkdirs();
            }
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8));
            gson.toJson(config, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.severe("Failed to create new json file");
            e.printStackTrace();
        }
        return config;
    }

    public static <T> T loadConfig(File folder, String name, Class<T> configType) {
        try {
            File configFile = new File(folder, name);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8));
            Object file = gson.fromJson(reader, configType);
            reader.close();
            return (T) file;
        } catch (IOException e) {
            logger.severe("Failed to load json");
            e.printStackTrace();
            return null;
        }
    }

    public static void saveConfig(File folder, String name, Object config) {
        try {
            File configFile = new File(folder, name);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8));
            gson.toJson(config, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.severe("Failed to save json");
            e.printStackTrace();
        }
    }

    public static Gson getGson() {
        return gson;
    }
}
