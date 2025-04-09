package com.github.mam1zu.citauthplugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

public class ApiConnectionRunnable implements Runnable {

    private String apihost;
    private int apiport;
    private JavaPlugin plugin;
    private TokenManager mgr;
    private UUID uuid;
    private String mcid;
    private CommandSender sender;

    public ApiConnectionRunnable(JavaPlugin plugin, TokenManager mgr) {
        this.plugin = plugin;
        this.mgr = mgr;
        this.apihost = plugin.getConfig().getString("api_server.host");
        this.apiport = plugin.getConfig().getInt("api_server.port");
    }

    public ApiConnectionRunnable(JavaPlugin plugin, TokenManager mgr, CommandSender sender, UUID uuid) {
        this.plugin = plugin;
        this.mgr = mgr;
        this.sender = sender;
        this.apihost = plugin.getConfig().getString("api_server.host");
        this.apiport = plugin.getConfig().getInt("api_server.port");
        this.uuid = uuid;
    }

    public ApiConnectionRunnable(JavaPlugin plugin, TokenManager mgr, CommandSender sender, String mcid) {
        this.plugin = plugin;
        this.mgr = mgr;
        this.sender = sender;
        this.apihost = plugin.getConfig().getString("api_server.host");
        this.apiport = plugin.getConfig().getInt("api_server.port");
        this.mcid = mcid;
    }

    @Override
    public void run() {
        if(this.sender != null)
            sender.sendMessage("Authenticating user...");
        else
            plugin.getLogger().info("Authenticating user...");
        boolean auth_result = authenticateUser(0);
        if(this.sender != null) {
            if(auth_result) {
                if(this.mcid != null) {
                    sender.sendMessage("Authentication succeeded: "+mcid);
                    return;
                }
                else {
                    sender.sendMessage("Authentication succeeded: "+uuid);
                    return;
                }
            }
            else {
                if(this.mcid != null) {
                    sender.sendMessage("Authentication failed: "+mcid);
                    return;
                }
                else {
                    sender.sendMessage("Autnentication failed: "+uuid);
                }
            }
        }
    }

    public boolean authenticateUser(int loop_count) {
        URL url;
        HttpURLConnection con = null;
        String p_uuid = null;

        if(this.mcid != null) {
            if((p_uuid = Bukkit.getOfflinePlayer(this.mcid).getUniqueId().toString()) == null) {
                return false;
            }
        }

        if(this.uuid != null){
            if(Bukkit.getOfflinePlayer(this.uuid) != null) {
                p_uuid = this.uuid.toString();
            }
            else {
                return false;
            }
        }

        p_uuid = p_uuid.replace("-", "");
        p_uuid = p_uuid.toLowerCase();

        try {
            if(this.plugin.getConfig().getBoolean("api_server.useSSL")) {
                url = new URL("https://"+this.apihost+"/api/auth");
                con = (HttpsURLConnection) url.openConnection();
            }
            else {
                url = new URL("http://"+this.apihost+":"+this.apiport+"/api/auth");
                con = (HttpURLConnection) url.openConnection();
            }
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.setRequestProperty("Authorization", "Bearer "+mgr.getAccessToken());
            con.connect();

            JSONObject body = new JSONObject();
            body.put("uuid", p_uuid);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            writer.write(body.toString());
            writer.flush();
            plugin.getLogger().info(body.toString());

            int responseCode = con.getResponseCode();
            switch(responseCode) {
                case 200:
                    return true;
                case 400:
                    plugin.getLogger().info("bad_request");
                    return false;
                case 401:
                    if(loop_count == 0) {
                        //step 1: try refresh or fetch
                        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                        String error = reader.readLine();
                        plugin.getLogger().info(error);
                        if(error.equals("expired_access_token")) {
                            mgr.refreshToken();
                            plugin.getLogger().info("TOKEN_EXPIRED");
                        }
                        else
                            mgr.fetchToken();
                        return this.authenticateUser(1);
                    }
                    else if(loop_count == 1) {
                        //step 2: step 1 failed, then retry fetch
                        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                        String error = reader.readLine();
                        plugin.getLogger().info(error);

                        mgr.fetchToken();
                        return this.authenticateUser(2);
                    }
                case 503:
                    sender.sendMessage("API seems unavailable now.");
                default:
                    return false;
            }
        } catch (FileNotFoundException e) {
            if(loop_count != 0) return false;
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            try {
                String error = reader.readLine();
                if(error.equals("expired_access_token"))
                    mgr.refreshToken();
                else
                    mgr.fetchToken();
            } catch (IOException e_io) {
                e_io.printStackTrace();
            }
            return this.authenticateUser(1);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}