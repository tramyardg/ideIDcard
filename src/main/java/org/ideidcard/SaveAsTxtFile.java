package org.ideidcard;

import java.text.SimpleDateFormat;
import java.util.Date;
import static java.nio.file.StandardOpenOption.*;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveAsTxtFile {

    private String timestampFileName() {
	String fileName = new SimpleDateFormat("yyyyMMddHHmmss'.txt'").format(new Date());
	String yyyy = fileName.substring(0, 4);
	int yLength = yyyy.length();
	String mt = fileName.substring(yLength, yLength + 2);
	String dd = fileName.substring(yLength + 2, yLength + 4);
	return yyyy + "-" + mt + "-" + dd + "-" + fileName.substring(8);
    }

    public void createFile(String txt) {
	byte data[] = txt.getBytes();
	Path p = Paths.get("./data.log/" + timestampFileName());
	// for append: Files.newOutputStream(p, CREATE, APPEND))) {
	try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE))) {
	    out.write(data, 0, data.length);
	} catch (IOException x) {
	    System.err.println(x);
	}
    }

}
