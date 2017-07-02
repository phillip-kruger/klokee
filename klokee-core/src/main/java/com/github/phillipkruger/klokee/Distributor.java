package com.github.phillipkruger.klokee;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import lombok.extern.java.Log;

/**
 * Sending the message on
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Log
@Stateless
public class Distributor {

    @Inject
    private Event<KlokeeMessage> broadcaster;
    
    public void distributeMessage(byte[] content) {
        broadcaster.fire(new KlokeeMessage(content));
    }
}