package cz.muni.fi.pa165.jmsexample;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class HelloReceiver {

    @JmsListener(destination = "hello-destination")
    public void receiveMessage(String text) {
        System.out.println("Received <" + text + ">");
    }

}