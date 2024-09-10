package me.terramain.ozonhelperserver.api;

import com.google.zxing.WriterException;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
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
    public static void generate(File file, List<String> barcodeTexts, List<String> productNames){
        final double pixelConst = 3.793627;
        final int width = (int) (43 * pixelConst);
        final int height = (int) (25 * pixelConst);
        PdfApi.generatePdf(file, (pdfDocument, defaultFont) -> {
            Document document = new Document(pdfDocument,new PageSize(width,height));
            document.setMargins(0,0,0,0);

            for (int i = 0; i < barcodeTexts.size(); i++) {
                pdfDocument.addNewPage();
                String barcodeText = barcodeTexts.get(i);
                String productName = productNames.get(i);
                if (productName.length()>135){
                    productName = productName.substring(0,130) + "...";
                }
                try {
                    new File("cache").mkdirs();
                    File barcodeImage = BarcodeApi.generateBarcode(barcodeText, "cache\\cache_barcode.png");
                    try {
                        Image image = new Image(ImageDataFactory.create(barcodeImage.getAbsolutePath()));
                        image   .setHeight((float)( height * 0.5))
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
                document.add(new Paragraph(barcodeText).setFont(defaultFont).setFontSize(9).setMargins(0,0,0,4));
                document.add(new Paragraph(productName).setFont(defaultFont).setFontSize(6).setMargins(0,4,0,4));
            }
        });
    }
    public static void generate(File file, String articul) throws ConnectException {
        String info = OzonApi.getProductInfo(articul);
        String barcode = OzonApi.getDataFromJson(info, "barcode");
        String name = OzonApi.getDataFromJson(info, "name");
        generate(file, List.of(barcode), List.of(name));
    }
    public static void generate(File file, List<String> articuls) throws ConnectException {
        List<String> infoList = OzonApi.getProductsInfo(articuls);
        List<String> names = new ArrayList<>(List.copyOf(infoList));
        names.replaceAll(name -> OzonApi.getDataFromJson(name,"name"));
        List<String> barcodes = new ArrayList<>(List.copyOf(infoList));
        barcodes.replaceAll(barcode -> OzonApi.getDataFromJson(barcode,"barcode"));
        generate(file, barcodes, names);
    }


}
