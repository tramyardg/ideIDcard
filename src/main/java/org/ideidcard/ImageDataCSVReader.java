package org.ideidcard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVReader;

public class ImageDataCSVReader {

    public static void main(String[] args) {
	try {
	    CSVReader reader = new CSVReader(new FileReader("./data.log/testcsv2.csv"), ',');
	    List<ImageData> imgDataList = new ArrayList<>();

	    List<String[]> records = reader.readAll();
	    Iterator<String[]> iterator = records.iterator();

	    while (iterator.hasNext()) {
		String[] record = iterator.next();
		ImageData imgData = new ImageData();
		imgData.setId(record[0]);
		imgData.setDateProcessed(record[1]);
		imgData.setImagePath(record[2]);
		imgData.setImageContent(record[3]);
		imgDataList.add(imgData);
	    }

	    System.out.println(imgDataList);
	    reader.close();

	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
