package com.github.phillipkruger.klokee.transformer.csv;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * All the supported CSV formats. 
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Data @AllArgsConstructor @EqualsAndHashCode(exclude={"delimiter"})
public class Format implements Serializable {
    
    public static final char CHAR_PIPE = "|".charAt(0);
    public static final Format PIPE = new Format("PIPE", CHAR_PIPE);
    
    public static final char CHAR_COMMA = ",".charAt(0);
    public static final Format COMMA = new Format("COMMA", CHAR_COMMA);
    
    public static final char CHAR_SEMICOMMA = ";".charAt(0);
    public static final Format SEMICOMMA = new Format("SEMICOMMA", CHAR_SEMICOMMA);
    
    private String value;
    private Character delimiter;
    
    public static Format autoDetectFormat(byte[] csvFile){
    
        String contents = new String(csvFile);
        int noOfPipes = StringUtils.countMatches(contents, new String(new char[]{CHAR_PIPE}));
        int noOfCommas = StringUtils.countMatches(contents, new String(new char[]{CHAR_COMMA}));
        int noOfSemicommas = StringUtils.countMatches(contents, new String(new char[]{CHAR_SEMICOMMA}));
        
        if(noOfCommas>noOfPipes && noOfCommas>noOfSemicommas)return Format.COMMA;
        if(noOfPipes>noOfCommas && noOfPipes>noOfSemicommas)return Format.PIPE;
        if(noOfSemicommas>noOfCommas && noOfSemicommas>noOfPipes)return Format.SEMICOMMA;
        
        return Format.COMMA; // default
    }

    /**
     * Autodetects the format of a CSV file in a BufferedInputStream. The BufferedInputStream must have a buffer
     * size of at least 32768 bytes.
     * WARNING: as this method needs <code>mark()</code> and <code>reset()</code> to read ahead in the stream, any
     * existing mark will be replaced! Don't use this method if you need to preserve an existing mark.
     * @param bis
     * @return
     * @throws IOException
     */
//    public static Format autoDetectFormat(BufferedInputStream bis) throws IOException {
//        bis.mark(32768);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(bis), 1);
//        String line1 = reader.readLine();
//        String line2 = reader.readLine();
//        bis.reset();
//
//        // Detect the format from these two lines
//        byte[] lines = (line1 + '\n' + line2).getBytes();
//        return Format.autoDetectFormat(lines);
//    }

    /**
     * Autodetects the format of a CSV file in a BufferedReader. The BufferedReader must have a buffer
     * size of at least 32768 characters.
     * WARNING: as this method needs <code>mark()</code> and <code>reset()</code> to read ahead in the stream, any
     * existing mark will be replaced! Don't use this method if you need to preserve an existing mark.
     * @param reader
     * @return
     * @throws IOException
     */
//    public static Format autoDetectFormat(BufferedReader reader) throws IOException {
//        reader.mark(32768);
//        String line1 = reader.readLine();
//        String line2 = reader.readLine();
//
//        // Detect the format from these two lines
//        byte[] lines = (line1 + '\n' + line2).getBytes();
//        Format format = Format.autoDetectFormat(lines);
//        reader.reset();
//        return format;
//    }

}