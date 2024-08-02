package me.terramain.ozonhelperserver.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.terramain.ozonhelperserver.Main;
import me.terramain.ozonhelperserver.api.apiHelper.ApiHelper;
import me.terramain.ozonhelperserver.api.pdf.PdfApi;
import me.terramain.textexecuter.TextHelper;

import java.io.File;
import java.io.IOException;

public class OzonApi {
    public static String getInfo(String articul) {
        return getDefaultApiHelper("https://api-seller.ozon.ru/v2/product/info")
                .setBody("{\"offer_id\":\"" + articul + "\"}")
                .request().getResult();
    }
    public static String getStocks(String articul) {
        String info = getInfo(articul);
        JsonObject jsonObject = JsonParser.parseString(info).getAsJsonObject();
        return jsonObject.getAsJsonObject("result").getAsJsonObject("stocks").toString();
    }
    public static String getBarcodeString(String articul) {
        String info = getInfo(articul);
        JsonObject jsonObject = JsonParser.parseString(info).getAsJsonObject();
        return jsonObject.getAsJsonObject("result").get("barcode").getAsString();
    }
    public static String getBarcode(String articul) {
        return "{\"barcode\": \"" + getBarcodeString(articul) + "\"}";
    }
    public static String getName(String articul){
        String info = getInfo(articul);
        JsonObject jsonObject = JsonParser.parseString(info).getAsJsonObject();
        return jsonObject.getAsJsonObject("result").get("name").getAsString();
    }

    public static File generatePdfBarcode(String articul, boolean clearCache) {
        File pdf = ApiHelper.getNonExsistFile("cache\\barcode","pdf");
        File png = ApiHelper.getNonExsistFile("cache\\barcode","png");
        try {
            pdf.createNewFile();
        } catch (IOException e) {throw new RuntimeException(e);}
        PdfApi.barcodePdf(getBarcodeString(articul), pdf, png);
        png.delete();
        return pdf;
    }
    public static String getPdfBarcode(String articul, boolean jsonable) {
        File pdf = generatePdfBarcode(articul, true);
        String result = ApiHelper.toByteString(pdf);
        if (jsonable) return "{\"bytes\":\"" + result + "\"}";
        System.out.println(pdf.getPath());
        pdf.delete();
        return result;
    }

    public static String format(String json){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(gson.fromJson(json, Object.class));
    }
    public static ApiHelper getDefaultApiHelper(String ip){
        return ApiHelper.builder()
                .requestIp(ip)
                .setHeader("Api-Key", TextHelper.readFile(Main.config.api.api_key.file).strip())
                .setHeader("Client-Id",Main.config.api.client_id)
                .setHeader("Content-Type","application/json");
    }


}
