package com.github.mam1zu.citauthplugin;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandManager implements CommandExecutor {
    private JavaPlugin plugin;
    private APIConnection apicon;
    private final Pattern pattern = Pattern.compile("^([a-zA-Z]|[0-9]|_){2,16}$");
    CommandManager(JavaPlugin plugin, APIConnection apicon) {
        this.plugin = plugin;
        this.apicon = apicon;
        this.plugin.getCommand("citauth").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            //help
            return true;
        }
        if(args[0].equalsIgnoreCase("checkstatus")) {

            Bukkit.getLogger().info("Trying to check the status of CITAUTH-API...");

            if(apicon.checkStatus()) {
                sender.sendMessage("CITAUTH-API is available.");
                return true;
            }
            else {
                sender.sendMessage("CITAUTH-API is temporally unavailable...");
                return true;
            }

        }

        if(args[0].equalsIgnoreCase("auth")) {

            if(!(args.length == 2)) {
                sender.sendMessage("/citauth auth [MCID]");
                return true;
            }
            Matcher m = pattern.matcher(args[1]);
            if(!m.find()) {
                sender.sendMessage("invalid username");
                return true;
            }
            sender.sendMessage("Authenticating user...");
            if(apicon.authenticateUser(args[1])) {
                sender.sendMessage("Authentication succeeded: "+args[1]);
                return true;
            }
            else {
                sender.sendMessage("Authentication failed: "+args[1]);
                return true;
            }
        }
        return false;
    }
}
