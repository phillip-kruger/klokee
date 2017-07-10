package com.github.phillipkruger.klokee;

import com.github.phillipkruger.klokee.handler.KlokeeProperties;
import com.github.phillipkruger.klokee.handler.MessageHandler;
import java.util.Properties;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import lombok.extern.java.Log;

/**
 * The Klokee service. This actually do the work
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Log
@Stateless
public class KlokeeService {

    private MessageHandler messageHandler;
    
    @Inject
    private Event<KlokeeMessage> broadcaster;
    
    @Inject @KlokeeProperties
    protected Properties properties;
    
    @PostConstruct
    public void init(){
        String handlerName = properties.getProperty(HANDLER,null);
        if(handlerName!=null){
            messageHandler = CDI.current().select(MessageHandler.class, new NamedAnnotation(handlerName)).get();
        }else{
            log.severe("Message handler not included. Update your pom to include at least one handler and define the handler property");
        }
    }
    
    @Asynchronous
    public void checkMessage(){
        if(messageHandler!=null){
            String handlerName = messageHandler.getName();
            // Check if there is a file / message
            if(messageHandler.messageExist()){
                // Get the file contents
                byte[] content = messageHandler.getContent();
                // TODO: Unzip
                // TODO: Make XML (or Json ?)
                // TODO: Transform
                // Distribute
                
                distributeMessage(handlerName,content);
                cleanup();
            }else{
                log.log(Level.WARNING, "No input exist for [{0}]", handlerName);
            }
        }else{
            log.severe("Message handler not included. Update your pom to include at least one handler and define the handler property");
        }
        
    }
    
    @PreDestroy
    public void shutdown(){
        String handlerName = properties.getProperty(HANDLER,null);
        if(handlerName!=null){
            CDI.current().select(MessageHandler.class, new NamedAnnotation(handlerName)).destroy(messageHandler);
        }else{
            log.severe("Message handler not included. Update your pom to include at least one handler and define the handler property");
        }
    }
    
    private void distributeMessage(String handlerName, byte[] content) {
        broadcaster.fire(new KlokeeMessage(handlerName,content));
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
    private static final String HANDLER = "handler";
}
