package com.github.mam1zu.citauthplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class APIConnection {
    private String apihost;
    private String apiport;
    private JavaPlugin plugin;
    public APIConnection(JavaPlugin plugin, String apihost, String apiport) {
        this.plugin = plugin;
        this.apihost = apihost;
        this.apiport = apiport;
    }

    @Deprecated
    public boolean checkStatus_old() {
        try {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            });

            HttpURLConnection con = null;

            URL url = new URL("http://"+this.apihost+":"+this.apiport+"/api/checkStatus");

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);

            int responseCode = con.getResponseCode();

            StringBuilder resBody = new StringBuilder();
            String line = null;

            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
                while ((line = br.readLine()) != null) {
                    resBody.append(line);
                }
            } catch(IOException e) {
                e.printStackTrace();
            }


            con.disconnect();

            switch (responseCode) {
                case 200:
                    return true;
                default:
                    return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkStatus() {
        try{
            URL url = new URL("http://"+this.apihost+":"+this.apiport+"/api/checkStatus");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            int responseCode = con.getResponseCode();
            return responseCode == 200;
        } catch(MalformedURLException e) {
            Bukkit.getLogger().info("invalid API access url");
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean authenticateUser(String mcid) {

        boolean result = false;
        HttpURLConnection con = null;
        URL url;

        try {
            url = new URL("http://"+this.apihost+":"+this.apiport+"/api/authenticateUser/"+mcid);
            Bukkit.getLogger().info(url.toString());
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            int responseCode = con.getResponseCode();
            result = (responseCode == 200);
        } catch (MalformedURLException e) {
            Bukkit.getLogger().warning("URL invalid");
            e.printStackTrace();
        } catch (IOException e) {
            Bukkit.getLogger().warning("Connection failed");
        } finally {
            con.disconnect();
        }

        return result;

    }
}
