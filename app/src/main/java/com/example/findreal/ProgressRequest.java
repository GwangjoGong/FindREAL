package com.example.findreal;

public class ProgressRequest {

    private String image_base64;
    private String timestamp;
    private String service;


    public ProgressRequest(String image_base64, String timestamp, String service){
        this.image_base64 = image_base64;
        this.timestamp = timestamp;
        this.service = service;
    }

    public String getImage() {return this.image_base64;}
    public String getTimestamp() {return this.timestamp;}
    public String getService() {return this.service;}
}
