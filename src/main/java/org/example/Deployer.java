package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Deployer extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(Deployer.class);

    @Override
    public void start() throws Exception {
        long delay = 1000;

        for (int i=0; i<50; i++) {
            vertx.setTimer(delay, id -> deploy());
            delay += 1000;
        }
    }

    private void deploy() {
        vertx.deployVerticle(new EmptyVerticle(), ar -> {
            if (ar.succeeded()) {
                String id = ar.result();
                logger.info("{} successfully deployed", id);
                vertx.setTimer(5000, tid -> undeployLater(id));

            } else {
                logger.error("Error while deploying", ar.cause());
            }
        });
    }

    private void undeployLater(String id) {
        vertx.undeploy(id, ar -> {
            if (ar.succeeded()) {
                logger.info("{} was un-deployed", id);
            } else {
                logger.error("{} could not be un-deployed", id);
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Deployer());
    }
}
