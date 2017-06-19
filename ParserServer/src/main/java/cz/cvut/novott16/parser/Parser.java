/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import cz.cvut.novott16.model.OneChar;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 *
 * @author TomasNovotny
 */
public class Parser {

    public ArrayList<OneChar> parse(File source) {
        ArrayList<OneChar> charackters = new ArrayList<>();
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(source),"UTF8"));
            String line;
            int page = 0;
            float lbx = 0;
            float lby = 0;
            float urx = 0;
            float ury = 0;
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

                    start = line.indexOf("bbox=");
                    start = line.indexOf('\"', start);
                    end = line.indexOf('\"', ++start);
                    String[] coords = line.substring(start, end).split(",");
                    lbx = Float.parseFloat(coords[0]);
                    lby = Float.parseFloat(coords[1]);
                    urx = Float.parseFloat(coords[2]);
                    ury = Float.parseFloat(coords[3]);

                    start = line.indexOf("font=\"");
                    if(line.contains("+")){
                    start = line.indexOf('+', start);}else{
                     start =   line.indexOf("\"");
                    }
                    String font = line.substring(++start, line.indexOf("\"", ++start));
                    start = line.indexOf("size=\"");
                    start = line.indexOf('\"', start);

                     int size = (int)Math.round(Double.parseDouble(line.substring(++start, line.indexOf("\"",++start)-1)));
                  
                     charackters.add(new OneChar(ps, lbx, lby, urx, ury, page,font,size));

                } else if //page num parsing
                        (line.startsWith("<page ")) {
                    int start = line.indexOf("id=");
                    start = line.indexOf('\"', start);
                    int end = line.indexOf('\"', ++start);
                    page = Integer.parseInt(line.substring(start, end));
                } else if (line.endsWith("<text> </text>")) {
                    charackters.add(new OneChar(" ", lbx, lby, urx, ury, page, null, -1));
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return charackters;
    }
}
