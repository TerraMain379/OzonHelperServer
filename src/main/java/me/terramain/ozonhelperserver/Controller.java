package me.terramain.ozonhelperserver;

import me.terramain.ozonhelperserver.api.BarcodeApi;
import me.terramain.ozonhelperserver.api.OzonApi;
import me.terramain.ozonhelperserver.api.apiHelper.ApiHelper;
import me.terramain.ozonhelperserver.api.pdf.PdfApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
public class Controller {

    @GetMapping
    public String openTest(){
        return "{server:\"open\"}";
    }

    @GetMapping("/info/{articul}")
    public String getInfo(@PathVariable String articul,
                          @RequestParam(name= "format",required = false,defaultValue = "false") String formatParam
    ){
        String result = OzonApi.getInfo(articul);
        boolean format = Boolean.parseBoolean(formatParam);
        return format?OzonApi.format(result):result;
    }

    @GetMapping("/info/stocks/{articul}")
    public String getStocks(@PathVariable String articul,
                            @RequestParam(name= "format",required = false,defaultValue = "false") String formatParam
    ) {
        String result = OzonApi.getStocks(articul);
        boolean format = Boolean.parseBoolean(formatParam);
        return format?OzonApi.format(result):result;
    }

    @GetMapping("/info/name/{articul}")
    public String getName(@PathVariable String articul,
                            @RequestParam(name= "format",required = false,defaultValue = "false") String formatParam
    ) {
        String result = OzonApi.getName(articul);
        boolean format = Boolean.parseBoolean(formatParam);
        return format?OzonApi.format(result):result;
    }

    @GetMapping("/info/barcode/{articul}")
    public String getBarcode1(@PathVariable String articul,
                              @RequestParam(name= "format",required = false,defaultValue = "false") String formatParam,
                              @RequestParam(name= "type",required = false,defaultValue = "json") String type
    ) {
        if (type.equals("json")) {
            String result = OzonApi.getBarcode(articul);
            boolean format = Boolean.parseBoolean(formatParam);
            return format ? OzonApi.format(result) : result;
        }
        if (type.equals("value")){
            return OzonApi.getBarcodeString(articul);
        }
        if (type.equals("pdf")){
            return OzonApi.getPdfBarcode(articul, false);
        }
        if (type.equals("jpdf")){
            String result = OzonApi.getPdfBarcode(articul, true);
            boolean format = Boolean.parseBoolean(formatParam);
            return format ? OzonApi.format(result) : result;
        }
        return null;
    }

    @GetMapping("/barcode/{articul}")
    public String getBarcode2(@PathVariable String articul,
                             @RequestParam(name= "format",required = false,defaultValue = "false") String formatParam,
                              @RequestParam(name= "type",required = false,defaultValue = "json") String type
    ) {
        return getBarcode1(articul,formatParam,type);
    }

    @GetMapping("/barcode/generate/{barcode}")
    public String genBarcode(@PathVariable String barcode,
                             @RequestParam(name = "type",required = false,defaultValue = "pdf") String type,
                             @RequestParam(name = "return",required = false,defaultValue = "filepath") String returnType
    ) {
        File file = ApiHelper.getNonExsistFile("cache\\generate_barcode",type);
        try {
            file.createNewFile();
        } catch (IOException e) {throw new RuntimeException(e);}

        if (type.equals("png")) BarcodeApi.generateOzonBarcode(barcode,file);
        else if (type.equals("pdf")) PdfApi.barcodePdf(barcode,file,ApiHelper.getNonExsistFile("cache\\generate_barcode","png"));
        else return ApiHelper.errorJson("false param: type");

        if (returnType.equals("filepath")) return "{\"filepath\": "+file.getPath()+"\"}";
        else if (returnType.equals("bytes")) return "{\"bytes\": "+ApiHelper.toByteString(file)+"\"}";
        else return ApiHelper.errorJson("false param: return");
    }

    /*
        /info/{articul} ?format=false --- json
        /info/stocks/{articul} ?format=true --- json
        ?(/info)/barcode/{articul} ?format=true ?type=json(json|value|pdf|jpdf) --- json|string|bytes_string|json(bytes_string)
        /barcode/generate/{barcode} ?type=pdf(pdf|png) ?return=filepath(filepath|bytes) --- json(filepath)|bytes(file)
    */

}
