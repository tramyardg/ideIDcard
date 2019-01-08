package org.ideidcard;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.BufferedOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

public class ImageDataCSV {

    private final Logger logger = Logger.getLogger(ImageDataCSV.class.getName());
    private static final String CSV_DATA_PATH = "./data.log/csvdata.csv";

    public void csvToBean() {
	CSVReader reader = null;
	try {
	    reader = new CSVReader(new FileReader(CSV_DATA_PATH), ',');
	    HeaderColumnNameMappingStrategy<ImageData> headerColumnNameStrategy = new HeaderColumnNameMappingStrategy<>();
	    headerColumnNameStrategy.setType(ImageData.class);
	    CsvToBean<ImageData> csvToBean = new CsvToBean<>();
	    List<ImageData> imgDataList = csvToBean.parse(headerColumnNameStrategy, reader);
	    logger.info(imgDataList.toString());
	} catch (IOException e) {
	    logger.log(Level.WARNING, "Exception found!", e);
	} finally {
	    try {
		reader.close();
	    } catch (NullPointerException | IOException e) {
		logger.log(Level.WARNING, "Exception found!", e);
	    }
	}
    }

    public void reader() {
	CSVReader reader = null;
	try {
	    reader = new CSVReader(new FileReader(CSV_DATA_PATH), ',');
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
	} catch (IOException e) {
	    logger.log(Level.WARNING, "Exception found!", e);
	} finally {
	    try {
		reader.close();
	    } catch (IOException e) {
		logger.log(Level.WARNING, "Exception found!", e);
	    }
	}
    }

    private List<String[]> toStringArray(List<ImageData> imgDataList) {
	List<String[]> records = new ArrayList<>();
	Iterator<ImageData> it = imgDataList.iterator();
	while (it.hasNext()) {
	    ImageData imgData = it.next();
	    records.add(new String[] { imgData.getId(), imgData.getDateProcessed(), imgData.getImagePath(),
		    imgData.getImageContent(), imgData.getCroppedImg() });
	}
	return records;
    }

    public void saveDataAsCSV(List<ImageData> imgDataList) {
	StringWriter writer = new StringWriter();
	try {
	    CSVWriter csvWriter = new CSVWriter(writer, ',');
	    List<String[]> data = toStringArray(imgDataList);
	    csvWriter.writeAll(data);
	    csvWriter.close();
	} catch (IOException e) {
	    logger.log(Level.WARNING, "Exception found!", e);
	}
	byte[] data = writer.toString().getBytes();
	Path p = Paths.get(CSV_DATA_PATH);
	try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
	    out.write(data, 0, data.length);
	} catch (IOException x) {
	    logger.log(Level.WARNING, "Exception found!", x);
	}
    }

    @SuppressWarnings("unused")
    private List<ImageData> getExampleData() {
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
	return imgDataList;
    }

}
