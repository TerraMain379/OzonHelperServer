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
    @PostMapping("/generate/{type}")
    public static String generate(@PathVariable String type,
                                  @RequestBody String body
    ){
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        String articul = jsonObject.has("articul") ? jsonObject.get("articul").getAsString() : null;
        String path = jsonObject.has("path") ? jsonObject.get("path").getAsString() : Config.defFolder+"\\barcodes." + type;
        File barcode = new File(path);
        if (type.equals("pdf")){
            try {
                OzonBarcodeApi.generate(barcode, articul);
            }
            catch (NoSuchElementException e){
                return ErrorApi.getErrorMassage(1);
            }
            catch (IllegalArgumentException e){
                return ErrorApi.getErrorMassage(2, new String[]{type});
            } catch (ConnectException e) {
                return ErrorApi.getErrorMassage(5);
            }
            return "{\"status\":\"generated\",\"path\":\"" + barcode.getAbsolutePath() + "\"}";
        }
        if (type.equals("png")){
            try {
                BarcodeApi.generateBarcode(articul, barcode);
            } catch (IOException | WriterException e) {
                return ErrorApi.getErrorMassage(3);
            }
            return "{\"status\":\"generated\",\"path\":\"" + barcode.getAbsolutePath() + "\"}";
        }
        return ErrorApi.getErrorMassage(2, new String[]{type});
    }

    @PostMapping("/generatelist")
    public static String generateList(@RequestBody String body){
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        String path = jsonObject.has("path") ? jsonObject.get("path").getAsString() : Config.defFolder+"\\barcodes.pdf";

        List<String> articuls = new ArrayList<>();
        for (JsonElement jsonElement : jsonObject.getAsJsonArray("articuls")) {
            articuls.add(jsonElement.getAsString());
        }

        File barcode = new File(path);

        try {
                OzonBarcodeApi.generate(barcode, articuls);
        }
        catch (NoSuchElementException e){
            return ErrorApi.getErrorMassage(1);
        }
        catch (ConnectException e) {
            return ErrorApi.getErrorMassage(5);
        }
        return JsonApi.fix("{\"status\":\"generated\",\"path\":\"" + barcode.getAbsolutePath() + "\"}");
    }

    @PostMapping("/fbolist")
    public static String fboList(@RequestBody String body){
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        String supply_order_id = jsonObject.get("supply_order_id").getAsString();

        String path = jsonObject.has("path") ? jsonObject.get("path").getAsString() : Config.defFolder+"\\barcodes.pdf";
        File barcode = new File(path);

        try {
            OzonBarcodeApi.generate(barcode, OzonApi.getFBOItems(supply_order_id));
        }
        catch (NoSuchElementException e){
            return ErrorApi.getErrorMassage(1);
        }
        catch (ConnectException e) {
            return ErrorApi.getErrorMassage(5);
        }
        return JsonApi.fix("{\"status\":\"generated\",\"path\":\"" + barcode.getAbsolutePath() + "\"}");
    }
}
