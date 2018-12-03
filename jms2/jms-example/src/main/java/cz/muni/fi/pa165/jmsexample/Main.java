package cz.muni.fi.pa165.jmsexample;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main {

    static class Consumer implements Runnable {

        @Override
        public void run() {
            try {
                InitialContext jndi = new InitialContext();
                ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup("connectionFactory");
                Connection connection = connectionFactory.createConnection();
                connection.start();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = (Destination) jndi.lookup("Task2.queue");
                MessageConsumer consumer = session.createConsumer(destination);
                Message message = consumer.receive();

                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("Received: " + text);
                }

                consumer.close();
                session.close();
                connection.close();

            } catch (JMSException ex) {
                System.err.println("Problem with JMS: " + ex);
            } catch (NamingException ex) {
                System.err.println("Problem with JDNI: " + ex);
            }

        }

    }

    static class Producer implements Runnable {

        @Override
        public void run() {
            try {
                InitialContext jndi = new InitialContext();
                ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup("connectionFactory");
                Connection connection = connectionFactory.createConnection();
                connection.start();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = (Destination) jndi.lookup("Task2.queue");
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                String text = "Hello world123!";
                TextMessage message = session.createTextMessage(text);
                System.out.println("Sent message: " + message.getText());
                producer.send(message);
                session.close();
                connection.close();
                session.close();
                connection.close();
            } catch (JMSException ex) {
                System.err.println("Problem with JMS: " + ex);
            } catch (NamingException ex) {
                System.err.println("Problem with JDNI: " + ex);
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
