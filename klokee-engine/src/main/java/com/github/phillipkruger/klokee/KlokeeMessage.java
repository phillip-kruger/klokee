package com.github.phillipkruger.klokee;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The message that get distributed
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class KlokeeMessage implements Serializable{
    
    private String handler;
    private byte[] content;
    
}
