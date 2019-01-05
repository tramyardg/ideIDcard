package org.ideidcard;

import static java.nio.file.StandardOpenOption.*;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveAs {

    private static final Logger LOGGER = Logger.getLogger(SaveAs.class.getName());

    void saveDataAs(String str, boolean isAppend) {
	byte[] data = str.getBytes();
	Path p = null;
	if (isAppend) {
	    p = Paths.get(ImageDataCSV.CSV_DATA_PATH);
	    try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
		out.write(data, 0, data.length);
	    } catch (IOException x) {
		LOGGER.log(Level.WARNING, "Exception found!", x);
	    }
	} else {
	    p = Paths.get("./data.log/" + Util.timestampFileName());
	    try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE))) {
		out.write(data, 0, data.length);
	    } catch (IOException x) {
		LOGGER.log(Level.WARNING, "Exception found!", x);
	    }
	}
    }

}
