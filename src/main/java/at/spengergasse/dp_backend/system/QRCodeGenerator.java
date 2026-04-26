package at.spengergasse.dp_backend.system;


import at.spengergasse.dp_backend.exceptions.ExeException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.validation.constraints.Null;

import java.io.IOException;
import java.nio.file.Paths;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;


public class QRCodeGenerator
{
    /**
     * Funktion fur die QR Generiren
     * @param qrCodeContent
     * @param qrCodeFileName    Opzional - Defautl : QRCode.png in root
     * @param width             Opzional - Default : 200
     * @param height            Opzional - Default : 200
     * @throws WriterException
     * @throws IOException
     */
    public void createQRCode(String qrCodeContent , String qrCodeFileName, int width, int height) throws ExeException
    {
        qrCodeContent = qrCodeContent ==  null ? "" : qrCodeContent.trim();// Il link o testo da codificare
        qrCodeFileName = qrCodeFileName ==  null ? "" : qrCodeFileName.trim();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeContent, BarcodeFormat.QR_CODE, width, height);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", Paths.get(qrCodeFileName));

        } catch (WriterException | IOException e) {
            throw new ExeException(e.getMessage() , e);
        }

    }

    public void createQRCode(String qrCodeContent) throws  ExeException
    { createQRCode(qrCodeContent, "QRcode.png" );}

    public void createQRCode(String qrCodeContent, String qrCodeFileName) throws  ExeException
    { createQRCode(qrCodeContent, qrCodeFileName, 200,200 );}

}//end Class
