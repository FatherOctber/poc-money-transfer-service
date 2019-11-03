package com.fatheroctober.moneytransfer.api.service;

import com.fatheroctober.moneytransfer.api.MoneyTransferApi;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseTest {
    private static HttpServer server;

    @BeforeClass
    public static void setup() throws Exception {
        ResourceConfig rc = ResourceConfig.forApplication(new MoneyTransferApi());
        URI uri = UriBuilder.fromUri("http://localhost/").port(8080).build();
        server = GrizzlyHttpServerFactory.createHttpServer(uri, rc);

        Logger logger = Logger.getLogger("org.glassfish.grizzly.http.server.HttpHandler");
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        logger.addHandler(ch);
    }

    @AfterClass
    public static void shutdown() throws Exception {
        server.shutdownNow();
    }
}
