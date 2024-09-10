package me.terramain.ozonhelperserver.api.ozon;

import me.terramain.ozonhelperserver.Config;
import me.terramain.ozonhelperserver.api.RequestApi;

public class OzonRequestApi extends RequestApi {
    public OzonRequestApi(String ip, String method) {
        super(ip.charAt(0)=='/'?"https://api-seller.ozon.ru"+ip:ip, method);
        headParam("Client-Id", Config.clientId);
        headParam("Api-Key", Config.apiKey);
    }
}
