package com.github.mam1zu.citauthplugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import com.google.gson.JsonObject;

public class TokenManager {
    JavaPlugin plugin;
    FileConfiguration fc;
    File token_file;

    public TokenManager(JavaPlugin plugin) throws IOException, InvalidConfigurationException {

        this.plugin = plugin;
        this.token_file = new File("./CitAuthPlugin/token.yml");
        this.fc = new YamlConfiguration();

        if(token_file.exists()) {
            this.fc.load(token_file);
        }
        else {
            token_file.createNewFile();
            fc.addDefault("access_token", "access_token");
            fc.addDefault("refresh_token", "refresh_token");
            fc.save(token_file);
        }

    }

    public void refreshToken() {

    }

    public boolean fetchToken() {

        boolean result = false;
        HttpURLConnection con = null;
        URL url;

        String auth_host = this.plugin.getConfig().getString("auth_server.host");
        String auth_port = this.plugin.getConfig().getString("auth_server.port");
        String userid = this.plugin.getConfig().getString("auth_server.userid");
        String password = this.plugin.getConfig().getString("auth_server.password");

        try {
            url = new URL("http://"+auth_host+":"+auth_port+"/auth/client");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.connect();

            JSONObject body = new JSONObject();
            body.put("userid", userid);
            body.put("passhash", password);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            writer.write(body.toString());
            writer.close();
            int responseCode = con.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            if(responseCode == 200) {
                StringBuilder str = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    str.append(line);
                }
                JSONObject token = new JSONObject(line);
                String access_token = token.getString("access_token");
                String refresh_token = token.getString("refresh_token");
                this.setAccessToken(access_token);
                this.setRefreshToken(refresh_token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void setAccessToken(String access_token) {
        fc.set("access_token", access_token);
    }

    public void setRefreshToken(String refresh_token) {
        fc.set("refresh_token", refresh_token);
    }

    public String getAccessToken() {
        return fc.getString("access_token");
    }

    public String getRefreshToken() {
        return fc.getString("refresh_token");
    }

    public void save() throws IOException {
        fc.save(this.token_file);
    }
}
