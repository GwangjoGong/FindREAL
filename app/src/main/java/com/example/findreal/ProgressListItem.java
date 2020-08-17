package com.example.findreal;

import java.io.Serializable;

public class ProgressListItem implements Serializable {

    private String image_base64;
    private String timestamp;
    private String service;


    public void setImage(String image) {this.image_base64 = image;}
    public void setTimestamp(String timestamp) {this.timestamp = timestamp;}
    public void setService (String service) {this.service = service;}

    public String getImage() {return this.image_base64;}
    public String getTimestamp() {return this.timestamp;}
    public String getService() {return this.service;}

}
