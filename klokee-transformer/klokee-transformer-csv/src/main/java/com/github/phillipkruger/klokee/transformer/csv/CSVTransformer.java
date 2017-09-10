package com.github.phillipkruger.klokee.transformer.csv;

import com.github.phillipkruger.klokee.handler.KlokeeProperties;
import com.github.phillipkruger.klokee.handler.Transformer;
import java.util.Properties;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.java.Log;

/**
 * Transformer that can represent a csv message as an XML message
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Log
@Named("csv")
public class CSVTransformer implements Transformer {
    private static final String NAME = "csv";
    
    @Inject @KlokeeProperties
    protected Properties properties;
    
    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public byte[] transform(byte[] input) {
        
        if(input!=null && input.length>0){
            
        }
        
        return input;
    }
    
}