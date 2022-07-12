package org.example.eventbus.types;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.TimeoutStream;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;

public class HttpServer extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.createHttpServer()
                .requestHandler(this::handler)
                .listen(8080);
    }

    private void handler(HttpServerRequest request) {
        if ("/".equals(request.path())) {
            request.response().sendFile("index.html");

        } else if ("/sse".equals(request.path())) {
            sse(request);

        } else {
            request.response().setStatusCode(404);
        }
    }

    private void sse(HttpServerRequest request) {
        HttpServerResponse response = request.response();

        response.putHeader("Content-Type", "text/event-stream")
                .putHeader("Cache-Control", "no-cache")
                .setChunked(true);

        MessageConsumer<JsonObject> consumer = vertx.eventBus().consumer("sensor.updates");
        consumer.handler(event -> {
            response.write("event: update\n");
            response.write("data: " + event.body().encode() + "\n\n");
        });

        TimeoutStream ticks = vertx.periodicStream(1000);
        ticks.handler(id -> vertx.eventBus().<JsonObject>request("sensor.average", "", replyHandler -> {
                if (replyHandler.succeeded()) {
                    response.write("event: average\n");
                    response.write("data: " + replyHandler.result().body().encode() + "\n\n");
                }
            })
        );

        response.endHandler(handler -> {
            consumer.unregister();
            ticks.cancel();
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle("org.example.eventbus.types.HeatSensor",
                new DeploymentOptions().setInstances(4));

        vertx.deployVerticle("org.example.eventbus.types.Listener");
        vertx.deployVerticle("org.example.eventbus.types.SensorData");
        vertx.deployVerticle("org.example.eventbus.types.HttpServer");
    }

}
