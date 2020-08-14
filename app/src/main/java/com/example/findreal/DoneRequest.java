package com.example.findreal;

public class DoneRequest {

    private String image_base64;
    private double real;
    private double fake;
    private String error;

    public DoneRequest(String image_base64, double real, double fake, String error){
        this.image_base64 = image_base64;
        this.real = real;
        this.fake = fake;
        this.error = error;
    }

    public String getImage() {return this.image_base64;}
    public double getReal() {return this.real;}
    public double getFake() {return this.fake;}
    public String getError() {return this.error;}
}
