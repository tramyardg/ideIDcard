package org.ideidcard;

public class ImageData {

    private String id;
    private String dateProcessed;
    private String imagePath;
    private String imageContent;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getDateProcessed() {
	return dateProcessed;
    }

    public void setDateProcessed(String dateProcessed) {
	this.dateProcessed = dateProcessed;
    }

    public String getImagePath() {
	return imagePath;
    }

    public void setImagePath(String imagePath) {
	this.imagePath = imagePath;
    }

    public String getImageContent() {
	return imageContent;
    }

    public void setImageContent(String imageContent) {
	this.imageContent = imageContent;
    }

    @Override
    public String toString() {
	return "{" + id + "::" + dateProcessed + "::" + imagePath + "::" + imageContent + "}";
    }
}
