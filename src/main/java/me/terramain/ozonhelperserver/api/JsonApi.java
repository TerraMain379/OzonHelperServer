package me.terramain.ozonhelperserver.api;

import me.terramain.textexecuter.TextBuilder;

import java.util.List;

public class JsonApi {
    public static String fix(String json){//TODO: fix from new textexecuter version!!!
        TextBuilder textBuilder = new TextBuilder(json);
        for (int i = 0; i < textBuilder.length(); i++) {
            if (textBuilder.getChar(i)=='\\'){
                textBuilder.insert(i,'\\');
                i++;
            }
        }
        return textBuilder.getText();
    }
}
