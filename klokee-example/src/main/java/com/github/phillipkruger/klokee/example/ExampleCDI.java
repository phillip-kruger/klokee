package com.github.phillipkruger.klokee.example;

import com.github.phillipkruger.klokee.KlokeeMessage;
import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import lombok.extern.java.Log;

/**
 * Example CDI Service
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Log
@RequestScoped
public class ExampleCDI {

    public void receiveMessage(@Observes KlokeeMessage klokeeMessage){
        log.log(Level.SEVERE, "Example CDI service just received a message {0} - {1}", new Object[]{klokeeMessage.getHandler(), new String(klokeeMessage.getContent())});
    }
}
