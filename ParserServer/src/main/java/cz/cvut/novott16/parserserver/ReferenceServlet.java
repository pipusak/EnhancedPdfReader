/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.novott16.parserserver;

import cz.cvut.novott16.HeadlineParser.StyleParser;
import cz.cvut.novott16.freecite.FreeCiteClient;
import cz.cvut.novott16.model.OneChar;
import cz.cvut.novott16.model.Reference;
import cz.cvut.novott16.referenceexctractor.PDFMinerController;
import cz.cvut.novott16.referenceexctractor.ReferenceExctractor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import parser.Parser;

/**
 *
 * @author martin
 */
@WebServlet(name = "GetReferences", value = "/getref")
@MultipartConfig
public class ReferenceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
//page: 2, coords: [449.082, 634.25, 460.699, 646.255], text: "adased"
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
         String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
    Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
   // String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
    InputStream fileContent = filePart.getInputStream();
    
           int rand =(int)( Math.random()*1000000000);
           String sourcePath = "C:\\tmp\\tmp_"+rand+".pdf";

           final Path destination = Paths.get(sourcePath);

   File file = destination.toFile();
   if(file.exists()){
   file.delete();
   }
    Files.copy(fileContent, destination);

            
           String path= PDFMinerController.pdf2text(sourcePath);
            
                    
        File source = new File(path);
        System.out.println("XML file is readable "+source.exists());
       List<Reference> refList= ReferenceExctractor.getReferences(source);
       Parser p = new Parser();
       StyleParser hp = new StyleParser();
        ArrayList<OneChar> text = p.parse(source);
       String headline=  hp.parseHeadline(text);
              //response.setContentType("text/xml");
              StringBuilder sb =new StringBuilder();
              sb.append(headline).append("\n");
              System.out.println("Reference list size "+refList.size());
              for(Reference ref:refList ){
                  System.out.println("Reference "+ref.toString());
            sb.append(ref.toString()).append("\n");
              
            
              }
              response.getWriter().println(sb.toString());
	
 
   if(file.exists()){
   file.delete();
       System.out.println("PDF file deleted");
   }
        System.out.println("Source exist "+source.exists());
   if(source.exists()){
   source.delete();
       System.out.println("Source File deleted");
   }
   
        }
}
