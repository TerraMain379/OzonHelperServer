package me.terramain.ozonhelperserver;

import me.terramain.textexecuter.TextHelper;

public class Config {
    public static String clientId;
    public static String apiKey;
    public static String defFolder;
    public static void load(){
        String[] lines = TextHelper.readFile("config.data").split("\n");
        clientId = lines[0];
        apiKey = lines[1];
        defFolder = lines[2];
    }
}
