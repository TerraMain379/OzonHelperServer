package me.terramain.ozonhelperserver.api.apiHelper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Map;

public class ApiHelper {
    public static ApiHelper builder(){
        return new ApiHelper();
    }

    RequestData requestData;
    String jsonData;
    public ApiHelper() {
    }

    public ApiHelper requestIp(String ip){
        requestData = new RequestData(ip);
        return this;
    }
    public ApiHelper setHeader(String key, String value){
        requestData.headers.put(key,value);
        return this;
    }
    public ApiHelper setBody(String body){
        requestData.body = body;
        return this;
    }
    public ApiHelper request(){
        try {
            URL url = new URL(requestData.ip);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(requestData.method);
            for (Map.Entry<String,String> entry : requestData.headers.entrySet()){
                connection.setRequestProperty(entry.getKey(),entry.getValue());
            }

            if (requestData.body!=null) {
                connection.setDoOutput(true);
                try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                    outputStream.writeBytes(requestData.body);
                    outputStream.flush();
                }
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                result.append(inputLine);
            }
            reader.close();
            connection.disconnect();
            jsonData = result.toString();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return this;
    }
    public String getResult(){
        return jsonData;
    }


    public static File getNonExsistFile(String defaultPath, String extension){
        File file = new File(defaultPath+"."+extension);
        int number = 1;
        while (file.exists()){
            file = new File(defaultPath+number+"."+extension);
            number++;
        }
        return file;
    }
    public static String toByteString(File file) {
        byte[] bytes = new byte[0];
        try {
            bytes = readAllBytes(new FileInputStream(file));
        } catch (IOException e) {throw new RuntimeException(e);}
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static byte[] readAllBytes(FileInputStream fileInputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int bytesRead;
        byte[] data = new byte[16384];
        while ((bytesRead = fileInputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        fileInputStream.close();
        return buffer.toByteArray();
    }

    public static String errorJson(String massage){
        return "{\"error\": \""+massage+"\"}";
    }
}
