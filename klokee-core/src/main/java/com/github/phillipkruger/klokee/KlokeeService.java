package com.github.phillipkruger.klokee;

import com.github.phillipkruger.klokee.handler.MessageHandler;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import lombok.extern.java.Log;

/**
 * The Klokee service. This actually do the work
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Log
@Stateless
public class KlokeeService {

    @EJB
    private MessageHandler messageHandler;
    @EJB
    private Distributor distributor;
    
    @Asynchronous
    public void checkMessage(){
        if(messageHandler!=null){
            // Check if there is a file / message
            if(messageHandler.messageExist()){
                // Get the file contents
                byte[] content = messageHandler.getContent();
                // TODO: Unzip
                // TODO: Make XML (or Json ?)
                // TODO: Transform
                // Distribute
                distributor.distributeMessage(content);
                // TODO: Clean up
            }else{
                log.severe("No input exist");
            }
        }else{
            log.severe("Message handler not included. Update your pom to include at least once handler");
        }
        
    }
    
}
