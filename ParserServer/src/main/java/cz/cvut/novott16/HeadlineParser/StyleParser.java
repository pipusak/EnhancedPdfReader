/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.novott16.HeadlineParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cz.cvut.novott16.model.OneChar;
import cz.cvut.novott16.pagemapper.PageMap;
import model.Style;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


/**
 *
 * @author TomasNovotny
 */
public class StyleParser {
    HashMap<String,Style> styleMap;
     ArrayList<Style> stList;
     int mTextStyle=0;

    public StyleParser() {
        styleMap = new HashMap<>();
        stList = new ArrayList<>();
    }
   public String parseHeadline(ArrayList<OneChar> text){
   
        for(OneChar ch:text){
        if(ch.getSize()>0){
        if(styleMap.containsKey(ch.styleKey())){
        styleMap.get(ch.styleKey()).countChar();
        }else{
        styleMap.put(ch.styleKey(), new Style(ch.getSize(),ch.getFont()));
            
        }}
            
        }
               
            for(String key:styleMap.keySet()){
            stList.add(styleMap.get(key));
            }
              Collections.sort(stList,new StyleComparator());
              for(int i=0;i<stList.size();i++){
              if(stList.get(mTextStyle).getCharCount()<stList.get(i).getCharCount()){
              mTextStyle=i;
              }
              }
            //  System.out.println("mTextStyle is at "+mTextStyle);
              
               printHeadlineHistogram();
               //use cut by eye or use cut everithing above common
               //List<Style> subStyleLs  =    stList.subList(stList.size()-5, stList.size());
       List<Style> subStyleLs  =    stList.subList(mTextStyle, stList.size());
        for(int i=0;i<subStyleLs.size();i++){
        if(subStyleLs.get(i).getSize()==stList.get(mTextStyle).getSize()){
        subStyleLs.remove(i);
        }
        }
         HashMap<String,Style> nStyleMap = new HashMap<>();
         
        for(Style nst:subStyleLs){
       //     System.out.println("Style "+nst.styleKey());
        nStyleMap.put(nst.styleKey(), nst);
        } 
        
        HeadlineSelector hs = new HeadlineSelector();
        return hs.headliner(nStyleMap, text);
   
    }
   
            void printHeadlineHistogram(){
          
         
      

         
       try {
            DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();

            
              for(Style st:stList){
            line_chart_dataset.addValue(st.getCharCount() , "style" , st.styleKey() );
       //           System.out.println(st.getFont()+" size= "+st.getSize()+" count= "+st.getCharCount()); 
              }

      JFreeChart lineChartObject = ChartFactory.createLineChart(
         "Style occurance",
         "font size","number of chars",
         line_chart_dataset,PlotOrientation.VERTICAL,
         true,true,false);

      int widthofI = 800; /* Width of the image */
      int heightofI = 600; /* Height of the image */ 
      File lineChart = new File( "styleHis.jpeg" ); 
      ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, widthofI ,heightofI);
       } catch (IOException ex) {
           Logger.getLogger(PageMap.class.getName()).log(Level.SEVERE, null, ex);
       }
      
      }
            
            
            class StyleComparator implements Comparator<Style> {
      @Override
    public int compare(Style o1, Style o2) {
        if(o1.getSize()<o2.getSize()){
        return -1;
        }else if(o1.getSize()>o2.getSize()){
        return 1 ;
        }else{return 0;}
    }
}

}
