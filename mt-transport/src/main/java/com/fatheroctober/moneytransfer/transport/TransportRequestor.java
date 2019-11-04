package com.fatheroctober.moneytransfer.transport;

import com.fatheroctober.moneytransfer.message.TransportMessage;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.Optional;

public class TransportRequestor {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportRequestor.class);

    private Session session;
    private MessageProducer requestProducer;
    private MessageConsumer replyConsumer;
    private Destination replyQueue;

    public static TransportRequestor requestor(Connection connection,
                                               String requestQueueName,
                                               String replyQueueName) throws Exception {
        TransportRequestor producer = new TransportRequestor();
        producer.initialize(connection, requestQueueName, replyQueueName);
        return producer;
    }

    protected void initialize(Connection connection, String requestQueueName, String replyQueueName) throws JMSException {
        Preconditions.checkNotNull(connection);
        Preconditions.checkNotNull(requestQueueName);
        Preconditions.checkNotNull(replyQueueName);

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination producerQueue = session.createQueue(requestQueueName);
        requestProducer = session.createProducer(producerQueue);

        replyQueue = session.createQueue(replyQueueName);
        replyConsumer = session.createConsumer(replyQueue);
    }

    public void send(TransportMessage message) throws JMSException {
        Preconditions.checkNotNull(message);

        ObjectMessage objectMessage = session.createObjectMessage();
        objectMessage.setJMSCorrelationID(message.getMsgId());
        objectMessage.setObject(message);
        objectMessage.setJMSReplyTo(replyQueue);

        requestProducer.send(objectMessage);
        logMessage(objectMessage, true);
    }

    public TransportMessage receiveSync() throws JMSException {
        Message msg = replyConsumer.receive();
        if (msg instanceof ObjectMessage) {
            ObjectMessage replyMessage = (ObjectMessage) msg;
            logMessage(replyMessage, false);
            return Optional.ofNullable(replyMessage.getObject())
                    .map(TransportMessage.class::cast)
                    .orElseThrow(() -> new RuntimeException("Invalid message body"));

        } else throw new RuntimeException("Unknown transport message");
    }

    private void logMessage(ObjectMessage objectMessage, boolean isSend) throws JMSException {
        if (isSend) {
            LOGGER.info("\t" + Thread.currentThread().getId() + ": sent msg");
        } else {
            LOGGER.info("\t" + Thread.currentThread().getId() + ": receive msg");
        }
        LOGGER.info("\t" + Thread.currentThread().getId() + ": time:       " + System.currentTimeMillis() + " ms");
        LOGGER.info("\t" + Thread.currentThread().getId() + ": message ID: " + objectMessage.getJMSMessageID());
        LOGGER.info("\t" + Thread.currentThread().getId() + ": correl. ID: " + objectMessage.getJMSCorrelationID());
        LOGGER.info("\t" + Thread.currentThread().getId() + ": reply to:   " + objectMessage.getJMSReplyTo());
        LOGGER.info("\t" + Thread.currentThread().getId() + ": contents:   " + objectMessage.getObject().toString());
    }
}
