package me.terramain.ozonhelperserver.config;

public class ApiConfig {
    public String client_id;
    public FileLink api_key;

    public ApiConfig(String client_id, FileLink api_key) {
        this.client_id = client_id;
        this.api_key = api_key;
    }
}
