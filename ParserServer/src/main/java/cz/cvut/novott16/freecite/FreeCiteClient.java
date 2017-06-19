/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.novott16.freecite;

import cz.cvut.novott16.freecite.Citations.Citation;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author TomasNovotny
 */
public class FreeCiteClient {

   public static List<Citation> parse(String[] cits)  {

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httppost = new HttpPost("http://freecite.library.brown.edu/citations/create");
            httppost.addHeader("Accept", "text/xml");
            List<NameValuePair> formparams = new ArrayList();
            if(cits.length>1){
            for(String cit:cits){
            formparams.add(new BasicNameValuePair("citation[]", cit));
                System.out.println("add "+cit);
            }
            }else{
                formparams.add(new BasicNameValuePair("citation", cits[0]));
                 System.out.println("add "+cits[0]);
            
        }
            
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
            
            
            httppost.setEntity(entity);



            System.out.println("Executing request: " + httppost.getRequestLine());
            try (CloseableHttpResponse response = httpclient.execute(httppost)) {
               System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                String res = EntityUtils.toString(response.getEntity());
                res=res.replaceAll("\\x0c", " ");
                
               System.out.println(res);
               return unmarshal(res);
            }
        }catch(Exception e){
            System.err.println(e);
        }
            return new ArrayList();
        }
    

    
    public static List<Citation> unmarshal(String file ) throws JAXBException{
    
                    JAXBContext jaxbContext = JAXBContext.newInstance(Citations.class);    
         
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();    
            Citations e=(Citations) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(file.getBytes()));
            List l =e.getCitationOrContextObjects();
            ArrayList<Citation> cit = new ArrayList();
            for(Object obj :l){
            if(obj instanceof Citation){
                
                cit.add((Citation) obj);

            }
            }
            return cit;
    }
}

