package com.github.phillipkruger.klokee;

import com.github.phillipkruger.klokee.handler.MessageHandler;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
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
    
    @Inject
    private Event<KlokeeMessage> broadcaster;
    
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
                distributeMessage(content);
                // TODO: Clean up
                cleanUp();
            }else{
                log.severe("No input exist");
            }
        }else{
            log.severe("Message handler not included. Update your pom to include at least once handler");
        }
        
    }
    
    private void distributeMessage(byte[] content) {
        broadcaster.fire(new KlokeeMessage(content));
    }
    
    private void cleanUp(){
        messageHandler.cleanup();
    }
}
