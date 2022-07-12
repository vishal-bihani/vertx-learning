package org.example.basics;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmptyVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(EmptyVerticle.class);

    @Override
    public void start() throws Exception {
        logger.info("EmptyVerticle started!");
    }

    @Override
    public void stop() throws Exception {
        logger.info("EmptyVerticle stopped!");
    }
}
