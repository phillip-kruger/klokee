package com.github.phillipkruger.klokee;

import com.github.phillipkruger.klokee.handler.KlokeeProperties;
import com.github.phillipkruger.klokee.handler.MessageHandler;
import java.util.Properties;
import javax.ejb.Asynchronous;
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

    @Inject
    private MessageHandler messageHandler;
    
    @Inject
    private Event<KlokeeMessage> broadcaster;
    
    @Inject @KlokeeProperties
    protected Properties properties;
    
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
                cleanup();
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
    
    private void cleanup() {
        String uri = properties.getProperty(URI,null);
        if(uri!=null){
            String cleanup = properties.getProperty(CLEANUP,null);
            if(cleanup!=null && cleanup.equalsIgnoreCase(DELETE)){
                messageHandler.delete(uri);
            }else if(cleanup!=null && cleanup.equalsIgnoreCase(HIDE)){
                messageHandler.hide(uri);
            }else if(cleanup!=null && cleanup.equalsIgnoreCase(BACKUP)){
                messageHandler.backup(uri); 
            }
        }
        
    }
    
    private static final String URI = "uri";
    private static final String CLEANUP = "cleanup";
    private static final String DELETE = "delete";
    private static final String HIDE = "hide";
    private static final String BACKUP = "backup";
}
