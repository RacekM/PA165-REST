package cz.muni.fi.pa165.jmsexample;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Main {

    private static final String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    static class Consumer implements Runnable {

        @Override
        public void run() {
            try {
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

                Connection connection = connectionFactory.createConnection();
                connection.start();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                Destination destination = session.createQueue("TASK1.HELLOWORLDQUEUE");

                MessageConsumer consumer = session.createConsumer(destination);

                Message message = consumer.receive(1000);

                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("Received: " + text);
                } else {
                    System.out.println("Received: " + message);
                }

                consumer.close();
                session.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }


            //TODO: Obtain connection factory from ActiveMQ
            //TODO: Obtain and start connection from factory
            //TODO: Obtain session from connection
            //TODO: Create consumer and receive one message
            //TODO: Print message to System.out
        }

    }

    static class Producer implements Runnable {

        @Override
        public void run() {
            try {
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
                Connection connection = connectionFactory.createConnection();
                connection.start();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = session.createQueue("TASK1.HELLOWORLDQUEUE");
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                
                String text = "Hello world!";
                TextMessage message = session.createTextMessage(text);
                System.out.println("Sent message: " + message.getText());
                
                producer.send(message);
                
                connection.close();
                session.close();
                connection.close();
            } catch (JMSException ex) {
                System.err.println("Problem with JMS: " + ex);
            }
        }

    }

    public static void main(String[] args) {

        Thread t1 = new Thread(new Consumer());
        Thread t2 = new Thread(new Producer());

        t1.start();
        t2.start();
    }
}
