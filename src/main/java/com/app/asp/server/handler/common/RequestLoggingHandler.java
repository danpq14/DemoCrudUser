package com.app.asp.server.handler.common;

import java.util.Arrays;
import java.util.Set;

import com.app.util.AppParams;
import com.app.util.LoggerInterface;
import com.app.util.StringPool;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.rxjava.core.http.HttpServerRequest;
import io.vertx.rxjava.ext.web.RoutingContext;

/**
 * Created by HungDX on 23-Apr-16.
 */
public class RequestLoggingHandler implements Handler<RoutingContext>, LoggerInterface {

    @Override
    public void handle(RoutingContext routingContext) {

        routingContext.vertx().executeBlocking(future -> {
            try {
                HttpServerRequest httpServerRequest = routingContext.request();
                routingContext.put(AppParams.IP_ADDRESS, httpServerRequest.getHeader(AppParams.X_FORWARD_FOR));

                logger.info("[REQUEST] ************* " + httpServerRequest.method() + StringPool.DOUBLE_SPACE + httpServerRequest.uri() + " *************");
                Set<String> headerNames = httpServerRequest.headers().names();
                headerNames.stream().filter(header -> Arrays.asList(REQUIRED_HEADERS).contains(header)).forEach(header -> logger.info("[REQUEST] HEADER: " + header + StringPool.SPACE + StringPool.COLON + StringPool.SPACE + httpServerRequest.getHeader(header)));
                if (routingContext.request().method().compareTo(HttpMethod.POST) == 0
                        || routingContext.request().method().compareTo(HttpMethod.PUT) == 0
                        || routingContext.request().method().compareTo(HttpMethod.PATCH) == 0) {
                    if (routingContext.getBodyAsString() != null && !routingContext.getBodyAsString().contains(AppParams.DATA)) {
                        logger.info("[REQUEST] BODY: " + routingContext.getBodyAsString() + StringPool.NEW_LINE);
                    }
                }
                String userId = routingContext.getCookie(AppParams.USER_ID) != null ? routingContext.getCookie(AppParams.USER_ID).getValue() : StringPool.BLANK;
                routingContext.put(AppParams.USER_ID, userId);
                // TODO: FAKE DATA TYPE
                routingContext.put(AppParams.USER_ID, userId);
                if (userId != null && !userId.isEmpty()) {
                    validateUser(userId);
                }
                future.complete();
            } catch (Exception e) {
                routingContext.fail(e);
            }
        }, asyncResult -> {
            if (asyncResult.succeeded()) {
                routingContext.next();
            } else {
                routingContext.fail(asyncResult.cause());
            }
        });
    }

    private void validateUser(String userId) throws Exception {
    }

    private static final String[] REQUIRED_HEADERS = {HttpHeaders.ACCEPT.toString(), HttpHeaders.CONTENT_TYPE.toString(), AppParams.X_DATE, AppParams.X_EXPIRES, AppParams.X_AUTHORIZATION};

//    private static final Logger LOGGER = Logger.getLogger(RequestLoggingHandler.class.getName());
}
