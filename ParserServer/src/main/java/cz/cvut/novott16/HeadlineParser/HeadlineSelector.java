/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.novott16.HeadlineParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cz.cvut.novott16.model.OneChar;
import model.Style;

/**
 *
 * @author TomasNovotny
 */
public class HeadlineSelector {
    public String headliner(HashMap<String,Style> styles, ArrayList<OneChar> text){
    ArrayList<String[]> headlines= new ArrayList<>();
       String styleKey="";
        StringBuilder sb = new StringBuilder();
    for(OneChar ch:text){
        if(styles.containsKey(ch.styleKey())){
            
            if(styleKey.equals(ch.styleKey())){
            sb.append(ch.getText());
            }else if(styleKey.equals("")){
            sb=new StringBuilder();
            sb.append(ch.getText());
            styleKey=ch.styleKey();
            }else{
            if(sb.length()>4){
             headlines.add(new String[]{styleKey,sb.toString()});
            
            }
            sb=new StringBuilder();
            sb.append(ch.getText());
            styleKey=ch.styleKey(); 
            }
            
        } else if(ch.getSize()==-1){
          
        sb.append(ch.getText());
        }else if(!styleKey.equals("")){
         if(sb.length()>4){
        headlines.add(new String[]{styleKey,sb.toString()});
                }
        styleKey="";
        }
 
    }
    

   for(String[] pair:headlines){
      // System.out.println("f= "+pair[0]+" t= "+pair[1]);
   } 
    return headlines.get(0)[1];
    }
}
