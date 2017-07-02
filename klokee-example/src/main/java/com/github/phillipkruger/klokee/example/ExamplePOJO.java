package com.github.phillipkruger.klokee.example;

import com.github.phillipkruger.klokee.KlokeeMessage;
import java.util.logging.Level;
import javax.enterprise.event.Observes;
import lombok.extern.java.Log;

/**
 * Example POJO
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Log
public class ExamplePOJO {

    public void receiveMessage(@Observes KlokeeMessage klokeeMessage){
        log.log(Level.SEVERE, "Example POJO just received a message [{0}]", new String(klokeeMessage.getContent()));
    }
}
