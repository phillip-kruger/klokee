package com.github.phillipkruger.klokee.transformer.compress;

import com.github.phillipkruger.klokee.handler.KlokeeProperties;
import com.github.phillipkruger.klokee.handler.Transformer;
import java.util.Properties;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.java.Log;

/**
 * Transformer that can uncompress a message
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Log
@Named("compress")
public class CompressTransformer implements Transformer {
    private static final String NAME = "compress";
    
    @Inject @KlokeeProperties
    protected Properties properties;
    
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public byte[] transform(byte[] input) {
        log.severe(">> Now transforming with " + NAME);
        return input;
    }
    
}