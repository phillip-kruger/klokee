package com.github.phillipkruger.klokee.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * Properties as defined by klokee user
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Log
@ApplicationScoped
public class KlokeeProperties {

    @Getter
    private final Properties properties = new Properties();
    private final String PROPERTIES_FILE_NAME = "klokee.properties";
    
    @PostConstruct
    private void init(){
        // Properties
        try (InputStream propertiesStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)){
            if(propertiesStream!=null){
                properties.load(propertiesStream);
            }else{
                log.log(Level.FINEST, "Can not load properties [klokee.properties]");
            }
        } catch (NullPointerException | IOException ex) {
            log.log(Level.FINEST, "Can not load properties [klokee.properties] - {0}", ex.getMessage());
        }
    }
}
