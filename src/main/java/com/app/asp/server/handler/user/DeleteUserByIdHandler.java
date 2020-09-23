package com.app.asp.server.handler.user;

import java.sql.SQLException;
import java.util.Map;

import com.app.asp.service.NewUserService;
import com.app.util.AppParams;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.web.RoutingContext;

public class DeleteUserByIdHandler implements Handler<RoutingContext> {

	@Override
	public void handle(RoutingContext context) {
			
		context.vertx().executeBlocking(future -> {
			
			String id = context.request().getParam("id");
			try {
				Map userResponse = NewUserService.deleteUser(id);
				
				context.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
				context.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());
				context.put(AppParams.RESPONSE_DATA, userResponse);
			} catch (SQLException e) {
				context.fail(e);
				e.printStackTrace();
			}
		}, resultHandler -> {
			if(resultHandler.succeeded()) {
				context.next();
			} else {
				context.fail(resultHandler.cause());
			}
		});
	}

}
