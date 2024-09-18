package me.terramain.ozonhelperserver.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.terramain.ozonhelperserver.api.ozon.OzonRequestApi;

import java.net.ConnectException;

public class OzonFBOListApi {
    public static int getListSize(int supply_order_id) throws ConnectException {
        String result = new OzonRequestApi("/v1/supply-order/get","POST")
                .setBody("{\"supply_order_id\":\"" + supply_order_id + "\"}")
                .request();
        JsonObject json = JsonParser.parseString(result).getAsJsonObject();
        return json.get("total_items_count").getAsInt();
    }

    public static int getList(int supply_order_id, int listSize) throws ConnectException {
        String result = new OzonRequestApi("/v1/supply-order/items","POST")
                .setBody("{\"supply_order_id\":\"" + supply_order_id + "\",\"page\":1,\"page_size\":" + listSize + "}")
                .request();
        JsonArray array = JsonParser.parseString(result).getAsJsonObject().get("items").getAsJsonArray();
        return -1;
    }
}
