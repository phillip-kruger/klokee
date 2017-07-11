package com.github.phillipkruger.klokee.transformer.compress;

import com.github.phillipkruger.klokee.handler.KlokeeProperties;
import com.github.phillipkruger.klokee.handler.Transformer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.java.Log;

/**
 * Transformer that can uncompress a message
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 * TODO: support multiple entries in zip 
 */
@Log
@Named("unzip")
public class UnzipTransformer implements Transformer {
    private static final String NAME = "unzip";
    
    @Inject @KlokeeProperties
    protected Properties properties;
    
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public byte[] transform(byte[] input) {
        
        if(input!=null && input.length>0){
            try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(input))){
                ZipEntry ze = zis.getNextEntry();
                if(ze==null)return input;
                while(ze!=null){
                    try {
                        if(!ze.isDirectory()){
                            // Here we get the first content
                            byte[] content = toByteArray(zis);
                            return content;
                        }
                        ze = zis.getNextEntry();
                    } catch (IOException ex) {
                        // TODO: Log
                        return input;
                    }
                }
            } catch (IOException io){
                // TODO: Log
                return input;
            }
        }
        
        return input;
    }
    
    
    private byte[] toByteArray(ZipInputStream zis) throws IOException {
        byte[] buffer = new byte[1024];
        
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int len;
            while ((len = zis.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();

        }
    }
}