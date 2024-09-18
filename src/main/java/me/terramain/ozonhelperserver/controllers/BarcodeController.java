package me.terramain.ozonhelperserver.controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.WriterException;
import me.terramain.ozonhelperserver.Config;
import me.terramain.ozonhelperserver.api.ErrorApi;
import me.terramain.ozonhelperserver.api.JsonApi;
import me.terramain.ozonhelperserver.api.OzonBarcodeApi;
import me.terramain.ozonhelperserver.api.barcode.BarcodeApi;
import me.terramain.ozonhelperserver.api.ozon.OzonApi;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/barcode")
public class BarcodeController {
    @PostMapping("/generatelist")
    public static String generateList(@RequestBody String body){
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

        List<String> articuls = new ArrayList<>();
        for (JsonElement jsonElement : jsonObject.getAsJsonArray("articuls")) {
            articuls.add(jsonElement.getAsString());
        }

        String path;
        try {
            path = OzonBarcodeApi.generate(Config.defFolder, articuls);
        }
        catch (NoSuchElementException e){
            return ErrorApi.getErrorMassage(1);
        }
        catch (ConnectException e) {
            return ErrorApi.getErrorMassage(5);
        }
        return JsonApi.fix("{\"status\":\"generated\",\"path\":\"" + path + "\"}");
    }

    @PostMapping("/fbolist")
    public static String fboList(@RequestBody String body){
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        String supply_order_id = jsonObject.get("supply_order_id").getAsString();

        String path;

        try {
            path = OzonBarcodeApi.generate(Config.defFolder, OzonApi.getFBOItems(supply_order_id));
        }
        catch (NoSuchElementException e){
            return ErrorApi.getErrorMassage(1);
        }
        catch (ConnectException e) {
            return ErrorApi.getErrorMassage(5);
        }
        return JsonApi.fix("{\"status\":\"generated\",\"path\":\"" + path + "\"}");
    }
}
