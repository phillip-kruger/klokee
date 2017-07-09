package com.github.phillipkruger.klokee.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import lombok.extern.java.Log;

/**
 * Produce Klokee Properties
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Dependent
@Log
public class KlokeePropertiesReader {
    
    @Produces @KlokeeProperties
    public Properties provideKlokeeProperties(InjectionPoint ip) {
        String filename = ip.getAnnotated().getAnnotation(KlokeeProperties.class).value();
        return loadProperties(filename);
    }
    
    @Produces @KlokeeProperty
    public String produceString(final InjectionPoint ip) {
        String filename = ip.getAnnotated().getAnnotation(KlokeeProperty.class).value();
        Properties properties = loadProperties(filename);
        return properties.getProperty(getKey(ip));

    }
    
    @Produces @KlokeeProperty
    public int produceInt(final InjectionPoint ip) {
        String filename = ip.getAnnotated().getAnnotation(KlokeeProperty.class).value();
        Properties properties = loadProperties(filename);
        return Integer.valueOf(properties.getProperty(getKey(ip)));

    }

    @Produces @KlokeeProperty
    public boolean produceBoolean(final InjectionPoint ip) {
        String filename = ip.getAnnotated().getAnnotation(KlokeeProperty.class).value();
        Properties properties = loadProperties(filename);
        return Boolean.valueOf(properties.getProperty(getKey(ip)));

    }
    
    private Properties loadProperties(String filename){
        Properties properties = new Properties();
        try (InputStream propertiesStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)){
            if(propertiesStream!=null){
                properties.load(propertiesStream);
            }else{
                log.log(Level.FINEST, "Can not load properties [{0}]", filename);
            }
        } catch (NullPointerException | IOException ex) {
            log.log(Level.FINEST, "Can not load properties [{0}] - {1}", new Object[]{filename, ex.getMessage()});
        }
        
        return properties;
    }
    
    private String getKey(final InjectionPoint ip) {

        return (ip.getAnnotated()
                .isAnnotationPresent(KlokeeProperty.class) && !ip.getAnnotated().getAnnotation(KlokeeProperty.class).key().isEmpty()) ? ip.getAnnotated().getAnnotation(KlokeeProperty.class).key() : ip.getMember().getName();
    }
}
