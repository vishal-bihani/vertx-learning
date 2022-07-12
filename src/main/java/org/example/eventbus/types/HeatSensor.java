package org.example.eventbus.types;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

public class HeatSensor extends AbstractVerticle {
    private final Logger log = LoggerFactory.getLogger(HeatSensor.class);
    private final Random random = new Random();
    private final String sensorId = UUID.randomUUID().toString();
    private final double temperature = 21.0;

    @Override
    public void start() {
        scheduleTemperatureUpdate();
    }

    private void scheduleTemperatureUpdate() {
        vertx.setTimer(random.nextInt(5000) + 1000, this::updateTemperature);
    }

    private void updateTemperature(Long id) {
        double updatedTemperature = temperature + (delta() / 10);
        log.info("Publishing sensor id: {} --- updated temperature: {}", sensorId, updatedTemperature);

        JsonObject payload = new JsonObject();
        payload.put("id", sensorId);
        payload.put("temp", updatedTemperature);

        // Pub/Sub
        vertx.eventBus().publish("sensor.updates", payload);
        scheduleTemperatureUpdate();
    }

    private double delta() {
        if (random.nextInt() > 0) {
            return random.nextGaussian();
        }
        return -random.nextGaussian();
    }
}
