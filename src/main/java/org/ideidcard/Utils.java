package org.ideidcard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {
    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

    private Utils() {
	throw new IllegalStateException("Utility class");
    }

    static String timestampFileName() {
	String fileName = new SimpleDateFormat("yyyyMMddHHmmss'.txt'").format(new Date());
	String yyyy = fileName.substring(0, 4);
	int yLength = yyyy.length();
	String mt = fileName.substring(yLength, yLength + 2);
	String dd = fileName.substring(yLength + 2, yLength + 4);
	return yyyy + "-" + mt + "-" + dd + "-" + fileName.substring(8);
    }

    static String replaceBacklashWithDouble(String p) {
	return p.replace("\\", "\\\\");
    }

    static int getLineNumberCSV(String location) {
	Path path = null;
	try {
	    path = Paths.get(location);
	    // skip(1L) skips header
	    return (int) Files.lines(path).skip(1L).count();
	} catch (IOException e) {
	    LOGGER.log(Level.WARNING, "Exception found!", e);
	    return -1;
	}
    }

}
