package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class SomeVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        vertx.createHttpServer()
                .requestHandler(req -> req.response().end("OK"))
                .listen(8080, ar -> {
                    if (ar.succeeded()) {
                        startPromise.complete();
                    } else {
                        startPromise.fail(ar.cause());
                    }
                });
    }
}
