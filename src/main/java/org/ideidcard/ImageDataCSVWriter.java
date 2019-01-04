package org.ideidcard;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVWriter;

public class ImageDataCSVWriter {

    public static void main(String[] args) {

	StringWriter writer = new StringWriter();
	try {

	    // using custom delimiter and quote character
	    CSVWriter csvWriter = new CSVWriter(writer, ',');

	    List<ImageData> imgDataList = new ArrayList<>();

	    ImageData ex1 = new ImageData();
	    ex1.setId("3");
	    ex1.setDateProcessed("2019-01-03-193530");
	    ex1.setImagePath("D:\\Downloads\\54191-verses-about-knowledge.800w.tn.jpg");
	    ex1.setImageContent("I KNOW GOD HAS A PLAN.");

	    ImageData ex2 = new ImageData();
	    ex2.setId("4");
	    ex2.setDateProcessed("2020-01-03-193530");
	    ex2.setImagePath("D:\\Downloads\\54191-verses-about-knowledge.800w.tn.png");
	    ex2.setImageContent("I KNOW GOD HAS A PLAN FOR YOU.");

	    imgDataList.add(ex1);
	    imgDataList.add(ex2);

	    List<String[]> data = toStringArray(imgDataList);

	    csvWriter.writeAll(data);
	    csvWriter.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	System.err.println(writer);

    }

    private static List<String[]> toStringArray(List<ImageData> imgDataList) {
	List<String[]> records = new ArrayList<>();
	// adding header record
	records.add(new String[] { "id", "dateProcessed", "imagePath", "imageContent" });

	Iterator<ImageData> it = imgDataList.iterator();
	while (it.hasNext()) {
	    ImageData imgData = it.next();
	    records.add(new String[] { imgData.getId(), imgData.getDateProcessed(), imgData.getImagePath(),
		    imgData.getImageContent() });
	}
	return records;
    }
}
