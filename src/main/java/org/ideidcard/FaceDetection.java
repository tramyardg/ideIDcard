package org.ideidcard;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetection {
    private final Logger logger = Logger.getLogger(FaceDetection.class.getName());
    private static final String CASECADE_CLASSIFIER_XML = "haarcascade_frontalface_alt.xml";
    private static final String CROPPED_IMG_PATH = "./data.log/cropped.images/";

    private String imageSrcPath;
    private String croppedImageName;
    private boolean isFaceDetected;
    
    public boolean isFaceDetected() {
        return isFaceDetected;
    }

    public void setFaceDetected(boolean isFaceDetected) {
        this.isFaceDetected = isFaceDetected;
    }

    FaceDetection(String imgSrcPath) {
	this.imageSrcPath = Utils.replaceBacklashWithDouble(imgSrcPath);
	String txtFileName = Utils.timestampFileName();
	this.croppedImageName = txtFileName.substring(0, txtFileName.lastIndexOf('.')) + ".png";
	this.isFaceDetected = false;
    }

    public void detectFace() {
	nu.pattern.OpenCV.loadShared();

	CascadeClassifier faceDetector = new CascadeClassifier();
	faceDetector.load(CASECADE_CLASSIFIER_XML);

	logger.info(imageSrcPath);

	// Input image
	Mat image = Imgcodecs.imread(imageSrcPath);
	
	// Detecting faces
	MatOfRect faceDetections = new MatOfRect();
	faceDetector.detectMultiScale(image, faceDetections);

	// Creating a rectangular box showing faces detected
	Rect rectCrop = null;
	for (Rect rect : faceDetections.toArray()) {
	    Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
		    new Scalar(0, 255, 0));
	    rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);
	}

	if (rectCrop != null) {
	    setFaceDetected(true);
	    // Saving the cropped image
	    Mat markedImage = new Mat(image, rectCrop);
	    Imgcodecs.imwrite(CROPPED_IMG_PATH + croppedImageName, markedImage);
	}
    }

    public String convertedImageToBase64() {
	byte[] bytes = null;
	try {
	    bytes = FileUtils.readFileToByteArray(new File(CROPPED_IMG_PATH + croppedImageName));
	} catch (IOException e) {
	    logger.log(Level.WARNING, "Exception found!", e);
	}
	String encodedData = Base64.getEncoder().encodeToString(bytes);
	logger.info(encodedData);
	return encodedData;
    }
}
