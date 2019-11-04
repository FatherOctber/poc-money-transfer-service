package com.fatheroctober.moneytransfer.transport;

import com.fatheroctober.moneytransfer.message.TransportMessage;
import com.fatheroctober.moneytransfer.transport.util.ProcessStrategy;

import java.util.function.Consumer;

public class TestProcessStrategy implements ProcessStrategy {
    private final Consumer<TransportMessage> assertConsumer;

    public TestProcessStrategy(Consumer<TransportMessage> assertConsumer) {
        this.assertConsumer = assertConsumer;
    }

    @Override
    public TransportMessage process(TransportMessage incomingRq) {
        assertConsumer.accept(incomingRq);
        throttle();
        return incomingRq;
    }

    private static void throttle() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
