package com.fatheroctober.moneytransfer.transport;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import java.net.URI;

public class TransportBroker {

    public static void main(String[] args) throws  Exception {
        int port = 61616;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        BrokerService broker = BrokerFactory.createBroker(new URI(
                "broker:(tcp://localhost:" + port + ")"));
        broker.start();
    }
}
