package org.example.basics;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class MixedThreading extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(MixedThreading.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Context context = vertx.getOrCreateContext();

        new Thread(() -> {
            try {
                run(context);
                startPromise.complete();
            } catch (InterruptedException e) {
                log.error("Something went wrong! with message: {}", e.getMessage());
            }
        }).start();
    }

    public void run(Context context) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        log.info("In a non Vert.x thread");

        context.runOnContext(handler -> {
            log.info("In the event loop");

            vertx.setTimer(1000, timerHandler -> {
                log.info("Final countdown");
                latch.countDown();
            });
        });

        log.info("Waiting on countdown latch");
        latch.await();
        log.info("Terminated");
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MixedThreading());
    }
}
