package com.mskola.files;

import java.io.Serializable;
import java.util.ArrayList;

public class storageFile implements Serializable{
    String strData = null;
    private ArrayList<byte[]> allImages = new ArrayList<byte[]>();
    private String operation;
    
    public void setStrData(String data){
        strData = data;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    
    public void addImageFile(byte[] imageFile){
        this.allImages.add(imageFile);
    }
    
    
    public String getStrData(){
        return strData;
    }
    
    public String getOperation() {    
        return operation;
    }
    
    
    public ArrayList<byte[]> getImageFiles(){
        return allImages;
    }
}
