package me.terramain.ozonhelperserver;

import me.terramain.textexecuter.TextHelper;
import me.terramain.textexecuter.textbuilder.TextBuilder;

public class Config {
    public static String clientId;
    public static String apiKey;
    public static String defFolder;
    public static void load(){
        String[] lines = fixText(TextHelper.readFile("config.data")).split("\n");
        clientId = lines[0];
        apiKey = lines[1];
        defFolder = lines[2];
    }

    private static String fixText(String text){
        return new TextBuilder(text).removeChars((char) 65279).getText();
    }
}
