package me.terramain.ozonhelperserver.api.barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.IOException;

public class BarcodeApi {
    public static int DEFAULT_WIDTH = 440;
    public static int DEFAULT_HEIGHT = 150;
    public static BarcodeFormat DEFAULT_BARCODE_FORMAT = BarcodeFormat.CODE_128;

    public static void generateBarcode(String text, File file, BarcodeFormat barcodeFormat, int width, int height) throws IOException, WriterException {
        if (!file.exists()) file.createNewFile();
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, barcodeFormat, width, height);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", file.toPath());
    }
    public static File generateBarcode(String text, String path, BarcodeFormat barcodeFormat, int width, int height) throws IOException, WriterException {
        generateBarcode(text, new File(path), barcodeFormat, width, height);
        return new File(path);
    }

    public static void generateBarcode(String text, File file, int width, int height) throws IOException, WriterException {
        generateBarcode(text,file,DEFAULT_BARCODE_FORMAT,width,height);
    }
    public static File generateBarcode(String text, String path, int width, int height) throws IOException, WriterException {
        return generateBarcode(text,path,DEFAULT_BARCODE_FORMAT,width,height);
    }

    public static void generateBarcode(String text, File file) throws IOException, WriterException {
        generateBarcode(text,file,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }
    public static File generateBarcode(String text, String path) throws IOException, WriterException {
        return generateBarcode(text,path,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }
}
