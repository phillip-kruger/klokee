package com.github.phillipkruger.klokee.example;

import com.github.phillipkruger.klokee.KlokeeMessage;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import lombok.extern.java.Log;

/**
 * Some example EJB service
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Log
@Stateless
public class ExampleEJB {

    public void receiveMessage(@Observes KlokeeMessage klokeeMessage){
        log.log(Level.SEVERE, "Example EJB service just received a message [{0}]", new String(klokeeMessage.getContent()));
    }
}
