package com.github.mam1zu.citauthplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
    JavaPlugin plugin;
    ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public String getHost() {
        return this.plugin.getConfig().getString("api_server.host");
    }

    public int getPort() {
        return this.plugin.getConfig().getInt("api_server.port");
    }

    public String getApiUserId() {
        return this.plugin.getConfig().getString("auth_server.userid");
    }

    public String getApiPassword() {
        return this.plugin.getConfig().getString("auth_server.password");
    }

}
