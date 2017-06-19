package cz.cvut.novott16.swarm;


import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSFraction;
import org.wildfly.swarm.request.controller.RequestControllerFraction;
import org.wildfly.swarm.undertow.UndertowFraction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author TomasNovotny
 */
public class ParserServerApp {
    
    
     public static void main(String... args) throws Exception {
        ClassLoader classLoader = ParserServerApp.class.getClassLoader();
        
        Swarm swarm = new Swarm();

        swarm

                .fraction(jaxrsFraction())
                .fraction(undertowFraction())
                .fraction(requestControllerFraction())
                .start();

        swarm.deploy();
    }
     
         private static JAXRSFraction jaxrsFraction() {
        return new JAXRSFraction();
    }

    private static UndertowFraction undertowFraction() {
        return UndertowFraction.createDefaultFraction();
    }

    private static RequestControllerFraction requestControllerFraction() {
        return RequestControllerFraction.createDefaultFraction();
    }
}
