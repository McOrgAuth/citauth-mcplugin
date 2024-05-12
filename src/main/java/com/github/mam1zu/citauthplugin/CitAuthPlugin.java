package com.github.mam1zu.citauthplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CitAuthPlugin extends JavaPlugin {
    private final String apihost = "172.24.241.112";
    private final String apiport = "37564";
    private APIConnection apicon;
    private CommandManager cm;
    private JoinCheck jc;
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

        apicon = new APIConnection(this, apihost, apiport);
        cm = new CommandManager(this, apicon);
        jc = new JoinCheck(this, apicon);
        getServer().getPluginManager().registerEvents(jc, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
