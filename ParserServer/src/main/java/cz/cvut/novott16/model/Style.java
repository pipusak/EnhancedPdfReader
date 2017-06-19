/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Comparator;

/**
 *
 * @author TomasNovotny
 */
public class Style {

    int size;
    String font;
    int charCount;
    

    public Style(int size, String font) {
        this.size = size;
        this.font = font;
         size=1;
    }

    public int getSize() {
        return size;
    }

    public String getFont() {
        return font;
    }



    public int getCharCount() {
        return charCount;
    }

    public void countChar() {
         charCount++;
    }

 public String styleKey(){
    return font+size;
    }


    
}
