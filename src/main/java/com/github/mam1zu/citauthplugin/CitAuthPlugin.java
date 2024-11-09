package com.github.mam1zu.citauthplugin;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CitAuthPlugin extends JavaPlugin {
    private APIConnection apicon;
    private CommandManager cm;
    private JoinCheck jc;
    private ConfigManager config;
    private TokenManager mgr;
    
    @Override
    public void onEnable() {
        Bukkit.getLogger().info("----------------------------------------------------------------------------------------");
        Bukkit.getLogger().info(" #####    ######  ######## ######   ##   ##  ######## ### ###           ##   ##   ##### ");
        Bukkit.getLogger().info("##   ##     ##    ## ## ##  ## ###   #   ##  ## ## ##  ## ##            #######  ##   ##");
        Bukkit.getLogger().info("##   ##     ##       ##     ##  ##  ##   ##     ##     ## ##            ## # ##  ##   ##");
        Bukkit.getLogger().info("##          ##       ##     ######  ##   ##     ##     #####    ######  ##   ##  ##     ");
        Bukkit.getLogger().info("##   ##     ##       ##     ##  ##  ##   ##     ##     #  ##            ##   ##  ##   ##");
        Bukkit.getLogger().info("##   ##     ##       ##     ##  ##  ### ###     ##     ## ##            ##   ##  ##   ##");
        Bukkit.getLogger().info(" #####    ######    ####   ###  ##   #####     ####   ### ###           ##   ##   ##### ");
        Bukkit.getLogger().info("--------------------------------------UNDER CONSTRUCTION--------------------------------");
        Bukkit.getLogger().info("Developed by mam1zu(mam1zu.piyo@gmail.com)");
        Bukkit.getLogger().warning("CAUTION: This plugin is under construction");

        config = new ConfigManager(this);
        try {
            mgr = new TokenManager(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        apicon = new APIConnection(this, mgr, config.getHost(), config.getPort());
        cm = new CommandManager(this, apicon);
        jc = new JoinCheck(this, apicon);
        getServer().getPluginManager().registerEvents(jc, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
