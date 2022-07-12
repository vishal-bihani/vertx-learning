package org.example.basics;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Offload extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(Offload.class);

    @Override
    public void start() throws Exception {

        // Instead of deploying a verticle as worker verticle, we can
        // execute a piece of blocking code on worker thread

        vertx.setPeriodic(5000, id -> {
            logger.info("Tick!");
            vertx.executeBlocking(this::blockingCode, this::resultHandler);
        });
    }

    private void blockingCode(Promise<String> promise) {

        // This code runs on worker thread and not on event-loop

        logger.info("Started blocking code");
        try {
            Thread.sleep(4000);
            logger.info("Done");
            promise.complete("OK");

        } catch (InterruptedException e) {
            logger.error("Something went wrong");
            promise.fail(e);
        }
    }

    private void resultHandler(AsyncResult<String> ar) {

        // This code runs on event-loop not on worker thread

        if (ar.failed()) {
            logger.error("Whoops!", ar.cause());
            return;
        }
        logger.info("Blocking operation result: {}", ar.result());
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Offload());
    }
}
