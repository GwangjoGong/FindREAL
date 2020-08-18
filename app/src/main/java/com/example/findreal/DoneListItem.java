package com.example.findreal;

import java.io.Serializable;

public class DoneListItem implements Serializable {
    private String image_base64;
    private double real;
    private double fake;
    private String error;


    public void setImage(String image) {this.image_base64 = image;}
    public void setReal(double real) {this.real = real;}
    public void setFake(double fake) {this.fake = fake;}
    public void setError(String error) {this.error=error;}

    public String getImage() {return this.image_base64;}
    public double getReal() {return this.real;}
    public double getFake() {return this.fake;}
    public String getError() {return this.error;}

}
