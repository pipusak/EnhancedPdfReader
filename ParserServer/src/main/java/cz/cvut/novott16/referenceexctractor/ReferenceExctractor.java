/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.novott16.referenceexctractor;


import cz.cvut.novott16.HeadlineParser.StyleParser;
import cz.cvut.novott16.freecite.Citations.Citation;
import cz.cvut.novott16.freecite.FreeCiteClient;
import cz.cvut.novott16.model.OneChar;
import cz.cvut.novott16.model.Reference;
import cz.cvut.novott16.pagemapper.PageMapper;
import cz.cvut.novott16.referenceParser.ReferenceParser;
import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import parser.Parser;

/**
 *
 * @author TomasNovotny
 */
public class ReferenceExctractor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        //String path = "data/Probabilistic Models for Focused Web Crawling 2.pdf.xml";
        //String path = "data/novott16_noddes.xml";
        String path = "C:\\tmp\\A. Bergholz, et al., Improved phishing detection using model-based features.pdf";
        String pathXML=PDFMinerController.pdf2text(path);
        System.out.println(pathXML);
        File source = new File(pathXML);
       List<Reference> ref= getReferences(source);
       for(Reference r:ref){
           System.out.println(r.toString());
       }
    }
    
    
static public List<Reference> getReferences(File source){
        Parser p = new Parser();
        PageMapper pm = new PageMapper();

        int start = 0;
        int end = 0;
        int page = 1;
        ArrayList<OneChar> text = p.parse(source);
        for (int i = 0; i < text.size(); i++) {

            if (text.get(i).getPage() != page || i == text.size() - 1) {
                start = end;
                end = i;

                pm.addPage(page, (List<OneChar>) text.subList(start, end));
                page++;
            }

        }
        StyleParser hp = new StyleParser();
      String headline = hp.parseHeadline(text);

        ReferenceParser rp = new ReferenceParser();
        List<Reference> citeList = rp.parse(source);
               for(Reference r:citeList){
           System.out.println(+r.getNumber()+" "+r.getText());
       }
     List<Citation> cits =FreeCiteClient.parse(ref2Array(citeList));
    for(Citation ct:cits){
        System.out.println("Citation "+ct.toString());
    }
  
     
//    System.out.println("size cits "+cits.size());
     
     for(int y=0;y<citeList.size();y++){
         
         Reference ref =citeList.get(y);
         try{
    //         System.out.println("Matched and updated");
             System.out.println("old text "+ref.getText());
            
         
         Citation cit=cits.get(citeList.get(y).getNumber()-1);
         
         ref.setText("Authors: "+cit.getAuthors().toString()+"Title: "+cit.getTitle());
             System.out.println("new text "+ref.getText());
//              System.out.println("");
         citeList.set(y, ref);
         }catch(Exception e){
             
             System.err.println(e);
         }
        
     }
     
     
     return citeList;
     
    }

   static String[] ref2Array(List<Reference> citeList) {
        
        int maxId=0;
          for(int i=0;i<citeList.size();i++){
          if(citeList.get(i).getNumber()>maxId){
              
              maxId=citeList.get(i).getNumber();
              
          }
              
          }
        
        String[] citStr = new String[maxId];
        
        for (int i = 0; i < citeList.size(); i++) {
           String subjectString = Normalizer.normalize(citeList.get(i).getText(), Normalizer.Form.NFD);
            
                        subjectString=subjectString.replaceAll("-\\n", "");
                        subjectString=subjectString.replaceAll("- ", "");
            citStr[citeList.get(i).getNumber()-1] = subjectString.replaceAll("[^\\x00-\\x7F]", "");
         
         
        }
        return citStr;
    }

}
