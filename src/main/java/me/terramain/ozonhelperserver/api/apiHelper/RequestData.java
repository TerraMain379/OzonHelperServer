package me.terramain.ozonhelperserver.api.apiHelper;

import java.util.HashMap;
import java.util.Map;

public class RequestData {
    public String ip;
    public Map<String,String> headers;
    public String method;
    public String body;

    public RequestData(String ip) {
        this.ip = ip;
        this.headers = new HashMap<>();
        this.method = "GET";
    }
}
