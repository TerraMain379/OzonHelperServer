package me.terramain.ozonhelperserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        Config.load();
        SpringApplication.run(Main.class);

        //System.out.println(OzonFBOListApi.getListSize(31314442));

        String jsonString = "{\"articuls\":[\"1\",\"1\",\"1\"]}";
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        // Получаем массив "articuls"
        JsonArray articulsArray = jsonObject.get("articuls").getAsJsonArray();

        // Обходим и выводим каждый элемент массива
        for (JsonElement element : articulsArray) {
            System.out.println(element.getAsString());
        }

    }
}
