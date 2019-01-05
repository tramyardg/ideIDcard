package org.ideidcard;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

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
    
}
