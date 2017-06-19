/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.novott16.pagemapper;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author TomasNovotny
 */
public class PageMap {
   boolean[][] bits;
   int pageNum;
   ArrayList squares;
    int[] fancyHistogram  ;
     int[] histogram  ;
    int width;
    int height;
    
    //factor of division for histogram (divides width) to col num
   static final int hCol = 10;
 
// lower and upper bound for histogram column checking
   long u ;
    long l ;
   
    public PageMap(int number, int width ,int height) {
        this.pageNum=number;
        bits= new boolean[width][height];
        this.width=width;
        this.height=height;
        histogram= new int[7];
        fancyHistogram = new int[width/hCol];
        
        u = Math.round(0.6*width/hCol);
        l = Math.round(0.4*width/hCol);
            }
   
    
    
   void markObject(float lbx,float lby,float rux,float ruy){
       
       int x1 = Math.round(lbx);
       int x2 = Math.round(rux);
    int y1 = Math.round(lby);
       int y2 = Math.round(ruy);
       
       for (int y=y1;y<=y2;y++){
       for (int x= x1;x<=x2;x++){
          
           bits[x][y]=true;
       }}
       
       
   }
   
   void printBits(File output){
       FileWriter fw = null;
       try {
           
           fw = new FileWriter(output,true);
           fw.append("============================================================================================================================ page start");
           for (int y=height-1;y>=0;y--){
               for (int x = 0;x<width;x++){
                   if(bits[x][y]){
                       fw.append("|");
                   }else{
                      fw.append(" ");}
               }fw.append("\n");
           }          
       } catch (IOException ex) {
           Logger.getLogger(PageMap.class.getName()).log(Level.SEVERE, null, ex);
       } finally {
           try {
               fw.flush();
               fw.append("============================================================================================================================ page end");
               fw.close();
           } catch (IOException ex) {
               Logger.getLogger(PageMap.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
   }
   
      void printJPG(){
       try {
           
           byte BLACK = (byte)0, WHITE = (byte)255;
           byte[] map = {BLACK, WHITE};
           IndexColorModel icm = new IndexColorModel(1, map.length, map, map, map);
           // create checkered data
           int[] data = new int[width*height];
           for(int i=height-1; i>=0; i--)
               for(int j=0; j<width; j++)
                   data[(height-1-i)*width + j] = bits[j][i]? BLACK:WHITE;
           // create image from color model and data
           WritableRaster raster = icm.createCompatibleWritableRaster( width, height);
           raster.setPixels(0, 0, width, height, data);
           BufferedImage bi = new BufferedImage(icm, raster, false, null);
           // output to a file
           ImageIO.write(bi, "jpg", new File("test"+pageNum+".jpg"));
       } catch (IOException ex) {
           Logger.getLogger(PageMap.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
      
      void printFancyHistogram(){
          
          System.out.println("Histogram for page "+pageNum );
      

           for(int j=0; j<width; j++){
           for(int i=height-1; i>=0; i--){
                 if(bits[j][i])
                     fancyHistogram[Math.round(j/hCol)]++;
      
           }
           }
         
          


          
    
       try {
            DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
      
            for(int i = 0;i<fancyHistogram.length;i++)
            line_chart_dataset.addValue(fancyHistogram[i] , "colum" , Integer.toString(i) );
      

      JFreeChart lineChartObject = ChartFactory.createLineChart(
         "Pixel density in columns","columns",
         "pixels",
         line_chart_dataset,PlotOrientation.VERTICAL,
         true,true,false);

      int widthofI = 640; /* Width of the image */
      int heightofI = 480; /* Height of the image */ 
      File lineChart = new File( "FancyHis"+pageNum+".jpeg" ); 
      ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, widthofI ,heightofI);
       } catch (IOException ex) {
           Logger.getLogger(PageMap.class.getName()).log(Level.SEVERE, null, ex);
       }
      
      }

      
            void printHistogram(){
          
          System.out.println("Histogram for page "+pageNum );
      

           for(int j=0; j<width; j++){
           for(int i=height-1; i>=0; i--){
                 if(bits[j][i])
                     histogram[Math.round(j/85)]++;
      
           }
           }
          
          
    
       try {
            DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
      
            for(int i = 0;i<histogram.length;i++)
            line_chart_dataset.addValue(histogram[i] , "colum" , Integer.toString(i) );
      

      JFreeChart lineChartObject = ChartFactory.createLineChart(
         "number of chars per column","columns",
         "number of chars",
         line_chart_dataset,PlotOrientation.VERTICAL,
         true,true,false);

      int widthofI = 640; /* Width of the image */
      int heightofI = 480; /* Height of the image */ 
      File lineChart = new File( "sipleHis"+pageNum+".jpeg" ); 
      ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, widthofI ,heightofI);
       } catch (IOException ex) {
           Logger.getLogger(PageMap.class.getName()).log(Level.SEVERE, null, ex);
       }
      
      }
            
      public boolean isDoubleColumn(){
          
          

          int min= Integer.MAX_VALUE;
          int pos=0;
          int sum=0;
          for(int i=(int) l;i<u;i++){
          if(min>fancyHistogram[i]){
          min=fancyHistogram[i];
          pos=i;
          
          }
          sum+=fancyHistogram[i];
          }
          int avg = sum/(int)(u-l);
       //   System.out.println("avg "+avg);
         //     System.out.println("min "+min);
         //     System.out.println("pos "+pos);
          
        int a = fancyHistogram[pos-4]+fancyHistogram[pos-3]+fancyHistogram[pos-2];
        int b = fancyHistogram[pos-1]+fancyHistogram[pos]+fancyHistogram[pos+1];
        int c =fancyHistogram[pos+2]+fancyHistogram[pos+3]+fancyHistogram[pos+4];
          if((a>b)&&(b<c)&&min<(avg/2)){
          return true;
          }
          
      
          return false;
      }
   
}
