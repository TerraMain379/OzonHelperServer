package me.terramain.ozonhelperserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OzonApi {
    public static String getInfo(String articul, boolean format) throws IOException {
        URL url = new URL("https://api-seller.ozon.ru/v2/product/info");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Api-Key", "a5ce3178-7cfd-435e-b158-11e406501c78");
        connection.setRequestProperty("Client-Id", "1628857");
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setDoOutput(true);
        String jsonInputString = "{\"offer_id\":\""+ articul +"\"}";
        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.writeBytes(jsonInputString);
            outputStream.flush();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder result = new StringBuilder();

        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            result.append(inputLine);
        }
        reader.close();
        connection.disconnect();
        if (format) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(gson.fromJson(result.toString(), Object.class));
        }
        return result.toString();
    }

    public static String getStocks(String articul, boolean format) throws IOException {
        String info = getInfo(articul,false);

    }
}
