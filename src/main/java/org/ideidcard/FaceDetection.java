package org.ideidcard;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetection {

    public static void main(String[] args) {
	nu.pattern.OpenCV.loadShared();

	CascadeClassifier faceDetector = new CascadeClassifier();
	faceDetector.load("haarcascade_frontalface_alt.xml");

	// Input image
	Mat image = Imgcodecs.imread("D:\\Downloads\\testimage.jpg");

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

	// Saving the output image
	String filename = "Ouput.jpg";
	Imgcodecs.imwrite("D:\\Downloads\\" + filename, image);

	Mat markedImage = new Mat(image, rectCrop);
	Imgcodecs.imwrite("D:\\Downloads\\cropimage.jpg", markedImage);
	
    }
}
