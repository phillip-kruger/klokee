package com.github.phillipkruger.klokee.handler;

/**
 * Define the message handler
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
public interface MessageHandler {

    public boolean messageExist();
    public byte[] getContent(); // TODO: Change to List<byte[]> to support multiple files
    public void cleanup();
}
