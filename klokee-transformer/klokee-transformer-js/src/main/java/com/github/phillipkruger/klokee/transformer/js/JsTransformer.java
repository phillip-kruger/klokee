package com.github.phillipkruger.klokee.transformer.js;

import com.github.phillipkruger.klokee.handler.KlokeeProperties;
import com.github.phillipkruger.klokee.handler.Transformer;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.inject.Named;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.transform.TransformerException;
import lombok.extern.java.Log;

/**
 * Transformer that can push a message through a Javascript
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Log
@Named("javascript")
public class JsTransformer implements Transformer {
    private static final String NAME = "javascript";

    @Inject @KlokeeProperties
    protected Properties properties;
    
    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public byte[] transform(byte[] input) {
        
        if(input!=null && input.length>0){
            String uri = properties.getProperty(JS,null);
            if(uri!=null){
                try (InputStream jsStream = new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(uri))){
                    log.log(Level.SEVERE,"Now transforming with [" + NAME + "] using [{0}]", uri);
                    return transform(input, jsStream);
                } catch (IOException | TransformerException ex) {
                     log.log(Level.SEVERE, null, ex);
                }
            }
        }
        return input;
    }
    
    private byte[] transform(byte[] i, InputStream jsStream) throws TransformerException, IOException {
        String script = streamToString(jsStream);
        
        try {
            engine.eval(script);
            Invocable invocable = (Invocable) engine;
            if(i==null || i.length==0){
                return (byte[])invocable.invokeFunction(METHOD_NAME);
            }else{
                return (byte[])invocable.invokeFunction(METHOD_NAME, i);
            }
        } catch (ScriptException | NoSuchMethodException ex) {
            log.severe(script);
            log.log(Level.SEVERE, "Could not run script: {0} - returning all keys", ex.getMessage());
        }
        return null;
        
        
    }
    
    private String streamToString(InputStream inputStream) throws IOException{
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
 
        buffer.flush();
        byte[] byteArray = buffer.toByteArray();
         
        return new String(byteArray, StandardCharsets.UTF_8);
    }
    
    private static final String METHOD_NAME = "transform";
    private static final String JS = "jsUri";
    private static final String SCRIPT_ENGINE_NAME = "nashorn";
    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName(SCRIPT_ENGINE_NAME);
}