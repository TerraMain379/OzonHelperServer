package me.terramain.ozonhelperserver.api;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class BarcodeApi {
    public static final int OZON_DPI = 230;

    public static void generateOzonBarcode(String barcode, File file){
        generateBarcode(barcode,file,1087,634);
    }
    public static void generateBarcode(String barcode, File file, int width, int height) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {throw new RuntimeException(e);}
        }
        String imageFormat = "png"; // Формат изображения
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(barcode, BarcodeFormat.CODE_128, width, height);
            Path path = FileSystems.getDefault().getPath(file.getPath());
            MatrixToImageWriter.writeToPath(bitMatrix, "png", path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
