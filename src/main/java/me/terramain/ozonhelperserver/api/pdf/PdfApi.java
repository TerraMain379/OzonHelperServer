package me.terramain.ozonhelperserver.api.pdf;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import me.terramain.ozonhelperserver.api.BarcodeApi;

import java.io.*;
import java.net.MalformedURLException;

public class PdfApi {
    public static void generatePdf(File file, DocAction docAction){
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {throw new RuntimeException(e);}
        }
        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf);
            docAction.action(document);

            document.close();
            pdf.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void barcodePdf(String barcode, File file, File cachePngFile){
        if (!cachePngFile.exists()) {
            try {
                cachePngFile.createNewFile();
            } catch (IOException e) {throw new RuntimeException(e);}
        }
        BarcodeApi.generateOzonBarcode(barcode,cachePngFile);

        generatePdf(file,document -> {
            document.add(new Paragraph("hello!"));
            ImageData data; try {
                data = ImageDataFactory.create(cachePngFile.getPath());
            } catch (MalformedURLException e) {throw new RuntimeException(e);}
            Image image = new Image(data);
            document.add(image);
        });
    }


}
