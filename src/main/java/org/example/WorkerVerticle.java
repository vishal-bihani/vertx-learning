package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(WorkerVerticle.class);

    @Override
    public void start() throws Exception {
        vertx.setPeriodic(10_000, id -> {
            try {
                logger.info("Zzz");
                Thread.sleep(8000);
                logger.info("Up");

            } catch (InterruptedException e) {
                logger.error("Something went wrong!");
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        DeploymentOptions deploymentOptions = new DeploymentOptions()
                .setInstances(2)
                .setWorker(true);

        vertx.deployVerticle("org.example.WorkerVerticle", deploymentOptions);
    }
}
