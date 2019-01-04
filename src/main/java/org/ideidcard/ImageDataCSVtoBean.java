package org.ideidcard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

public class ImageDataCSVtoBean {

    public static void main(String[] args) {
	try {
	    CSVReader reader = new CSVReader(new FileReader("./data.log/testcsv2.csv"), ',');
	    
	    HeaderColumnNameMappingStrategy<ImageData> headerColumnNameStrategy = new HeaderColumnNameMappingStrategy<>();
	    headerColumnNameStrategy.setType(ImageData.class);
	    
	    CsvToBean<ImageData> csvToBean = new CsvToBean<>();
	    List<ImageData> imgDataList = csvToBean.parse(headerColumnNameStrategy, reader);
	    
	    System.out.println(imgDataList);	    
	    reader.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
