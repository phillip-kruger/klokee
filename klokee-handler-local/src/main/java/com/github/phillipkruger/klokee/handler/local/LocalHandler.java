package com.github.phillipkruger.klokee.handler.local;

import com.github.phillipkruger.klokee.handler.KlokeeProperties;
import com.github.phillipkruger.klokee.handler.MessageHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import lombok.extern.java.Log;

/**
 * Handle local files
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 * TODO: Support regex
 */
@Log
@Stateless
@Local(value = MessageHandler.class)
public class LocalHandler implements MessageHandler {
    public static final String NAME = "Local";

    @Inject
    private KlokeeProperties klokeeProperties;
    
    @Override
    public boolean messageExist() {
        String uri = klokeeProperties.getProperties().getProperty(URI,null);
        if(uri!=null){        
            return Files.exists(Paths.get(uri));
        }
        return false;
    }

    @Override
    public byte[] getContent() {
        if(messageExist()){
            String uri = klokeeProperties.getProperties().getProperty(URI,null);
            if(uri!=null){        
                try {
                    byte[] b = Files.readAllBytes(Paths.get(uri));
                    return b;
                } catch (IOException ex) {
                    log.severe(ex.getMessage());
                }
            }
        }
        return null;
    }
    
    @Override
    public void cleanup() {
        if(messageExist()){
            String uri = klokeeProperties.getProperties().getProperty(URI,null);
            if(uri!=null){
                String cleanup = klokeeProperties.getProperties().getProperty(CLEANUP,null);
                if(cleanup!=null && cleanup.equalsIgnoreCase(DELETE)){
                    try {
                        Files.delete(Paths.get(uri));
                    } catch (IOException ex) {
                        log.log(Level.SEVERE, "Can not delete [{0}] - {1}", new Object[]{uri, ex.getMessage()});
                    }
                }else if(cleanup!=null && cleanup.equalsIgnoreCase(HIDE)){
                    // TODO: Files.move(path, path1, cos)
                }
                
            }
        }
    }
    
    private static final String URI = "uri";
    private static final String CLEANUP = "cleanup";
    private static final String DELETE = "delete";
    private static final String HIDE = "hide";
    
    
    
}