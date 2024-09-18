package me.terramain.ozonhelperserver.api.barcode;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;

import java.io.IOException;

public interface PdfAction {
    void action(PdfDocument pdfDocument);
}
