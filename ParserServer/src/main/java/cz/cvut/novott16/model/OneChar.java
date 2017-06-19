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
public class OneChar {

    //num of refenrence
   

    int page;
    //position in text
    float lbx;
    float lby;
    float rux;
    float ruy;
    String font;
    int size;
    //text it self 
    String text;



    public OneChar(String text, float lbx, float lby, float rux, float ruy,int page, String font, int size) {
        this.page = page;
        this.lbx = lbx;
        this.lby = lby;
        this.rux = rux;
        this.ruy = ruy;
        this.font = font;
        this.size = size;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

public float[] getBBox(){

return new float[] {lbx,lby,rux,ruy};
}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(" <position = ");
        sb.append(lbx).append(", ").append(lby).append(", ").append(rux).append(", ").append(ruy);
        sb.append(" page= ").append(page).append(" /> ");
        sb.append(text).append("\n");

        return sb.toString();

    }
    
    public String styleKey(){
    return font+size;
    }

    public String getFont() {
        return font;
    }

    public int getSize() {
        return size;
    }
    
         
    public int getPage(){
    
    return page;}
}
