package org.example.eventbus.types;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SensorData extends AbstractVerticle {
    private final Map<String, Double> lastTemperature = new HashMap<>();

    @Override
    public void start() throws Exception {
        vertx.eventBus()
                .consumer("sensor.updates", this::update);

        vertx.eventBus()
                .consumer("sensor.average", this::average);
    }

    private void update(Message<JsonObject> payload) {
        JsonObject temperatureUpdate = payload.body();
        lastTemperature.put(temperatureUpdate.getString("id"), temperatureUpdate.getDouble("temp"));
    }

    private void average(Message<JsonObject> incomingMessage) {
        double average = lastTemperature.values().stream()
                .collect(Collectors.averagingDouble(Double::doubleValue));

        JsonObject outgoingMessage = new JsonObject();
        outgoingMessage.put("average", average);

        incomingMessage.reply(outgoingMessage);
    }
}
