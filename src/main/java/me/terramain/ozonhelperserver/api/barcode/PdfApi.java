package me.terramain.ozonhelperserver.api.barcode;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import java.io.File;
import java.io.IOException;

public class PdfApi {
    public static void generatePdf(File file, PdfAction pdfAction){
        try {
            if (!file.exists()) file.createNewFile();

            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(writer);

            PdfFont font = PdfFontFactory.createFont("Arial.ttf", "Identity-H");

            pdfAction.action(pdfDocument, font);

            pdfDocument.close();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
