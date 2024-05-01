package com.github.mam1zu.citauthplugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIConnection {
    String apihost;
    String apiport;
    public APIConnection(String apihost, String apiport) {
        this.apihost = apihost;
        this.apiport = apiport;
    }

    public boolean checkStatus() throws IOException {
        try {

            HttpURLConnection con = null;

            URL url = new URL("http://"+this.apihost+":"+this.apiport+"/api/checkStatus");//TODO: to remove hardcoding URL

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
            } /*catch(IOException e) {
                System.out.println("[CITAUTH]システムは現在利用できません");
                e.printStackTrace();
            }*/

            con.disconnect();
            System.out.println("Response body:" + resBody);

            switch (responseCode) {
                case 200:
                    System.out.println("[CITAUTH]システムは現在利用可能です");
                    System.out.println("[CITAUTH]Response code:　" + responseCode);
                    return true;
                case 503:
                    System.out.println("[CITAUTH]システムは現在利用できません");
                    System.out.println("[CITAUTH]Response code: " + responseCode);
                    return false;
                default:
                    System.out.println("[CITAUTH]想定されていない数値です");
                    System.out.println("[CITAUTH]Response coce: " + responseCode);
                    return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
