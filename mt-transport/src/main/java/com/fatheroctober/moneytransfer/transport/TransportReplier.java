package com.fatheroctober.moneytransfer.transport;

import com.fatheroctober.moneytransfer.message.TransportMessage;
import com.fatheroctober.moneytransfer.transport.util.ProcessStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.Optional;

public class TransportReplier implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportReplier.class);

    private Session session;
    private ProcessStrategy processStrategy;

    public static TransportReplier replier(Connection connection, String requestQueueName, ProcessStrategy strategy) throws Exception {

        TransportReplier replier = new TransportReplier();
        replier.initialize(connection, requestQueueName);
        replier.processStrategy = strategy;
        return replier;
    }

    protected void initialize(Connection connection, String requestQueueName) throws Exception {
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination requestQueue = session.createQueue(requestQueueName);

        MessageConsumer requestConsumer = session.createConsumer(requestQueue);
        requestConsumer.setMessageListener(this);
    }


    public void onMessage(Message message) {
        try {
            if ((message instanceof ObjectMessage) && (message.getJMSReplyTo() != null)) {
                ObjectMessage requestMessage = (ObjectMessage) message;

                logMessage(requestMessage, "Received request");

                TransportMessage msg = Optional.ofNullable(requestMessage.getObject())
                        .map(TransportMessage.class::cast)
                        .orElseThrow(() -> new RuntimeException("Invalid message body"));

                Destination replyDestination = message.getJMSReplyTo();
                MessageProducer replyProducer = session.createProducer(replyDestination);

                //sync processing
                TransportMessage response = processStrategy.process(msg);

                logMessage(requestMessage, "Sent reply");
                ObjectMessage replyMessage = session.createObjectMessage();
                replyMessage.setObject(response);
                replyMessage.setJMSCorrelationID(requestMessage.getJMSCorrelationID());
                replyProducer.send(replyMessage);
            } else {
                throw new RuntimeException("Invalid message detected");
            }
        } catch (Exception e) {
            LOGGER.error("\t" + Thread.currentThread().getId() + ": failed on message listen");
            e.printStackTrace();
            sendErrorResponse(message, e);

        }
    }

    private void sendErrorResponse(Message message, Exception e) {
        try {
            Destination replyDestination = message.getJMSReplyTo();
            MessageProducer replyProducer = session.createProducer(replyDestination);
            ObjectMessage replyMessage = session.createObjectMessage();
            replyMessage.setObject(TransportMessage.error(message.getJMSCorrelationID(), e.getMessage()));
            replyMessage.setJMSCorrelationID(message.getJMSCorrelationID());
            replyProducer.send(replyMessage);
        } catch (Exception ex) {
            LOGGER.error("\t" + Thread.currentThread().getId() + ": failed on error msg creation");
            ex.printStackTrace();
        }
    }

    private void logMessage(ObjectMessage objectMessage, String heder) throws JMSException {
        LOGGER.info("\t" + Thread.currentThread().getId() + ": " + heder);
        LOGGER.info("\t" + Thread.currentThread().getId() + ": time:       " + System.currentTimeMillis() + " ms");
        LOGGER.info("\t" + Thread.currentThread().getId() + ": message ID: " + objectMessage.getJMSMessageID());
        LOGGER.info("\t" + Thread.currentThread().getId() + ": correl. ID: " + objectMessage.getJMSCorrelationID());
        LOGGER.info("\t" + Thread.currentThread().getId() + ": reply to:   " + objectMessage.getJMSReplyTo());
        LOGGER.info("\t" + Thread.currentThread().getId() + ": contents:   " + objectMessage.getObject().toString());
    }
}
