package com.fatheroctober.moneytransfer.api;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainApplication {

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost/").port(8080).build();
    }

    static final URI BASE_URI = getBaseURI();

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new MoneyTransferApi());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Starting grizzly...");
        HttpServer httpServer = startServer();
        Logger l = Logger.getLogger("org.glassfish.grizzly.http.server.HttpHandler");
        l.setLevel(Level.ALL);
        l.setUseParentHandlers(false);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        l.addHandler(ch);
        System.out.printf("Jersey app started with WADL available at %sapplication.wadl%n", BASE_URI);
        System.out.println("Hit enter to stop it...");
        System.in.read();
        httpServer.shutdownNow();
    }
}
