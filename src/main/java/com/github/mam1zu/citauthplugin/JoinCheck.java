package com.github.mam1zu.citauthplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinCheck implements Listener {

    private APIConnection apicon;
    private JavaPlugin plugin;
    private boolean useWhitelistTemporarily = false;
    JoinCheck(JavaPlugin plugin, APIConnection apicon) {
        this.plugin = plugin;
        this.apicon = apicon;
    }

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
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
            result = apicon.authenticateUser(event.getPlayer().getName());
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
    }
}
