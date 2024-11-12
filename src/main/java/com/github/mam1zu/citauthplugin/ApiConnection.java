package com.github.mam1zu.citauthplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class ApiConnection {
    private String apihost;
    private int apiport;
    private JavaPlugin plugin;
    private TokenManager mgr;
    public ApiConnection(JavaPlugin plugin, TokenManager mgr, String apihost, int apiport) {
        this.plugin = plugin;
        this.mgr = mgr;
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
            URL url = new URL("http://"+this.apihost+":"+this.apiport+"/api/status");
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

        String uuid_string = Bukkit.getOfflinePlayer(mcid).getUniqueId().toString();

        if(uuid_string == null) {
            Bukkit.getLogger().info("Player not found.");
            return false;
        }
        UUID uuid = UUID.fromString(uuid_string);

        return this.authenticateUser(uuid, 0);

    }

    public boolean authenticateUser(UUID uuid, int count) {

        boolean result = false;
        HttpURLConnection con = null;
        URL url;


        if(Bukkit.getOfflinePlayer(uuid) == null) {
            Bukkit.getLogger().info("Player not found.");
            return false;
        }

        String uuid_str = uuid.toString().replace("-", "");
        uuid_str = uuid_str.toLowerCase();

        try {
            url = new URL("http://"+this.apihost+":"+this.apiport+"/api/auth");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.setRequestProperty("Authorization", "Bearer "+mgr.getAccessToken());
            con.connect();

            JSONObject body = new JSONObject();
            body.put("uuid", uuid_str);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
            writer.write(body.toString());
            writer.close();
            int responsecode = con.getResponseCode();
            switch(responsecode) {
                case 200:
                    break;
                case 401:
                    if(count != 0) break;
                    mgr.fetchToken();
                    this.authenticateUser(uuid, 1);
                    break;
                case 503:
                    Bukkit.getLogger().info("API is temporarily unavailable.");
                    break;
            }
            result = responsecode == 200;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
