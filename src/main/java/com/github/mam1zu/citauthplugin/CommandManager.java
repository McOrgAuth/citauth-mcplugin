package com.github.mam1zu.citauthplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandManager implements CommandExecutor {
    private JavaPlugin plugin;
    private ApiConnection apicon;
    private final Pattern pattern = Pattern.compile("^([a-zA-Z]|[0-9]|_){2,16}$");
    CommandManager(JavaPlugin plugin, ApiConnection apicon) {
        this.plugin = plugin;
        this.apicon = apicon;
        this.plugin.getCommand("citauth").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.isOp() && !sender.hasPermission("citauth.use")) {
            return true;
        }

        if(args.length == 0) {
            showHelpMessage(sender);
            return true;
        }
        if(args[0].equalsIgnoreCase("status")) {

            sender.sendMessage("Trying to check the status of CITAUTH-API...");

            if(apicon.checkStatus()) {
                sender.sendMessage("CITAUTH-API is available.");
                return true;
            }
            else {
                sender.sendMessage("CITAUTH-API is temporarily unavailable.");
                return true;
            }
        }
        else if(args[0].equalsIgnoreCase("auth")) {

            if(args.length != 2) {
                sender.sendMessage("/citauth auth [MCID]");
                return true;
            }
            Matcher m = pattern.matcher(args[1]);
            if(!m.find()) {
                sender.sendMessage("invalid player name");
                return true;
            }

            try {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, new ApiConnectionRunnable(plugin, new TokenManager(plugin), sender, args[1]));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            showHelpMessage(sender);
        }
        return true;
    }
    public void showHelpMessage(CommandSender sender) {
        sender.sendMessage("/citauth status: check the status of CITAUTH-API");
        sender.sendMessage("/citauth auth [mcid]: check whether the player is registered or not");
    }
}
