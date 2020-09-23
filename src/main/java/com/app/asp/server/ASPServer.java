package com.app.asp.server;

import com.app.asp.server.vertical.ASPVertical;
import com.app.util.LoggerInterface;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import java.util.logging.Level;

public class ASPServer implements Runnable, LoggerInterface {

    private int workerPoolSize;
    private long workerMaxExecuteTime;
    private int eventLoopPoolSize;
    private long eventLoopMaxExecuteTime;
    private long threadCheckInterval;

    private ASPVertical vertxVertical;

    public void setWorkerPoolSize(int workerPoolSize) {
        this.workerPoolSize = workerPoolSize;
    }

    public void setWorkerMaxExecuteTime(long workerMaxExecuteTime) {
        this.workerMaxExecuteTime = workerMaxExecuteTime;
    }

    public void setEventLoopPoolSize(int eventLoopPoolSize) {
        this.eventLoopPoolSize = eventLoopPoolSize;
    }

    public void setEventLoopMaxExecuteTime(long eventLoopMaxExecuteTime) {
        this.eventLoopMaxExecuteTime = eventLoopMaxExecuteTime;
    }

    public void setThreadCheckInterval(long threadCheckInterval) {
        this.threadCheckInterval = threadCheckInterval;
    }

    public void setVertxVertical(ASPVertical vertxVertical) {
        this.vertxVertical = vertxVertical;
    }

    public void init() throws InterruptedException {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {

            VertxOptions vertxOptions = new VertxOptions();

            vertxOptions.setWorkerPoolSize(workerPoolSize);
            vertxOptions.setMaxWorkerExecuteTime(workerMaxExecuteTime);
            vertxOptions.setEventLoopPoolSize(eventLoopPoolSize);
            vertxOptions.setMaxEventLoopExecuteTime(eventLoopMaxExecuteTime);
            vertxOptions.setBlockedThreadCheckInterval(threadCheckInterval);

            Vertx.vertx(vertxOptions).deployVerticle(vertxVertical);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
    }

}
