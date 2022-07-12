package org.example.eventbus.types;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

public class Listener extends AbstractVerticle {
    private final Logger log = LoggerFactory.getLogger(Listener.class);
    private final DecimalFormat format = new DecimalFormat("#.##");

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();

        eventBus.<JsonObject>consumer("sensor.updates", event -> {
            JsonObject payload = event.body();
            log.info("Received sensor id: {} --- temperature: {}",
                    payload.getString("id"),
                    format.format(payload.getDouble("temp"))
            );
        });
    }
}
