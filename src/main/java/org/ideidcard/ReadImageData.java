package org.ideidcard;

import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ReadImageData {
  
    ReadImageData() {}

    public static String readImage(String imageLocation) {
	ITesseract instance = new Tesseract();
	try {
	    String imgText = instance.doOCR(new File(imageLocation));
	    return imgText;
	} catch (TesseractException e) {
	    e.getMessage();
	    return "Error while reading image";
	}
    }
}
