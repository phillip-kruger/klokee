package com.github.phillipkruger.klokee.handler.ftp;

import com.github.phillipkruger.klokee.handler.KlokeeProperties;
import com.github.phillipkruger.klokee.handler.MessageHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import lombok.extern.java.Log;

/**
 * Handle ftp files
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 * TODO: Support regex
 */
@Log
@Named("ftp")
public class FtpHandler implements MessageHandler {
    private static final String NAME = "ftp";
    
    @Inject @KlokeeProperties
    protected Properties properties;
    
    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public boolean messageExist() {
        String uri = properties.getProperty(URI,null);
        if(uri!=null){        
            return Files.exists(Paths.get(uri));
        }
        return false;
    }

    @Override
    public byte[] getContent() {
        if(messageExist()){
            String uri = properties.getProperty(URI,null);
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
    public void delete(@NotNull String uri){
        try {
            Files.delete(Paths.get(uri));
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Can not delete [{0}] - {1}", new Object[]{uri, ex.getMessage()});
        }
    }
    
    @Override
    public void hide(@NotNull String uri){
        Path sourcePath = Paths.get(uri);
        String filename = sourcePath.getFileName().toString();
        Path destinationPath = Paths.get(sourcePath.getParent().toString(),DOT + filename);
        move(sourcePath,destinationPath);
    }
    
    @Override
    public void backup(@NotNull String uri){
        Path sourcePath = Paths.get(uri);
        String filename = sourcePath.getFileName().toString();
        SimpleDateFormat sdf = new SimpleDateFormat(BACKUP_DATE_FORMAT);
        String dateString = sdf.format(new Date());
        Path destinationPath = Paths.get(sourcePath.getParent().toString(), filename + DOT + dateString);
        move(sourcePath,destinationPath);
    }
    
    private void move(@NotNull Path sourcePath,@NotNull Path destinationPath){
        try {
            Files.move(sourcePath, destinationPath,StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Can not move [{0}] - {1}", new Object[]{sourcePath.toString(), ex.getMessage()});
        }
    }
    
    private static final String URI = "uri";
    private static final String DOT = ".";
    private static final String BACKUP_DATE_FORMAT = "yyyy_MM_dd_HH_mm_ss_SSS";

}