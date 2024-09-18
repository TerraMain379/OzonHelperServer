package me.terramain.ozonhelperserver.api.ozon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.terramain.ozonhelperserver.ArraySortedMap;
import me.terramain.ozonhelperserver.api.ErrorApi;
import me.terramain.textexecuter.textbuilder.TextBuilder;

import java.net.ConnectException;
import java.util.*;

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
        ArraySortedMap<String, Integer> articulsMap = new ArraySortedMap<>();
        for (String articul : articuls) {
            if (articulsMap.containsKey(articul)){
                int value = articulsMap.get(articul);
                articulsMap.put(articul, value+1);
            }
            else articulsMap.put(articul, 1);
        }

        articuls = new String[articulsMap.size()];
        Iterator<Map.Entry<String, Integer>> iterator = articulsMap.entrySet().iterator();
        for (int i = 0; i < articulsMap.size(); i++) {
            articuls[i] = "\"" + iterator.next().getKey() + "\"";
        }

        String body = "{\"offer_id\":" + Arrays.toString(articuls) + "}";
        JsonObject json = JsonParser.parseString(
                new OzonRequestApi("/v2/product/info/list", "POST")
                        .setBody("offer_id", Arrays.toString(articuls), false)
                        .request()
        ).getAsJsonObject();
        List<String> infoBlocks = new ArrayList<>();
        JsonArray jsonArray = json.getAsJsonObject("result").getAsJsonArray("items");
        for (int i = 0; i < articulsMap.size(); i++) {
            int value = articulsMap.getByIndex(i);
            for (int j = 0; j < value; j++) {
                infoBlocks.add(jsonArray.get(i).toString());
            }
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

    public static List<String> getFBOItems(String supply_order_id) throws ConnectException {
        int num = getFBOItemsNumber(supply_order_id);
        TextBuilder textBuilder = new TextBuilder("{\"supply_order_id\": \"")
                .append(supply_order_id)
                .append("\", \"page\": 1, \"page_size\": ")
                .append(num)
                .append("}");
        String json = new OzonRequestApi("https://api-seller.ozon.ru/v1/supply-order/items", "POST")
                .setBody(textBuilder.getText())
                .request();
        List<String> articuls = new ArrayList<>();
        JsonArray array;
        try {
            array = JsonParser.parseString(json).getAsJsonObject().getAsJsonArray("items");
        } catch (RuntimeException e) {
            throw new NoSuchElementException();
        }
        for (JsonElement jsonElement : array) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String articul = jsonObject.get("offer_id").getAsString();
            int number = jsonObject.get("quantity").getAsNumber().intValue();
            for (int i = 0; i < number; i++) {
                articuls.add(articul);
            }
        }
        return articuls;
    }
    public static int getFBOItemsNumber(String supply_order_id) throws ConnectException {

        String json = new OzonRequestApi("https://api-seller.ozon.ru/v1/supply-order/get", "POST")
                .setBody("supply_order_id",supply_order_id)
                .request();
        return JsonParser.parseString(json).getAsJsonObject().get("total_items_count").getAsInt();
    }
}
