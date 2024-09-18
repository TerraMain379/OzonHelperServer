package me.terramain.ozonhelperserver.api;

import com.google.zxing.WriterException;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import me.terramain.ozonhelperserver.api.barcode.BarcodeApi;
import me.terramain.ozonhelperserver.api.barcode.PdfApi;
import me.terramain.ozonhelperserver.api.ozon.OzonApi;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class OzonBarcodeApi {
    public static String generate(String path, List<String> barcodeTexts, List<String> productNames){
        File file = new File(path+"\\barcode.pdf");
        for (int i = 1; file.exists(); i++) {
            file = new File(path+"\\barcode (" + i + ").pdf");
        }
        final double pixelConst = 3.793627;
        final int width = (int) (43 * pixelConst);
        final int height = (int) (25 * pixelConst);
        List<File> images = new ArrayList<>();
        PdfApi.generatePdf(file, (pdfDocument) -> {
            PdfFont defFont;
            PdfFont boldFont;
            try {
                defFont = PdfFontFactory.createFont("Arial.ttf", "Identity-H");
                boldFont = PdfFontFactory.createFont("Arial_bold.ttf", "Identity-H");
            } catch (IOException e) { throw new RuntimeException(e); }
            Document document = new Document(pdfDocument,new PageSize(width,height));
            document.setMargins(0,0,0,0);

            for (int i = 0; i < barcodeTexts.size(); i++) {
                pdfDocument.addNewPage();
                String barcodeText = barcodeTexts.get(i);
                String productName = productNames.get(i);
                if (productName.length()>165){
                    productName = productName.substring(0,130) + "...";
                }
                try {
                    new File("cache").mkdirs();
                    File barcodeImage = BarcodeApi.generateBarcode(barcodeText, "cache\\cache_barcode" + i + ".png");
                    images.add(barcodeImage);
                    try {
                        Image image = new Image(ImageDataFactory.create(barcodeImage.getAbsolutePath()));
                        image   .setHeight((float)( height * 0.4))
                                .setWidth(width)
                                .setMargins(0,0,0,0);
                        document.add(image);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IOException | WriterException e) {
                    e.printStackTrace();
                    document.add(new Paragraph("__________"));
                }
                document.add(new Paragraph(barcodeText).setFont(defFont).setFontSize(9).setMargins(0,0,0,4));
                document.add(new Paragraph(productName).setFont(boldFont).setFontSize(7).setMargins(0,4,0,4));
            }
        });
        images.forEach(File::delete);
        return file.getAbsolutePath();
    }
    public static String generate(String path, String articul) throws ConnectException {
        String info = OzonApi.getProductInfo(articul);
        String barcode = OzonApi.getDataFromJson(info, "barcode");
        String name = OzonApi.getDataFromJson(info, "name");
        return generate(path, List.of(barcode), List.of(name));
    }
    public static String generate(String path, List<String> articuls) throws ConnectException {
        List<String> infoList = OzonApi.getProductsInfo(articuls);
        List<String> names = new ArrayList<>(List.copyOf(infoList));
        names.replaceAll(name -> OzonApi.getDataFromJson(name,"name"));
        List<String> barcodes = new ArrayList<>(List.copyOf(infoList));
        barcodes.replaceAll(barcode -> OzonApi.getDataFromJson(barcode,"barcode"));
        return generate(path, barcodes, names);
    }


}
