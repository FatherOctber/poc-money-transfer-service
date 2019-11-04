package com.fatheroctober.moneytransfer.transport;

import com.fatheroctober.moneytransfer.message.TransportMessage;
import com.fatheroctober.moneytransfer.transport.util.ActiveMqTransport;
import com.fatheroctober.moneytransfer.transport.util.ITransport;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.*;

import javax.jms.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class WorkflowTest {
    private ITransport transport;
    private AtomicInteger messageCount = new AtomicInteger();

    @Rule
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

    @Before
    public void setUp() throws Exception {
        transport = new ActiveMqTransport(broker.getVmURL());
        messageCount.set(0);
    }

    @After
    public void tearDown() throws Exception {
        if (broker != null) {
            broker.stop();
        }
    }

    @Test
    public void testSingularFlow() throws Exception {
        TransportMessage testMessage = createMsg("123", "Hello");
        Connection connection = transport.connection();
        connection.start();

        TransportRequestor requestor = TransportRequestor.requestor(connection, "testQ", "testR");
        TransportReplier.replier(connection, "testQ", new TestProcessStrategy(
                actualMessage -> Assert.assertEquals(testMessage, actualMessage)));

        requestor.send(testMessage);
        TransportMessage response = requestor.receiveSync();

        Assert.assertEquals(testMessage, response);
        connection.close();
    }

    @Test
    public void testMultipleActorsFlow() throws Exception {
        final int messages = 5;
        Connection connection = transport.connection();
        connection.start();

        ExecutorService repliers = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            repliers.execute(() -> {
                try {
                    TransportReplier.replier(connection, "testQ", new TestProcessStrategy(
                            actualMessage -> {
                                Assert.assertEquals("test test", actualMessage.getBody());
                                messageCount.incrementAndGet();
                            }));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        ExecutorService requestors = Executors.newFixedThreadPool(messages);
        for (int i = 0; i < messages; i++) {
            final int correlationId = i;
            requestors.execute(() -> {
                try {
                    TransportMessage testMsg = createMsg("id" + correlationId, "test test");
                    TransportRequestor requestor = TransportRequestor.requestor(connection, "testQ", "test" + correlationId);
                    requestor.send(testMsg);
                    TransportMessage response = requestor.receiveSync();
                    Assert.assertEquals(testMsg, response);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if (requestors.awaitTermination(5, TimeUnit.SECONDS)) {
            repliers.shutdown();
        }
        Assert.assertEquals(messages, messageCount.get());

        connection.close();
    }

    private static TransportMessage createMsg(String id, String text) {
        return new TransportMessage<>(id, text);
    }


}
