package org.ideidcard;

public class ImageData {
  
    private String id;
    private String dateProcessed;
    private String imagePath;
    private String imageContent;
    private String croppedImg;

    public ImageData() {
    }

  
    public ImageData(String id, String dateProcessed, String imagePath, String imageContent) {
	this.id = id;
	this.dateProcessed = dateProcessed;
	this.imagePath = imagePath;
	this.imageContent = imageContent;
    }

    public ImageData(String id, String dateProcessed, String imagePath, String imageContent, String croppedImg) {
  	this.id = id;
  	this.dateProcessed = dateProcessed;
  	this.imagePath = imagePath;
  	this.imageContent = imageContent;
  	this.croppedImg = croppedImg;
      }

    
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
    
    public String getCroppedImg() {
	return croppedImg;
    }

    public void setCroppedImg(String croppedImgBase64) {
	this.croppedImg = croppedImgBase64;
    }
    
    @Override
    public String toString() {
	return "{" + id + "::" + dateProcessed + "::" + imagePath + "::" + imageContent + "}";
    }
}
