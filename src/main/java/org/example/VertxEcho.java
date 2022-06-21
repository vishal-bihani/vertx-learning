package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

public class VertxEcho {

    private static int numberOfConnections;

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        // Create a TCP server
        vertx.createNetServer()
                .connectHandler(VertxEcho::handleNewClient)
                .listen(3000);

        // Periodic calling
        vertx.setPeriodic(5000, id -> System.out.println(howMany()));

        // Create an HTTP server
        vertx.createHttpServer()
                .requestHandler(request -> request.response().end(howMany()))
                .listen(8080);
    }

    private static void handleNewClient(NetSocket socket) {
        numberOfConnections++;

        // Getting the buffer from client and returning the same string
        socket.handler(buffer -> {
            socket.write(buffer);
            if (buffer.toString().endsWith("/quit\n")) {
                socket.close();
            }
        });

        // When connection is closed the number of connections has to be
        // decremented
        socket.closeHandler(v -> numberOfConnections--);
    }

    private static String howMany() {
        return "Total number of " + numberOfConnections + " connections";
    }
}
