/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.novott16.referenceexctractor;

import cz.cvut.novott16.referenceParser.ReferenceParser;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 *
 * @author TomasNovotny
 */
public class PDFMinerController {

    public static void main(String[] args) throws IOException, InterruptedException {

        boolean mine = true;
        String dirRes = "collection";
        String dirData = "text/";

        File f = new File(dirRes);
        File[] matchingFiles;

        matchingFiles = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith("pdf");
            }
        });

        if (mine) {
            for (File file : matchingFiles) {
               // Runtime.getRuntime().exec("pdf2txt.py -t xml -o \"" + dirData + file.getName() + ".xml\" " + dirRes + "/\"" + file.getName() + "\"");
ProcessBuilder pb = new ProcessBuilder("cmd", "/c",
                                      "pdf2txt.py -t xml -o \"" + dirData + file.getName() + ".xml\" " + dirRes + "\"" + file.getName() + "\"");
  Process p = pb.start();
 // p.waitFor();
            }

            Thread.sleep(600);
        }
        f = new File(dirData);
        matchingFiles = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });

        for (File file : matchingFiles) {
            System.out.println("=================================");
            System.out.println("File name " + file.getName());
            System.out.println(new ReferenceParser().parse(file).toString());

        }

    }
    
    public static String pdf2text(String source) {
        System.out.println("Source for miner "+source);
    try{
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c",
                                      "pdf2txt.py -t xml -o \"" + source + ".xml\" \"" + source+ "\"");
        
         File errlog = new File("C:\\tmp\\errlog.txt"); 

 pb.redirectError(errlog);
  Process p = pb.start();
p.waitFor();
       //Thread.sleep(10000);
        
        System.out.println("FILE Proccesesd OK");
        return source+".xml";
    }catch(Exception e){
    
    }
        return "";
    }
}
