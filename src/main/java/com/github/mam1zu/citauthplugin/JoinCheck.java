package com.github.mam1zu.citauthplugin;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinCheck implements Listener {

    private ApiConnection apicon;
    private JavaPlugin plugin;
    
    JoinCheck(JavaPlugin plugin, ApiConnection apicon) {
        this.plugin = plugin;
        this.apicon = apicon;
    }

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        if(plugin.getServer().getWhitelistedPlayers().contains(Bukkit.getOfflinePlayer(event.getUniqueId()))) {
            plugin.getLogger().info("Whitelisted user: "+event.getName());
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);
            return;
        }
        plugin.getLogger().info("Authenticating user...");
        boolean result = apicon.authenticateUser(event.getUniqueId(), 0);
        if(result) {
            plugin.getLogger().info("Authentication succeeded: "+event.getName());
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);
        }
        else {
            plugin.getLogger().info("Authentication failed: "+event.getName());
            event.setKickMessage("[CITAUTH]You are not registered on CITAUTH-SYSTEM!");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
    }
    /* 
    @EventHandler
    public void onPlayerLoginEvent_old(PlayerLoginEvent event) {
        boolean result = false;
        if(!apicon.checkStatus()) {
            Bukkit.getLogger().warning("CITAUTH-API is temporally unavailable.");
            if(this.useWhitelistTemporarily) {
                Bukkit.getLogger().info("use whitelist instead.");
                result = plugin.getServer().getWhitelistedPlayers().contains(event.getPlayer());
                if(result) {
                    event.setResult(PlayerLoginEvent.Result.ALLOWED);
                    return;
                }
                else {
                    event.setKickMessage("[CITAUTH]You are not registered on CITAUTH-SYSTEM!");
                    event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                    return;
                }
            }
            else {
                event.setKickMessage("[CITAUTH]CITAUTH-SYSTEM is down, please try again later");
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                return;
            }
        }
        else {
            plugin.getLogger().info("Authenticating user...");
            result = apicon.authenticateUser(event.getPlayer().getName());
            if(result) {
                event.setResult(PlayerLoginEvent.Result.ALLOWED);
                plugin.getLogger().info("Authentication succeeded: "+event.getPlayer().getName());
                return;
            }
            else {
                event.setKickMessage("[CITAUTH]You are not registered on CITAUTH-SYSTEM!");
                plugin.getLogger().info("Authentication failed: " +event.getPlayer().getName());
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                return;
            }
        }
    }
    */
}
