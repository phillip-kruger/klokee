package com.github.phillipkruger.klokee.handler.local;

import com.github.phillipkruger.klokee.handler.KlokeeProperties;
import com.github.phillipkruger.klokee.handler.MessageHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    
    private static final String URI = "uri";
    
}