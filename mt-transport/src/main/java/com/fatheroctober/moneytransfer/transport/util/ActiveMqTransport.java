package com.fatheroctober.moneytransfer.transport.util;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;

public class ActiveMqTransport implements ITransport{
    private ConnectionFactory connectionFactory;

    public ActiveMqTransport(String url) {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(url);
        activeMQConnectionFactory.setTrustAllPackages(true);
        connectionFactory = activeMQConnectionFactory;

    }

    public Connection connection() throws Exception {
        Connection connection = connectionFactory.createConnection();
        ((ActiveMQConnection) connection).setUseAsyncSend(true);

        return connection;
    }
}
