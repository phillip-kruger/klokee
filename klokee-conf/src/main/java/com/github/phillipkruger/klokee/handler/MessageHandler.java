package com.github.phillipkruger.klokee.handler;

import javax.validation.constraints.NotNull;

/**
 * Define the message handler
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
public interface MessageHandler {

    public String getName();
    
    public boolean messageExist();
    public byte[] getContent(); // TODO: Change to List<byte[]> to support multiple files
    
    public void delete(@NotNull String uri);
    public void hide(@NotNull String uri);
    public void backup(@NotNull String uri);
    
}
