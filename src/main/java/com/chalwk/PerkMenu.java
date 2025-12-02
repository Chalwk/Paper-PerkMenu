package com.chalwk;

import com.chalwk.commands.PerkMenuCommand;
import com.chalwk.config.ConfigManager;
import com.chalwk.listeners.PerkMenuListener;
import com.chalwk.managers.PerkManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PerkMenu extends JavaPlugin {

    private ConfigManager configManager;
    private PerkManager perkManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.perkManager = new PerkManager(this);

        configManager.loadConfig();

        getCommand("perks").setExecutor(new PerkMenuCommand(this));
        getServer().getPluginManager().registerEvents(new PerkMenuListener(this), this);

        getLogger().info("PerkMenu v" + getDescription().getVersion() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PerkMenu has been disabled!");
    }

    public void reload() {
        configManager.reloadConfig();
        getLogger().info("Configuration reloaded!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PerkManager getPerkManager() {
        return perkManager;
    }
}