package cz.muni.fi.pa165.jmsexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

@SpringBootApplication
@EnableJms
public class Main {

    public static void main(String[] args) throws JMSException {
        // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        // Send a message
        MessageCreator helloMessageCreator = (Session session) -> session.createTextMessage("Hello");
        MessageCreator worldMessageCreator = (Session session) -> session.createTextMessage("World!");

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        System.out.println("Sending the hello message.");
        jmsTemplate.send("hello-destination", helloMessageCreator);
        System.out.println("Sending the world message.");
        jmsTemplate.send("world-destination", worldMessageCreator);

    }
}
