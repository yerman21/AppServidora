package com.webservice.appservidora.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
//<!-- Autor: Yerry Aguirre :) -->
import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public class CodeQR {
	
	public static byte[] encodeToQR(String text) {
	   int width = 600;
	   int height = 400;
	   String imageFormat = "png"; // "jpeg" "gif" "tiff"
	   
	   ByteOutputStream outputStream = new ByteOutputStream();
	   try {
		MatrixToImageWriter.writeToStream(
				   new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height)
				   , imageFormat
				   , outputStream
				   );
	} catch (IOException | WriterException e) {
		e.printStackTrace();
	}
	   return outputStream.getBytes();
	}
	
	public static String getBase64QR(String texto) {
		return Base64.getEncoder().encodeToString(encodeToQR(texto));	
	}
	
	public static String decodeQR(byte[] bytesImage){
		try {
			//preparando la imagen QR para leerla
			BufferedImage qrBufferedImage = ImageIO.read(new ByteArrayInputStream(bytesImage));
			LuminanceSource source = new BufferedImageLuminanceSource(qrBufferedImage);		
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			//con esta clase decodificaremos la imagen qr
			Reader reader = new MultiFormatReader();
			Result stringBarCode = reader.decode(bitmap);
			
			return stringBarCode.getText();
		} catch (NotFoundException | ChecksumException | FormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
