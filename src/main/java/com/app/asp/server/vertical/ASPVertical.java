/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.asp.server.vertical;

import java.util.logging.Level;

import com.app.asp.server.handler.common.RequestLoggingHandler;
import com.app.asp.server.handler.common.ResponseHandler;
import com.app.asp.server.handler.user.InsertUserHandler;
import com.app.asp.server.handler.user.SearchUserByIdHandler;
import com.app.asp.server.handler.user.SearchUserByPhoneHandler;
import com.app.asp.server.handler.user.UpdateUserHandler;
import com.app.util.LoggerInterface;
import com.app.util.StringPool;

import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.BodyHandler;
import io.vertx.rxjava.ext.web.handler.ResponseTimeHandler;
import io.vertx.rxjava.ext.web.handler.TimeoutHandler;

/**
 *
 * @author hungdt
 */
public class ASPVertical extends AbstractVerticle implements LoggerInterface {

    private String serverHost;
    private int serverPort;
    private boolean connectionKeepAlive;
    private long connectionTimeOut;
    private int connectionIdleTimeOut;
    private String apiPrefix;

    public static HttpClient httpClient;
    public static HttpClient httpsClient;

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setConnectionKeepAlive(boolean connectionKeepAlive) {
        this.connectionKeepAlive = connectionKeepAlive;
    }

    public void setConnectionTimeOut(long connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public void setConnectionIdleTimeOut(int connectionIdleTimeOut) {
        this.connectionIdleTimeOut = connectionIdleTimeOut;
    }

    public void setApiPrefix(String apiPrefix) {
        this.apiPrefix = apiPrefix;
    }

    @Override
    public void start() throws Exception {

        logger.log(Level.INFO, "[INIT] STARTING UP ASP API SERVER...");

        httpClient = vertx.createHttpClient();
        httpsClient = vertx.createHttpClient(new HttpClientOptions().setSsl(true).setTrustAll(true));

        super.start();

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(ResponseTimeHandler.create());
        router.route().handler(TimeoutHandler.create(connectionTimeOut));
        router.route().handler(new RequestLoggingHandler());

//        router.mountSubRouter(apiPrefix, demoCRUD());
        router.mountSubRouter("/api/dan", newRouter());
        router.route().last().handler(new ResponseHandler());

        HttpServerOptions httpServerOptions = new HttpServerOptions();

        httpServerOptions.setHost(serverHost);
        httpServerOptions.setPort(serverPort);
        httpServerOptions.setTcpKeepAlive(connectionKeepAlive);
        httpServerOptions.setIdleTimeout(connectionIdleTimeOut);

        HttpServer httpServer = vertx.createHttpServer(httpServerOptions);

        httpServer.requestHandler(router::accept);
        httpServer.listen(result -> {
            if (result.failed()) {
                logger.log(Level.SEVERE, "[INIT] START ASP API ERROR ", result.cause());
            } else {
                logger.log(Level.INFO, "[INIT] ASP API STARTED AT " + StringPool.SPACE + "{0}" + StringPool.COLON + "{1}", new Object[]{serverHost, serverPort});
            }
        });
    }


    
    private Router newRouter() {
    	Router router = Router.router(vertx);
    	router.route(HttpMethod.GET, "/user/:id").handler(new SearchUserByIdHandler());
    	router.route(HttpMethod.GET, "/phone/:phone").handler(new SearchUserByPhoneHandler());
    	router.route(HttpMethod.POST, "/user").handler(new InsertUserHandler());
    	router.route(HttpMethod.PUT, "/user").handler(new UpdateUserHandler());
    	return router;
    }

}
