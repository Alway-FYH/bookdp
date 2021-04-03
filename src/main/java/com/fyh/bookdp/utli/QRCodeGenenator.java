package com.fyh.bookdp.utli;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenenator {
    public static  void generaQR(String text,int width,int height,String filePath){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE,width,height);
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix,"PNG",path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
