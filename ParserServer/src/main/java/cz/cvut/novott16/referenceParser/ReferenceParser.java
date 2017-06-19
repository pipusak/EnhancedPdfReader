/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.novott16.referenceParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import cz.cvut.novott16.model.Reference;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 *
 * @author TomasNovotny
 */
public class ReferenceParser {

    //pointer for method witch cathces reference word    
    private int charPointer = 0;
    private String word = "references";
    private int wSize = word.length();
    private int refPage = 0;
    private int refCount = 0;
    private ArrayList<Reference> refList = new ArrayList<Reference>();

    public static void main(String[] args) {
        String path = "data/Focused Crawling Using Context Graphs.pdf.xml";

        File source = new File(path);

        ReferenceParser p = new ReferenceParser();
        ArrayList<Reference> refList = p.parse(source);
       // System.out.println("References count " + p.refCount + " last at page " + p.refPage);
       // System.out.println(msg);
       for(Reference ref:refList){
           System.out.println(ref.toString());
       }
    }

    public ArrayList<Reference> parse(File source) {

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(source),"UTF8"));
            String line;
            int page = 0;
            boolean ref = false;
            boolean refParse = false;
            StringBuilder num = null;
            float lbx = -1;
            float lby = -1;
            float urx = -1;
            float ury = -1;
            while (br.ready()) {
                line = br.readLine();

                //detectinf text section
                if (line.startsWith("<text ")) {

                    int start = line.indexOf('>');
                    int end = line.indexOf('<', 2);
                    if (start > end) {
                        break;
                    }
                    String ps = line.substring(start + 1, end);

                    //checking for reference
                    if (wordFinder(ps)) {
                        refPage = page;
                        refCount++;
                    }

                    //parsing start of reference
                    if (ps.equals("[")) {
                        ref = true;
                        num = new StringBuilder();

                        start = line.indexOf("bbox=");
                        start = line.indexOf('\"', start);
                        end = line.indexOf('\"', ++start);
                        String[] coords = line.substring(start, end).split(",");
                        lbx = Float.parseFloat(coords[0]);
                        lby = Float.parseFloat(coords[1]);

                    } else if (ps.equals("]") && ref) {
                        try {
                            //     System.out.println("num= "+num.toString());
                            int refNum = Integer.parseInt(num.toString());

                            start = line.indexOf("bbox=");
                            start = line.indexOf('\"', start);
                            end = line.indexOf('\"', ++start);
                            String[] coords = line.substring(start, end).split(",");
                            urx = Float.parseFloat(coords[2]);
                            ury = Float.parseFloat(coords[3]);

                            refList.add(new Reference(refNum, lbx, lby, urx, ury, page));
                            ref = false;
                        } catch (NumberFormatException ex) {
                            ref = false;
                        }
                    } else if (ref) {
                        num.append(ps);
                    }

                } else if //page num parsing
                        (line.startsWith("<page ")) {
                    int start = line.indexOf("id=");
                    start = line.indexOf('\"', start);
                    int end = line.indexOf('\"', ++start);
                    page = Integer.parseInt(line.substring(start, end));
                    //   System.out.println(page);
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(ReferenceParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        fillRef(source);
        return refList;
    }

    void fillRef(File source) {

        try {

           BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(source),"UTF8"));
            String line;
            int page = 0;
            boolean ref = false;
            boolean refParse = false;
            StringBuilder num = null;
            StringBuilder text = null;
            int refNum = 0;
            int refCountSd = 0;

            while (br.ready()) {
                line = br.readLine();

                //detectinf text section
               // if (page >= refPage) {

                    if (line.startsWith("<text>") && refParse) {
                        text.append(" ");
                    } else if (line.startsWith("<text ")) {

                        int start = line.indexOf('>');
                        int end = line.indexOf('<', 2);
                        if (start > end) {
                            break;
                        }

                        String ps = line.substring(start + 1, end);

                        //checking for reference
                        if (wordFinder(ps)) {
                            refPage = page;
                            refCountSd++;
                        }
                        if (refCountSd == refCount) {
                            //parsing start of reference
                            if (ps.equals("[")) {
                                ref = true;
                                num = new StringBuilder();
                                if (text != null) {
                                   // System.out.println("["+refNum+"] "+text.toString());
                                    for (Reference rf : refList) {
                                        if (rf.getNumber() == refNum) {
                                            rf.setText(text.toString());
                                        }

                                    }
                                    refParse = false;
                                }
                            } else if (ps.equals("]") && ref) {
                                try {
                                    //     System.out.println("num= "+num.toString());
                                    refNum = Integer.parseInt(num.toString());
                                    refParse = true;
                                    text = new StringBuilder();
                                    ref = false;
                                } catch (NumberFormatException ex) {
                                    ref = false;
                                }
                            } else if (ref) {
                                num.append(ps);
                            } else if (refParse) {
                                text.append(ps);
                            }

                        //}
                    }
                } else if //page num parsing
                        (line.startsWith("<page ")) {
                    int start = line.indexOf("id=");
                    start = line.indexOf('\"', start);
                    int end = line.indexOf('\"', ++start);
                    page = Integer.parseInt(line.substring(start, end));
                    //   System.out.println(page);
                }

            }
            if (text != null) {
                for (Reference rf : refList) {
                    if (rf.getNumber() == refNum) {
                        rf.setText(text.toString());
                    }

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ReferenceParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }

    boolean wordFinder(String st) {

        char ch = st.charAt(0);

        if (ch < 97) {
            ch = (char) (ch + 32);
        }

        if (word.charAt(charPointer) == ch) {
            charPointer++;

            if (charPointer == wSize) {
                charPointer = 0;
                return true;

            }
        } else {

            charPointer = 0;
        }
        if (st.length() > 1) {

            return wordFinder(st.substring(1));
        }
        return false;
    }

}
