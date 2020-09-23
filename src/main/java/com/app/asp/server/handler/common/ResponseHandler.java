package com.app.asp.server.handler.common;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import com.app.util.AppConstants;
import com.app.util.AppParams;
import com.app.util.AppUtil;
import com.app.util.ContextUtil;
import com.app.util.LoggerInterface;
import com.app.util.StringPool;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.http.HttpServerResponse;
import io.vertx.rxjava.ext.web.RoutingContext;

public class ResponseHandler implements Handler<RoutingContext>, LoggerInterface {

	@Override
	public void handle(RoutingContext routingContext) {
		int responseCode = ContextUtil.getInt(routingContext, AppParams.RESPONSE_CODE,
				HttpResponseStatus.NOT_FOUND.code());
		String responseDesc = ContextUtil.getString(routingContext, AppParams.RESPONSE_MSG,
				HttpResponseStatus.NOT_FOUND.reasonPhrase());
		Map responseBodyMap = ContextUtil.getMapData(routingContext, AppParams.RESPONSE_DATA, new LinkedHashMap<>());
		String responseBody = new JsonObject(responseBodyMap).encode();
		HttpServerResponse httpServerResponse = routingContext.response();
		httpServerResponse.setStatusCode(responseCode);
		httpServerResponse.setStatusMessage(responseDesc);
		httpServerResponse.putHeader(HttpHeaders.CONTENT_TYPE.toString(), AppConstants.CONTENT_TYPE_APPLICATION_JSON);
		httpServerResponse.putHeader(HttpHeaders.CONTENT_LENGTH.toString(), AppUtil.getContentLength(responseBody));
		logger.info(StringPool.NEW_LINE);
		logger.log(Level.INFO, "[RESPONSE] " + responseCode + StringPool.DOUBLE_SPACE + responseDesc);
		logger.log(Level.INFO, "[RESPONSE] BODY: " + responseBody);
		logger.log(Level.INFO,
				"[RESPONSE] ****************************** DONE ******************************" + StringPool.NEW_LINE);
		httpServerResponse.end(new JsonObject(responseBody).encode());
	}

}
