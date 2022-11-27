package eu.shindapp.hanaya.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigUtils {

    public void init() {
        if (!configurationDirectoryExist()) {
            createConfigurationsDirectory();
        }
        if (!configurationFileExist()) {
            createConfigurationFile();
        }
    }

    private void createConfigurationsDirectory() {
        try {
            Files.createDirectory(Paths.get("./config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createConfigurationFile() {
        try {
            Path path = Paths.get("./config/config.json");
            Files.createFile(path);
            InputStream inputStream = new FileInputStream("src/main/resources/config/config.json");
            Files.write(path, inputStream.readAllBytes().clone());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean configurationDirectoryExist() {
        return Files.exists(Paths.get("./config"));
    }

    private boolean configurationFileExist() {
        return Files.exists(Paths.get("./config/config.json"));
    }

    public String getString(String key) {
        if (configurationDirectoryExist()) {
            if (configurationFileExist()) {
                try {
                    JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader("./config/config.json"));
                    return (String) jsonObject.get(key);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            } else init();
        } else init();
        return null;
    }

    public Boolean getBoolean(String key) {
        if (configurationDirectoryExist()) {
            if (configurationFileExist()) {
                try {
                    JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader("./config/config.json"));
                    return (boolean) jsonObject.get(key);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            } else init();
        } else init();
        return false;
    }

    public Integer getInt(String key) {
        if (configurationDirectoryExist()) {
            if (configurationFileExist()) {
                try {
                    JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader("./config/config.json"));
                    return (Integer) jsonObject.get(key);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            } else init();
        } else init();
        return null;
    }
}
