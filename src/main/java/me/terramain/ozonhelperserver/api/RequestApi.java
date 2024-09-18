package me.terramain.ozonhelperserver.api;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestApi {
    private final String ip;
    private final String method;
    private String body;
    private Map<String, String> headParams;

    public RequestApi(String ip, String method) {
        this.ip = ip;
        this.method = method;
        this.headParams = new HashMap<>();
    }

    public RequestApi(String ip) {
        this(ip,"GET");
    }


    public RequestApi headParam(String key, String value){
        headParams.put(key,value);
        return this;
    }
    public RequestApi setBody(String body){
        this.body = body;
        return this;
    }
    public RequestApi setBody(String key, String value){
        return setBody(key, value, true);
    }
    public RequestApi setBody(String key, String value, boolean stringValue){
        if (stringValue) value = '"'+value+'"';
        this.body = "{\"" + key + "\":" + value + "}";
        return this;
    }
    public RequestApi setBody(Object o){
        Gson gson = new Gson();
        return setBody(gson.toJson(o));
    }

    public String request() throws ConnectException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(ip).openConnection();
            connection.setRequestMethod(method);
            headParams.forEach(connection::setRequestProperty);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            int responseCode = connection.getResponseCode();

            InputStream inputStream;
            if (responseCode >= 200 && responseCode < 300) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = reader.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }

        } catch (Exception e) {
            throw new ConnectException();
        }
    }

}
