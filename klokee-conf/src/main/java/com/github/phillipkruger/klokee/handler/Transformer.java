package com.github.phillipkruger.klokee.handler;

/**
 * Define a transformer
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
public interface Transformer {

    public String getName();
    public byte[] transform(byte[] input);
}
