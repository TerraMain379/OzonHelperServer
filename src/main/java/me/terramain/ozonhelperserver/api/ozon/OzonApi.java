package me.terramain.ozonhelperserver.api.ozon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.terramain.textexecuter.TextBuilder;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class OzonApi {
    public static String getProductInfo(String articul) throws ConnectException {
        String json = new OzonRequestApi("/v2/product/info","POST")
                .setBody("offer_id",articul)
                .request();
        if (json==null) return null;
        JsonObject result = JsonParser.parseString(json).getAsJsonObject();
        if (result.has("result")) return result.get("result").toString();
        throw new NoSuchElementException();
    }
    public static List<String> getProductsInfo(String[] articuls) throws ConnectException {
        TextBuilder bodyParam = new TextBuilder("[");
        for (String articul : articuls) {
            bodyParam.append("\"").append(articul).append("\",");
        }
        bodyParam.removeChar(bodyParam.length()-1).append("]");

        String json = new OzonRequestApi("/v2/product/info/list","POST")
                .setBody("offer_id", bodyParam.getText(), false)
                .request();
        JsonArray jsonElements = JsonParser.parseString(json).getAsJsonObject().get("result").getAsJsonObject().get("items").getAsJsonArray();
        List<String> infoBlocks = new ArrayList<>();
        for (JsonElement jsonElement : jsonElements) {
            infoBlocks.add(jsonElement.toString());
        }
        return infoBlocks;
    }
    public static List<String> getProductsInfo(List<String> articuls) throws ConnectException {
        return getProductsInfo(articuls.toArray(new String[]{}));
    }

    public static String getDataFromJson(String json, String dataType){
        if (json==null) return null;
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return switch (dataType) {
            case "id" -> jsonObject.get("id").getAsString();
            case "name" -> jsonObject.get("name").getAsString();
            case "barcode" -> jsonObject.get("barcode").getAsString();
            case "barcodes" -> jsonObject.get("barcodes").toString();
            case "images" -> jsonObject.get("images").toString();
            case "price" -> jsonObject.get("price").getAsString();
            case "stocks" -> jsonObject.get("stocks").toString();
            default -> throw new IllegalArgumentException();
        };
    }

    public static String getProductInfo(String articul, String dataType) throws ConnectException {
        if (dataType==null) return getProductInfo(articul);
        return getDataFromJson(getProductInfo(articul),dataType);
    }
    public static List<String> getProductsInfo(String[] articuls, String dataType) throws ConnectException {
        List<String> productsInfo = getProductsInfo(articuls);
        productsInfo.replaceAll(json -> getDataFromJson(json, dataType));
        return productsInfo;
    }
    public static List<String> getProductsInfo(List<String> articuls, String dataType) throws ConnectException {
        List<String> productsInfo = getProductsInfo(articuls);
        productsInfo.replaceAll(json -> getDataFromJson(json, dataType));
        return productsInfo;
    }
}
