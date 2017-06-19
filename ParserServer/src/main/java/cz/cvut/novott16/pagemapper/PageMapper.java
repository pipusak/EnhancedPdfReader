/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.novott16.pagemapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cz.cvut.novott16.model.OneChar;

/**
 *
 * @author TomasNovotny
 */
public class PageMapper {
    
    File output = new File("output.txt");
    FileWriter writer;

    public PageMapper() {
        try {
            this.writer = new FileWriter(output);
            writer.append("");
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(PageMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
   ArrayList<PageMap> pageMaps = new ArrayList<>();
   
   
   public void addPage(int num, List<OneChar> textContent){
   PageMap pm = getPageMap(num);
   
   //using for eliminating gaps between words
   //rux = lbx for next word
   boolean space =false;
   float spaceOffset = 0;
   
   for(OneChar ch:textContent){
   float[] bb=ch.getBBox();
     if(ch.getText().equals(" ")){
     spaceOffset = bb[2];
     space=true;
     }else
     if(space){
     pm.markObject(spaceOffset, bb[1], bb[2], bb[3]);
     space=false;
     }else{
       pm.markObject(bb[0], bb[1], bb[2], bb[3]);
     }
   }
  pm.printJPG();
  // pm.printHistogram();
   pm.printFancyHistogram();
       System.out.println("is DoubleColumn "+  pm.isDoubleColumn());
   }
   
   PageMap getPageMap(int i){
   
   Iterator<PageMap> it =pageMaps.iterator();
   PageMap pg = null;
   while(it.hasNext()){
        pg = it.next();
       if(pg.pageNum==i){
       return pg;    
       }
   } 
   
   pg = new PageMap(i,595,842);
   pageMaps.add(pg);
   return pg;
   }
       
}
