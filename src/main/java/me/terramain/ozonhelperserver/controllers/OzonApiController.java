package me.terramain.ozonhelperserver.controllers;

import com.google.gson.JsonParser;
import me.terramain.ozonhelperserver.api.ErrorApi;
import me.terramain.ozonhelperserver.api.ozon.OzonApi;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/ozonapi")
public class OzonApiController {
    @PostMapping("/info")
    public static String info(
                              @RequestBody String body
    ){
        return info(null,body);
    }
    @PostMapping("/info/{type}")
    public static String info(@PathVariable(name = "type") String type,
                              @RequestBody String body
    ){
        String articul = JsonParser.parseString(body).getAsJsonObject().get("articul").getAsString();
        try {
            return OzonApi.getProductInfo(articul, type);
        }
        catch (NoSuchElementException e){
            return ErrorApi.getErrorMassage(1);
        }
        catch (IllegalArgumentException e){
            return ErrorApi.getErrorMassage(2, new String[]{type});
        } catch (ConnectException e) {
            return ErrorApi.getErrorMassage(5);
        }
    }
}
