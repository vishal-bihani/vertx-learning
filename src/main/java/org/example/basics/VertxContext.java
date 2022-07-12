package org.example.basics;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertxContext {
    public static final Logger log = LoggerFactory.getLogger(VertxContext.class);

    public static void main(String[] args) {

        /*
        Contexts are a part of verticle, but it can also be created from a
        Vertx instance. Code inside context runs on the event loop.

        Well I read that code is assigned to multiple event loop threads
        but seems like runs on single event loop thread or maybe running
        on single event loop thread on my machine.
         */

        Vertx vertx = Vertx.vertx();

        vertx.getOrCreateContext()
                .runOnContext(handler -> log.info("ABC"));

        vertx.getOrCreateContext()
                .runOnContext(handler -> log.info("123"));

        vertx.getOrCreateContext()
                .runOnContext(handler -> log.info("@#$"));

        vertx.getOrCreateContext()
                .runOnContext(handler -> log.info("DEF"));

        vertx.getOrCreateContext()
                .runOnContext(handler -> log.info("456"));

        vertx.getOrCreateContext()
                .runOnContext(handler -> log.info("~!%"));

        vertx.getOrCreateContext()
                .runOnContext(handler -> log.info("GHI"));

        vertx.getOrCreateContext()
                .runOnContext(handler -> log.info("789"));

        vertx.getOrCreateContext()
                .runOnContext(handler -> log.info("^&*"));

        vertx.getOrCreateContext()
                .runOnContext(handler -> log.info("JKL"));

        vertx.getOrCreateContext()
                .runOnContext(handler -> log.info("061"));

        vertx.getOrCreateContext()
                .runOnContext(handler -> log.info("()_"));


        Context context = vertx.getOrCreateContext();

        /*
        Using class fields instead context is much faster.
         */

        context.put("Foo", "Bar");

        context.exceptionHandler(exceptionHandler -> {
            if ("Tada".equals(exceptionHandler.getMessage())) {
                log.error("Handled exception with message {}", exceptionHandler.getMessage());
                return;
            }
            log.error("Handled exception with message {}", exceptionHandler.getMessage());
        });

        context.runOnContext(handler -> {
            throw new RuntimeException("Tada");
        });

        context.runOnContext(handler -> log.info("Key: Foo, Value: {}", (String) context.get("Foo")));
    }
}
