package com.github.mam1zu.citauthplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class CitAuthPlugin extends JavaPlugin {
    String apihost = "172.24.241.112";
    String apiport = "37564";
    @Override
    public void onEnable() {
        System.out.println(
                "-----------------------------------------------------------------------------------------"+
                " #####    ######  ######## ######   ##   ##  ######## ### ###           ##   ##   #####" +
                "##   ##     ##    ## ## ##  ## ###   #   ##  ## ## ##  ## ##            #######  ##   ##"+
                "##   ##     ##       ##     ##  ##  ##   ##     ##     ## ##            ## # ##  ##   ##"+
                "##          ##       ##     ######  ##   ##     ##     #####    ######  ##   ##  ##"+
                "##   ##     ##       ##     ##  ##  ##   ##     ##     #  ##            ##   ##  ##   ##"+
                "##   ##     ##       ##     ##  ##  ### ###     ##     ## ##            ##   ##  ##   ##"+
                " #####    ######    ####   ###  ##   #####     ####   ### ###           ##   ##   #####"+
                "--------------------------------------UNDER CONSTRUCTION--------------------------------");
        System.out.println("Developed by mam1zu(mam1zu.piyo@gmail.com)");
        System.out.println("CAUTION: This plugin is under construction");
        APIConnection con = new APIConnection(apihost, apiport);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
