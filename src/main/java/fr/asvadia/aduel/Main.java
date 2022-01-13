package fr.asvadia.aduel;

import fr.asvadia.aduel.commands.DuelCommands;
import fr.asvadia.aduel.utils.DuelListeners;
import fr.asvadia.aduel.utils.file.FileManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
        try {
            FileManager.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        getCommand("duel").setExecutor(new DuelCommands());
        getServer().getPluginManager().registerEvents(new DuelListeners(), this);
    }

    public static Main getInstance() {
        return instance;
    }
}
