/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.novott16.model;

/**
 *
 * @author TomasNovotny
 */
public class Reference {

    //num of refenrence
    int number;

    int page;
    //position in text
    float lbx;
    float lby;
    float rux;
    float ruy;
    
    //text it self 
    String text;
    
    public Reference(int number, float lbx, float lby, float rux, float ruy, int page) {
        this.number = number;
        this.lbx = lbx;
        this.lby = lby;
        this.rux = rux;
        this.ruy = ruy;
        this.page = page;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    
    
    //page: 2, coords: [449.082, 634.25, 460.699, 646.255], text: "adased"
    @Override
     public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{ \"page\":").append(page);
        sb.append(", \"coords\": [").append(lbx).append(", ").append(lby).append(", ").append(rux).append(", ").append(ruy).append("], ");
        sb.append("\"text\": \"").append(text).append("\" }");

        return sb.toString();

    }
}
